package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
/**
 * Custom adapter to display and manage a list of entrants for the admin view.
 * This adapter is responsible for binding entrant data to the list items and handling user actions,
 * such as deleting an entrant.
 */
public class AdminEntrantListAdapter extends ArrayAdapter<Entrant> {

    private EntrantDB entrantDB;
    private Admin admin;

    /**
     * Constructs a new adapter for the list of entrants.
     *
     * @param context  The context in where the adapter is used.
     * @param entrants The list of entrants to be displayed in the adapter.
     */
    public AdminEntrantListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
        this.entrantDB = new EntrantDB();
        this.admin = new Admin(context);
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
        ImageButton replaceProfilePicture = convertView.findViewById(R.id.profile_picture); // for later

        entrantNameText.setText("User: "+String.join(" ", entrant.getNameAsList()));
        entrantDeviceText.setText(entrant.getID());

        convertView.setOnClickListener(v -> showProfileDetailsDialog(entrant));

        // deleting an entrant
        deleteEntrantButton.setOnClickListener(view -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete this user?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        admin.deleteEntrant(getContext(), entrant);
                        remove(entrant);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        return convertView;
    }

    /**
     * Shows the extra information of each entrant as a dialog, including facility details and delete functionality.
     *
     * @param entrant The entrant whose profile is to be displayed.
     */
    private void showProfileDetailsDialog(Entrant entrant) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.extra_profile_info, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // entrant info
        TextView nameTextView = dialogView.findViewById(R.id.dialog_name);
        TextView userIdTextView = dialogView.findViewById(R.id.dialog_user_id);
        TextView phoneTextView = dialogView.findViewById(R.id.dialog_phone);
        TextView emailTextView = dialogView.findViewById(R.id.dialog_email);
        TextView roleTextView = dialogView.findViewById(R.id.dialog_role);
        TextView eventsTextView = dialogView.findViewById(R.id.organized);

        nameTextView.setText(String.join(" ", entrant.getNameAsList()));
        userIdTextView.setText("User ID: " + entrant.getID());
        phoneTextView.setText("Phone: " + entrant.getPhone());
        emailTextView.setText("Email: " + entrant.getEmail());
        roleTextView.setText("Role: " + entrant.getRole());

        ArrayList<String> organizedEvents = entrant.getOrganized();
        if (organizedEvents != null && !organizedEvents.isEmpty()) {
            StringBuilder eventsString = new StringBuilder();
            for (String event : organizedEvents) {
                eventsString.append("• ").append(event).append("\n");
            }
            eventsTextView.setText(eventsString.toString());
        } else {
            eventsTextView.setText("No events organized.");
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // facility info
        TextView facilityNameTextView = dialogView.findViewById(R.id.facility_name);
        TextView facilityIdTextView = dialogView.findViewById(R.id.facility_id);
        TextView facilityLocationTextView = dialogView.findViewById(R.id.facility_location);

        String facilityId = entrant.getFacility();

        if (facilityId != null) {
            DocumentReference facilityRef = db.collection("facilities").document(facilityId);

            facilityRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String facilityName = documentSnapshot.getString("name");
                    String facilityLocation = documentSnapshot.getString("location");

                    facilityNameTextView.setText("Facility Name: " + (facilityName != null ? facilityName : "N/A"));
                    facilityIdTextView.setText("Facility ID: " + facilityId);
                    facilityLocationTextView.setText("Facility Location: " + (facilityLocation != null ? facilityLocation : "N/A"));
                } else {
                    facilityNameTextView.setText("Facility Name: N/A");
                    facilityIdTextView.setText("Facility ID: N/A");
                    facilityLocationTextView.setText("Facility Location: N/A");
                }
            }).addOnFailureListener(e -> {
                // Handle errors
                facilityNameTextView.setText("Facility Name: Error");
                facilityIdTextView.setText("Facility ID: Error");
                facilityLocationTextView.setText("Facility Location: Error");
                Log.e("FacilityFetchError", "Error fetching facility details", e);
            });
        } else {
            facilityNameTextView.setText("Facility Name: N/A");
            facilityIdTextView.setText("Facility ID: N/A");
            facilityLocationTextView.setText("Facility Location: N/A");
        }

        Button closeButton = dialogView.findViewById(R.id.close_button);
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }
}
