package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * this class is an activity that allows an entrant to view the event details
 * @author Samyak
 * @version 2.0
 */
public class EntrantViewActivity extends AppCompatActivity {

    // declare the retrieved event, entrant and id
    private Event event;
    private String id;
    private Entrant entrant;

    // declare and initialize the databases and the variable to see if
    private Boolean inWaitlist = false;
    private EventDB eventDB = new EventDB();
    private EntrantDB entrantDB = new EntrantDB();

    // declare all views necessary
    private ImageView posterImage;
    private TextView monthText, dateText, timeText, locationText, nameText, descriptionText,
            capacityText, priceText, needsLocationText, registrationOpenText, registrationCloseText,
            loadingText;
    private LinearLayout parentLayout;
    private Button joinButton;
    private ImageButton backButton;

    /**
     * sets the layout, assigns all the views and sets up all the on click listeners
     * @param savedInstanceState stores the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout to the view event screen
        setContentView(R.layout.content);

        // find all the views using their ids
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
        parentLayout = findViewById(R.id.parent_layout);
        loadingText = findViewById(R.id.loading_event_textview);

        // get the id of the event from the previous activity(QR Scanner activity)
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        // get the current user
        getCurrentEntrant();

        // handler for the user to add themselves to waitlist
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

        // handler to go back to the home screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * gets the current user from the database using the id stored in shared preference
     */
    protected void getCurrentEntrant(){
        // get the id from shared preferences
        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Not Found");

        // get entrant from database
        entrantDB.getEntrant(ID).thenAccept(user -> {
            if(user != null){
                // set the user as the entrant and get the event
                entrant = user;
                getEvent();
            } else {
                Toast.makeText(EntrantViewActivity.this, "Could not load profile.", Toast.LENGTH_LONG).show();
            }
        // handle exceptions
        }).exceptionally(e -> {
            Toast.makeText(EntrantViewActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

    /**
     * gets the event scanned by the user
     */
    protected void getEvent(){
        // get event from database
        eventDB.getEvent(id).thenAccept(event -> {
            if(event != null){
                // set the event and check if the user is already in the waitlist and set the inWaitList variable
                this.event = event;
                ArrayList<String> waitList = event.getWaitList();
                for(String entrant : waitList){
                    if(entrant.equals(this.entrant.getID())){
                        inWaitlist = true;
                        joinButton.setText(R.string.leave_waitlist);
                        break;
                    }
                }
                // set the views with the event's details
                setViews();
            // if event not found then change the layout to default page
            } else {
                setContentView(R.layout.event_not_found);
                backButton = findViewById(R.id.back_button);

                // handler to go back to home screen
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });

                Toast.makeText(EntrantViewActivity.this, "Event does not exist", Toast.LENGTH_SHORT).show();
            }
        // handle exceptions
        }).exceptionally(e -> {
            Toast.makeText(EntrantViewActivity.this, "Failed to load event: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

    /**
     * sets the views to show the event details
     */
    protected void setViews(){
        // set the date, location, name, capacity and price
        monthText.setText(event.getMonth(event.getDateTime()));
        dateText.setText(event.getDay(event.getDateTime()));
        timeText.setText(event.getTime(event.getDateTime()));
        locationText.setText(event.getGeolocation());
        nameText.setText(event.getName());
        capacityText.setText("Capacity: " + event.getMaxCapacity());
        priceText.setText("Price: " + event.getPrice());

        // if no poster is present then change the visibility of the imageview otherwise show the poster
        if(event.getImage() == null){
            posterImage.setVisibility(View.GONE);
        } else {
            Picasso.get()
                    .load(event.getImage())
                    .into(posterImage);
        }

        // if no description is present then change the visibility of the textview otherwise show the description
        if(event.getDescription().isEmpty()){
            descriptionText.setVisibility(View.GONE);
        } else {
            descriptionText.setText(event.getDescription());
        }

        // set the needs geolocation requirement
        if(event.getNeedsGeolocation()){
            needsLocationText.setText("Requires Location: Yes");
        } else{
            needsLocationText.setText("Requires Location: No");
        }

        // set the registration open and close date
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        registrationOpenText.setText("Registration Opens: " + formatter.format(event.getRegistrationOpen()));
        registrationCloseText.setText("Registration Closes: " + formatter.format(event.getRegistrationClose()));

        // go from loading page to event details page
        loadingText.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);
    }

    /**
     * adds/removes an entrant to/from the waitlist depending on if the entrant is already in the
     * waitlist
     */
    protected void addEntrant(){
        AlertDialog joinDialog;
        // check if entrant is in waitlist
        if (!inWaitlist) {
            String message = "";

            // alert entrant that event requires geolocation
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
                            // check if there is space in waitlist
                            if(event.getWaitList().size() < event.getWaitListLimit()){
                                Date curDate = new Date();

                                // check if registration has closed
                                if(curDate.before(event.getRegistrationClose())){

                                    // check if registration has opened
                                    if(curDate.after(event.getRegistrationOpen())){

                                        // add entrant to waitlist, add event to entrant's
                                        // waitlisted event and update both event and entrant and
                                        // change the join waitlist button text and update inWaitlist
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
        // entrant is already in waitlist
        else {
            joinDialog = new AlertDialog.Builder(EntrantViewActivity.this)
                    .setTitle("Confirm Leaving Waitlist")
                    .setMessage("Are you sure you want to leave the waitlist for this event?")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // delete entrant from waitlist, delete event from entrant's waitlisted
                            // events and update both event and entrant and change the leave
                            // waitlist button text and update inWaitlist
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
