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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class OrganizerActivity extends AppCompatActivity {

    private EditText nameView, descriptionView, maxCapacityView, priceView;
    private Button createEventButton, dateTimeSelectButton, registrationOpenButton, registrationCloseButton;
    private TextView dateTimeView, registrationOpenView, registrationCloseView;
    private Date selectedDateTime, registrationOpen, registrationClose;

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
        dateTimeSelectButton = findViewById(R.id.buttonSelectDateTime);
        dateTimeView = findViewById(R.id.textViewEventDateTime);
        registrationOpenButton = findViewById(R.id.buttonSelectRegistrationDate);
        registrationOpenView = findViewById(R.id.textViewRegistrationDate);
        registrationCloseButton = findViewById(R.id.buttonSelectCloseDate);
        registrationCloseView = findViewById(R.id.textViewCloseDate);

        dateTimeSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateTime();
            }
        });

        registrationOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                selectRegistrationOpen();
            }
        });

        registrationCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                selectRegistrationClose();
            }
        });


        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameView.getText().toString().trim();
                String description = descriptionView.getText().toString().trim();
                String maxCapacity = maxCapacityView.getText().toString().trim();
                String price = priceView.getText().toString().trim();
                String dateTime = dateTimeView.getText().toString().trim();
                String registrationOpenDate = registrationOpenView.getText().toString().trim();
                String registrationCloseDate = registrationCloseView.getText().toString().trim();

                if (name.isEmpty() || description.isEmpty() || maxCapacity.isEmpty() || price.isEmpty() || dateTime.equals("Not selected") || registrationOpenDate.equals("Not selected") || registrationCloseDate.equals("Not selected")) {
                    Toast.makeText(OrganizerActivity.this, "Name, Date & Time, Description, Max Capacity and Price are required fields", Toast.LENGTH_LONG).show();
                } else {
                    Event event = new Event();
                    event.setName(name);
                    event.setDateTime(selectedDateTime);
                    event.setDescription(description);
                    event.setRegistrationOpen(registrationOpen);
                    event.setRegistrationClose(registrationClose);
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

    protected void selectDateTime(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OrganizerActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(OrganizerActivity.this,
                            (timeView, selectedHour, selectedMinute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                calendar.set(Calendar.SECOND, 0);

                                selectedDateTime = calendar.getTime();

                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                dateTimeView.setText(formatter.format(selectedDateTime.getTime()));

                            }, hour, minute, true);

                    timePickerDialog.show();

                }, year, month, day);

        datePickerDialog.show();
    }

    protected void selectRegistrationOpen(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OrganizerActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    registrationOpen = calendar.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    registrationOpenView.setText(formatter.format(registrationOpen.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }

    protected void selectRegistrationClose(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OrganizerActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);
                    registrationClose = calendar.getTime();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    registrationCloseView.setText(formatter.format(registrationClose.getTime()));
                }, year, month, day);
        datePickerDialog.show();
    }
}