package com.example.bubbletracksapp;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

/**
 * Send notifications to entrants
 * Organizers
 */
public class Notification extends AppCompatActivity {

    private static final String CHANNEL_ID = "my_channel";
    private static final int NOTIF_ID = 1;

    private NotificationManager notificationManager ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_attendees);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        Button notifyButton = findViewById(R.id.notify_button);
        notifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotification(view);
            }
        });
    }


       public void showNotification(View view) {
           Intent intent = new Intent(this, Notification.class);
           PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
           NotificationCompat.Builder builder = null;
           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                       .setSmallIcon(R.drawable.baseline_notifications_24)
                       .setContentTitle("Notification")
                       .setContentText("This is a notification")
                       .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                       .setContentIntent(pendingIntent)
                       .setAutoCancel(true);

           }

           android.app.Notification notification;
           notification = builder.build();
           notificationManager.notify(NOTIF_ID, notification);
       }

        private void createNotificationChannel(){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence channelName = "My Channel";
                String channelDescription = "My Channel Description";

                int importance = NotificationManager.IMPORTANCE_DEFAULT;

                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, importance);
                channel.setDescription(channelDescription);

                notificationManager.createNotificationChannel(channel);
            }
        }
    }

