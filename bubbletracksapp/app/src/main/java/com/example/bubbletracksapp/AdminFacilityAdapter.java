package com.example.bubbletracksapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Custom adapter to display a list of facilities.
 * This adapter binds facility data to list items, with a "Delete" button for each item.
 */
public class AdminFacilityAdapter extends ArrayAdapter<Facility> {

    // Constructor
    public AdminFacilityAdapter(Context context, ArrayList<Facility> facilities) {
        super(context, 0, facilities);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            // Inflate the layout for each list item (facility)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.facility_item, parent, false);
        }

        // Get the current facility for this position
        Facility facility = getItem(position);

        // Bind the data to the views in the layout
        TextView facilityNameText = convertView.findViewById(R.id.facility_name);
        TextView facilityIdText = convertView.findViewById(R.id.facility_id);
        TextView facilityLocationText = convertView.findViewById(R.id.facility_location);
        TextView facilityOrganizerText = convertView.findViewById(R.id.facility_organizer);
        Button deleteButton = convertView.findViewById(R.id.delete_facility_button);
        TextView eventsTextView = convertView.findViewById(R.id.organized);

        // Set the text to the facility data
        if (facility != null) {
            facilityNameText.setText("Facility Name: " + facility.getName());
            facilityIdText.setText("Facility ID: " + facility.getId());
            facilityLocationText.setText("Facility Location: " + facility.getLocation());
            facilityOrganizerText.setText("Organizer: " + facility.getOrganizer());

            ArrayList<String> organizedEvents = facility.getEventList();
            if (organizedEvents != null && !organizedEvents.isEmpty()) {
                StringBuilder eventsString = new StringBuilder();
                for (String event : organizedEvents) {
                    eventsString.append("• ").append(event).append("\n");
                }
                eventsTextView.setText(eventsString.toString());
            } else {
                eventsTextView.setText("No events organized.");
            }
            // Ensure the delete button works when clicked
            deleteButton.setOnClickListener(v -> {
                if (facility.getId() != null && !facility.getId().isEmpty()) {
                    // Create reference to the Firestore facility document
                    DocumentReference facilityRef = FirebaseFirestore.getInstance()
                            .collection("facilities")
                            .document(facility.getId());

                    // Call the delete facility method
                    deleteFacility(facilityRef, facilityNameText, facilityIdText, facilityLocationText, facilityOrganizerText);
                } else {
                    // If the facility ID is null or empty, show a toast
                    Toast.makeText(getContext(), "Facility ID is missing. Cannot delete.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return convertView;
    }

    // Method to delete facility
    private void deleteFacility(DocumentReference facilityRef, TextView facilityNameText, TextView facilityIdText,
                                TextView facilityLocationText, TextView facilityOrganizerText) {
        facilityRef.delete()
                .addOnSuccessListener(aVoid -> {
                    // After successful deletion, clear the facility data from the UI
                    facilityNameText.setText("Facility Name: N/A");
                    facilityIdText.setText("Facility ID: N/A");
                    facilityLocationText.setText("Facility Location: N/A");
                    facilityOrganizerText.setText("Organizer: N/A");
                    Toast.makeText(getContext(), "Facility deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Handle failure and log the error
                    Toast.makeText(getContext(), "Failed to delete facility", Toast.LENGTH_SHORT).show();
                    Log.e("FacilityDeleteError", "Error deleting facility", e);
                });
    }
}
