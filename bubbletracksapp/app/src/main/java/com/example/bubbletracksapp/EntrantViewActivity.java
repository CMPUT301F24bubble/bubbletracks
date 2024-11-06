package com.example.bubbletracksapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class EntrantViewActivity extends AppCompatActivity {

    private Event event;
    private String id;

    private ImageView posterImage;
    private TextView monthText, dateText, timeText, locationText, nameText, descriptionText,
            capacityText, priceText, needsLocationText;

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


        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        getEvent();

    }

    protected void getEvent(){
        EventDB eventDB = new EventDB();

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

}
