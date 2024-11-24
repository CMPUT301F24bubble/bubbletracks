package com.example.bubbletracksapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest; // For importing post notification permissions

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;


import java.util.ArrayList;
import java.util.UUID;
//TODO: add timestamp to remove notification in database after a set time
/**
 * Organizer sends notifications to chosen entrants to sign up for events
 * @author Erza
 */
public class OrganizerNotificationActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "notify_entrants";
    private static final Integer SELECTED_NOTIFICATION_ID = 1;
    private static final Integer NON_SELECTED_NOTIFICATION_ID = 2;
    private static final Integer CONFIRMED_NOTIFICATION_ID = 3;
    private static final Integer CANCELLED_NOTIFICATION_ID = 4;
    private Event event;
    NotificationDB db = new NotificationDB();
    private String id;
    Notifications notification;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    /*
     * TODO: access the firebase to notify specific entrants based on checkboxes/permissions
     *
     */

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main);

       // View view = getLayoutInflater().inflate(R.layout.notification_main, null);;

        createNotificationChannel();

        // Test notification on organizer
        // TODO: send notification to database of entrants in event

        CheckBox checkInvited = findViewById(R.id.checkbox_notify_selected);
        CheckBox checkRejected = findViewById(R.id.checkbox_notify_non_selected);
        CheckBox checkConfirmed = findViewById(R.id.checkbox_notify_confirmed_attendees);
        CheckBox checkCancelled = findViewById(R.id.checkbox_notify_cancelled);
        Button notif_button = findViewById(R.id.button_confirm);
        notif_button.setOnClickListener(view -> checkNotificationPermission(checkInvited, checkRejected, checkConfirmed, checkCancelled));
    }

    /**
     * Create notification channel for user
     */
    //TODO: for entrant
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Entrant Notification Channel";
            String description = "Notification channel for the entrant user to be notified about updates to their event";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Check if there are notification permission for organizer to send a notification to the entrant
     * @param checkInvited entrants invited to event
     * @param checkRejected entrants not invited for event
     * @param checkConfirmed entrants who confirmed registration for event
     * @param checkCancelled entrants who cancelled invitation to event
     */
    //TODO: for entrants to check
    private void checkNotificationPermission(CheckBox checkInvited, CheckBox checkRejected, CheckBox checkConfirmed, CheckBox checkCancelled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the notification permission is granted
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                //showNotification(checkInvited, checkRejected, checkConfirmed, checkCancelled);
                sendNotification(checkInvited, checkRejected, checkConfirmed, checkCancelled);
            } else {
                // Request the notification permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            // For API levels below 33, permission is not required
            //showNotification(checkInvited, checkRejected, checkConfirmed, checkCancelled);
        }
    }

    /**
     * @param checkInvited entrants invited to event
     * @param checkRejected entrants not invited for event
     * @param checkConfirmed entrants who confirmed registration for event
     * @param checkCancelled entrants who cancelled invitation to event
     */
    //TODO: for entrant --> remove after all 4 notifications are made
/*    private void showNotification(CheckBox checkInvited, CheckBox checkRejected, CheckBox checkConfirmed, CheckBox checkCancelled) {
        Intent intent = new Intent(this, OrganizerNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

*//*        NotificationCompat.Builder invitedBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Bubble Tracks App")
                .setContentText("You are sampled for the event!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Mark your calendars, because you have been chosen to participate in this event. Thank you for signing up for this events' waitlist. "))
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationCompat.Builder rejectedBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Bubble Tracks App")
                .setContentText("Waitlist pending update")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Thank you for joining the waitlist, but unfortunately you have not been selected for the event. Thanks for your cooperation!"))
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationCompat.Builder confirmedBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Bubble Tracks App")
                .setContentText("Thank you for confirming your attendance for this event!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Keep an eye out for event updates."))
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationCompat.Builder cancelledBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Bubble Tracks App")
                .setContentText("We hate to see you go!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("You have chosen to cancel your attendance for the event, but keep an eye out for new ones to come."))
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);*//*

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            // Attempt to post the notification
            if (checkInvited.isChecked()) {
               // ArrayList<String> invitedList = event.getInvitedList();
                String testUserID = "9be104ee-e9e8-4df4-b93f-c3ec0aef750c";
                int userHash = testUserID.hashCode();
                notificationManager.notify(userHash, invitedBuilder.build()); // TODO: Notification id is the user id
            }
            if (checkRejected.isChecked()) {
                ArrayList<String> rejectedList = event.getRejectedList();
                notificationManager.notify(NON_SELECTED_NOTIFICATION_ID, rejectedBuilder.build());
            }
            if (checkConfirmed.isChecked()) {
               // ArrayList<String> confirmedList = event.getEnrolledList();
                notificationManager.notify(CONFIRMED_NOTIFICATION_ID, confirmedBuilder.build());
            }
            if (checkCancelled.isChecked()) {
                ArrayList<String> cancelledListList = event.getCancelledList();
                notificationManager.notify(CANCELLED_NOTIFICATION_ID, cancelledBuilder.build());
            }
        } catch (SecurityException e) {
            // Log the exception or handle it if the notification couldn't be posted
            Log.e("Notification", "Permission denied for posting notification: " + e.getMessage());
            Toast.makeText(this, "Permission required to show notifications", Toast.LENGTH_SHORT).show();
        }
    }*/


    private void sendNotification(CheckBox checkInvited, CheckBox checkRejected, CheckBox checkConfirmed, CheckBox checkCancelled) {
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("person1");
        testList.add("person2");
        testList.add("9be104ee-e9e8-4df4-b93f-c3ec0aef750c");
        testList.add("c8d942fe-1319-4015-955a-c0b09964183d");

        Object timestamp = System.currentTimeMillis();
        Intent intent = getIntent();

        // add invited notification to database
        if (checkInvited.isChecked()) {
            Log.d("Notification(invited)", "This is invited action: ");
            //***ArrayList<String> confirmedList = event.getEnrolledList();
            Notifications notification = new Notifications(
                    testList,
                    "You Are Invited!",
                    "Congratulations, you are invited to the event!",
                    "Accept your invitation to confirm your registration.",
                    //"Thank you for confirming your attendance to " + event.getName() + "!"
                    UUID.randomUUID().toString()
            );
            db.addNotification(notification);
        }
        // add rejected notification to database
        if (checkRejected.isChecked()) {
            Log.d("Notification(rejected)", "This is rejected action: ");
            //***ArrayList<String> confirmedList = event.getEnrolledList();
            Notifications notification = new Notifications(
                    testList,
                    "Event Waitlist Update",
                    "",
                    "Thank you for your participation in joining the waitlist, but unfortunately you have not been selected to join.",
                    //"Thank you for confirming your attendance to " + event.getName() + "!"
                    UUID.randomUUID().toString()
            );
            db.addNotification(notification);
        }

        // Send to confirmed entrants
        if (checkConfirmed.isChecked()) {
            Log.d("Notification(confirmed)", "This is confirmed action: ");
            //***ArrayList<String> confirmedList = event.getEnrolledList();
            Notifications notification = new Notifications(
                    testList,
                    "Thank you for confirming your attendance",
                    "",
                    "Mark your calendars for your event details.",
                    //"Thank you for confirming your attendance to " + event.getName() + "!"
                    UUID.randomUUID().toString()
            );
            db.addNotification(notification);
        }

        if (checkCancelled.isChecked()) {
            Log.d("Notification(cancelled)", "This is cancelled action: ");
            //***ArrayList<String> confirmedList = event.getEnrolledList();
            Notifications notification = new Notifications(
                    testList,
                    "Registration Cancel Confirmation",
                    "",
                    "We appreciate your consideration for joining this event. Hope to see you in future ones!",
                    UUID.randomUUID().toString()
            );
            db.addNotification(notification);
        }


        Toast.makeText(this, "Notifications sent!", Toast.LENGTH_LONG).show();

    }

}
