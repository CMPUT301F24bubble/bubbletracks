package com.example.bubbletracksapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * this class is an activity that allows an organizer to create an event
 * @author Samyak
 * @version 2.0
 */
public class OrganizerActivity extends AppCompatActivity {

    // new event to be created
    private Event event = new Event();

    // date at the beginning of the activity to validate selected dates
    private Date curDate = new Date();

    // declare all views necessary
    private ScrollView parentLayout;
    private EditText nameText, descriptionText, maxCapacityText, priceText, waitListLimitText;
    private ImageButton dateTimeButton, registrationOpenButton, registrationCloseButton, backButton;
    private Button uploadPhotoButton, createButton;
    private TextView dateTimeText, registrationOpenText, registrationCloseText, loadingText;
    private ImageView posterImage;
    private CheckBox requireGeolocationCheckBox;

    // declare Date variables
    private Date dateTime, registrationClose;
    private Date registrationOpen = new Date();

    // Declares an ActivityResultLauncher that will handle the result of an image upload action
    private ActivityResultLauncher<String> uploadImageLauncher;

    // declare uri variable
    private Uri posterUri;

    /**
     * sets the layout, assigns all the views and sets up all the on click listeners
     * @param savedInstanceState stores the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the layout to the create event page
        setContentView(R.layout.create_event);

        // get the id and location of the facility from previous activity
        Intent intent = getIntent();
        event.setFacility(intent.getStringExtra("id"));
        event.setGeolocation(intent.getStringExtra("location")); ;

        // find all the views using their ids
        nameText = findViewById(R.id.textName);
        dateTimeButton = findViewById(R.id.buttonDateTime);
        dateTimeText = findViewById(R.id.textDateTime);
        descriptionText = findViewById(R.id.textDescription);
        registrationOpenButton = findViewById(R.id.buttonRegistrationOpen);
        registrationOpenText = findViewById(R.id.textRegistrationOpen);
        registrationCloseButton = findViewById(R.id.buttonRegistrationClose);
        registrationCloseText = findViewById(R.id.textRegistrationClose);
        maxCapacityText = findViewById(R.id.textMaxCapacity);
        priceText = findViewById(R.id.textPrice);
        waitListLimitText = findViewById(R.id.textWaitListLimit);
        requireGeolocationCheckBox = findViewById(R.id.checkBoxRequireGeolocation);
        uploadPhotoButton = findViewById(R.id.buttonUploadPhoto);
        posterImage = findViewById(R.id.imagePoster);
        createButton = findViewById(R.id.buttonCreate);
        backButton =  findViewById(R.id.back_button);
        parentLayout = findViewById(R.id.parent_layout);
        loadingText = findViewById(R.id.loading_textview);

        // initialize the activity result launcher for the image picker
        uploadImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                posterImage.setImageURI(uri);
                posterUri = uri;
            }
        });

        // handler to go back to the home screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // handler for button for selecting the date and time
        dateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateTime();
            }
        });

        // handler for button for selecting the registration open date
        registrationOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                selectRegistrationOpen();
            }
        });

        // handler for button for selecting the registration close date
        registrationCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                selectRegistrationClose();
            }
        });

        // handler for button for uploading the poster
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        // handler to create the event
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
    }


    /**
     * opens dialogue to select the event's date and time and stores it in dateTime
     */
    protected void selectDateTime(){
        final Calendar calendar = Calendar.getInstance();

        // get the current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OrganizerActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // show the selected date
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                    // get current time
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(OrganizerActivity.this,
                            (timeView, selectedHour, selectedMinute) -> {
                                // show the selected time
                                calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                                calendar.set(Calendar.MINUTE, selectedMinute);
                                calendar.set(Calendar.SECOND, 0);

                                // store the selected time
                                dateTime = calendar.getTime();

                                // format the selected date
                                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                                dateTimeText.setText(formatter.format(dateTime.getTime()));
                            // show current time
                            }, hour, minute, true);
                    timePickerDialog.show();
                // show current date
                }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * opens dialogue to select the event's registration open date and stores it in registrationOpen
     */
    protected void selectRegistrationOpen(){
        final Calendar calendar = Calendar.getInstance();

        // get the current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OrganizerActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // show the selected date and default time
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);

                    // set the selected date
                    registrationOpen = calendar.getTime();

                    // store the selected date
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    registrationOpenText.setText(formatter.format(registrationOpen.getTime()));
                // show current date
                }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * opens dialogue to select the event's registration close date and stores it in registrationClose
     */
    protected void selectRegistrationClose(){
        final Calendar calendar = Calendar.getInstance();

        // get the current date
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(OrganizerActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // show the selected date and default time
                    calendar.set(Calendar.YEAR, selectedYear);
                    calendar.set(Calendar.MONTH, selectedMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
                    calendar.set(Calendar.HOUR_OF_DAY, 23);
                    calendar.set(Calendar.MINUTE, 59);
                    calendar.set(Calendar.SECOND, 59);

                    // set the selected date
                    registrationClose = calendar.getTime();

                    // store the selected date
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    registrationCloseText.setText(formatter.format(registrationClose.getTime()));
                // show current date
                }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * opens the action pick api to select an image
     */
    protected void uploadImage(){
        // launch activity result launcher
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        uploadImageLauncher.launch("image/*");
        posterImage.setImageTintList(null);
    }

    /**
     * creates an event, stores it in the database, switches the layout to show the generated QR code
     * @throws WriterException if QR code generation fails
     * @throws NumberFormatException if parsing of max capacity, price, or waitlist limit fails
     * @throws IllegalArgumentException if required fields are empty or invalid
     * (Firebase-related errors)
     */
    protected void createEvent(){
        // change the visibility of the views to show a loading page
        loadingText.setVisibility(View.VISIBLE);
        parentLayout.setVisibility(View.GONE);

        // extract the necessary fields
        String name = nameText.getText().toString().trim();
        String dateTimeString = dateTimeText.getText().toString().trim();
        String registrationCloseString = registrationCloseText.getText().toString().trim();
        String maxCapacity = maxCapacityText.getText().toString().trim();

        // extract the optional fields
        String description = descriptionText.getText().toString().trim();
        String price = priceText.getText().toString().trim();
        String waitListLimit = waitListLimitText.getText().toString().trim();

        // check if necessary fields have been filled
        if (name.isEmpty() || maxCapacity.isEmpty() || dateTimeString.equals("Not selected")
        || registrationCloseString.equals("Not selected")) {
            Toast.makeText(OrganizerActivity.this, "Name, Date & Time, Max Capacity and " +
            "registration close date are required fields", Toast.LENGTH_LONG).show();
        } else {
            // populate the necessary fields for the new event
            event.setName(name);
            event.setDateTime(dateTime);
            event.setRegistrationClose(registrationClose);

            // populate the optional fields with default or selected values
            event.setDescription(description);
            event.setRegistrationOpen(registrationOpen);
            event.setNeedsGeolocation(requireGeolocationCheckBox.isChecked());

            // check if the selected date and times are not in the past
            if(curDate.after(dateTime) || curDate.after(registrationClose) || curDate.after(registrationOpen)){
                Toast.makeText(OrganizerActivity.this, "The event and registration " +
                        "dates can't be in the past", Toast.LENGTH_LONG).show();
                removeLoadingScreen();          // return from the loading page
                return;
            }

            // check if registration open date is before registration close date which is before event date
            if(!(registrationOpen.before(registrationClose) && registrationClose.before(dateTime))){
                Toast.makeText(OrganizerActivity.this, "Registration open date needs to " +
                        "be before registration close date and registration close date needs to be " +
                        "before the event", Toast.LENGTH_LONG).show();
                removeLoadingScreen();          // return from the loading page
                return;
            }

            // check to see if the capacity is a number
            try {
                event.setMaxCapacity(Integer.parseInt(maxCapacity));
            } catch (NumberFormatException e) {
                Toast.makeText(OrganizerActivity.this, "Please enter a valid capacity", Toast.LENGTH_LONG).show();
                removeLoadingScreen();         // return form the loading page
                return;
            }

            // check to see if capacity is not negative
            if(Integer.parseInt(maxCapacity) < 0){
                Toast.makeText(OrganizerActivity.this, "Capacity cannot be negative", Toast.LENGTH_LONG).show();
                removeLoadingScreen();      // return form the loading page
                return;
            }

            // if price is empty set it to default 0
            if(price.isEmpty()){
                event.setPrice(0);
            } else {
                // check if the price is a number
                try {
                    event.setPrice(Integer.parseInt(price));
                } catch (NumberFormatException e) {
                    Toast.makeText(OrganizerActivity.this, "Please enter a valid price", Toast.LENGTH_LONG).show();
                    removeLoadingScreen();      // return from the loading page
                    return;
                }

                // check if the price is positive
                if(Integer.parseInt(price) < 0){
                    Toast.makeText(OrganizerActivity.this, "Price cannot be negative", Toast.LENGTH_LONG).show();
                    removeLoadingScreen();      // return from the loading page
                    return;
                }
            }

            // if waitlist limit is empty set it to the max possible value
            if(waitListLimit.isEmpty()){
                event.setWaitListLimit(Integer.MAX_VALUE);
            } else {
                // check if waitlist limit is a number
                try {
                    event.setWaitListLimit(Integer.parseInt(waitListLimit));
                } catch (NumberFormatException e) {
                    Toast.makeText(OrganizerActivity.this, "Please enter a valid waitlist limit", Toast.LENGTH_LONG).show();
                    removeLoadingScreen();
                    return;
                }

                // check if waitlist limit is positive
                if(Integer.parseInt(price) < 0){
                    Toast.makeText(OrganizerActivity.this, "Waitlist limit cannot be negative", Toast.LENGTH_LONG).show();
                    removeLoadingScreen();
                    return;
                }
            }

            // if no poster is uploaded then just upload the event otherwise upload the image to storage first
            if(posterUri == null){
                uploadEvent();
            } else {
                // make a firebase storage instance and a filename
                String posterFilename = "posters/" + event.getId() + "poster.jpg";
                StorageReference posterStorageReference = FirebaseStorage.getInstance().getReference(posterFilename);

                // store the poster in firebase storage
                posterStorageReference.putFile(posterUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // get the download url of the image
                                posterStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        // store the download string in the event class
                                        String downloadUrl = uri.toString();
                                        event.setImage(downloadUrl);
                                        uploadEvent();
                                    }
                                    // handle errors
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            // handle errors
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(OrganizerActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
        removeLoadingScreen();
    }

    /**
     * gets the current entrant and updates the entrant in the Database
     * to store id of the created event
     */
    protected void updateEntrant(){
        // get the current user's id
        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Not Found");
        EntrantDB entrantDB = new EntrantDB();

        // get the entrant from the database
        entrantDB.getEntrant(ID).thenAccept(user -> {
            if(user != null){
                // update the user to have the event's id in its events organized list and store it
                user.addToEventsOrganized(event.getId());
                entrantDB.updateEntrant(user);
            } else {
                Toast.makeText(OrganizerActivity.this, "Could not load profile.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(OrganizerActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

    /**
     * generates and uploads the qr code, changes the layout, uploads the event to the DB
     * and updates the facility and entrant
     */
    protected void uploadEvent(){
        // change to a new layout to show the generated QR code
        setContentView(R.layout.qr_code);
        ImageView qrCode = findViewById(R.id.imageViewQRCode);
        ImageButton backButton = findViewById(R.id.back_button);

        QRGenerator qrGenerator = new QRGenerator();
        try{
            // generate the bitmap to show the qrCode
            Bitmap qrBitmap = qrGenerator.generateQRCode(event.getId());
            qrCode.setImageBitmap(qrBitmap);

            // convert to byte array to upload the qr code
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] qrByteArray = stream.toByteArray();

            // set the filename and storage reference
            String QRFilename = "QRCodes/" + event.getId() + "qrcode.png";
            StorageReference QRcodeStorageReference = FirebaseStorage.getInstance().getReference(QRFilename);

            // store the QR code in firebase storage
            QRcodeStorageReference.putBytes(qrByteArray)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // get the download url of the image
                            QRcodeStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // store the download link in the event class
                                    String downloadUrl = uri.toString();
                                    event.setQRCode(downloadUrl);

                                    // store the event
                                    EventDB eventDB = new EventDB();
                                    eventDB.addEvent(event);

                                    // update the facility and entrant to have the event in their events and events hosted lists
                                    updateFacility();
                                    updateEntrant();
                                }
                                // handle errors
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        // handle errors
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OrganizerActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (WriterException exception){
            Log.e("OrganizerActivity", "Qr code generation failed", exception);
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * removes the loading screen to go back to the event creation screen
     */
    protected void removeLoadingScreen(){
        loadingText.setVisibility(View.GONE);
        parentLayout.setVisibility(View.VISIBLE);
    }

    /**
     * updates the facility to have the id of the event in its events list
     */
    protected void updateFacility(){
        FacilityDB facilityDB = new FacilityDB();
        facilityDB.getFacility(event.getFacility()).thenAccept(curFacility -> {
            if(curFacility != null){
                curFacility.addToEventList(event.getId());
                facilityDB.updateFacility(curFacility);
            } else {
                Toast.makeText(OrganizerActivity.this, "Could not find your facility.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(OrganizerActivity.this, "Could not load your facility: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });
    }

}