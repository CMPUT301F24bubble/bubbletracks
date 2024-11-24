package com.example.bubbletracksapp;

import static android.system.Os.remove;
import static java.security.AccessController.getContext;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;

public class Admin {

    private Context context;
    public Admin(Context context){
        this.context = context;
    }
    /**
     * Allow to delete the event from the list and Firestore.
     *
     * @param eventToDelete Event to be deleted.
     */public void deleteEvent(Context context, Event eventToDelete) {
        if (eventToDelete == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String eventId = eventToDelete.getId();

        // batch is a way to do multiple things without crashing everything. If one action fails, the other actions won't continue
        WriteBatch batch = db.batch();

        DocumentReference eventRef = db.collection("events").document(eventId);
        batch.delete(eventRef); // Add the delete operation for the event

        List<String> subcategories = Arrays.asList("enrolled", "invited", "organized", "waitlist");

        // Start querying the "entrants" collection
        db.collection("entrants")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot entrantDoc : querySnapshot.getDocuments()) {
                        DocumentReference entrantRef = entrantDoc.getReference();
                        Map<String, Object> entrantData = entrantDoc.getData();

                        if (entrantData != null) {
                            // go through each subcategory
                            for (String subcategory : subcategories) {
                                if (entrantData.containsKey(subcategory)) {
                                    List<String> eventList = (List<String>) entrantData.get(subcategory);
                                    if (eventList != null && eventList.contains(eventId)) {
                                        // Remove the event ID from the list
                                        eventList.remove(eventId);
                                        batch.update(entrantRef, subcategory, eventList); // Add update to batch
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
                                Log.e("DeleteEvent", "Error deleting event or references: ", e);
                                Toast.makeText(context, "Failed to delete event. Try again.", Toast.LENGTH_SHORT).show();
                            });

                })
                .addOnFailureListener(e -> {
                    Log.e("DeleteEvent", "Error fetching entrant references: ", e);
                    Toast.makeText(context, "Failed to fetch related data. Try again.", Toast.LENGTH_SHORT).show();
                });


    }


    // **Profiles**
    public void deleteEntrant(Context context, Entrant entrantToDelete) {
        if (entrantToDelete == null) {
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String profileId = entrantToDelete.getID();  // Profile ID to delete

        // Initialize Firestore batch operation
        WriteBatch batch = db.batch();

        // Reference to the entrant profile document to delete
        DocumentReference profileRef = db.collection("entrants").document(profileId);
        batch.delete(profileRef);

        // List of subcategories where profile IDs might be referenced
        List<String> subcategories = Arrays.asList("cancelled", "enrolled", "invited", "rejected", "wait");

        // Iterate over all documents in the "entrants" collection
        db.collection("entrants")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot entrantDoc : querySnapshot.getDocuments()) {
                        DocumentReference entrantRef = entrantDoc.getReference();
                        Map<String, Object> entrantData = entrantDoc.getData();

                        if (entrantData != null) {
                            // Go through each subcategory to find and remove profile references
                            for (String subcategory : subcategories) {
                                if (entrantData.containsKey(subcategory)) {
                                    List<String> profileList = (List<String>) entrantData.get(subcategory);
                                    if (profileList != null && profileList.contains(profileId)) {
                                        // Remove the profile ID from the list of that subcategory
                                        profileList.remove(profileId);
                                        batch.update(entrantRef, subcategory, profileList); // Update the document with the new list
                                    }
                                }
                            }
                        }
                    }

                    // Commit the batch operation (delete profile and update references)
                    batch.commit()
                            .addOnSuccessListener(aVoid -> {
                                // Successfully deleted the profile and updated references
                                Toast.makeText(context, "Profile and references deleted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                // Handle error during batch commit
                                Log.e("DeleteProfile", "Error deleting profile or references: ", e);
                                Toast.makeText(context, "Failed to delete profile. Try again.", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    // Handle error fetching entrants
                    Log.e("DeleteProfile", "Error fetching entrant references: ", e);
                    Toast.makeText(context, "Failed to fetch related data. Try again.", Toast.LENGTH_SHORT).show();
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



