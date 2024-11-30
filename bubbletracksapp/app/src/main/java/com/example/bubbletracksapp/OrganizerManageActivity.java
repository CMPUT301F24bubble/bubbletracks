package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * this class is an activity that allows organizers to manage their facility
 * @author Samyak
 * @version 1.0
 */
public class OrganizerManageActivity extends AppCompatActivity {

    // declare the facility, id and location variables
    private Facility facility;
    private String id, location;

    // declare the views necessary
    private ImageButton backButton;
    private TextView nameText, locationText, loadingText;
    private Button createEventButton, editFacilityButton;
    private ScrollView parentLayout;

    /**
     * sets the layout, assigns all the views and sets up all the on click listeners
     * @param savedInstanceState stores the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout
        setContentView(R.layout.facility_profile);

        // assign the views
        backButton = findViewById(R.id.back_button);
        nameText = findViewById(R.id.facility_name_text);
        locationText = findViewById(R.id.facility_location_text);
        createEventButton = findViewById(R.id.create_event_button);
        editFacilityButton = findViewById(R.id.edit_facility_button);
        loadingText = findViewById(R.id.loading_facility_textview);
        parentLayout = findViewById(R.id.parent_layout);

        // get the id of facility
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        // get the facility
        getFacility();

        // handler to go back to home screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(OrganizerManageActivity.this, MainActivity.class);
                startActivity(backIntent);
            }
        });

        // handler to create an event
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sends the location and id of the event
                Intent createEventIntent = new Intent(OrganizerManageActivity.this, OrganizerActivity.class);
                createEventIntent.putExtra("id", id);
                createEventIntent.putExtra("location", location);
                startActivity(createEventIntent);
            }
        });

        // handler to edit your facility
        editFacilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editFacilityIntent = new Intent(OrganizerManageActivity.this, OrganizerEditFacilityActivity.class);
                editFacilityIntent.putExtra("facility", facility);
                startActivity(editFacilityIntent);
            }
        });

    }

    /**
     * gets the facility from the database
     */
    protected void getFacility(){
        FacilityDB facilityDB = new FacilityDB();
        facilityDB.getFacility(id).thenAccept(curFacility -> {
            if(curFacility != null){
                facility = curFacility;
                loadingText.setVisibility(View.GONE);
                parentLayout.setVisibility(View.VISIBLE);
                location = curFacility.getLocation();
                setViews();
            } else {
                Toast.makeText(OrganizerManageActivity.this, "Could not find your facility.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(OrganizerManageActivity.this, "Could not load your profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

    /**
     * sets the views given the facility
     */
    protected void setViews(){
        nameText.setText(facility.getName());
        locationText.setText(facility.getLocation());
    }

}
