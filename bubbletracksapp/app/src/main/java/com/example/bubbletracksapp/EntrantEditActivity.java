package com.example.bubbletracksapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bubbletracksapp.databinding.ProfileManagementBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;

import android.Manifest; // For importing notification permissions
import android.widget.Toast;

import java.util.Map;

/**
 * This class allows an entrant to update their profile information.
 * INCOMPLETE:
 * There is currently no data validation.
 * There is no way to set the profile picture.
 * @author Zoe, Erza
 */
public class EntrantEditActivity extends AppCompatActivity {

    private ActivityResultLauncher<String[]> requestPermissionLauncher;
    private ProfileManagementBinding binding;
    private Entrant currentUser;
    private EntrantDB db = new EntrantDB();
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String ID;
    // Initialize fields
    private EditText entrantFirstNameInput;
    private EditText entrantLastNameInput;
    private EditText entrantEmailInput;
    private EditText entrantPhoneInput;
    private CheckBox entrantNotificationInput;

    // Initialize views
    private TextView deviceIDNote;
    private TextView locationNote;

    /**
     * Start up activity for entrant to edit their profile
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ProfileManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*
        Launcher to request permission from Entrant
         */
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
                    @Override
                    public void onActivityResult(Map<String, Boolean> o) {
                        if (Boolean.TRUE.equals(o.get(Manifest.permission.POST_NOTIFICATIONS))) {
                            // Permission granted
                            Log.d("Notification check", "Notif permission granted");
                        } else {
                            // Permission denied
                            Log.d("Notification check", "Notif permission denied");
                        }
                        if (Boolean.TRUE.equals(o.get(Manifest.permission.ACCESS_COARSE_LOCATION))) {
                            // Permission granted
                            Log.d("Geolocation check", "Geolocation permission granted");
                        } else {
                            // Permission denied
                            Log.d("Geolocation check", "Geolocation permission denied");
                        }
                    }
                }
        );
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        entrantFirstNameInput = binding.entrantNameInput;
        entrantLastNameInput = binding.entrantNameInput2;
        entrantEmailInput = binding.entrantEmailInput;
        entrantPhoneInput = binding.entrantPhoneInput;
        entrantNotificationInput = binding.notificationToggle;

        deviceIDNote = binding.deviceIDNote;
        locationNote = binding.locationNote;

        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        ID = localID.getString("ID", "Device ID not found");
        String tempLocation = "No last location found. Allow location and update your profile";

        // Displays the user's profile
        deviceIDNote.setText(String.format("Device ID: %s", ID));
        locationNote.setText(tempLocation);
        db.getEntrant(ID).thenAccept(user -> {
            if(user != null){
                currentUser = user;
                if (!currentUser.getName()[0].isBlank()) {
                    entrantFirstNameInput.setText(currentUser.getName()[0]); }

                if(!currentUser.getName()[1].isBlank()) {
                    entrantLastNameInput.setText(currentUser.getName()[1]);
                }

                if (!currentUser.getEmail().isBlank()) {
                    entrantEmailInput.setText(currentUser.getEmail()); }

                if (!currentUser.getPhone().isBlank()) {
                    entrantPhoneInput.setText(currentUser.getPhone()); }

                entrantNotificationInput.setChecked(currentUser.getNotification());

                if(currentUser.getGeolocation() != new LatLng(0,0)) {
                    LatLng location = currentUser.getGeolocation();
                    String stringLocation = String.format("Your last location: (%f,%f)", location.latitude, location.longitude);
                    locationNote.setText(stringLocation);
                }
            } else {
                Toast.makeText(EntrantEditActivity.this, "Could not load profile.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(EntrantEditActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });

        // Handler to go back to homescreen
        Button backButton = binding.profileBack;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Handler for update button (updates database)
        Button updateProfile = binding.profileUpdate;
        updateProfile.setOnClickListener(new View.OnClickListener() {

            /**
             * Action after entrant wants to update their profile
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {

                String newFirst = entrantFirstNameInput.getText().toString();
                String newLast = entrantLastNameInput.getText().toString();
                String newEmail = entrantEmailInput.getText().toString();
                String newPhone = entrantPhoneInput.getText().toString();
                boolean notificationPermission = entrantNotificationInput.isChecked();

                db.getEntrant(ID).thenAccept(user -> {
                    if(user != null){
                        currentUser = user;

                        currentUser.setName(newFirst, newLast);
                        currentUser.setPhone(newPhone);

                        // Extremely loose input validation; make sure email contains a "@" and "."
                        if (!((newEmail.length() - newEmail.replace("@", "").length())==1)
                        || !((newEmail.length() - newEmail.replace(".","").length())==1)){
                            Toast.makeText(EntrantEditActivity.this, "Invalid email", Toast.LENGTH_LONG).show();
                            currentUser.setEmail("");
                        }
                        else {
                            currentUser.setEmail(newEmail);
                        }

                        if (!notificationPermission){
                            currentUser.setNotification(false);
                            Log.d("Notification check", "User opted out of notifications");
                        }
                        else {
                            checkNotificationPermission(currentUser);
                        }

                        // Gets the coarse location of the person and updates it.
                        // If it cant find it, it does not update the location.
                        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
                        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if (location != null) {
                                                // Update the user's location
                                                double lat = location.getLatitude();
                                                double lng = location.getLongitude();
                                                LatLng newGeolocation = new LatLng(lat, lng);
                                                currentUser.setGeolocation(newGeolocation);

                                                // Update the location node
                                                String stringLocation = String.format("Your last location: (%f,%f)", lat, lng);
                                                locationNote.setText(stringLocation);

                                                db.updateEntrant(currentUser);
                                                Log.d("getCurrentLocation", newGeolocation.toString());
                                            }
                                            else
                                            {
                                                db.updateEntrant(currentUser);

                                                Log.w("EntrantEditActivity", "No location could be found. Location was not updated");
                                            }
                                            Log.d("New user name:", currentUser.getNameAsString());
                                        }
                                    });

                        }
                    } else {
                        Log.d("User not found", "");
                    }
                }).exceptionally(e -> {
                    Log.e("Error", e.getMessage());
                    Toast.makeText(EntrantEditActivity.this, "Failed to load user: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                });
            }
        });
        checkPermissions();
    }

    /**
     * Launcher to ask user for notification permission
     * @param currentUser entrant updating their profile
     */
    private void checkNotificationPermission(Entrant currentUser) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the notification permission is granted
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                currentUser.setNotification(true);
                Log.d("Notification check","Notification permission done");
            } else {
                currentUser.setNotification(false);
            }
        }
    }

    /**
     * Launcher to ask user for notification permission for posts and location
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] permissions = {Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACCESS_COARSE_LOCATION};
            // Check if both the notification permissions are granted
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Notification check","Notification permission done");
            }
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions check","Both permissions done");
            }
            else {
                requestPermissionLauncher.launch(permissions);
            }
        }
    }
}
