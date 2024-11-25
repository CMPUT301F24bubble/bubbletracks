package com.example.bubbletracksapp;

import static java.util.UUID.randomUUID;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.bubbletracksapp.databinding.HomescreenBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 *Main Activity for the user.
 * @author Zoe
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private HomescreenBinding binding;
    public Entrant currentUser;
    private String currentDeviceID;
    private final String channelID = "channel_id";

    private Notifications notifications;
    private NotificationDB notificationDB;
    private ActivityResultLauncher<String> requestPermissionLauncher;


    /**
     * Set up creation of activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = HomescreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EntrantDB db = new EntrantDB();

        currentDeviceID = getDeviceID();
        Log.d("DeviceID:",currentDeviceID);

        db.getEntrant(currentDeviceID).thenAccept(user -> {
            if(user != null){
                currentUser = user;
            } else {
                currentUser = new Entrant(currentDeviceID);
                db.addEntrant(currentUser);
                Log.d("Added new Entrant",currentUser.getID());}
        }).exceptionally(e -> {
            Toast.makeText(MainActivity.this, "Failed to load user: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });

        notificationDB = new NotificationDB();
        notificationDB.listenForNotifications(currentDeviceID, this);


        Button eventsButton = binding.buttonEvents;
        Button createEventButton = binding.buttonCreateEvents;
        Button scanButton = binding.buttonScan;
        Button ticketsButton = binding.buttonTickets;
        Button profileButton = binding.buttonProfile;
        Button userEventsButton = binding.buttonEvents;
        Button eventHostButton = binding.buttonEventHost;

        Intent createEventIntent = new Intent(MainActivity.this, OrganizerActivity.class); //class where you are, then class where you wanan go
        switchActivityButton(createEventButton, createEventIntent);

        Intent scanIntent = new Intent(MainActivity.this, QRScanner.class);
        switchActivityButton(scanButton, scanIntent);

        Intent profileIntent = new Intent(MainActivity.this, EntrantEditActivity.class);
        switchActivityButton(profileButton, profileIntent);

        Intent userEventsIntent = new Intent(MainActivity.this, AppUserEventScreenGenerator.class);
        switchActivityButton(userEventsButton, userEventsIntent);

        eventHostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true)
                            .add(R.id.content_holder, OrganizerEventHosting.class, null)
                            .commit();
                }
            }
        });

        createNotificationChannel(); // Set notification channel for entrant user
    }
    /**
     * Generalized code for buttons that use start(Activity()
     * @param button is the button that will be clicked
     * @param intent is the intent passed to startActivity()
     */
    public void switchActivityButton(Button button, Intent intent){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }

    /**
     * Create the activity involving the options menu
     * @param menu The options menu in which you place your items.
     *
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Identify what was clicked among the options menu
     * @param item The menu item that was selected.
     *
     * @return true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * This function checks to see if the user has a local ID stored.
     * If they don't it generates one for them.
     * Either way, it returns the ID.
     * @return device ID
     **/
    public String getDeviceID() {
        // The first two lines of this function can be used in any activity
        // To fetch the current device id. You can expect it to never be "Not Found" elsewhere.
        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Not Found");
        if (ID.equals("Not Found")) {
            ID = randomUUID().toString();
            SharedPreferences.Editor editor = localID.edit();
            editor.putString("ID", ID);
            editor.apply();
        };
        return ID;
    }

    /**
     * Sets notification channel for user to receive notifications while in the app
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Entrant Notification Channel";
            String description = "Notification channel for the entrant user to be notified about updates to their event";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

/*    private void checkNotificationPermission(CheckBox checkInvited, CheckBox checkRejected, CheckBox checkConfirmed, CheckBox checkCancelled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Check if the notification permission is granted
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                showNotification(notifications);
            } else {
                // Request the notification permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            // For API levels below 33, permission is not required
        }
    }*/

    public void showNotification(Notifications newNotification) {
        Intent intent = new Intent(this, OrganizerNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder invitedBuilder = new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle(newNotification.getTitle())
                .setContentText(newNotification.getSmallText())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(newNotification.getBigText()))
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        try {
            // Attempt to post the notification
            // ArrayList<String> invitedList = event.getInvitedList();
            String testUserID = "9be104ee-e9e8-4df4-b93f-c3ec0aef750c";
            int userHash = testUserID.hashCode();
            notificationManager.notify(userHash, invitedBuilder.build()); // TODO: Notification id is the user id
        }catch (SecurityException e) {
                // Log the exception or handle it if the notification couldn't be posted
                Log.e("Notification", "Permission denied for posting notification: " + e.getMessage());
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}