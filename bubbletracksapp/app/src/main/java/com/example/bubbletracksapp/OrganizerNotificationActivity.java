package com.example.bubbletracksapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest; // For importing post notification permissions

import com.example.bubbletracksapp.databinding.LotteryMainExtendBinding;
import com.example.bubbletracksapp.databinding.NotificationMainBinding;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
//TODO: add timestamp to remove notification in database after a set time
/**
 * Organizer sends notifications to chosen entrants to sign up for events through checkboxes
 * @author Erza
 */
public class OrganizerNotificationActivity extends AppCompatActivity {

    private NotificationMainBinding binding;
    private static final String CHANNEL_ID = "notify_entrants";
    Event event;

    NotificationDB db = new NotificationDB();

    private ActivityResultLauncher<String> requestPermissionLauncher;


    /**
     * Create organizer notification activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = NotificationMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent in = getIntent();
        try {
            event = in.getParcelableExtra("event");
        } catch (Exception e) {
            Log.d("OrganizerEditActivity", "event extra was not passed correctly");
            throw new RuntimeException(e);
        }
        setContentView(binding.getRoot());

        // TODO: send notification to database of entrants in event

        CheckBox checkInvited = findViewById(R.id.checkbox_notify_selected);
        CheckBox checkRejected = findViewById(R.id.checkbox_notify_non_selected);
        CheckBox checkConfirmed = findViewById(R.id.checkbox_notify_confirmed_attendees);
        CheckBox checkCancelled = findViewById(R.id.checkbox_notify_cancelled);
        Button notifButton = findViewById(R.id.button_confirm);
        notifButton.setOnClickListener(view -> sendNotification(checkInvited, checkRejected, checkConfirmed, checkCancelled));

        binding.backButton.setOnClickListener(new View.OnClickListener()

        {
            /**
             * set details of event lists
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view){
                Intent intent = new Intent(OrganizerNotificationActivity.this, MainActivity.class);
                intent.putExtra("event", event);

                startActivity(intent);
            }
        });
    }

    /**
     * Check if there are notification permission for organizer to send a notification to the entrant
     *
     * @param checkInvited   entrants invited to event
     * @param checkRejected  entrants not invited for event
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
     * @param checkInvited   entrants invited to event
     * @param checkRejected  entrants not invited for event
     * @param checkConfirmed entrants who confirmed registration for event
     * @param checkCancelled entrants who cancelled invitation to event
     */
    private void sendNotification(CheckBox checkInvited, CheckBox checkRejected, CheckBox checkConfirmed, CheckBox checkCancelled) {
        ArrayList<String> testList = new ArrayList<String>();
        testList.add("person1");
        testList.add("person2");
        testList.add("9be104ee-e9e8-4df4-b93f-c3ec0aef750c");
        //testList.add("c8d942fe-1319-4015-955a-c0b09964183d");

        Date timestamp = new Date();
        Intent intent = getIntent();

        //TODO: check for null lists
        // add invited notification to database
        if (checkInvited.isChecked()) {
            Log.d("Notification(invited)", "This is invited action: ");
            ArrayList<String> invitedList = event.getInvitedList();
            if (invitedList != null) {
                Notifications notification = new Notifications(
                        invitedList,
                        "You Are Invited!",
                        "Congratulations, you are invited to the event!",
                        "Accept your invitation to confirm your registration.",
                        //"Thank you for confirming your attendance to " + event.getName() + "!"
                        UUID.randomUUID().toString(),
                        timestamp
                );
                db.addNotification(notification);
            } else {
                Toast.makeText(this, "There are no invited entrants in your event!", Toast.LENGTH_LONG).show();
            }
        }
        // add rejected notification to database
        if (checkRejected.isChecked()) {
            Log.d("Notification(rejected)", "This is rejected action: ");
            //Log.d("Notification(rejected)", "Event object: " + event);
            //entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            ArrayList<String> invitedList = event.getWaitList();
            Log.d("Notification(rejected)", "Event object: " + event.getWaitList());
            if (invitedList != null) {
                Notifications notification = new Notifications(
                        invitedList,
                        "Event Waitlist Update",
                        "Thank you for your participation in joining the waitlist for " + event.getName() + ".",
                        "",
                        //"Thank you for confirming your attendance to " + event.getName() + "!"
                        UUID.randomUUID().toString(),
                        timestamp
                );
                db.addNotification(notification);
            } else {
                Toast.makeText(this, "There are no entrants in your event waitlist!", Toast.LENGTH_LONG).show();
            }
            //});

        }

        // Send to confirmed entrants
        if (checkConfirmed.isChecked()) {
            Log.d("Notification(confirmed)", "This is confirmed action: ");
            ArrayList<String> confirmedList = event.getEnrolledList();
            if (confirmedList != null) {
                Notifications notification = new Notifications(
                        confirmedList,
                        "Thank you for confirming your attendance",
                        "Mark your calendars for your event details.",
                        "",
                        //"Thank you for confirming your attendance to " + event.getName() + "!"
                        UUID.randomUUID().toString(),
                        timestamp
                );
                db.addNotification(notification);
            } else {
                Toast.makeText(this, "There are no confirmed entrants in your event!", Toast.LENGTH_LONG).show();
            }

        }

        if (checkCancelled.isChecked()) {
            Log.d("Notification(cancelled)", "This is cancelled action: ");
            ArrayList<String> cancelledList = event.getCancelledList();
            if (cancelledList != null) {
                Notifications notification = new Notifications(
                        cancelledList,
                        "Registration Cancel Confirmation",
                        "We appreciate your consideration for joining this event. Hope to see you in future ones!",
                        "",
                        UUID.randomUUID().toString(),
                        timestamp
                );
                db.addNotification(notification);
            } else {
                Toast.makeText(this, "There are no entrants that have cancelled your event!", Toast.LENGTH_LONG).show();
            }
        }


        Toast.makeText(this, "Notification(s) sent!", Toast.LENGTH_LONG).show();

    }


}
