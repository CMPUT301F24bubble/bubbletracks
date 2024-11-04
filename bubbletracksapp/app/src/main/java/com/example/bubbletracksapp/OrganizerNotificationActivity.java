package com.example.bubbletracksapp;

import static android.icu.number.NumberRangeFormatter.with;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.Manifest; // For importing post notification permissions (MUST CHANGE SO THAT WE NEED TO REQUEST NOTIFICATIONS FROM SOMEWHERE)

/**
 * Organizer sends notifications to chosen entrants to sign up for events
 *
 */
public class OrganizerNotificationActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "notify_channel";
    private static final Integer NOTIFICATION_ID = 123;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    /*
     * TODO: access the firebase to notify specific entrants based on checkboxes/permissions
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_main);

        createNotificationChannel();

        // ENTRANT NEEDS TO ALLOW PERMISSION FOR NOTIFICATION (so far the notification is given to the organizer and not the entrant)
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        // Permission is granted, show notification
                        showNotification();
                    } else {
                        Toast.makeText(this, "Notification permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Button notif_button = findViewById(R.id.button_confirm);
        notif_button.setOnClickListener(view -> checkNotificationPermission());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the notification permission is granted
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                showNotification();
            } else {
                // Request the notification permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            // For API levels below 33, permission is not required
            showNotification();
        }
    }

    private void showNotification() {
        Toast toast = Toast.makeText(this /* MyActivity */, "Notification show?", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(this, OrganizerNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Much longer text that cannot fit one line..."))
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            // Attempt to post the notification
            notificationManager.notify((Integer) NOTIFICATION_ID, builder.build());
        } catch (SecurityException e) {
            // Log the exception or handle it if the notification couldn't be posted
            Log.e("Notification", "Permission denied for posting notification: " + e.getMessage());
            Toast.makeText(this, "Permission required to show notifications", Toast.LENGTH_SHORT).show();
        }
    }

}
