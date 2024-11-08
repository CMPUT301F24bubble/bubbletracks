package com.example.bubbletracksapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * this class is an activity that allows an organizer to create an event
 * @author Samyak
 * @version 1.0
 */
public class OrganizerActivity extends AppCompatActivity {

    // declare all views necessary
    /** edit text for the name */
    private EditText nameText;
    /** button to select the date and time */
    private ImageButton dateTimeButton;
    /** text view to show the date and time */
    private TextView dateTimeText;
    /** edit text for the description */
    private EditText descriptionText;
    /** button to select the location */
    private ImageButton locationButton;
    /** text view to display the selected location */
    private TextView locationText;
    /** button to set the registration opening date */
    private ImageButton registrationOpenButton;
    /** text view to display the registration opening date */
    private TextView registrationOpenText;
    /** button to set the registration closing date */
    private ImageButton registrationCloseButton;
    /** Text view to display the registration closing date */
    private TextView registrationCloseText;
    /** edit text for the maximum capacity */
    private EditText maxCapacityText;
    /** edit text for the price */
    private EditText priceText;
    /** edit text for the waitlist limit */
    private EditText waitListLimitText;
    /** checkbox for require geolocation */
    private CheckBox requireGeolocationCheckBox;
    /** button to upload a poster */
    private Button uploadPhotoButton;
    /** image view to display the uploaded poster */
    private ImageView posterImage;
    /** button to create and save */
    private Button createButton;


    // declare date variables
    /** date and time of the event */
    private Date dateTime;
    /** registration open date of the event */
    private Date registrationOpen;
    /** registration close date of the event */
    private Date registrationClose;


    // Declares an ActivityResultLauncher needed
    /** activity result launcher for the location search */
    private ActivityResultLauncher<Intent> autocompleteLauncher;
    /** activity result launcher for the image upload */
    private ActivityResultLauncher<String> uploadImageLauncher;

    /** fields to be extracted from the selected location */
    private List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.ADDRESS);

    /** uri for the image uploaded */
    private Uri posterUri;

    /** address of the selected location */
    // declare place variable
    private String location;

    /**
     * sets the layout, assigns all the views declared and sets up all the on click listeners
     * @param savedInstanceState stores the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the places API
        Places.initialize(getApplicationContext(), "AIzaSyBOt38qDT81Qj6jR0cmNHVGs3hPPw0XrZA");

        // set the layout to the create event page
        setContentView(R.layout.create_event);

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
        locationText = findViewById(R.id.textLocation);
        locationButton = findViewById(R.id.buttonLocation);

        // initialize the activity result launcher for the image picker
        uploadImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                posterImage.setImageURI(uri);
                posterUri = uri;
            }
        });

        // initialize the activity result launcher for the location search
        autocompleteLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    location = Autocomplete.getPlaceFromIntent(result.getData()).getAddress();
                    locationText.setText(location);
                }
            }
        );

        // handler for button for selecting the date and time
        dateTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateTime();
            }
        });

        // handler for button for selecting location
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectLocation();
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
     * opens google places API to search for a location and stores it in location
     */
    protected void selectLocation(){

        // launch activity result launcher
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(OrganizerActivity.this);
        autocompleteLauncher.launch(intent);
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
        // input validation is not complete, all fields need to be filled otherwise there will be errors

        // extract all the text views
        String name = nameText.getText().toString().trim();
        String dateTimeString = dateTimeText.getText().toString().trim();
        String description = descriptionText.getText().toString().trim();
        String registrationCloseString = registrationCloseText.getText().toString().trim();
        String maxCapacity = maxCapacityText.getText().toString().trim();
        String price = priceText.getText().toString().trim();
        String waitListLimit = waitListLimitText.getText().toString().trim();

        if (name.isEmpty() || maxCapacity.isEmpty() || dateTimeString.equals("Not selected")
        || registrationCloseString.equals("Not selected")) {
        Toast.makeText(OrganizerActivity.this, "Name, Date & Time, Max Capacity and " +
        "registration close date are required fields", Toast.LENGTH_LONG).show();
        } else {

            // create a new event class and store all the fields
            Event event = new Event();
            event.setName(name);
            event.setGeolocation(location);
            event.setDateTime(dateTime);
            event.setDescription(description);
            event.setRegistrationOpen(registrationOpen);
            event.setRegistrationClose(registrationClose);
            event.setMaxCapacity(Integer.parseInt(maxCapacity));
            event.setPrice(Integer.parseInt(price));
            event.setWaitListLimit(Integer.parseInt(waitListLimit));
            event.setNeedsGeolocation(requireGeolocationCheckBox.isChecked());
            event.setQRCode("https://www.bubbletracks.com/events/" + event.getId());

            // make a firebase storage instance and a filename
            String filename = "posters/" + System.currentTimeMillis() + ".jpg";
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filename);

            // store the image in firebase storage
            storageReference.putFile(posterUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // get the download url of the image
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {

                                @Override
                                public void onSuccess(Uri uri) {

                                    // store the download string in the event class
                                    String downloadUrl = uri.toString();
                                    event.setImage(downloadUrl);

                                    // store the event
                                    EventDB eventDB = new EventDB();
                                    eventDB.addEvent(event);

                                    // change to a new layout to show the generated QR code
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

}