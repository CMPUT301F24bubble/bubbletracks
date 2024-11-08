package com.example.bubbletracksapp;

import android.annotation.SuppressLint;
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


public class AppMain extends AppCompatActivity {

    private RecyclerView eventsplace;
    private AppEventAdapter eventAdapter;
    private List<AppEvent> waitlistEvents = new ArrayList<>();
    private List<AppEvent> registeredEvents = new ArrayList<>();
    private Button accept, decline;
    FirebaseFirestore firestore;
    private List<String> otherOption = Arrays.asList("Waitlist", "Registered");
    private Spinner statusSpinner;
    private AppUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.event_lists);

        // SETS UP DATABASE INSTANCE
        firestore = FirebaseFirestore.getInstance();

        // INITIALIZES DROP DOWN MENU/SPINNER
        statusSpinner = findViewById(R.id.listOptions);

        //setupSpinner(new ArrayList<>(allOptions));

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
        // SETS UP ON CLICK LISTENER FOR SPINNER
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {


                // Get selected item
                String selectedOption = parentView.getItemAtPosition(position).toString();
                // Display a Toast or perform actions based on the selected item
                Toast.makeText(AppMain.this, "Selected: " + selectedOption, Toast.LENGTH_SHORT).show();



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


    }




    // Method to display the selected list
    private void displayList(String listType, AppUser user1) {
        List<AppEvent> eventList =  new ArrayList<>();
        String regStatus = "unknown"; // Default status if no matching user is found

        // Determine which list to display
        if (listType.equals("Waitlist and Invited")) {
            eventList.addAll(user1.getInvitedEvents());
            eventList.addAll(user1.getWaitlistEvents());
        } else if (listType.equals("Registered")) {
            eventList.addAll(user1.getRegisteredEvents());
        } else {
            eventList = new ArrayList<>(); // Fallback to an empty list if listType is unexpected
        }

        // Loop through events and set regStatus if user is found in the list
        for (AppEvent event : eventList) {
            for (AppUser user : event.getAllUsers()) {
                if (user1.getDevice_id()!= null && user1.getDevice_id().equals(user.getDevice_id())) {
                    regStatus = user.getRegStatus(event); // Set regStatus if a matching user is found
                    break; // Break out of the loop once we find a matching user
                }
            }
        }

        // Initialize the adapter with the event list and the determined registration status
        eventAdapter = new AppEventAdapter(this, eventList, user1, null);
        eventsplace.setAdapter(eventAdapter);
    }



}