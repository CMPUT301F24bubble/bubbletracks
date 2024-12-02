package com.example.bubbletracksapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;

import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter between event host and the event list
 * @author
 */
public class EventHostListAdapter extends ArrayAdapter<Event>{
    interface EventHostI {
        void viewWaitlist(Event event);
        void editEvent(Event event);
    }
    private EventHostI listener;
    private OrganizerEventHosting activity;

    /**
     * Initialize the adapter with the list of events
     * @param context context of what adapter does
     * @param events list of entrants
     * @param activity activity that the adapter is called in
     */
    public EventHostListAdapter(Context context, ArrayList<Event> events, OrganizerEventHosting activity) {
        super(context, 0, events);
        this.activity = activity;
        if (context instanceof EventHostI) {
            listener = (EventHostI) context;
        } else {
            throw new RuntimeException(context + " must implement EventHostI");
        }
    }

    /**
     * Get the view of the event details
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
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

        ImageView browseEventPoster = view.findViewById(R.id.browseEventPoster);
        TextView browseEventDate = view.findViewById(R.id.event_title);
        TextView event_title = view.findViewById(R.id.event_title);
        TextView browseEventDescription = view.findViewById(R.id.browseEventDescription);
        TextView RegOpen = view.findViewById(R.id.RegOpen);
        TextView RegCLose= view.findViewById(R.id.RegClose);

        AppCompatImageButton seePeopleButton = view.findViewById(R.id.see_people_button);
        AppCompatImageButton notifyButton = view.findViewById(R.id.notify_event_button);
        AppCompatImageButton updatePosterButton = view.findViewById(R.id.update_poster_button);
        AppCompatImageButton deleteEventButton = view.findViewById(R.id.delete_button);

        if (event.getImage() != null) {
            Picasso.get()
                    .load(event.getImage())
                    .error(R.drawable.sushi_class) // Fallback in case of error
                    .into(browseEventPoster);
        } else {
            browseEventPoster.setImageResource(R.drawable.sushi_class); // Default image if null
        }

        String month = event.getMonth(event.getDateTime());
        String day = event.getDay(event.getDateTime());
        String time = event.getTime(event.getDateTime());
        if (month != null && day != null && time != null) {
            browseEventDate.setText(month + " " + day + " " + "@" + " " + time);
        } else {
            browseEventDate.setText("Unknown");
        }
        event_title.setText(event.getName());
        browseEventDescription.setText(event.getDescription());
        RegOpen.setText("Registration open: "+event.getRegistrationOpen());
        RegCLose.setText("Registration close "+event.getRegistrationClose());


        seePeopleButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Action to see the entrants
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                listener.viewWaitlist(event);
            }
        });

        updatePosterButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Action to edit the event details
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                listener.editEvent(event);
            }
        });

        updatePosterButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Action to edit the poster
             * @param view The view that was clicked
             */
            @Override
            public void onClick(View view) {
                activity.updatePoster(event);
            }
        });


        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Action to delete the event
             * @param view The view that was clicked.
             */
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Admin admin = new Admin(getContext());
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            admin.deleteEvent(getContext(), event);
                            remove(event);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create()
                        .show();
            }
        });
        return view;
    }

    /**
     * Allow to view the waitlist of entrants
     *
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
     *
     * @param event event that event host holds
     */
    public void editEvent(Event event) {

    }
}