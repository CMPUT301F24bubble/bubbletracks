package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class OrganizerManageActivity extends AppCompatActivity {

    private Facility facility;
    private String id, location;

    private ImageButton backButton;
    private TextView nameText, locationText;
    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.facility_profile);

        backButton = findViewById(R.id.back_button);
        nameText = findViewById(R.id.facility_name_text);
        locationText = findViewById(R.id.facility_location_text);
        createEventButton = findViewById(R.id.create_event_button);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getFacility();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent = new Intent(OrganizerManageActivity.this, MainActivity.class);
                startActivity(backIntent);
            }
        });

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createEventIntent = new Intent(OrganizerManageActivity.this, OrganizerActivity.class);
                createEventIntent.putExtra("id", id);
                createEventIntent.putExtra("location", location);
                startActivity(createEventIntent);
            }
        });

    }

    protected void getFacility(){
        FacilityDB facilityDB = new FacilityDB();
        facilityDB.getFacility(id).thenAccept(curFacility -> {
            if(curFacility != null){
                facility = curFacility;
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

    protected void setViews(){
        nameText.setText(facility.getName());
        locationText.setText(facility.getLocation());
    }

}
