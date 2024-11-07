package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.LotteryMainExtendBinding;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Organizer actions for entrants in waitlists. Allows the organizer to see who
 * is in the waitlist. And after sampling it shows who is invited and who
 * is not invited.
 * For people that were invited but rejected the invitation, it allows the organizer to
 * redraw them and replace them with someone from the waitlist.
 * @author Chester
 */
public class OrganizerEntrantListActivity extends AppCompatActivity
        implements CancelledListAdapter.CancelledEntrantI, InvitedListAdapter.InvitedEntrantI {

    private LotteryMainExtendBinding binding;

    Event event;
    EntrantDB entrantDB = new EntrantDB();
    //To be changed to waitlist class INCOMPLETE
    // waitList contains all the entrants that joined the waitlist
    // invitedList contains all the entrants that are invited to the event
    // cancelledList contains all the entrants that were invited but rejected the invitation
    // rejectedList contains all the entrants that are not being invited to the event
    // enrolledList contains all the entrants that were invited and accepted the invitation
    ArrayList<Entrant> waitList= new ArrayList<>();
    ArrayList<Entrant> invitedList= new ArrayList<>();
    ArrayList<Entrant> cancelledList = new ArrayList<>();
    ArrayList<Entrant> rejectedList = new ArrayList<>();
    ArrayList<Entrant> enrolledList = new ArrayList<>();

//    ArrayList<String> waitListIDs= new ArrayList<>();
//    ArrayList<String> invitedListIDs= new ArrayList<>();
//    ArrayList<String> cancelledListIDs = new ArrayList<>();
//    ArrayList<String> rejectedListIDs = new ArrayList<>();
//    ArrayList<String> enrolledListIDs = new ArrayList<>();

    int maximumNumberOfEntrants;
    OrganizerEditActivity organizerEditActivity;

    Context context;
    ListView waitlistListView;
    EntrantListAdapter waitlistAdapter;

    ListView invitedListView;
    InvitedListAdapter invitedAdapter;

    ListView cancelledListView;
    CancelledListAdapter cancelledAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = LotteryMainExtendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent in =  getIntent();
        try {
            event = in.getParcelableExtra("event");
        } catch (Exception e) {
            Log.d("OrganizerEntrantListActivity", "event extra was not passed correctly");
            throw new RuntimeException(e);
        }

        // Update all lists from Firebase
        updateEventLists();

        waitlistListView = binding.waitlistView;
        waitlistAdapter = new EntrantListAdapter(this, waitList);
        waitlistListView.setAdapter(waitlistAdapter);

        invitedListView = binding.invitedListView;
        invitedAdapter = new InvitedListAdapter(this, invitedList);
        invitedListView.setAdapter(invitedAdapter);

        cancelledListView = binding.cancelledListView;
        cancelledAdapter = new CancelledListAdapter(this, cancelledList);
        cancelledListView.setAdapter(cancelledAdapter);




        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.setWaitListWithEvents(waitList);
                event.setInvitedListWithEvents(invitedList);
                event.setRejectedListWithEvents(rejectedList);
                event.setCancelledListWithEvents(cancelledList);
                event.setEnrolledListWithEvents(enrolledList);
                Intent intent = new Intent(OrganizerEntrantListActivity.this, OrganizerEditActivity.class);
                intent.putExtra("event", event);

                startActivity(intent);
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


    // should return error if the list is empty INCOMPLETE
    /**
     * Allows the organizer to redraw an entrant.
     * It requires previous sampling of entrants.
     * @author Chester
     * @return The chosen entrant.
     */
    public Entrant redrawEntrant() {
        Collections.shuffle(rejectedList);
        Entrant chosenEntrant = rejectedList.get(0);
        rejectedList.remove(0);

        return chosenEntrant;
    }

    //Called when an Entrant cancels or rejects invitation and organize clicks on it
    public void cancelEntrant(Entrant entrant) {
        waitList.remove(entrant);
        invitedList.remove(entrant);
        rejectedList.remove(entrant);
        enrolledList.remove(entrant);
        cancelledList.add(entrant);
        UpdateListDisplay();
    }

    @Override
    public boolean hasEntrantAccepted(Entrant entrant) {
        // Should check if entant accepted invitation
        return enrolledList.contains(entrant);
    }


    // should return error if the list is empty INCOMPLETE
    /**
     * Allows the organizer to redraw an entrant from the people that were rejected.
     * It requires previous sampling of entrants.
     * @author Chester
     * @return The chosen entrant.
     */
    public void redrawCancelledEntrant(Entrant entrant) {
        Entrant chosenEntrant = redrawEntrant();
        invitedList.add(chosenEntrant);
        UpdateListDisplay();
    }


    /**
     * Updates the list view displays.
     * @author Chester
     */
    private void UpdateListDisplay() {
        waitlistAdapter.notifyDataSetChanged();
        invitedAdapter.notifyDataSetChanged();
        cancelledAdapter.notifyDataSetChanged();
    }
}
