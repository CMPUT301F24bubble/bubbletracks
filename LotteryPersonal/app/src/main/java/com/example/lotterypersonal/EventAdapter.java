package com.example.lotterypersonal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the event item for this position
        Event event = getItem(position);

        // Check if the view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
        }

        // Lookup views for data population
        TextView eventNameTextView = convertView.findViewById(R.id.eventNameTextView);
        TextView eventDateTextView = convertView.findViewById(R.id.eventDateTextView);

        // Populate the data into the template view
        eventNameTextView.setText(event.getEventName());
        eventDateTextView.setText(event.getEventDate());

        // Return the completed view to render on screen
        return convertView;
    }
}
