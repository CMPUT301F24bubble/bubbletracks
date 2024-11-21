package com.example.bubbletracksapp;

import android.view.View;

import androidx.appcompat.widget.AppCompatImageButton;

// just a place holder until the admin events view is done
// a bunch of snippets of code to add on to the real adapter where the pictures, events, profiles are shown
// probably should just
public class AdminEventDelete {
    AppCompatImageButton deleteEventButton = view.findViewById(R.id.delete_event_button);
        deleteEventButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            deleteEvent(position);
        }
    });

    /**
     * Allow to delete the event from the list
     * @param position position of the event to be deleted
     */
    public void deleteEvent(int position) {
        events.remove(position); // Remove event from the list
        notifyDataSetChanged(); // Notify adapter of data change
    }
}


// for deleting events. Shove onto the admin view of events
public class AdminEventListAdapter extends ArrayAdapter<Event> {

    public AdminEventListAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_item, parent, false);
        } else {
            view = convertView;
        }

        Event event = getItem(position);

        // Views to display event details
        TextView eventNameText = view.findViewById(R.id.event_name);
        AppCompatImageButton deleteEventButton = view.findViewById(R.id.delete_event_button);

        eventNameText.setText(event.getName());

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEvent(position);
            }
        });

        return view;
    }

    /**
     * Delete event from the list
     * @param position position of the event to delete
     */
    private void deleteEvent(int position) {
        Event eventToDelete = getItem(position);

        // Here, delete the event from the list (and any related data, such as images, QR codes)
        // Remove event from list
        remove(eventToDelete);

        // Notify adapter of the change
        notifyDataSetChanged();

        // Optionally, delete related resources (images, QR codes, etc.)
        // deleteRelatedData(eventToDelete);
    }

    /**
     * Optionally delete related data (e.g., QR codes, images)
     * @param event the event whose related data should be deleted
     */
    private void deleteRelatedData(Event event) {
        // Delete associated QR code, images, etc.
        // Example: delete the QR code or image
    }
}

