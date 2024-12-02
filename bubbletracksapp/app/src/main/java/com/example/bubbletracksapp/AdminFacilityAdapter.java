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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Custom adapter to display a list of facilities.
 * This adapter binds facility data to list items, with a "Delete" button for each item.
 */
public class AdminFacilityAdapter extends ArrayAdapter<Facility> {

    public AdminFacilityAdapter(Context context, ArrayList<Facility> facilities) {
        super(context, 0, facilities);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.facility_item, parent, false);
        }

        Facility facility = getItem(position);

        TextView facilityNameText = convertView.findViewById(R.id.facility_name);
        TextView facilityIdText = convertView.findViewById(R.id.facility_id);
        TextView facilityLocationText = convertView.findViewById(R.id.facility_location);
        TextView facilityOrganizerText = convertView.findViewById(R.id.facility_organizer);
        ImageButton deleteButton = convertView.findViewById(R.id.delete_facility_button);
        TextView eventsTextView = convertView.findViewById(R.id.organized);

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
            deleteButton.setOnClickListener(view -> {
                Admin admin = new Admin(getContext());
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Facility")
                        .setMessage("Are you sure you want to delete this facility?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            DocumentReference facilityRef = FirebaseFirestore.getInstance()
                                    .collection("facilities")
                                    .document(facility.getId());

                            admin.deleteFacility(getContext(), facilityRef);
                            remove(facility);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            });
        }

            return convertView;
        }
}


