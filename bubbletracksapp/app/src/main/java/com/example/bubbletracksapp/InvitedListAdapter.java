package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class InvitedListAdapter extends ArrayAdapter<Entrant> {
    interface InvitedEntrantI {
        void cancelEntrant(Entrant entrant);
        boolean hasEntrantAccepted(Entrant entrant);
    }
    private InvitedListAdapter.InvitedEntrantI listener;

    public InvitedListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
        if (context instanceof InvitedListAdapter.InvitedEntrantI) {
            listener = (InvitedListAdapter.InvitedEntrantI) context;
        } else {
            throw new RuntimeException(context + " must implement InvitedEntrantI");
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.attendee_status_item,
                    parent, false);
        } else {
            view = convertView;
        }
        Entrant entrant = getItem(position);

        TextView entrantName = view.findViewById(R.id.attendee_name);
        entrantName.setText(entrant.getNameAsString());

        TextView entrantState = view.findViewById(R.id.attendee_status);
        // SHOULD BE UPDATED WHEN CHANGED IN FIEBASE INCOMPLETE
        if (listener.hasEntrantAccepted(entrant)) {
            entrantState.setText(R.string.enrolledEntrantState);
            int color = ContextCompat.getColor(getContext(),R.color.positive);
            entrantState.setTextColor(color);
        }
        else {
            entrantState.setText(R.string.notEnrolledEntrantState);
            int color = ContextCompat.getColor(getContext(),R.color.neutral);
            entrantState.setTextColor(color);
        }

        ImageButton cancelEntrant = view.findViewById(R.id.cancel_entrant_button);
        cancelEntrant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog joinDialog;
                joinDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Cancelling Entrant")
                        .setMessage(String.format("Are you sure you want to cancel %s from this event?", entrant.getNameAsString()))
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listener.cancelEntrant(entrant);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                joinDialog.show();
            }
        });
        return view;
    }
}
