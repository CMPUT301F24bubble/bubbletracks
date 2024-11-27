/**
 *
 * @author Sarah, Chester helped explain components
 * @version 1.0
 */


package com.example.bubbletracksapp;
import android.os.Bundle;

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
        eventsCatalogue = eventsCatalogue.findViewById(R.id.event_catalogue);
        // SETS UP ADAPTER
        adapter = new BrowseEventsAdminAdapter(this, new CompletableFuture<>());
        eventsCatalogue.setAdapter(adapter);
        eventsCatalogue.setLayoutManager(new LinearLayoutManager(this));
    }
}
