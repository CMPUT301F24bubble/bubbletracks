package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Adapter between event host and the event list
 * @author
 */
public class EventHostListAdapter extends ArrayAdapter<Event>{
//    interface EventHostI {
//        void viewWaitlist(Event event);
//        void editEvent(Event event);
//    }
//    private EventHostI listener;

    /**
     * Initialize the adapter with the list of events
     * @param context context of what adapter does
     * @param events list of entrants
     */
    public EventHostListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
//        if (context instanceof EventHostI) {
//            listener = (EventHostI) context;
//        } else {
//            throw new RuntimeException(context + " must implement EventHostI");
//        }
    }

    /**
     * Get the view of the event details
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
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_hosting_item,
                    parent, false);
        } else {
            view = convertView;
        }
        Event event = getItem(position);

        TextView eventMonthText = view.findViewById(R.id.event_month);
        TextView eventDateText = view.findViewById(R.id.event_date);
        TextView eventTimeText = view.findViewById(R.id.event_time);
        TextView eventLocationText = view.findViewById(R.id.event_location);
        TextView eventTitleText = view.findViewById(R.id.event_title);

        AppCompatImageButton seePeopleButton = view.findViewById(R.id.see_people_button);
        AppCompatImageButton editEventButton = view.findViewById(R.id.edit_event_button);
        AppCompatImageButton deleteEventButton = view.findViewById(R.id.delete_button); // delete later for admin

        eventMonthText.setText(event.getMonth(event.getDateTime()));
        eventDateText.setText(event.getDay(event.getDateTime()));
        eventTimeText.setText(event.getTime(event.getDateTime()));
        eventLocationText.setText(event.getGeolocation());
        eventTitleText.setText(event.getName());

        seePeopleButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Action to see the entrants
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                viewWaitlist(event);
            }
        });

        editEventButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Action to edit the event details
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                editEvent(event);
            }
        });

        // Move to admin adapter later
        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {deleteEvent(position); }
        });


        return view;
    }

    /**
     * Allow to view the waitlist of entrants
     * @param event event that event host holds
     */
    public void viewWaitlist(Event event) {
        Context context = EventHostListAdapter.this.getContext();

        Intent intent = new Intent(EventHostListAdapter.this.getContext(), OrganizerEditActivity.class);
        intent.putExtra("event", event);

        context.startActivity(intent);

    }

    /**
     * Allow to edit event details
     * @param event event that event host holds
     */
    public void editEvent(Event event) {

    }

// delete this later and put into admin
    /**
     * Allow to delete the event from the list and Firestore.
     * @param position Position of the event to be deleted.
     */
    public void deleteEvent(int position) {
        Event eventToDelete = getItem(position);
        if (eventToDelete == null) {
            return;
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Event Deletion")
                .setMessage("Are you sure you want to delete this event? This action cannot be undone.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference eventRef = db.collection("events").document(eventToDelete.getId());

                    eventRef.delete()
                            .addOnSuccessListener(aVoid -> {
                                remove(eventToDelete);
                                notifyDataSetChanged();
                                Toast.makeText(getContext(), "Event deleted successfully", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Log.e("DeleteEvent", "Error deleting event: ", e);
                                Toast.makeText(getContext(), "Failed to delete event. Try again.", Toast.LENGTH_SHORT).show();
                            });

                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

}
