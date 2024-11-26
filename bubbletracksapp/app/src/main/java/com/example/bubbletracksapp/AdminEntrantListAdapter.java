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
     * @param context The context in where the adapter is used.
     * @param entrants The list of entrants to be displayed in the adapter.
     */
    public AdminEntrantListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
        this.entrantDB = new EntrantDB();
        this.admin = new Admin(context);
    }

    /**
     * Returns a view for a list item at the specified position in the list.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
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

        entrantNameText.setText(String.join(" ", entrant.getNameAsList()));
        entrantDeviceText.setText(entrant.getID());

        // Set click listener for the profile name to show profile details
        entrantNameText.setOnClickListener(v -> showProfileDetailsDialog(entrant));

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
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

        return convertView;
    }

    // Method to show the profile details dialog
    private void showProfileDetailsDialog(Entrant entrant) {
        // Create the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.extra_profile_info, null);
        builder.setView(dialogView);

        // Initialize views in the dialog
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
            eventsTextView.setText(eventsString.toString());  // Set the event details as bullet points
        } else {
            eventsTextView.setText("No events organized.");  // If no events, show this message
        }

        // Build the dialog
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();

        // Make sure the "Close" button dismisses the dialog, not the activity
        Button closeButton = dialogView.findViewById(R.id.close_button); // Assuming you have a close button
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> {
                dialog.dismiss();  // This will only close the dialog, not the activity
            });
        }
    }}
