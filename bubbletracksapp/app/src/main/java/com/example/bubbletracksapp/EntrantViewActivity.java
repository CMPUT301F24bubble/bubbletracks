package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import java.util.Date;

public class EntrantViewActivity extends AppCompatActivity {

    private Event event;
    private String id;
    private Boolean inWaitlist = false;
    private Entrant entrant = new Entrant();
    private EventDB eventDB = new EventDB();

    private ImageView posterImage;
    private TextView monthText, dateText, timeText, locationText, nameText, descriptionText,
            capacityText, priceText, needsLocationText;
    private Button joinButton;

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


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getEvent();

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEntrant();
            }
        });

    }

    protected void getEvent(){

        eventDB.getEvent(id).thenAccept(event -> {
            if(event != null){
                this.event = event;
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

    }

    protected void addEntrant(){
        AlertDialog joinDialog;
        if (!inWaitlist) { // Entrant wants to join waitlist
            joinDialog = new AlertDialog.Builder(EntrantViewActivity.this)
                    .setTitle("Confirm Joining Waitlist")
                    .setMessage("Are you sure you want to join the waitlist for this event?")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(event.getWaitList().size() < event.getWaitListLimit()){
                                Date curDate = new Date();
                                if(curDate.before(event.getRegistrationClose())){
                                    if(curDate.after(event.getRegistrationOpen())){
                                        event.addToWaitList(entrant);
                                        eventDB.updateEvent(event);
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
                            event.deleteFromWaitList(entrant);
                            eventDB.updateEvent(event);
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
