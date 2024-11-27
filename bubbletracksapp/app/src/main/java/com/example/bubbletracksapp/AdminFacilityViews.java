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
 * AdminFacilityViews is an activity that displays a list of facilities in a ListView.
 * The activity fetches facility details from the database and displays them using an adapter.
 * @author Gwen
 */
public class AdminFacilityViews extends AppCompatActivity {
    private ArrayList<Facility> facilities = new ArrayList<>(); // List of facilities to be displayed
    private AdminFacilityAdapter facilityAdapter; // Adapter to manage the list of facilities
    private FacilityDB facilityDB = new FacilityDB(); // Database handler for retrieving facility data
    private ListView facilityListView; // ListView for displaying the facilities

    /**
     * Sets up the UI and fetches the list of facilities for Admins.
     */
    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.facility_viewer); // Assuming 'facility_viewer' is the XML layout for facility display

        facilityListView = findViewById(R.id.facilityListView);
        ImageButton backButton = findViewById(R.id.back_button);

        // Fetch facilities from the database
        facilityDB.getAllFacilities().thenAccept(facilitiesList -> {
            if (facilitiesList != null && !facilitiesList.isEmpty()) {
                facilities = new ArrayList<>(facilitiesList);
                facilityAdapter = new AdminFacilityAdapter(this, facilities);
                facilityListView.setAdapter(facilityAdapter);
            } else {
                Toast.makeText(this, "No facilities found", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Log.e("AdminFacilityViews", "Error fetching facilities", e);
            Toast.makeText(this, "Failed to fetch facilities", Toast.LENGTH_LONG).show();
            return null;
        });

        // Set up the back button to finish the activity
        backButton.setOnClickListener(v -> finish());
    }
}
