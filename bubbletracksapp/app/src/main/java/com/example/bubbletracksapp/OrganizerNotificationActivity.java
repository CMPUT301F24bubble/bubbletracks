package com.example.bubbletracksapp;

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
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest; // For importing post notification permissions

/**
 * Organizer sends notifications to chosen entrants to sign up for events
 *
 */
public class OrganizerNotificationActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "notify_entrants";
    private static final Integer SELECTED_NOTIFICATION_ID = 1;
    private static final Integer NON_SELECTED_NOTIFICATION_ID = 2;
    private static final Integer CONFIRMED_NOTIFICATION_ID = 3;
    private static final Integer CANCELLED_NOTIFICATION_ID = 4;
    private Event event;

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
/*        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission is granted, show notification
                        Toast.makeText(this, "Notification permission!!", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, "Notification permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
        );*/

        CheckBox checkSelected = findViewById(R.id.checkbox_notify_selected);
        CheckBox checkNonSelected = findViewById(R.id.checkbox_notify_non_selected);
        CheckBox checkConfirmed = findViewById(R.id.checkbox_notify_confirmed_attendees);
        CheckBox checkCancelled = findViewById(R.id.checkbox_notify_cancelled);
        Button notif_button = findViewById(R.id.button_confirm);
        notif_button.setOnClickListener(view -> checkNotificationPermission(checkSelected, checkNonSelected, checkConfirmed, checkCancelled));
    }


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

    private void checkNotificationPermission(CheckBox checkSelected, CheckBox checkNonSelected, CheckBox checkConfirmed, CheckBox checkCancelled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the notification permission is granted
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                showNotification(checkSelected, checkNonSelected, checkConfirmed, checkCancelled);
            } else {
                // Request the notification permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            // For API levels below 33, permission is not required
            showNotification(checkSelected, checkNonSelected, checkConfirmed, checkCancelled);
        }
    }

    private void showNotification(CheckBox checkSelected, CheckBox checkNonSelected, CheckBox checkConfirmed, CheckBox checkCancelled) {
        Intent intent = new Intent(this, OrganizerNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder selectedBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Bubble Tracks App")
                .setContentText("You are sampled for the event!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Mark your calendars, because you have been chosen to participate in this event. Thank you for signing up for this events' waitlist. "))
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationCompat.Builder nonSelectedBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
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
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            // Attempt to post the notification
            if (checkSelected.isChecked()) {
                event.getEnrolledList();
                notificationManager.notify(SELECTED_NOTIFICATION_ID, selectedBuilder.build());
            }
            if (checkNonSelected.isChecked()) {
                event.getRejectedList();
                notificationManager.notify(NON_SELECTED_NOTIFICATION_ID, nonSelectedBuilder.build());
            }
            if (checkConfirmed.isChecked()) {
                event.getInvitedList();
                notificationManager.notify(CONFIRMED_NOTIFICATION_ID, confirmedBuilder.build());
            }
            if (checkCancelled.isChecked()) {
                event.getCancelledList();
                notificationManager.notify(CANCELLED_NOTIFICATION_ID, cancelledBuilder.build());
            }
        } catch (SecurityException e) {
            // Log the exception or handle it if the notification couldn't be posted
            Log.e("Notification", "Permission denied for posting notification: " + e.getMessage());
            Toast.makeText(this, "Permission required to show notifications", Toast.LENGTH_SHORT).show();
        }
    }

}
