package com.example.bubbletracksapp;

import android.content.Context;
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

import java.util.ArrayList;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class EntrantEditActivity extends AppCompatActivity {
    /**
     * This class allows entrant to update their profile information.
     * INCOMPLETE:
     * There is currently no data validation.
     * There is no way to set the profile picture.
     * There is no true current user; data is not entered into the database.
     */
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ProfileManagementBinding binding;
    private Entrant currentUser;
    EntrantDB db = new EntrantDB();

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
                entrantNameInput.setText(currentUser.getNameAsString());
                entrantEmailInput.setText(currentUser.getEmail());
                entrantPhoneInput.setText(currentUser.getPhone());
                entrantNotificationInput.setChecked(currentUser.getNotification());
            } else {
                Toast.makeText(EntrantEditActivity.this, "Could not load profile.", Toast.LENGTH_LONG).show();
            }
        }).exceptionally(e -> {
            Toast.makeText(EntrantEditActivity.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });

        // Update button functionality
        Button updateProfile = binding.profileUpdate;
        updateProfile.setOnClickListener(new View.OnClickListener() {

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
