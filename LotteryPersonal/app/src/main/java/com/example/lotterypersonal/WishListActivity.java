package com.example.lotterypersonal;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishListActivity extends AppCompatActivity {

    // ATTRIBUTES
    private FirebaseFirestore db;
    private ListView eventListView;
    private ArrayList<Event> eventList;
    private EventAdapter adapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Button acceptButton = findViewById(R.id.btnAccept);
        Button declineButton = findViewById(R.id.btnDecline);


        eventListView = findViewById(R.id.eventListView);

        // Initialize the event list
        eventList = new ArrayList<>();

        // Fetch and display events and invitation status
        fetchEvents();
    }



    // METHODS
    private void fetchEvents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = getCurrentUser().getUid();

        // Fetch all events
        db.collection("events")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Event event = document.toObject(Event.class);

                            // Check if the current user has an invitation in this event's invitedUsers map
                            Map<String, String> invitedUsers = (Map<String, String>) document.get("invitedUsers");
                            if (invitedUsers != null && invitedUsers.containsKey(userId)) {
                                // Set the invitation status for this event
                                String status = invitedUsers.get(userId);
                                event.setStatus(status);
                            } else {
                                // No invitation for this event
                                event.setStatus("Not Invited");
                            }

                            // Add event to the list
                            eventList.add(event);
                        }

                        // Set adapter for ListView
                        adapter = new EventAdapter(MainActivity.this, eventList);
                        eventList.setAdapter(adapter);
                    }
                });
    }
}