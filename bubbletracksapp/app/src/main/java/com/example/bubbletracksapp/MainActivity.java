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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
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

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 *Main Activity for the user. Launches homescreen, with one tab for each user role.
 * @author Zoe
 */
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private HomescreenBinding binding;
    private Entrant currentUser;
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

        Button entrantButton = binding.buttonEntrant;
        Button organizerButton = binding.buttonOrganizer;
        Button adminButton = binding.buttonAdmin;

        // Find out the current user and set button visibility accordingly
        Button createFacilityButton = binding.buttonCreateManageFacility;
        Intent createFacilityIntent = new Intent(MainActivity.this, OrganizerFacilityActivity.class); //class where you are, then class where you wanan go
        Intent manageFacilityIntent = new Intent(MainActivity.this, OrganizerManageActivity.class);

        db.getEntrant(currentDeviceID).thenAccept(user -> {
            if(user != null){
                currentUser = user;
                // Check user role
                if(currentUser.getRole().equals("admin")){
                    Log.d("User role:", "Woohoo, admin");
                    adminButton.setVisibility(View.VISIBLE);
                    organizerButton.setVisibility(View.VISIBLE);
                } else if (currentUser.getRole().equals("organizer")) {
                    Log.d("User role:", "Organizer");
                    organizerButton.setVisibility(View.VISIBLE);
                } else {
                    Log.d("User role:", "Entrant");
                }
                // Check user facility
                if(!user.getFacility().isEmpty()){
                    manageFacilityIntent.putExtra("id", user.getFacility());
                    switchActivityButton(createFacilityButton, manageFacilityIntent);
                    createFacilityButton.setText("MANAGE FACILITY");
                } else {
                    switchActivityButton(createFacilityButton, createFacilityIntent);
                }
            } else {
                // Make a new entrant if they haven't launched the app before.
                currentUser = new Entrant(currentDeviceID);
                switchActivityButton(createFacilityButton, createFacilityIntent);
                db.addEntrant(currentUser);
                Log.d("Added new Entrant",currentUser.getID());}
        }).exceptionally(e -> {
            Toast.makeText(MainActivity.this, "Failed to load user: " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        });

        notificationDB = new NotificationDB();
        notificationDB.listenForNotifications(currentDeviceID, this);

        // Code for the Organizer, Entrant, and Admin buttons
        organizerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, OrganizerFragment.class, null)
                        .setReorderingAllowed(true)
                        //.addToBackStack("") // Having this on the backstack can be annoying.
                        .commit();
                }
            });

        entrantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, EntrantFragment.class, null)
                        .setReorderingAllowed(true)
                        //.addToBackStack("") // Having this on the backstack can be annoying.
                        .commit();
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainerView, AdminFragment.class, null)
                        .setReorderingAllowed(true)
                        //.addToBackStack("") // Having this on the backstack can be annoying.
                        .commit();
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
                intent.putExtra("user", currentUser);
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

    /**
     * Show the notification for user to be given
     * @param newNotification notification details from database
     */
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
            long timestamp = System.currentTimeMillis();
            int notificationID = (currentDeviceID + timestamp).hashCode();
            notificationManager.notify(notificationID, invitedBuilder.build()); // TODO: Notification id is the user id
            notificationDB.markNotificationAsDelivered(newNotification.getId(), currentDeviceID);
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