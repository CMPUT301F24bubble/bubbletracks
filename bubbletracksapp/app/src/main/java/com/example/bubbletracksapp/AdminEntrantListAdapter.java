package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AdminEntrantListAdapter extends ArrayAdapter<Entrant> {
    private EntrantDB entrantDB;

    public AdminEntrantListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
        this.entrantDB = new EntrantDB();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_profile, parent, false);
        }

        Entrant entrant = getItem(position);
        TextView entrantNameText = convertView.findViewById(R.id.user_name);
        TextView entrantDeviceText = convertView.findViewById(R.id.user_device);
        ImageButton deleteEntrantButton = convertView.findViewById(R.id.delete_button);
        ImageButton replaceProfilePicture = convertView.findViewById((R.id.profile_picture));

        entrantNameText.setText(String.join(" ", entrant.getNameAsList()));
        entrantDeviceText.setText(entrant.getID());

        deleteEntrantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntrant(position);
            }
        });
        return convertView;
    }

    //**Profiles**//
    public void deleteEntrant(int position) {
        Entrant entrantToDelete = getItem(position);  // Assuming getItem gives the Profile to delete
        if (entrantToDelete == null) {
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Profile Deletion")
                .setMessage("Are you sure you want to delete this profile? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    String profileId = entrantToDelete.getID();  // Profile ID to delete

                    WriteBatch batch = db.batch();

                    DocumentReference profileRef = db.collection("entrants").document(profileId);
                    batch.delete(profileRef);
                    // Define the subcategories where profile IDs may be referenced
                    List<String> subcategories = Arrays.asList("cancelled", "enrolled", "invited", "rejected", "wait");

                    db.collection("entrants")
                            .get()
                            .addOnSuccessListener(querySnapshot -> {
                                for (DocumentSnapshot entrantDoc : querySnapshot.getDocuments()) {
                                    DocumentReference entrantRef = entrantDoc.getReference();
                                    Map<String, Object> entrantData = entrantDoc.getData();

                                    if (entrantData != null) {
                                        // Go through each subcategory to check for profile references
                                        for (String subcategory : subcategories) {
                                            if (entrantData.containsKey(subcategory)) {
                                                List<String> profileList = (List<String>) entrantData.get(subcategory);
                                                if (profileList != null && profileList.contains(profileId)) {
                                                    // Remove the profile ID from the list
                                                    profileList.remove(profileId);
                                                    batch.update(entrantRef, subcategory, profileList);
                                                }
                                            }
                                        }
                                    }
                                }

                                // Commit the batch after processing all documents
                                batch.commit()
                                        .addOnSuccessListener(aVoid -> {
                                            remove(entrantToDelete); // Remove the profile from the adapter
                                            notifyDataSetChanged();
                                            Toast.makeText(getContext(), "Profile and references deleted successfully", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("DeleteProfile", "Error deleting profile or references: ", e);
                                            Toast.makeText(getContext(), "Failed to delete profile. Try again.", Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Log.e("DeleteProfile", "Error fetching entrant references: ", e);
                                Toast.makeText(getContext(), "Failed to fetch related data. Try again.", Toast.LENGTH_SHORT).show();
                            });

                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
} // remove this bracket later


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
