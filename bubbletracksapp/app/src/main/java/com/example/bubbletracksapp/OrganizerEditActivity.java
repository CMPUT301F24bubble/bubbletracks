package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.LotteryMainBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Allows the organizer to sample N entrants from the waitlist.
 * The Organizer needs to choose an N and then click on sample.
 * @author Chester
 */
public class OrganizerEditActivity extends AppCompatActivity {
    Event event;
    EntrantDB entrantDB;

    ArrayList<Entrant> waitList = new ArrayList<>();
    ArrayList<Entrant> invitedList = new ArrayList<>();
    ArrayList<Entrant> rejectedList = new ArrayList<>();
    ArrayList<Entrant> cancelledList = new ArrayList<>();
    ArrayList<Entrant> enrolledList = new ArrayList<>();

    ListView waitlistListView;
    EntrantListAdapter waitlistAdapter;


    private LotteryMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LotteryMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent in =  getIntent();
        try {
            event = in.getParcelableExtra("event");
        } catch (Exception e) {
            Log.d("OrganizerEditActivity", "event extra was not passed correctly");
            throw new RuntimeException(e);
        }

        // Update all lists from Firebase
        updateEventLists();

        if(invitedList.size() > 0)
        {
            // Go to homescreen
        }

        waitlistListView = binding.reusableListView;
        waitlistAdapter = new EntrantListAdapter(this, waitList);
        waitlistListView.setAdapter(waitlistAdapter);


        Spinner nSpinner = binding.waitlistChooseCount;
        List<String> spinList = new ArrayList<String>();
        for (int i=1; i<=waitList.size(); i++){
            spinList.add(String.valueOf(i));
        }
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nSpinner.setAdapter(spinAdapter);

        binding.chooseFromWaitlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner nSpin = binding.waitlistChooseCount;
                String nStr = nSpin.getSelectedItem().toString();
                int n = Integer.parseInt(nStr);
                drawEntrants(n);
                startListActivity();
            }
        });


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Filled with going to the last activity. INCOMPLETE
            }
        });

    }

    private void updateEventLists() {
        entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList = entrants;
            } else {
                Log.d("getWaitList", "No entrants in waitlist");
            }
        });
        entrantDB.getEntrantList(event.getInvitedList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList = entrants;
            } else {
                Log.d("getInvitedList", "No entrants in InvitedList");
            }
        });
        entrantDB.getEntrantList(event.getRejectedList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList = entrants;
            } else {
                Log.d("getRejectedList", "No entrants in RejectedList");
            }
        });
        entrantDB.getEntrantList(event.getCancelledList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList = entrants;
            } else {
                Log.d("getCancelledList", "No entrants in CancelledList");
            }
        });
        entrantDB.getEntrantList(event.getEnrolledList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList = entrants;
            } else {
                Log.d("getEnrolledList", "No entrants in EnrolledList");
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

        return true;
    }


    private void startListActivity() {
        event.setWaitListWithEvents(waitList);
        event.setInvitedListWithEvents(invitedList);
        event.setRejectedListWithEvents(rejectedList);
        event.setCancelledListWithEvents(cancelledList);
        event.setEnrolledListWithEvents(enrolledList);
        Intent intent = new Intent(OrganizerEditActivity.this, OrganizerEntrantListActivity.class);
        intent.putExtra("event", event);
        startActivity(intent);
    }

}
