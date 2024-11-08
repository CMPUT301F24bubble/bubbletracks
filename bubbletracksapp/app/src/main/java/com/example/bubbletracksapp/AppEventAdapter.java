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

public class AppEventAdapter extends RecyclerView.Adapter<AppEventAdapter.EventViewHolder>{

    // ATTRIBUTES
    Context context;
    List<AppEvent> eventList;
    AppUser user;
    Integer eventPicInteger;

    public AppEventAdapter (Context context, List<AppEvent> eventList, AppUser user, Integer eventPicInteger) {
        this.context = context;
        this.eventList = eventList;
        this.user = user;
        this.eventPicInteger = eventPicInteger;
    }

    // CONSTRUCTOR THAT MATCHES SUPER CLASS
    class EventViewHolder extends RecyclerView.ViewHolder {

        TextView eventTitle, eventRegStatus, eventDateTime ,eventLocation, eventWaitListCloses;
        ImageView eventPic;
        LinearLayout eventParent;
        Button accept, decline;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventRegStatus = itemView.findViewById(R.id.eventRegStatus);
            //eventDateTime = itemView.findViewById(R.id.eventDateTime);
            //eventLocation = itemView.findViewById(R.id.eventLocation);
            //eventWaitListCloses = itemView.findViewById(R.id.eventWaitlistClose);
            eventPic = itemView.findViewById(R.id.eventPic);
            eventParent = itemView.findViewById(R.id.eventParent);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);
        }
    }

    // METHODS
    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        AppEvent event = eventList.get(position);


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

        holder.eventTitle.setText(event.getEventTitle());
        String regStatus = event.getRegistrationStatus(user.getDevice_id());
        holder.eventRegStatus.setText(regStatus != null ? regStatus : "unknown");



        // Safe drawable handling
        Integer eventPicInteger = event.getEventPic();
        if (eventPicInteger != null){
            holder.eventPic.setImageDrawable(ContextCompat.getDrawable(context, eventPicInteger));
        } else {
            holder.eventPic.setImageResource(R.drawable.default_image); // Replace with a default drawable
        }

        if (position == 0) {
            holder.eventParent.setScaleX(1.0f); // Increase scale for the first item
            holder.eventParent.setScaleY(1.0f);
        } else {
            // Reset scale for other items to default
            holder.eventParent.setScaleX(0.7f);
            holder.eventParent.setScaleY(0.7f);
        }

        if ("INVITED".equals(regStatus)) {
            holder.accept.setVisibility(View.VISIBLE);
            holder.decline.setVisibility(View.VISIBLE);
        } else {
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
        }

    }
    @Override
    public int getItemCount() {
        return eventList.size();
    }

    private void handleAcceptClick(AppEvent event) {
        // Code to handle accepting this specific event
        Toast.makeText(context, "Accepted: " + event.getEventTitle(), Toast.LENGTH_SHORT).show();
        user.addRegEvent(event);
    }

    private void handleDeclineClick(AppEvent event) {
        // MAKES A NOTIFICATION, CHECKS IF YOU TRULY WANT TO DECLINE
        // Code to handle declining this specific event
        Toast.makeText(context, "Declined: " + event.getEventTitle(), Toast.LENGTH_SHORT).show();
        // Handle decline logic, if any
        user.removeWLEvent(event);
    }
}
