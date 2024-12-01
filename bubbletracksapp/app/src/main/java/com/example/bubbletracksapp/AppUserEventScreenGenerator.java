package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This is a class that displays a user's waitlist/invited list/registered list
 * depending on what needs to be displayed.
 */
public class AppUserEventScreenGenerator extends AppCompatActivity {

    private RecyclerView eventsplace;
    private AppEventAdapter eventAdapter;
    private TextView locationView, timeView;
    private ImageButton backButton;
    EntrantDB entrantDB = new EntrantDB();
    EventDB eventDB = new EventDB();
    private Spinner statusSpinner;
    private Entrant currentUser;
    List<Event> eventList = new ArrayList<>();

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
        Log.d("TAG", ID);
        // INITIALIZES DROP DOWN MENU/SPINNER
        statusSpinner = findViewById(R.id.listOptions);

        // CREATES AN ADAPTER FOR SPINNER
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.list_options, android.R.layout.simple_spinner_item);

        // SETS UP SPINNER ADAPTER
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // SETS UP STATUS SPINNER
        statusSpinner.setAdapter(spinnerAdapter);
        statusSpinner.setSelection(0);

        // SETS UP TEXT VIEWS
        timeView = findViewById(R.id.eventDateTime);
        locationView = findViewById(R.id.eventLocation);
        setDefaultActivityText();

        // SETS UP RECYCLER VIEW
        eventsplace = findViewById(R.id.waitlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        eventsplace.setLayoutManager(linearLayoutManager);
        eventsplace.setHasFixedSize(true);

        // CREATE AND SET EVENT ADAPTER with the event list and the determined registration status
        eventAdapter = new AppEventAdapter(AppUserEventScreenGenerator.this,
                eventList, currentUser);
        eventsplace.setAdapter(eventAdapter);

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

                    // SET THE TEXT VIEWS BELOW THE EVENT
                    setTextViews(eventList.get(pos));

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
                currentUser = theUser;
                eventAdapter.setUser(currentUser);

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
                            displayList("Waitlist");
                        } else if (selectedOption.equals("Registered")) {
                            displayList("Registered");
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

                // INITIALIZE BUTTON TO GO BACK TO HOME SCREEN
                backButton = findViewById(R.id.back_button);

                // IMPLEMENTED BUTTON TO GO BACK TO HOME SCREEN
                backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(AppUserEventScreenGenerator.this, MainActivity.class);
                        intent.putExtra("user", currentUser);
                        startActivity(intent);
                    }
                });

            }
        }).exceptionally(e -> {
            Toast.makeText(AppUserEventScreenGenerator.this, "Failed to load profile: " + e.getMessage(), Toast.LENGTH_LONG).show(); //also a fail
            return null;
        });

    }


    /**
     * Displays list, whether it is the waitlist or enrolled list
     * @param type the type of list to be displayed
     */
    private void displayList(String type) {
        if (currentUser == null) {
            // Log an error or handle the case when user is not yet available
            Log.e("AppUserEventScreen", "User object is null. Cannot display list.");
            return; // Exit the method early
        } else {
            if(Objects.equals(type, "Waitlist"))
            {
                setEventList(currentUser.getEventsWaitlist());
            }
            else if (Objects.equals(type, "Registered")) {
                setEventList(currentUser.getEventsEnrolled());
            }
        }
    }

    /**
     * Set the events of the adapter, depending if the user is looking at the events
     * they are waiting to join or the events they enrolled.
     * @param eventIDs The IDs of the events that will be set
     */
    private void setEventList(ArrayList<String> eventIDs) {
        eventDB.getEventList(eventIDs).thenAccept(events -> {
            if(events != null) {
                eventList.clear();
                eventList.addAll(events);
                eventAdapter.notifyDataSetChanged();
                // Set the date for the first event shown
                setTextViews(eventList.get(0));
            }
            else
            {
                eventList.clear();
                eventAdapter.notifyDataSetChanged();
                // If there are no events, set the default events
                setDefaultActivityText();
            }
        });
    }

    /**
     * Set the the text below the event being displayed. The location and time.
     * @param event The event that is being displayed
     */
    private void setTextViews(Event event) {
        // GET EVENT LOCATION
        String location = event.getGeolocation();

        //GET EVENT TIME
        Date eventDate = event.getDateTime();
        String eventTime = String.format("%s %s %s at %s", event.getMonth(eventDate),
                event.getDay(eventDate), event.getYear(eventDate), event.getTime(eventDate));

        // SET DISPLAY TEXT
        timeView.setText(eventTime);
        locationView.setText(location);
    }


    /**
     * Empty the text views of time and location.
     * It is used when no event is being displayed
     */
    private void setDefaultActivityText() {
        timeView.setText(" ");
        locationView.setText(" ");
    }

}