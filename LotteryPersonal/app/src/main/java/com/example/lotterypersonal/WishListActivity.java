package com.example.lotterypersonal;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WishListActivity extends AppCompatActivity {

    // ATTRIBUTES
    Button acceptButton = findViewById(R.id.btnAccept);
    Button declineButton = findViewById(R.id.btnDecline);

    // SETS ON CLICK LISTENERS FOR APPROPRIATE BUTTONS2
    acceptButton.setOnClickListener(v -> acceptInvitation(invitationId));
    declineButton.setOnClickListener(v -> declineInvitation(invitationId));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        eventListView = findViewById(R.id.eventListView);

        // Initialize the event list
        eventList = new ArrayList<>();

        // Fetch and display events and invitation status
        fetchEvents();
    }



    // METHODS
    private void fetchEvents() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

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




    // ACCEPTS INVITATION OF THE SPECIFIC PENDING INVITATION
    public void acceptInvitation(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventId);

        // Update status to "accepted"
        eventRef.update("status", "accepted")
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Invitation accepted"))
                .addOnFailureListener(e -> Log.w("TAG", "Error updating document", e));
    }

    // DECLINES INVITATION OF THE SPECIFIC PENDING INVITATION
    public void declineInvitation(String eventId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference invitationRef = db.collection("invitations").document(eventId);

        // Update status to "declined"
        invitationRef.update("status", "declined")
                .addOnSuccessListener(aVoid -> Log.d("TAG", "Invitation declined"))
                .addOnFailureListener(e -> Log.w("TAG", "Error updating document", e));
    }
}