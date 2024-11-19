/**
 *
 * @author Sarah, Chester
 * @version 1.0
 */

package com.example.bubbletracksapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is a class that displays a user's waitlist/invited list/registered list
 * depending on what needs to be displayed.
 */
public class AppUserEventScreenGenerator extends AppCompatActivity {

    private RecyclerView eventsplace;
    private AppEventAdapter eventAdapter;
    private List<Event> waitlistEvents = new ArrayList<>();
    private List<Event> registeredEvents = new ArrayList<>();
    private Button accept, decline;
    EntrantDB entrantDB = new EntrantDB();
    EventDB eventDB = new EventDB();
    private List<String> otherOption = Arrays.asList("Waitlist", "Registered");
    private Spinner statusSpinner;
    private Entrant user;


    /**
     * Initializes main components of the screen: drop down menu, the event display,
     * Also creates onScroll/onClicklisteners
     * Also Initializes the firebase.
     *
     * @param savedInstanceState
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.event_lists);


        SharedPreferences localID = getSharedPreferences("LocalID", Context.MODE_PRIVATE);
        String ID = localID.getString("ID", "Device ID not found");
        // INITIALIZES DROP DOWN MENU/SPINNER
        statusSpinner = findViewById(R.id.listOptions);

        // CREATES AN ADAPTER FOR SPINNER
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.list_options, android.R.layout.simple_spinner_item);

        // SETS UP ADAPTER
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // SETS UP STATUS SPINNER
        statusSpinner.setAdapter(spinnerAdapter);
        statusSpinner.setSelection(0);

        // SETS UP RECYCLER VIEW
        eventsplace = findViewById(R.id.waitlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventsplace.setLayoutManager(linearLayoutManager);
        eventsplace.setHasFixedSize(true);

        // Initialize a flag outside the listener to control the loop

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // SNAPPING THE SCROLL ITEMS
        SnapHelper snapHelper = new GravitySnapHelper(Gravity.START);
        snapHelper.attachToRecyclerView(eventsplace);

        //SET A TIMER FOR DEFAULT BUTTON
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            RecyclerView.ViewHolder viewHolderDefault = eventsplace.findViewHolderForAdapterPosition(0);

            if (viewHolderDefault != null) {
                LinearLayout eventParentDefault = viewHolderDefault.itemView.findViewById(R.id.eventParent);
                eventParentDefault.animate()
                        .scaleY(1)
                        .scaleX(1)
                        .setDuration(350)
                        .setInterpolator(new AccelerateInterpolator())
                        .start();
            }
        }, 100);

        // ADD ANIMATE SCROLL
        eventsplace.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(linearLayoutManager);
                if (view != null) {
                    int pos = linearLayoutManager.getPosition(view);
                    RecyclerView.ViewHolder viewHolder = eventsplace.findViewHolderForAdapterPosition(pos);
                    if (viewHolder != null) {
                        LinearLayout eventParent = viewHolder.itemView.findViewById(R.id.eventParent);

                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            eventParent.animate().scaleY(1.0f).scaleX(1.0f).setDuration(350).setInterpolator(new AccelerateInterpolator()).start();
                        } else {
                            eventParent.animate().scaleY(0.7f).scaleX(0.7f).setDuration(350).setInterpolator(new AccelerateInterpolator()).start();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        entrantDB.getEntrant(ID).thenAccept(theUser -> {
            if(theUser != null) {
                user = theUser;

                // SETS UP ON CLICK LISTENER FOR SPINNER
                statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        // Get selected item
                        String selectedOption = parentView.getItemAtPosition(position).toString();
                        // Display a Toast or perform actions based on the selected item
                        Toast.makeText(AppUserEventScreenGenerator.this, "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();

                        // Perform different actions based on selection
                        if (selectedOption.equals("Waitlist")) {
                            // Displays Waitlisted Event
                            displayList("Waitlist and Invited", user);
                        } else if (selectedOption.equals("Registered")) {
                            displayList("Registered", user);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }).exceptionally(e -> {
            Toast.makeText(AppUserEventScreenGenerator.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show(); //also a fail
            return null;
        });

    }

    // Method to display the selected list
    /**
     * Displays list
     *
     * @param listType
     * @param user1
     *
     */
    private void displayList(String listType, Entrant user1) {
        if (user1 == null) {
            // Log an error or handle the case when user is not yet available
            Log.e("AppUserEventScreen", "User object is null. Cannot display list.");
            return; // Exit the method early
        } else {
        String regStatus = "unknown"; // Default status if no matching user is found
            eventDB.getEventList(user1.getEventsWaitlist()).thenAccept(events -> {
                List<Event> eventList = events;
                // Initialize the adapter with the event list and the determined registration status
                eventAdapter = new AppEventAdapter(this, eventList, user1, null);
                eventsplace.setAdapter(eventAdapter);

            });
        }
    }



}