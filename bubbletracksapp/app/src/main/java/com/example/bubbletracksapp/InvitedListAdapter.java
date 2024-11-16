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

/**
 * Adapter between invited entrants and the entrant list
 * @author Chester
 */
public class InvitedListAdapter extends ArrayAdapter<Entrant> {
    interface InvitedEntrantI {
        void cancelEntrant(Entrant entrant);
        boolean hasEntrantAccepted(Entrant entrant);
    }
    private InvitedListAdapter.InvitedEntrantI listener;

    /**
     * Initialize the adapter with the list of entrants
     * @param context context of what adapter does
     * @param entrants list of entrants
     */
    public InvitedListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
        if (context instanceof InvitedListAdapter.InvitedEntrantI) {
            listener = (InvitedListAdapter.InvitedEntrantI) context;
        } else {
            throw new RuntimeException(context + " must implement InvitedEntrantI");
        }
    }

    /**
     * Get the view of the attendee list status
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return view
     */
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
            /**
             * Action for wanting to cancel an entrant from an event
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                AlertDialog joinDialog;
                joinDialog = new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Cancelling Entrant")
                        .setMessage(String.format("Are you sure you want to cancel %s from this event?", entrant.getNameAsString()))
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            /**
                             * Action after confirming the cancellation
                             *
                             * @param dialogInterface the dialog that received the click
                             * @param i the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                listener.cancelEntrant(entrant);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            /**
                             * Action after cancelling instead of confirming the cancellation
                             * @param dialogInterface the dialog that received the click
                             * @param i the button that was clicked (ex.
                             *              {@link DialogInterface#BUTTON_POSITIVE}) or the position
                             *              of the item clicked
                             */
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
