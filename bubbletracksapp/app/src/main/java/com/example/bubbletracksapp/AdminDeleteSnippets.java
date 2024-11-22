// Sources
//https://firebase.google.com/docs/reference/kotlin/com/google/firebase/firestore/WriteBatch
//
//
//package com.example.bubbletracksapp;
//
//import android.app.AlertDialog;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.appcompat.widget.AppCompatImageButton;
//
// import com.example.bubbletracksapp.Entrant;
// import com.google.firebase.firestore.WriteBatch; //THIS ONE IS NEWWWWW
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class AdminDeleteSnippets {
//}
//
////** For events**//
//AppCompatImageButton deleteEventButton = view.findViewById(R.id.delete_button); // delete later for admin
//// this part in getView
//        deleteEventButton.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {deleteEvent(position); }
//});
//
//public void deleteEvent(int position) {
//    Event eventToDelete = getItem(position);
//    if (eventToDelete == null) {
//        return;
//    }
//
//    new AlertDialog.Builder(getContext())
//            .setTitle("Confirm Event Deletion")
//            .setMessage("Are you sure you want to delete this event? This action cannot be undone.")
//            .setPositiveButton("Delete", (dialog, which) -> {
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                DocumentReference eventRef = db.collection("events").document(eventToDelete.getId());
//
//                eventRef.delete()
//                        .addOnSuccessListener(aVoid -> {
//                            remove(eventToDelete);
//                            notifyDataSetChanged();
//                            Toast.makeText(getContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show();
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.e("DeleteEvent", "Error deleting event: ", e);
//                            Toast.makeText(getContext(), "Failed to delete event. Try again.", Toast.LENGTH_SHORT).show();
//                        });
//
//                dialog.dismiss();
//            })
//            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
//            .create()
//            .show();
//}
//
//
////**Profiles**//
//public void deleteProfile(int position) {
//    Entrant profileToDelete = getItem(position);  // Assuming getItem gives the Profile to delete
//    if (profileToDelete == null) {
//        return;
//    }
//
//    new AlertDialog.Builder(getContext())
//            .setTitle("Confirm Profile Deletion")
//            .setMessage("Are you sure you want to delete this profile? This action cannot be undone.")
//            .setPositiveButton("Delete", (dialog, which) -> {
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                String profileId = profileToDelete.getId();  // Profile ID to delete
//
//                WriteBatch batch = db.batch();
//
//                DocumentReference profileRef = db.collection("entrants").document(profileId);
//                batch.delete(profileRef);
//                // Define the subcategories where profile IDs may be referenced
//                List<String> subcategories = Arrays.asList("cancelled", "enrolled", "invited", "rejected", "wait");
//
//                db.collection("entrants")
//                        .get()
//                        .addOnSuccessListener(querySnapshot -> {
//                            for (DocumentSnapshot entrantDoc : querySnapshot.getDocuments()) {
//                                DocumentReference entrantRef = entrantDoc.getReference();
//                                Map<String, Object> entrantData = entrantDoc.getData();
//
//                                if (entrantData != null) {
//                                    // Go through each subcategory to check for profile references
//                                    for (String subcategory : subcategories) {
//                                        if (entrantData.containsKey(subcategory)) {
//                                            List<String> profileList = (List<String>) entrantData.get(subcategory);
//                                            if (profileList != null && profileList.contains(profileId)) {
//                                                // Remove the profile ID from the list
//                                                profileList.remove(profileId);
//                                                batch.update(entrantRef, subcategory, profileList);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            // Commit the batch after processing all documents
//                            batch.commit()
//                                    .addOnSuccessListener(aVoid -> {
//                                        remove(profileToDelete); // Remove the profile from the adapter
//                                        notifyDataSetChanged();
//                                        Toast.makeText(getContext(), "Profile and references deleted successfully", Toast.LENGTH_SHORT).show();
//                                    })
//                                    .addOnFailureListener(e -> {
//                                        Log.e("DeleteProfile", "Error deleting profile or references: ", e);
//                                        Toast.makeText(getContext(), "Failed to delete profile. Try again.", Toast.LENGTH_SHORT).show();
//                                    });
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.e("DeleteProfile", "Error fetching entrant references: ", e);
//                            Toast.makeText(getContext(), "Failed to fetch related data. Try again.", Toast.LENGTH_SHORT).show();
//                        });
//
//                dialog.dismiss();
//            })
//            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
//            .create()
//            .show();
//}
//
//
//
////**Profiles Picture**//
////Replace pfp with default image?
////Make sure there is a pfp getter and setter
////Make sure there actually is a pfp in the database?
//public void resetProfileImage(int position) {
//    Entrant profile = getItem(position);
//    if (profile == null) {
//        return;
//    }
//
//    new AlertDialog.Builder(getContext())
//            .setTitle("Confirm Profile Image Reset")
//            .setMessage("Are you sure you want to reset this profile image? It will be replaced with the default image.")
//            .setPositiveButton("Reset Image", (dialog, which) -> {
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
//                String profileId = profile.getId();  // Profile ID to update
//
//                WriteBatch batch = db.batch();
//
//                DocumentReference profileRef = db.collection("entrants").document(profileId);
//
//                String defaultImageUrl = "bubbletracksapp/app/src/main/res/drawable/pfp_placeholder.png"; // Adjust the URL if needed
//                profile.setPfp(defaultImageUrl);  // Change to default pfp
//
//                batch.update(profileRef, "pfp", profile.getPfp()); // Change "pfp" to whatever the picture is named in the firebase
//
//                batch.commit()
//                        .addOnSuccessListener(aVoid -> {
//                            // No need to remove from adapter, just refresh the UI
//                            notifyDataSetChanged();
//                            Toast.makeText(getContext(), "Profile image reset to default successfully", Toast.LENGTH_SHORT).show();
//                        })
//                        .addOnFailureListener(e -> {
//                            Log.e("ResetProfileImage", "Error resetting profile image: ", e);
//                            Toast.makeText(getContext(), "Failed to reset profile image. Try again.", Toast.LENGTH_SHORT).show();
//                        });
//
//                dialog.dismiss();
//            })
//            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
//            .create()
//            .show();
//}
