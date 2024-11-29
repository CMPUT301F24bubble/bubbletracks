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

import com.example.bubbletracksapp.databinding.EventHostingListBinding;

import java.util.ArrayList;

/**
 * Hold event that organizer is hosting
 * @author Chester
 */
public class OrganizerEventHosting extends AppCompatActivity implements EventHostListAdapter.EventHostI {
    private EventHostingListBinding binding;

    private final EventDB eventDB = new EventDB();
    private ArrayList<Event> hostedEvents = new ArrayList<>();

    Entrant currentUser;

    private ListView eventListView;
    private EventHostListAdapter eventListAdapter;

    /**
     * Set up creation of activity
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = EventHostingListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent in = getIntent();
        try {
            currentUser = in.getParcelableExtra("user");
        } catch (Exception e) {
            Log.e("OrganizerEventHosting", "User extra was not passed correctly");
            throw new RuntimeException(e);
        }

        eventListView = binding.reusableListView;

        eventListAdapter = new EventHostListAdapter(OrganizerEventHosting.this, hostedEvents);
        eventListView.setAdapter(eventListAdapter);

        eventDB.getEventList(currentUser.getEventsOrganized()).thenAccept(events -> {
            if (events != null) {
                hostedEvents.clear();
                hostedEvents.addAll(events);
                eventListAdapter.notifyDataSetChanged();

                Log.d("OrganizerEventHosting", "Hosted events loaded");
            } else {
                Toast.makeText(OrganizerEventHosting.this, "No hosted events", Toast.LENGTH_LONG).show();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Go back to the home screen
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrganizerEventHosting.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Switches view to the view of the waitlist of entrants
     * @param event event being viewed
     */
    @Override
    public void viewWaitlist(Event event) {
        Intent intent = new Intent(OrganizerEventHosting.this, OrganizerEditActivity.class);
        intent.putExtra("event", event);
        intent.putExtra("user", currentUser);
        startActivity(intent);
    }

    /**
     * Switches view to the view of editing the event
     * @param event event being edited
     */
    @Override
    public void editEvent(Event event) {
//        Intent intent = new Intent(OrganizerEventHosting.this, OrganizerEditEventActivity.class);
//        intent.putExtra("event", event);
//        intent.putExtra("user", currentUser);
//        startActivity(intent);
    }
}