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

    int maximumNumberOfEntrants;
    OrganizerEditActivity organizerEditActivity;

    Context context = this;
    ListView waitlistListView;
    EntrantListAdapter waitlistAdapter;

    ListView invitedListView;
    InvitedListAdapter invitedAdapter;

    ListView cancelledListView;
    CancelledListAdapter cancelledAdapter;

    /**
     * Set up creation of activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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


        waitlistListView = binding.waitlistView;
        waitlistAdapter = new EntrantListAdapter(this, waitList);
        waitlistListView.setAdapter(waitlistAdapter);

        invitedListView = binding.invitedListView;
        invitedAdapter = new InvitedListAdapter(this, invitedList);
        invitedListView.setAdapter(invitedAdapter);

        cancelledListView = binding.cancelledListView;
        cancelledAdapter = new CancelledListAdapter(this, cancelledList);
        cancelledListView.setAdapter(cancelledAdapter);

        // Set up all lists from Firebase
        setEventLists();


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * set details of event lists
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                updateEventWithLists();

                Intent intent = new Intent(OrganizerEntrantListActivity.this, MainActivity.class);
                intent.putExtra("event", event);

                startActivity(intent);
            }
        });
        binding.viewAttendeeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateListDisplay();
            }
        });
    }


    private void setEventLists() {
        entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList = entrants;
                Log.d("getWaitList", "WaitList loaded");
                UpdateListDisplay();
            } else {
                Log.d("getWaitList", "No entrants in waitlist");
            }
        });
        entrantDB.getEntrantList(event.getInvitedList()).thenAccept(entrants -> {
            if(entrants != null){
                invitedList = entrants;
                Log.d("getInvitedList", "invitedList loaded");
                UpdateListDisplay();
            } else {
                Log.d("getInvitedList", "No entrants in InvitedList");
            }
        });
        entrantDB.getEntrantList(event.getRejectedList()).thenAccept(entrants -> {
            if(entrants != null){
                rejectedList = entrants;
                Log.d("getRejectedList", "rejectedList loaded");
                UpdateListDisplay();
            } else {
                Log.d("getRejectedList", "No entrants in RejectedList");
            }
        });
        entrantDB.getEntrantList(event.getCancelledList()).thenAccept(entrants -> {
            if(entrants != null){
                cancelledList = entrants;
                Log.d("getCancelledList", "cancelledList loaded");
                UpdateListDisplay();
            } else {
                Log.d("getCancelledList", "No entrants in CancelledList");
            }
        });
        entrantDB.getEntrantList(event.getEnrolledList()).thenAccept(entrants -> {
            if(entrants != null){
                enrolledList = entrants;
                Log.d("getEnrolledList", "enrolledList loaded");
                UpdateListDisplay();
            } else {
                Log.d("getEnrolledList", "No entrants in EnrolledList");
            }
        });
    }


    /**
     * Allows the organizer to redraw an entrant.
     * It requires previous sampling of entrants and the rejected list to have entrants.
     * @author Chester
     * @return The chosen entrant or null if the rejected list is empty.
     */
    public Entrant redrawEntrant() {
        if (rejectedList.isEmpty()) {
            Log.e("redrawEntrant", "The rejected list is empty");
            return null;
        }
        Collections.shuffle(rejectedList);
        Entrant chosenEntrant = rejectedList.get(0);
        rejectedList.remove(chosenEntrant);

        return chosenEntrant;
    }

    /**
     * Called when an Entrant cancels or rejects invitation and organize clicks on it
     * @param entrant The entrant
     */
    public void cancelEntrant(Entrant entrant) {
        waitList.remove(entrant);
        invitedList.remove(entrant);
        rejectedList.remove(entrant);
        enrolledList.remove(entrant);
        cancelledList.add(entrant);
        UpdateListDisplay();
        updateEventWithLists();
    }

    /**
     * checks if the entrant accepted the invitation
     * @param entrant The entrant
     * @return if the entrant accepted the invitation
     */
    @Override
    public boolean hasEntrantAccepted(Entrant entrant) {
        return enrolledList.contains(entrant);
    }


    /**
     * Allows the organizer to redraw an entrant from the people that were rejected.
     * It requires previous sampling of entrants.
     * @author Chester
     * @return The chosen entrant.
     */
    public void redrawCancelledEntrant(Entrant entrant) {
        Entrant chosenEntrant = redrawEntrant();
        if (chosenEntrant == null) {
            Log.e("redrawCancelledEntrant", "Could not redraw entrant. " +
                    "Check if there are entrants to redraw");
            return;
        }

        invitedList.add(chosenEntrant);
        UpdateListDisplay();
    }

    /**
     * Returns whether the rejected list is empty
     * Used to check whether redrawing is possible
     * @return the current Event
     */
    @Override
    public boolean isRejectedListEmpty() {
        return event.getRejectedList().isEmpty();
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

}
