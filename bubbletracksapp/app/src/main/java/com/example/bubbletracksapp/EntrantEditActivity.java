package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.example.bubbletracksapp.databinding.FragmentFirstBinding;
import com.example.bubbletracksapp.databinding.ProfileManagementBinding;
import com.google.android.gms.tasks.OnCompleteListener;

import android.Manifest; // For importing notification permissions
import android.widget.Toast;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import java.util.ArrayList;

/**
 * This class allows an entrant to update their profile information.
 * INCOMPLETE:
 * There is currently no data validation.
 * There is no way to set the profile picture.
 * @author Zoe, Erza
 */
public class EntrantEditActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ProfileManagementBinding binding;
    private Entrant currentUser;
    EntrantDB db = new EntrantDB();

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
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission granted
                        Log.d("Notification check", "Notif permission granted");
                    } else {
                        // Permission denied
                        Log.d("Notification check", "Notif permission denied");
                    }
                }
        );


        EditText entrantNameInput = binding.entrantNameInput;
        EditText entrantEmailInput = binding.entrantEmailInput;
        EditText entrantPhoneInput = binding.entrantPhoneInput;
        CheckBox entrantNotificationInput = binding.notificationToggle;

        TextView deviceIDNote = binding.deviceIDNote;

        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Device ID not found");

        // Displays the user's profile
        deviceIDNote.setText(ID);
        db.getEntrant(ID).thenAccept(user -> {
            if(user != null){
                currentUser = user;
                if (currentUser.getNameAsString().isBlank()) {
                    entrantNameInput.setText("Enter your name");
                } else {
                entrantNameInput.setText(currentUser.getNameAsString()); }

                if (currentUser.getEmail().isBlank()) {
                    entrantEmailInput.setText("Enter your email");
                } else {
                    entrantEmailInput.setText(currentUser.getEmail()); }

                if (currentUser.getPhone().isBlank()) {
                    entrantPhoneInput.setText("Enter your phone number");
                } else {
                    entrantPhoneInput.setText(currentUser.getPhone()); }

                entrantNotificationInput.setChecked(currentUser.getNotification());
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
                checkNotificationPermission(currentUser);

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

                        if (!notificationPermission){
                            currentUser.setNotification(false);
                            Log.d("Notification check", "User opted out of notifications");
                        }
                        else {
                            checkNotificationPermission(currentUser);

                        }

                        db.updateEntrant(currentUser);

                        Log.d("New user name:", currentUser.getNameAsString());
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
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }


    }
}
