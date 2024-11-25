package com.example.bubbletracksapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * AdminProfileViews is an activity that displays a list of user profiles in a ListView.
 * The activity fetches user profiles from the database and displays them using an adapter.
 * @author Gwen
 */
public class AdminProfileViews extends AppCompatActivity {
    private ArrayList<Entrant> entrantsProfiles = new ArrayList<>(); // List of user profiles to be displayed
    private AdminEntrantListAdapter profileAdapter; // Adapter to manage the list of profiles
    private EntrantDB entrantDB = new EntrantDB(); // Database handler for retrieving entrant data
    private ListView profileListView; // ListView for displaying the user profiles

    /**
     * Sets up the UI and fetches the list of user profiles for Admins.
     */
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_viewer);

        profileListView = findViewById(R.id.profileListView);
        ImageButton backButton = findViewById(R.id.back_button);

        entrantDB.getAllEntrants().thenAccept(entrants -> {
            if (entrants != null && !entrants.isEmpty()) {
                entrantsProfiles = new ArrayList<>(entrants);
                profileAdapter = new AdminEntrantListAdapter(this, entrantsProfiles);
                profileListView.setAdapter(profileAdapter);
            } else {
                Toast.makeText(this, "No user profiles found", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Log.e("AdminProfileViews", "Error fetching profiles", e);
            Toast.makeText(this, "Failed to fetch profiles", Toast.LENGTH_LONG).show();
            return null;
        });

        backButton.setOnClickListener(v -> finish());
    }
}
