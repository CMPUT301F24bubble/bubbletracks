package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.LotteryMainExtendBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

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
    Entrant currentUser;
    EntrantDB entrantDB = new EntrantDB();
    NotificationDB notificationDB = new NotificationDB();


    /**
     * waitList contains all the entrants that joined the waitlist
     */
    ArrayList<Entrant> waitList= new ArrayList<>();
    /**
     * invitedList contains all the entrants that are invited to the event
     */
    ArrayList<Entrant> invitedList= new ArrayList<>();
    /**
     * cancelledList contains all the entrants that were invited but rejected the invitation
     */
    ArrayList<Entrant> cancelledList = new ArrayList<>();
    /**
     * rejectedList contains all the entrants that are not being invited to the event
     */
    ArrayList<Entrant> rejectedList = new ArrayList<>();
    /**
     * enrolledList contains all the entrants that were invited and accepted the invitation
     */
    ArrayList<Entrant> enrolledList = new ArrayList<>();

    ArrayList<String> newInvitedList; // List of entrants to send invited notification to


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
            currentUser = in.getParcelableExtra("user");
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

        TextView waitListDescription = binding.waitListDescription;
        waitListDescription.setText(getString(R.string.wait_list_text, event.getName()));

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Go back to the events hosting screen
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                updateEventWithLists();

                Intent intent = new Intent(OrganizerEntrantListActivity.this, OrganizerEventHosting.class);
                intent.putExtra("user", currentUser);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });
        binding.viewAttendeeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event.updateEventFirebase();
                Intent intent = new Intent(OrganizerEntrantListActivity.this, OrganizerLocationsMap.class);
                intent.putExtra("event", event);
                startActivity(intent);
            }
        });
    }


    private void setEventLists() {
        entrantDB.getEntrantList(event.getWaitList()).thenAccept(entrants -> {
            if(entrants != null){
                waitList.addAll(entrants);
                UpdateListDisplay();
                Log.d("getWaitList", "WaitList loaded");
            } else {
                Log.d("getWaitList", "No entrants in waitlist");
            }
        });
        entrantDB.getEntrantList(event.getInvitedList()).thenAccept(entrants -> {
            if(entrants != null){
                invitedList.addAll(entrants);
                UpdateListDisplay();
                Log.d("getInvitedList", "invitedList loaded");
            } else {
                Log.d("getInvitedList", "No entrants in InvitedList");
            }
        });
        entrantDB.getEntrantList(event.getRejectedList()).thenAccept(entrants -> {
            if(entrants != null){
                rejectedList.addAll(entrants);
                UpdateListDisplay();
                Log.d("getRejectedList", "rejectedList loaded");
            } else {
                Log.d("getRejectedList", "No entrants in RejectedList");
            }
        });
        entrantDB.getEntrantList(event.getCancelledList()).thenAccept(entrants -> {
            if(entrants != null){
                cancelledList.addAll(entrants);
                UpdateListDisplay();
                Log.d("getCancelledList", "cancelledList loaded");
            } else {
                Log.d("getCancelledList", "No entrants in CancelledList");
            }
        });
        entrantDB.getEntrantList(event.getEnrolledList()).thenAccept(entrants -> {
            if(entrants != null){
                enrolledList.addAll(entrants);
                UpdateListDisplay();
                Log.d("getEnrolledList", "enrolledList loaded");
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

        entrant.deleteFromEventsWaitlist(event.getId());
        entrant.deleteFromEventsInvited(event.getId());
        entrant.deleteFromEventsEnrolled(event.getId());
        entrant.updateEntrantFirebase();

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
     */
    @Override
    public void redrawCancelledEntrant() {
        Entrant chosenEntrant = redrawEntrant();
        if (chosenEntrant == null) {
            Log.e("redrawCancelledEntrant", "Could not redraw entrant. " +
                    "Check if there are entrants to redraw");
            return;
        }

        //TODO: ADD NEW ARRAY TO ADD
        invitedList.add(chosenEntrant);
        newInvitedList.add(String.valueOf(chosenEntrant));
        UpdateListDisplay();
        resendNotifications();

        chosenEntrant.addToEventsInvited(event.getId());
        chosenEntrant.updateEntrantFirebase();
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

    /**
     * Sends invited notifications to redrawn entrants
     */
    private void resendNotifications() {
        if (event != null) {
            Date timestamp = new Date();

            if (newInvitedList != null) {
                Notifications notification = new Notifications(
                        newInvitedList,
                        "Event " + event.getName() + " update!",
                        "There has been a redraw for " + event.getName() + "and you are invited!",
                        "Accept your invitation to confirm your registration.",
                        UUID.randomUUID().toString(),
                        timestamp
                );
                notificationDB.addNotification(notification);
            } else {
                Toast.makeText(this, "Warning: There are no invited entrants in your event!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
