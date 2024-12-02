package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
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
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bubbletracksapp.databinding.ProfileManagementBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
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
    private EditText entrantNameInput;
    private EditText entrantEmailInput;
    private EditText entrantPhoneInput;
    private CheckBox entrantNotificationInput;
    private ImageView profilePictureImage;
    private ImageButton updatePictureButton;
    private ImageButton deletePictureButton;

    // Initialize views
    private TextView deviceIDNote;
    private TextView locationNote;

    private ActivityResultLauncher<String> uploadImageLauncher;

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


        entrantNameInput = binding.entrantNameInput;
        entrantEmailInput = binding.entrantEmailInput;
        entrantPhoneInput = binding.entrantPhoneInput;
        entrantNotificationInput = binding.notificationToggle;
        profilePictureImage = binding.profileImage;
        updatePictureButton = binding.pictureUpdate;
        deletePictureButton = binding.deleteProfilePicture;

        // initialize the activity result launcher for the image picker
        uploadImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                String filename = "profile-pictures/" + currentUser.getID() + "profilePicture.jpg";
                StorageReference storageReference = FirebaseStorage.getInstance().getReference(filename);

                storageReference.putFile(uri)
                        // upload file
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // get the download url of the image
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri downloadUri) {
                                        // update the download string in the event class and update the event
                                        Toast.makeText(EntrantEditActivity.this, "Profile Picture updated", Toast.LENGTH_SHORT).show();
                                        String downloadUrl = downloadUri.toString();
                                        currentUser.setProfilePicture(downloadUrl);
                                        db.updateEntrant(currentUser);
                                        profilePictureImage.setImageURI(uri);
                                    }
                                    // handle errors
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EntrantEditActivity.this, "Failed to get download URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            // handle errors
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EntrantEditActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        deviceIDNote = binding.deviceIDNote;
        locationNote = binding.locationNote;

        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        ID = localID.getString("ID", "Device ID not found");
        String tempLocation = "No last location found. Allow location and update your profile";

        deviceIDNote.setText(ID);
        locationNote.setText(tempLocation);
        db.getEntrant(ID).thenAccept(user -> {
            if(user != null){
                currentUser = user;
                // Displays the user's profile
                if(!currentUser.getProfilePicture().isEmpty()){
                    Picasso.get().load(currentUser.getProfilePicture()).into(profilePictureImage);
                }
                if (!currentUser.getNameAsString().isBlank()) {entrantNameInput.setText(currentUser.getNameAsString()); }

                if (!currentUser.getNameAsString().isBlank()) {entrantNameInput.setText(currentUser.getNameAsString()); }

                if (!currentUser.getNameAsString().isBlank()) {entrantNameInput.setText(currentUser.getNameAsString()); }

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
        ImageButton backButton = binding.profileBack;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntrantEditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        deletePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentUser.isDefaultPicture()){
                    Toast.makeText(EntrantEditActivity.this, "Cannot delete default profile picture", Toast.LENGTH_SHORT).show();
                } else {
                    currentUser.deleteProfilePic();
                    Picasso.get().load(currentUser.getProfilePicture()).into(profilePictureImage);
                    Toast.makeText(EntrantEditActivity.this, "Deleted Profile Picture", Toast.LENGTH_SHORT).show();
                }
            }
        });

        updatePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch activity result launcher
                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                uploadImageLauncher.launch("image/*");
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

                String[] newFullName = entrantNameInput.getText().toString().split(" ");
                String newFirst = newFullName[0], newLast = newFullName[1];
                String newEmail = entrantEmailInput.getText().toString();
                String newPhone = entrantPhoneInput.getText().toString();
                boolean notificationPermission = entrantNotificationInput.isChecked();


                db.getEntrant(ID).thenAccept(user -> {
                    if(user != null){
                        currentUser = user;

                        currentUser.setName(newFirst, newLast);
                        currentUser.setPhone(newPhone);
                        currentUser.setEmail(newEmail);

                        if(currentUser.isDefaultPicture()){
                            Picasso.get().load(currentUser.setDefaultPicture()).into(profilePictureImage);
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
