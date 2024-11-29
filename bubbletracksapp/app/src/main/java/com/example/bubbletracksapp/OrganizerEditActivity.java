package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.LotteryMainBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Allows the organizer to sample N entrants from the waitlist.
 * The Organizer needs to choose an N and then click on sample.
 * @author Chester
 */
public class OrganizerEditActivity extends AppCompatActivity {
    Event event;
    EntrantDB entrantDB = new EntrantDB();

    ArrayList<Entrant> waitList = new ArrayList<>();
    ArrayList<Entrant> invitedList = new ArrayList<>();
    ArrayList<Entrant> rejectedList = new ArrayList<>();
    ArrayList<Entrant> cancelledList = new ArrayList<>();
    ArrayList<Entrant> enrolledList = new ArrayList<>();

    ListView waitlistListView;
    EntrantListAdapter waitlistAdapter;

    NotificationDB db = new NotificationDB();


    private LotteryMainBinding binding;

    /**
     * Set up creation of activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LotteryMainBinding.inflate(getLayoutInflater());

        Intent in =  getIntent();
        try {
            event = in.getParcelableExtra("event");
        } catch (Exception e) {
            Log.d("OrganizerEditActivity", "event extra was not passed correctly");
            throw new RuntimeException(e);
        }

        //If the Event has been pooled already, just show the lists
        if(event.getInvitedList().size() > 0)
        {
            startListActivity();
        }
        setContentView(binding.getRoot());

        waitlistListView = binding.reusableListView;

        entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList = entrants;
                waitlistAdapter = new EntrantListAdapter(this, waitList);
                waitlistListView.setAdapter(waitlistAdapter);

                Log.d("getWaitList", "WaitList loaded");

                Spinner nSpinner = binding.waitlistChooseCount;
                List<String> spinList = new ArrayList<String>();
                for (int i=1; i<=waitList.size(); i++){
                    spinList.add(String.valueOf(i));
                }
                ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinList);
                spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                nSpinner.setAdapter(spinAdapter);


            } else {
                Log.d("getWaitList", "No entrants in waitlist");
            }
        });


        binding.chooseFromWaitlistButton.setOnClickListener(new View.OnClickListener() {
            /**
             * set actions upon clicking on choosing from waitlist button
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Spinner nSpin = binding.waitlistChooseCount;
                String nStr = nSpin.getSelectedItem().toString();
                int n = Integer.parseInt(nStr);
                drawEntrants(n);
                sendNotifications();
                updateEventWithLists();
                startListActivity();
            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Set actions upon clicking the back button
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerEditActivity.this, MainActivity.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });

        binding.viewAttendeeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrganizerEditActivity.this, OrganizerLocationsMap.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });

    }

    // should return error if n is bigger than the size of waitlist INCOMPLETE
    // Assuming it is the fist time it is called INCOMPLETE
    /**
     * Allows the organizer to draw n entrants from the waitlist.
     * @param n The number of entrants to sample.
     * @author Chester
     * @return true if successfully sampled entrants
     */
    public boolean drawEntrants(int n) {
        Collections.shuffle(waitList);
        invitedList.clear();
        rejectedList.clear();
        enrolledList.clear();
        invitedList.addAll(waitList.subList(0, n));
        rejectedList.addAll(waitList.subList(n, waitList.size()));
        //TODO: CREATE NOTIFICATION FOR ENTRANTS
        return true;
    }

    private void sendNotifications() {
        if (event != null) {
            ArrayList<String> invitedList = event.getInvitedList();
            ArrayList<String> rejectedList = event.getRejectedList();
            Date timestamp = new Date();

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

            if (rejectedList != null) {
                Notifications notification = new Notifications(
                        rejectedList,
                        "Event Waitlist Update",
                        "We're sorry, but you have not been selected to enroll for your event.",
                        "",
                        //"Thank you for confirming your attendance to " + event.getName() + "!"
                        UUID.randomUUID().toString(),
                        timestamp
                );
                db.addNotification(notification);
            } else {
                Toast.makeText(this, "There are no entrants in your event waitlist!", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Shows the lists
     */
    private void startListActivity() {
        event.updateEventFirebase();
        Intent intent = new Intent(OrganizerEditActivity.this, OrganizerEntrantListActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

    /**
     * Updates event lists
     */
    private void updateEventWithLists() {
        event.setWaitListWithEvents(waitList);
        event.setInvitedListWithEvents(invitedList);
        event.setRejectedListWithEvents(rejectedList);
        event.setCancelledListWithEvents(cancelledList);
        event.setEnrolledListWithEvents(enrolledList);
    }

}
