package com.example.bubbletracksapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class EntrantViewActivity extends AppCompatActivity {

    // declare the retrieved event, entrant and id
    private Event event;
    private String id;
    private Entrant entrant;

    // declare and initialize the databases and the variable to see if
    private Boolean inWaitlist = false;
    private EventDB eventDB = new EventDB();
    private EntrantDB entrantDB = new EntrantDB();

    private ImageView posterImage;
    private TextView monthText, dateText, timeText, locationText, nameText, descriptionText,
            capacityText, priceText, needsLocationText, registrationOpenText, registrationCloseText;
    private Button joinButton;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content);

        posterImage = findViewById(R.id.event_image);
        monthText = findViewById(R.id.event_month);
        dateText = findViewById(R.id.event_date);
        timeText = findViewById(R.id.event_time);
        locationText = findViewById(R.id.event_location);
        nameText = findViewById(R.id.event_title);
        descriptionText = findViewById(R.id.event_description);
        capacityText = findViewById(R.id.event_capacity);
        priceText = findViewById(R.id.event_price);
        needsLocationText = findViewById(R.id.event_requires_geo);
        joinButton = findViewById(R.id.join_waitlist_button);
        registrationOpenText = findViewById(R.id.event_registration_open);
        registrationCloseText = findViewById(R.id.event_registration_close);
        backButton = findViewById(R.id.back_button);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getCurrentEntrant();

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If the event needs a geolocation, make sure the entrant has a geolocation
                if(!event.getNeedsGeolocation() || !Objects.equals(entrant.getGeolocation(), new LatLng(0, 0)))
                {
                    addEntrant();
                }
                else {
                    Toast.makeText(EntrantViewActivity.this, "Allow geolocation and update your profile" +
                            " to join this waitlist", Toast.LENGTH_LONG).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    protected void getCurrentEntrant(){
        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Not Found");
        entrantDB.getEntrant(ID).thenAccept(user -> {
            if(user != null){
                entrant = user;
                getEvent();
            } else {
                Toast.makeText(EntrantViewActivity.this, "Could not load profile.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(EntrantViewActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

    protected void getEvent(){

        eventDB.getEvent(id).thenAccept(event -> {
            if(event != null){
                this.event = event;
                ArrayList<String> waitList = event.getWaitList();
                for(String entrant : waitList){
                    if(entrant.equals(this.entrant.getID())){
                        inWaitlist = true;
                        joinButton.setText(R.string.leave_waitlist);
                        break;
                    }
                }
                setViews();
            } else {
                Toast.makeText(EntrantViewActivity.this, "Event does not exist", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(EntrantViewActivity.this, "Failed to load event: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

    protected void setViews(){
        Picasso.get()
                .load(event.getImage())
                .into(posterImage);
        monthText.setText(event.getMonth(event.getDateTime()));
        dateText.setText(event.getDay(event.getDateTime()));
        timeText.setText(event.getTime(event.getDateTime()));
        locationText.setText(event.getGeolocation());
        nameText.setText(event.getName());
        descriptionText.setText(event.getDescription());
        capacityText.setText("Capacity: " + event.getMaxCapacity());
        priceText.setText("Price: " + event.getPrice());
        if(event.getNeedsGeolocation()){
            needsLocationText.setText("Requires Location: Yes");
        } else{
            needsLocationText.setText("Requires Location: No");
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        registrationOpenText.setText("Registration Opens: " + formatter.format(event.getRegistrationOpen()));
        registrationCloseText.setText("Registration Closes: " + formatter.format(event.getRegistrationClose()));

    }

    protected void addEntrant(){
        AlertDialog joinDialog;
        if (!inWaitlist) { // Entrant wants to join waitlist
            String message = "";
            if(event.getNeedsGeolocation()){
                message = "This waitlist will share information about your current location. Are you sure you want to join the waitlist for this event?";
            } else{
                message = "Are you sure you want to join the waitlist for this event?";
            }
            joinDialog = new AlertDialog.Builder(EntrantViewActivity.this)
                    .setTitle("Confirm Joining Waitlist")
                    .setMessage(message)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(event.getWaitList().size() < event.getWaitListLimit()){
                                Date curDate = new Date();
                                if(curDate.before(event.getRegistrationClose())){
                                    if(curDate.after(event.getRegistrationOpen())){
                                        event.addToWaitList(entrant.getID());
                                        entrant.addToEventsWaitlist(event.getId());
                                        eventDB.updateEvent(event);
                                        entrantDB.updateEntrant(entrant);
                                        joinButton.setText(R.string.leave_waitlist);
                                        inWaitlist = true;
                                        dialogInterface.dismiss();
                                        Toast.makeText(EntrantViewActivity.this, "You have " +
                                                "been added to the waitlist", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(EntrantViewActivity.this, "Sorry, " +
                                                "registration has not opened yet", Toast.LENGTH_LONG).show();
                                    }
                                } else{
                                    Toast.makeText(EntrantViewActivity.this, "Sorry, " +
                                            "registration has closed", Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(EntrantViewActivity.this, "Sorry, wait " +
                                        "list is full for this event", Toast.LENGTH_LONG).show();
                            }

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
        }
        else { // Entrant wants to leave waitlist
            joinDialog = new AlertDialog.Builder(EntrantViewActivity.this)
                    .setTitle("Confirm Leaving Waitlist")
                    .setMessage("Are you sure you want to leave the waitlist for this event?")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            event.deleteFromWaitList(entrant.getID());
                            entrant.deleteFromEventsWaitlist(event.getId());
                            eventDB.updateEvent(event);
                            entrantDB.updateEntrant(entrant);
                            joinButton.setText(R.string.join_waitlist);
                            inWaitlist = false;
                            dialogInterface.dismiss();
                            Toast.makeText(EntrantViewActivity.this, "You have been " +
                                    "removed from the waitlist", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
        }
        joinDialog.show();
    }

}
