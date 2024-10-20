package com.example.lotterypersonal;

import java.util.ArrayList;

public class Entrant extends User{

    // Attributes
    private String entrantId;
    private String name;
    private String email;
    private ArrayList<String> eventIds;
    private DbUtil db;


    // Methods to accept or decline an invitation
    public void acceptInvite(String eventId, String entrantId) {
        // Fetch current status from the database for the user in this event
        String currentStatus = db.getStatusForUserInEvent(eventId, entrantId);

        if ("invited".equals(currentStatus)) {
            // Update status to "registered"
            boolean updateSuccess = db.updateUserStatus(eventId, entrantId, "registered");
        } else {
            System.out.println("No invitation to accept.");
        }
    }



    public void declineInvite(String eventId, String entrantId) {
        // Fetch current status from the database for the user in this event
        String currentStatus = db.getStatusForUserInEvent(eventId, entrantId);

        if ("invited".equals(currentStatus)) {
            // Update status to "waitlisted"
            boolean updateSuccess = db.updateUserStatus(eventId, entrantId, "waitlisted");
        } else {
            System.out.println("No invitation to decline.");
        }
    }

}
