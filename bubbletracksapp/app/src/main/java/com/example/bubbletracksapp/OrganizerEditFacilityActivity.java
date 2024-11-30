package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

public class OrganizerEditFacilityActivity extends AppCompatActivity {

    // new facility to be created
    private Facility facility;

    // declare the views necessary
    private ImageButton backButton, locationButton;
    private EditText nameText;
    private TextView locationText;
    private Button updateButton;

    // Declares an ActivityResultLauncher that will handle the location selection action
    private ActivityResultLauncher<Intent> autocompleteLauncher;

    // fields to extract from the selected Location
    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS);

    /**
     * sets the layout, assigns all the views and sets up all the on click listeners
     * @param savedInstanceState stores the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the places API
        Places.initialize(getApplicationContext(), "AIzaSyBOt38qDT81Qj6jR0cmNHVGs3hPPw0XrZA");

        // set the layout to the create facility page
        setContentView(R.layout.edit_facility);

        // find all the views using their ids
        backButton = findViewById(R.id.back_button);
        nameText = findViewById(R.id.facility_name_edit);
        locationButton = findViewById(R.id.facility_location_button);
        locationText = findViewById(R.id.facility_location_text);
        updateButton = findViewById(R.id.update_button);

        // initialize the activity result launcher for the location search
        autocompleteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        locationText.setText(Autocomplete.getPlaceFromIntent(result.getData()).getAddress());
                    }
                }
        );

        facility = getIntent().getParcelableExtra("facility");

        setViews();

        // handler to go back to home screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // handler for selecting location
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation();
            }
        });

        // handler to create the event
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFacility();
            }
        });

    }

    /**
     * starts the intent for the overlay of google autocomplete API
     */
    protected void selectLocation() {
        // launch activity result launcher
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(OrganizerEditFacilityActivity.this);
        autocompleteLauncher.launch(intent);
    }

    /**
     * creates a facility and stores it in the database
     */
    protected void updateFacility() {

        String nameString = nameText.getText().toString().trim();
        String locationString = locationText.getText().toString().trim();
        facility.setName(nameString);
        facility.setLocation(locationString);
        new FacilityDB().updateFacility(facility);
        Intent intent = new Intent(OrganizerEditFacilityActivity.this, OrganizerManageActivity.class);
        intent.putExtra("id", facility.getId());
        startActivity(intent);
    }

    protected void setViews(){
        nameText.setText(facility.getName());
        locationText.setText(facility.getLocation());
    }

}
