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
    private Admin admin;
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
        ImageButton replaceProfilePicture = convertView.findViewById((R.id.profile_picture));

        entrantNameText.setText(String.join(" ", entrant.getNameAsList()));
        entrantDeviceText.setText(entrant.getID());

        deleteEntrantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            admin.deleteEntrant(getContext(), entrant); // Delete from Firestore first
                            remove(entrant);  // Then remove from the adapter
                            notifyDataSetChanged();  // Notify adapter of changes
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            }
        });

        return convertView;
    }
}


