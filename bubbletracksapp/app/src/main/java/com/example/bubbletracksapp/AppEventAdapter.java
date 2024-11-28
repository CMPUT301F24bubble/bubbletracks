package com.example.bubbletracksapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


/**
 * Class that updates what the user sees on the screen for their waitlist and registered list
 */
public class AppEventAdapter extends RecyclerView.Adapter<AppEventAdapter.EventViewHolder>{

    // ATTRIBUTES
    Context context;
    List<Event> eventList;
    Entrant user;
    Integer eventPicInteger;

    public AppEventAdapter (Context context, List<Event> eventList, Entrant user, Integer eventPicInteger) {
        this.context = context;
        this.eventList = eventList;
        this.user = user;
        this.eventPicInteger = eventPicInteger;
    }

    // CONSTRUCTOR THAT MATCHES SUPER CLASS
    /**
     * Class that initializes the components of the event_display.xml
     */
    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle, eventRegStatus, eventDateTime ,eventLocation;
        ImageView eventPic;
        LinearLayout eventParent;
        Button accept, decline;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventRegStatus = itemView.findViewById(R.id.eventRegStatus);
            eventDateTime = itemView.findViewById(R.id.eventDateTime);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventPic = itemView.findViewById(R.id.eventPic);
            eventParent = itemView.findViewById(R.id.eventParent);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);
        }
    }

    // METHODS
    /**
     * Called when RecyclerView needs a new {@link EventViewHolder} to represent an item.
     *
     * This method will be invoked only when there are no existing {@link EventViewHolder}s
     * that can be reused. A new {@link EventViewHolder} is created to hold the layout
     * for each individual item in the RecyclerView.
     *
     * @param parent The {@link ViewGroup} into which the new View will be added after
     *               it is bound to an adapter position.
     * @param viewType The view type of the new View. This parameter can be used to
     *                 differentiate between multiple view types if your adapter uses them.
     * @return A new {@link EventViewHolder} that holds a View of the given view type.
     * @throws NullPointerException If {@code parent} is null.
     */
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }


    /**
     * Displays the event from the user's event list
     *
     *  INCOMPLETE: NEEDS LOCATION/DATE/TIME/IMAGE DATA:)
     * @param holder The {@link ViewGroup} into which the new View will be added after
     *               it is bound to an adapter position.
     * @param position The view type of the new View. This parameter can be used to
     *                 differentiate between multiple view types if your adapter uses them.
     * @
     */
    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        // Set up the accept button with an OnClickListener, passing the event object
        holder.accept.setOnClickListener(v -> {
            handleAcceptClick(event);
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            eventList.remove(position);
            // Notify the adapter about the removed item
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, eventList.size());
        });

        // Set up the decline button similarly
        holder.decline.setOnClickListener(v -> {
            handleDeclineClick(event);
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            eventList.remove(position);
            // Notify the adapter about the removed item
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, eventList.size());

        });

        // SETS EVENT TITLE
        holder.eventTitle.setText(event.getName());

        // SETS EVENT REG STATUS
        String regStatus = null;
        if(user.getEventsEnrolled().contains(event.getId()) && user.getEventsEnrolled() != null)
        {
            regStatus = "REGISTERED";
        }
        else if(user.getEventsInvited().contains(event.getId()) && user.getEventsInvited()!=null)
        {
            regStatus = "INVITED";
        }
        else if(user.getEventsWaitlist().contains(event.getId()) && user.getEventsWaitlist()!=null)
        {
            regStatus = "WAITLISTED";
        }
        holder.eventRegStatus.setText(regStatus != null ? regStatus : "unknown");


        // SETS EVENT LOCATION
        //holder.eventLocation.setText();

        //SET EVENT TIME
        //holder.eventDateTime.setText();


        // SETS EVENT IMAGE
        holder.eventPic.setImageResource(R.drawable.default_image); // Replace with a default drawable


        // DETERMINES EVENT DISPLAY SIZE
        if (position == 0) {
            holder.eventParent.setScaleX(1.0f); // Increase scale for the first item
            holder.eventParent.setScaleY(1.0f);
        } else {
            // Reset scale for other items to default
            holder.eventParent.setScaleX(0.7f);
            holder.eventParent.setScaleY(0.7f);
        }

        // DETERMINES IF ACCEPT/DECLINE BUTTONS SHOULD BE VISIBLE
        if ("INVITED".equals(regStatus)) {
            holder.accept.setVisibility(View.VISIBLE);
            holder.decline.setVisibility(View.VISIBLE);
        } else {
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
        }

    }

    /**
     * Returns
     *
     * @return an integer which equals how many events the user has in the specific list
     *
     */
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    /**
     * Puts event in registered if user accepts invite
     * @param event
     */
    private void handleAcceptClick(Event event) {
        // Code to handle accepting this specific event
        Toast.makeText(context, "Accepted: " + event.getName(), Toast.LENGTH_SHORT).show();
        user.acceptEvent(event.getId());
        event.addToEnrolledList(user.getID());
        event.updateEventFirebase();
    }

    /**
     * Will delete event from user waitlist/enrolled events if they decline the invite
     * @param event
     */
    private void handleDeclineClick(Event event) {
        // MAKES A NOTIFICATION, CHECKS IF YOU TRULY WANT TO DECLINE
        // Code to handle declining this specific event
        Toast.makeText(context, "Declined: " + event.getName(), Toast.LENGTH_SHORT).show();

        // Handle decline logic, if any
        if (user.getEventsWaitlist().contains(event.getId()) && user.getEventsEnrolled().contains(event.getId())) {
            user.deleteFromEventsWaitlist(event.getId());
            user.deleteFromEventsEnrolled(event.getId());

        } else if (user.getEventsWaitlist().contains(event.getId())) {
            user.deleteFromEventsWaitlist(event.getId());

        } else if (user.getEventsEnrolled().contains(event.getId())) {
            user.deleteFromEventsEnrolled(event.getId());
        }

        event.cancelEntrant(user.getID());
        user.updateEntrantFirebase();

    }
}
