package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class OrganizerFacilityActivity extends AppCompatActivity {

    private Facility facility = new Facility();

    private ImageButton backButton, locationButton;
    private EditText nameText;
    private TextView locationText;
    private Button createButton;

    private ActivityResultLauncher<Intent> autocompleteLauncher;

    // fields to extract from the selected Location
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the places API
        Places.initialize(getApplicationContext(), "AIzaSyBOt38qDT81Qj6jR0cmNHVGs3hPPw0XrZA");

        // set the layout to the create facility page
        setContentView(R.layout.create_facility);

        // find all the views using their ids
        backButton = findViewById(R.id.back_button);
        nameText = findViewById(R.id.facility_name_edit);
        locationButton = findViewById(R.id.facility_location_button);
        locationText = findViewById(R.id.facility_location_text);
        createButton = findViewById(R.id.create_button);

        // initialize the activity result launcher for the location search
        autocompleteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        locationText.setText(Autocomplete.getPlaceFromIntent(result.getData()).getAddress());
                    }
                }
        );

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // handler for button for selecting location
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation();
            }
        });

        // handler to create the event
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFacility();
            }
        });

    }

    protected void selectLocation() {
        // launch activity result launcher
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(OrganizerFacilityActivity.this);
        autocompleteLauncher.launch(intent);
    }

    protected void createFacility() {

        String nameString = nameText.getText().toString().trim();
        String locationString = locationText.getText().toString().trim();
        if(nameString.isEmpty() || locationString.equals("Location not set") ){
            Toast.makeText(OrganizerFacilityActivity.this, "Sorry, a facility needs" +
                    " to have a name and a location", Toast.LENGTH_LONG).show();
        } else {
            facility.setName(nameString);
            facility.setLocation(locationString);
            updateOrganizer();
        }

    }

    protected void updateOrganizer(){
        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Not Found");
        EntrantDB entrantDB = new EntrantDB();

        // get the entrant from the database
        entrantDB.getEntrant(ID).thenAccept(user -> {
            if(user != null){

                // update facility's organizer to current entrant
                facility.setOrganizer(user.getID());
                user.setFacility(facility.getId());
                entrantDB.updateEntrant(user);

                FacilityDB facilityDB = new FacilityDB();
                facilityDB.addFacility(facility);

                Intent intent = new Intent(OrganizerFacilityActivity.this, OrganizerManageActivity.class);
                intent.putExtra("id", facility.getId());
                startActivity(intent);

            } else {
                Toast.makeText(OrganizerFacilityActivity.this, "Could not load profile.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(OrganizerFacilityActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

}
