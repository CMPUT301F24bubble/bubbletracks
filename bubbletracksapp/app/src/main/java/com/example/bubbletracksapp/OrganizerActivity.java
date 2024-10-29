package com.example.bubbletracksapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrganizerActivity extends AppCompatActivity {

    private EditText nameView, descriptionView, maxCapacityView, priceView;
    private Button createEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // just a place holder for now
        setContentView(R.layout.create_event);

        nameView = findViewById(R.id.editTextEventName);
        descriptionView = findViewById(R.id.editTextEventDescription);
        maxCapacityView = findViewById(R.id.editTextMaxCapacity);
        priceView = findViewById(R.id.editTextPrice);
        createEventButton = findViewById(R.id.buttonSubmitEvent);


        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameView.getText().toString().trim();
                String description = descriptionView.getText().toString().trim();
                String maxCapacity = maxCapacityView.getText().toString().trim();
                String price = priceView.getText().toString().trim();

                if (name.isEmpty() || description.isEmpty() || maxCapacity.isEmpty() || price.isEmpty()) {
                    Toast.makeText(OrganizerActivity.this, "Name, Description, Max Capacity and Price are required fields", Toast.LENGTH_LONG).show();
                } else {
                    Event event = new Event();
                    event.setName(name);
                    event.setDescription(description);
                    event.setMaxCapacity(Integer.parseInt(maxCapacity));
                    event.setPrice(Integer.parseInt(price));
                    event.setQRCode("https://www.bubbletracks.com/events/" + event.getId());
                    EventDB eventDB = new EventDB();
                    eventDB.addEvent(event);
                    setContentView(R.layout.qr_code);
                    ImageView qrCode = findViewById(R.id.imageViewQRCode);
                    QRGenerator qrGenerator = new QRGenerator();
                    try{
                        Bitmap qrBitmap = qrGenerator.generateQRCode(event.getQRCode());
                        qrCode.setImageBitmap(qrBitmap);
                    } catch (WriterException exception){
                        Log.e("OrganizerActivity", "Qr code generation failed", exception);
                    }
                }
            }
        });
    }
}