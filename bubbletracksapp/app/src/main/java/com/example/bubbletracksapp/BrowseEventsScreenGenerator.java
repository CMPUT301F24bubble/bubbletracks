/**
 *
 * @author Sarah, Chester helped explain components
 * @version 1.0
 */


package com.example.bubbletracksapp;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class BrowseEventsScreenGenerator extends AppCompatActivity {


    // ATTRIBUTES
    private CompletableFuture<ArrayList<Event>> allTheEvents = new CompletableFuture<>();
    private EventDB eventDB = new EventDB();
    private RecyclerView eventsCatalogue;
    private BrowseEventsAdminAdapter adapter;
    private TextView noEventsInDB;

    // CONSTRUCTOR
    public BrowseEventsScreenGenerator(){}

    /**
     * Initializes main component of the screen: the recycler view.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_events);
        // SETS UP RECYCLER VIEW
        eventsCatalogue = eventsCatalogue.findViewById(R.id.event_db);

        displayEvents();
    }


    private void displayEvents() {
        allTheEvents.whenComplete((events, throwable) -> runOnUiThread(() -> {
            if (throwable != null) {
                // Handle errors (show a dialog or toast)
                noEventsInDB.setText("An error occurred: " + throwable.getMessage());
                noEventsInDB.setVisibility(View.VISIBLE);
                eventsCatalogue.setVisibility(View.GONE);
            } else if (events != null && !events.isEmpty()) {
                // Populate RecyclerView and hide placeholder
                BrowseEventsAdminAdapter adapter = new BrowseEventsAdminAdapter(this, new CompletableFuture<>());
                eventsCatalogue.setAdapter(adapter);
                eventsCatalogue.setVisibility(View.VISIBLE);
                noEventsInDB.setVisibility(View.GONE);
            } else {
                // Show placeholder and hide RecyclerView
                noEventsInDB.setVisibility(View.VISIBLE);
                eventsCatalogue.setVisibility(View.GONE);
            }
        }));
    }
}



