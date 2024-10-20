package com.example.lotterypersonal;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HostEvent extends Event{

    // ATTRIBUTES
    private ArrayList<String> waitlisted;
    private ArrayList<String> invited;
    private ArrayList<String> registered;
    private Integer capacity;
    private Integer tot_w;
    private Integer tot_r;
    private Date reg_closes;


    // Constructor
    public HostEvent() {
        // Required empty constructor for Firestore
    }



    // SEND INVITATIONS TO 'X' WAITLISTED INDIVIDUALS
    // Send Invitations Method
    public void inviteUsersToEvent(String eventId, List<String> selectedUserIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventId);

        // Prepare the invited users map
        Map<String, Object> updates = new HashMap<>();
        Map<String, String> invitedUsersMap = new HashMap<>();
        for (String userId : selectedUserIds) {
            invitedUsersMap.put(userId, "invited");  // Set the invitation status to "invited"
        }

        updates.put("invitedUsers", invitedUsersMap);  // Update the invitedUsers field in Firestore

        // Update the event document with the invited users
        eventRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                    // Invitation was successful
                    Log.d("TAG", "Users successfully invited to event.");
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                    Log.w("TAG", "Error inviting users", e);
                });
    }


}
