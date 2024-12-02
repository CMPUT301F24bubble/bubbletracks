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
/**
 * Organizer sends notifications to chosen entrants to sign up for events through checkboxes
 * @author Erza
 */
public class OrganizerNotificationActivity extends AppCompatActivity {

    private NotificationMainBinding binding;
    Event event;

    NotificationDB db = new NotificationDB();


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

        CheckBox checkInvited = findViewById(R.id.checkbox_notify_selected);
        CheckBox checkWaitlist = findViewById(R.id.checkbox_notify_waitlist);
        CheckBox checkConfirmed = findViewById(R.id.checkbox_notify_confirmed_attendees);
        CheckBox checkCancelled = findViewById(R.id.checkbox_notify_cancelled);
        Button notifButton = findViewById(R.id.button_confirm);
        notifButton.setOnClickListener(view -> sendNotification(checkInvited, checkWaitlist, checkConfirmed, checkCancelled));

        binding.backButton.setOnClickListener(new View.OnClickListener()

        {
            /**
             * set details of event lists
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view){
                Intent intent = new Intent(OrganizerNotificationActivity.this, OrganizerEventHosting.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Organizer sends a notification based on the type of entrant they selected to send to
     * @param checkInvited   entrants invited to event
     * @param checkWaitlist  entrants in waitlist for event
     * @param checkConfirmed entrants who confirmed registration for event
     * @param checkCancelled entrants who cancelled invitation to event
     */
    private void sendNotification(CheckBox checkInvited, CheckBox checkWaitlist, CheckBox checkConfirmed, CheckBox checkCancelled) {

        Date timestamp = new Date();
        // Send to invited entrants
        if (checkInvited.isChecked()) {
            Log.d("Notification(invited)", "This is invited action: ");
            ArrayList<String> invitedList = event.getInvitedList();
            if (invitedList != null) {
                Notifications notification = new Notifications(
                        invitedList,
                        "You Are Invited!",
                        "Congratulations, you are invited to the event" + event.getName() + "!",
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
        // send wait list entrants
        if (checkWaitlist.isChecked()) {
            Log.d("Notification(waitlist)", "This is waitlist action: ");
            //Log.d("Notification(waitlist)", "Event object: " + event);
            //entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            ArrayList<String> invitedList = event.getWaitList();
            Log.d("Notification(waitlist)", "Event object: " + event.getWaitList());
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
                        "Thank you for confirming your attendance to " + event.getName() + "!",
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
        // Send to cancelled entrants
        if (checkCancelled.isChecked()) {
            Log.d("Notification(cancelled)", "This is cancelled action: ");
            ArrayList<String> cancelledList = event.getCancelledList();
            if (cancelledList != null) {
                Notifications notification = new Notifications(
                        cancelledList,
                        "Registration Cancel Confirmation",
                        "You have been cancelled to enroll for " + event.getName() + ". We appreciate your consideration in joining. Hope to see you in future events!",
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
