package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.LotteryMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Allows the organizer to sample N entrants from the waitlist.
 * The Organizer needs to choose an N and then click on sample.
 * @author Chester
 */
public class OrganizerEditActivity extends AppCompatActivity {
    Event event;
    Entrant currentUser;
    EntrantDB entrantDB = new EntrantDB();

    ArrayList<Entrant> waitList = new ArrayList<>();
    ArrayList<Entrant> invitedList = new ArrayList<>();
    ArrayList<Entrant> rejectedList = new ArrayList<>();
    ArrayList<Entrant> cancelledList = new ArrayList<>();
    ArrayList<Entrant> enrolledList = new ArrayList<>();

    List<String> spinList = new ArrayList<String>();

    ListView waitlistListView;
    EntrantListAdapter waitlistAdapter;


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
            currentUser = in.getParcelableExtra("user");
        } catch (Exception e) {
            Log.d("OrganizerEditActivity", "event extra was not passed correctly");
            throw new RuntimeException(e);
        }

        //If the Event has been pooled already, just show the lists
        if(!event.getInvitedList().isEmpty() || !event.getRejectedList().isEmpty()
                || !event.getCancelledList().isEmpty() || !event.getEnrolledList().isEmpty())
        {
            startListActivity();
        }
        setContentView(binding.getRoot());

        waitlistListView = binding.reusableListView;
        waitlistAdapter = new EntrantListAdapter(this, waitList);
        waitlistListView.setAdapter(waitlistAdapter);

        TextView waitListDescription = binding.waitListDescription;
        waitListDescription.setText(getString(R.string.wait_list_text, event.getName()));

        Spinner nSpin = binding.waitlistChooseCount;
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nSpin.setAdapter(spinAdapter);

        entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList.addAll(entrants);
                waitlistAdapter.notifyDataSetChanged();

                Log.d("getWaitList", "WaitList loaded");

                for (int i=1; i<=waitList.size(); i++){
                    spinList.add(String.valueOf(i));
                }
                spinAdapter.notifyDataSetChanged();

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
                // You can only draw entrants if the registration date passed and there are users in waitlist.
                if(!event.getRegistrationClose().after(new Date())) {
                    if(!waitList.isEmpty()) {
                        String nStr = nSpin.getSelectedItem().toString();
                        int n = Integer.parseInt(nStr);
                        drawEntrants(n);
                        updateEventWithLists();
                        startListActivity();
                    }
                    else {
                        Toast.makeText(OrganizerEditActivity.this, "No users in waitlist.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(OrganizerEditActivity.this, "Registration date is still open. Wait until it closes.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Set actions upon clicking the back button
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                updateEventWithLists();
                Intent intent = new Intent(OrganizerEditActivity.this, OrganizerEventHosting.class);
                intent.putExtra("user", currentUser);
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

    /**
     * Allows the organizer to draw n entrants from the waitlist.
     * @param n The number of entrants to sample.
     * @author Chester
     * @return true if successfully sampled entrants
     */
    public boolean drawEntrants(int n) {
        ArrayList<Entrant> newWaitlist = new ArrayList<>(waitList);
        Collections.shuffle(newWaitlist);
        invitedList.clear();
        rejectedList.clear();
        enrolledList.clear();
        invitedList.addAll(newWaitlist.subList(0, n));
        rejectedList.addAll(newWaitlist.subList(n, newWaitlist.size()));
        updateEventWithLists();
        addEntrantsInvitations();
        return true;
    }

    /**
     * Shows the lists
     */
    private void startListActivity() {
        Intent intent = new Intent(OrganizerEditActivity.this, OrganizerEntrantListActivity.class);
        intent.putExtra("user", currentUser);
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
        event.updateEventFirebase();
    }

    /**
     * Updates the entrants that were invited with their invitations
     */
    private void addEntrantsInvitations() {
        for (Entrant entrant: invitedList) {
            entrant.addToEventsInvited(event.getId());
            entrant.updateEntrantFirebase();
        }
    }
}
