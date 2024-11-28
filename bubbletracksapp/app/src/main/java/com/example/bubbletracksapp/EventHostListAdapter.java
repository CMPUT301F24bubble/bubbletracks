package com.example.bubbletracksapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

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
        Button updatePosterButton = view.findViewById(R.id.update_poster_button);

        AppCompatImageButton seePeopleButton = view.findViewById(R.id.see_people_button);
        AppCompatImageButton editEventButton = view.findViewById(R.id.edit_event_button);


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
                listener.viewWaitlist(event);
            }
        });

        editEventButton.setOnClickListener(new View.OnClickListener() {
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
            @Override
            public void onClick(View view) {
                activity.updatePoster(event);
            }
        });

        return view;
    }
}