package com.example.bubbletracksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EventHostListAdapter extends ArrayAdapter<Event>{
    interface EventHostI {
        void viewWaitlist(Event event);
        void editEvent(Event event);
    }
    private EventHostI listener;

    public EventHostListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        if (context instanceof EventHostI) {
            listener = (EventHostI) context;
        } else {
            throw new RuntimeException(context + " must implement EventHostI");
        }
    }

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

        Button seePeopleButton = view.findViewById(R.id.see_people_button);
        Button editEventButton = view.findViewById(R.id.edit_event_button);


        eventMonthText.setText(event.getMonth());
        eventDateText.setText(event.getDay());
        eventTimeText.setText(event.getTime());
        eventLocationText.setText(event.getLocation());
        eventTitleText.setText(event.getName());

        seePeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.viewWaitlist(event);
            }
        });

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.editEvent(event);
            }
        });


        return view;
    }
}