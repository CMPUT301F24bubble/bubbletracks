package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Admin class to manage administrative actions like deleting events and profiles
 * from Firestore and handling related updates for both events and profiles.
 * @author Gwen
 */
public class Admin {

    private Context context;

    /**
     * Constructor to initialize the Admin class with the provided context.
     *
     * @param context The context where the Admin actions will take place (e.g., Activity or Application context).
     */
    public Admin(Context context){
        this.context = context;
    }

    /**
     * Deletes an event from the "events" collection and removes references to the event
     * from the corresponding lists in the "entrants" collection.
     *
     * @param context The context where the deletion is being performed.
     * @param eventRef The event that needs to be deleted.
     */
    public void deleteEvent(Context context, DocumentReference eventRef) {
        if (eventRef == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventId = eventRef.getId();
        WriteBatch batch = db.batch();

        batch.delete(eventRef);

        // List of subcategories to check in entrants' data
        List<String> subcategories = Arrays.asList("enrolled", "invited", "organized", "waitlist");

        // Iterate through all entrant documents and update their lists by removing the event reference
        db.collection("entrants")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    // Iterate through each entrant document
                    for (DocumentSnapshot entrantDoc : querySnapshot.getDocuments()) {
                        DocumentReference entrantRef = entrantDoc.getReference();
                        Map<String, Object> entrantData = entrantDoc.getData();

                        if (entrantData != null) {
                            for (String subcategory : subcategories) {
                                if (entrantData.containsKey(subcategory)) {
                                    List<String> eventList = (List<String>) entrantData.get(subcategory);
                                    if (eventList != null && eventList.contains(eventId)) {
                                        eventList.remove(eventId); // Remove the event ID
                                        batch.update(entrantRef, subcategory, eventList); // Update the document
                                    }
                                }
                            }
                        }
                    }

                    batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Event and references deleted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("DeleteEvent", "Error deleting event or updating references: ", e);
                                Toast.makeText(context, "Failed to delete event. Try again.", Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteEvent", "Error fetching entrant data: ", e);
                    Toast.makeText(context, "Failed to fetch related data. Try again.", Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Deletes an entrant's profile from the "entrants" collection and removes any
     * references to the profile from the corresponding lists in the "events" collection.
     *
     * @param context The context where the deletion is being performed.
     * @param entrantToDelete The entrant object whose profile needs to be deleted.
     */
    public void deleteEntrant(Context context, Entrant entrantToDelete) {
        if (entrantToDelete == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String profileId = entrantToDelete.getID();
        WriteBatch batch = db.batch();

        DocumentReference profileRef = db.collection("entrants").document(profileId);
        batch.delete(profileRef);

        // List of subcategories where profile IDs might be referenced
        List<String> subcategories = Arrays.asList("cancelled", "enrolled", "invited", "rejected", "wait");

        // Check if the entrant has any events in the "organized" subcategory
        if (entrantToDelete.getOrganized() != null && !entrantToDelete.getOrganized().isEmpty()) {
            List<String> organizedEvents = entrantToDelete.getOrganized();

            // Delete each organized event
            for (String eventId : organizedEvents) {
                DocumentReference eventRef = db.collection("events").document(eventId);
                deleteEvent(context, eventRef);
            }
        }

        // Iterate through all event documents and update their lists by removing the profile reference
        db.collection("events")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot eventDoc : querySnapshot.getDocuments()) {
                        DocumentReference eventRef = eventDoc.getReference();
                        Map<String, Object> eventData = eventDoc.getData();

                        if (eventData != null) {
                            // Iterate over subcategories and remove the profile from each list where it exists
                            for (String subcategory : subcategories) {
                                if (eventData.containsKey(subcategory)) {
                                    List<String> profileList = (List<String>) eventData.get(subcategory);
                                    if (profileList != null && profileList.contains(profileId)) {
                                        profileList.remove(profileId); // Remove the profile ID
                                        batch.update(eventRef, subcategory, profileList); // Update the document
                                    }
                                }
                            }
                        }
                    }

                    batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(context, "Profile and references deleted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("DeleteProfile", "Error deleting profile or references: ", e);
                                Toast.makeText(context, "Failed to delete profile. Try again.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteProfile", "Error fetching entrant references: ", e);
                    Toast.makeText(context, "Failed to fetch related data. Try again.", Toast.LENGTH_SHORT).show();
                });

}

    /**
     * Deletes a facility from the "facilities" collection and performs cascading deletions:
     * - Deletes all events associated with the facility.
     * - Clears the facility reference from the organizer's document.
     * - Updates the organizer's role to "entrant".
     *
     * @param context The context where the deletion is being performed.
     * @param facilityRef The DocumentReference to the facility that needs to be deleted.
     */
    public void deleteFacility(Context context, DocumentReference facilityRef) {
        if (facilityRef == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        WriteBatch batch = db.batch();

        facilityRef.get().addOnSuccessListener(facilityDoc -> {
            if (facilityDoc.exists()) {
                Map<String, Object> facilityData = facilityDoc.getData();

                if (facilityData != null) {
                    List<String> eventIds = (List<String>) facilityData.get("events");
                    String organizerId = (String) facilityData.get("organizer");

                    // delete events in facility
                    if (eventIds != null && !eventIds.isEmpty()) {
                        for (String eventId : eventIds) {
                            DocumentReference eventRef = db.collection("events").document(eventId);
                            deleteEvent(context, eventRef);
                        }
                    }

                    if (organizerId != null) {
                        DocumentReference organizerRef = db.collection("entrants").document(organizerId);
                        batch.update(organizerRef, "facility", null);
                        batch.update(organizerRef, "role", "entrant"); // turn role to entrant
                    }
                }

                batch.delete(facilityRef);

                batch.commit().addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Facility and related data deleted successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Log.e("DeleteFacility", "Error deleting facility or related data: ", e);
                    Toast.makeText(context, "Failed to delete facility. Try again.", Toast.LENGTH_SHORT).show();
                });

            } else {
                Toast.makeText(context, "Facility not found!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Log.e("DeleteFacility", "Error fetching facility data: ", e);
            Toast.makeText(context, "Failed to fetch facility data. Try again.", Toast.LENGTH_SHORT).show();
        });
    }


} // remove this bracket later when implemented profile pictures



//
//    //**Profiles Picture**//
////Replace pfp with default image?
////Make sure there is a pfp getter and setter
////Make sure there actually is a pfp in the database?
//    public void resetProfileImage(int position) {
//        Entrant profile = getItem(position);
//        if (profile == null) {
//            return;
//        }
//
//        new AlertDialog.Builder(getContext())
//                .setTitle("Confirm Profile Image Reset")
//                .setMessage("Are you sure you want to reset this profile image? It will be replaced with the default image.")
//                .setPositiveButton("Reset Image", (dialog, which) -> {
//                    FirebaseFirestore db = FirebaseFirestore.getInstance();
//                    String profileId = profile.getID();  // Profile ID to update
//
//                    WriteBatch batch = db.batch();
//
//                    DocumentReference profileRef = db.collection("entrants").document(profileId);
//
//                    String defaultImageUrl = "bubbletracksapp/app/src/main/res/drawable/pfp_placeholder.png"; // Adjust the URL if needed
//                    profile.setPfp(defaultImageUrl);  // Change to default pfp
//
//                    batch.update(profileRef, "pfp", profile.getPfp()); // Change "pfp" to whatever the picture is named in the firebase
//
//                    batch.commit()
//                            .addOnSuccessListener(aVoid -> {
//                                // No need to remove from adapter, just refresh the UI
//                                notifyDataSetChanged();
//                                Toast.makeText(getContext(), "Profile image reset to default successfully", Toast.LENGTH_SHORT).show();
//                            })
//                            .addOnFailureListener(e -> {
//                                Log.e("ResetProfileImage", "Error resetting profile image: ", e);
//                                Toast.makeText(getContext(), "Failed to reset profile image. Try again.", Toast.LENGTH_SHORT).show();
//                            });
//
//                    dialog.dismiss();
//                })
//                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
//                .create()
//                .show();
//    }
//
//}



