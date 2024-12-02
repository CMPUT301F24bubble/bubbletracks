/**
 *
 * @author Sarah, Gwen
 * @version 1.0
 */


package com.example.bubbletracksapp;

import static android.system.Os.remove;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

// TODO: handleOnClickEvent Functions

public class BrowseEventsAdminAdapter extends RecyclerView.Adapter<BrowseEventsAdminAdapter.BrowseEventViewHolder> {

    // ATTRIBUTES (gets all event images from the database)
    Context context;
    ArrayList<Event> allTheEvents = new ArrayList<>();


    // CONSTRUCTOR
    public BrowseEventsAdminAdapter(BrowseEventsScreenGenerator context, ArrayList<Event> events) {
        this.context = context;
        this.allTheEvents = events;
    }

    // CREATES VIEW HOLDER

    /**
     * Class that initializes the components of the event_display.xml
     */
    class BrowseEventViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDescription, eventDate, eventID, eventOrganizerID;
        ImageView eventPic;
        ImageButton overflowImageButton;
        CardView eventParent;

        public BrowseEventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.browseEventTitle);
            eventDate = itemView.findViewById(R.id.browseEventDate);
            eventDescription = itemView.findViewById(R.id.browseEventDescription);
            eventID = itemView.findViewById(R.id.browseEventID);
            eventOrganizerID = itemView.findViewById(R.id.browseEventOrganizer);
            eventPic = itemView.findViewById(R.id.browseEventPoster);
            eventParent = itemView.findViewById(R.id.browseEventXMLID);
            overflowImageButton = itemView.findViewById(R.id.browseOverflowMenu);
        }
    }



    // METHODS

    /**
     * Called when RecyclerView needs a new {@link BrowseEventViewHolder} to represent an item.
     * <p>
     * This method will be invoked only when there are no existing {@link BrowseEventViewHolder}s
     * that can be reused. A new {@link BrowseEventViewHolder} is created to hold the layout
     * for each individual item in the RecyclerView.
     *
     * @param parent   The {@link ViewGroup} into which the new View will be added after
     *                 it is bound to an adapter position.
     * @param viewType The view type of the new View. This parameter can be used to
     *                 differentiate between multiple view types if your adapter uses them.
     * @return A new {@link BrowseEventViewHolder} that holds a View of the given view type.
     * @throws NullPointerException If {@code parent} is null.
     */
    @NonNull
    @Override
    public BrowseEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.vertical_item_event, parent, false);
        return new BrowseEventViewHolder(view);
    }


    /**
     * Displays the all events from the database
     *
     * @param holder   The {@link ViewGroup} into which the new View will be added after
     *                 it is bound to an adapter position.
     * @param position The view type of the new View. This parameter can be used to
     *                 differentiate between multiple view types if your adapter uses them.
     * @
     */
    @Override
    public void onBindViewHolder(@NonNull BrowseEventViewHolder holder, int position) {

        // HANDLES WHETHER THERE IS NO EVENT DATABASE OR THE POSITION GIVEN IS INVALID
        if (allTheEvents == null || position >= allTheEvents.size()) {
            return;
        }

        // GETS EVENT OF INTEREST
        if (!allTheEvents.isEmpty()) {
            Event event = allTheEvents.get(position);

            // SETS EVENT TITLE and handles null value
            holder.eventTitle.setText(event.getName() != null ? event.getName() : "No Title");

            // SETS EVENT DESCRIPTION and handles null value
            holder.eventDescription.setText(event.getDescription() != null ? event.getDescription() : "No Description");


            holder.eventID.setText("Event ID: "+event.getId());
            holder.eventOrganizerID.setText("Event Facility ID: "+ event.getFacility());
            // SETS EVENT IMAGE
            if (event.getImage() != null) {
                Log.d("ImageView", "ImageView is: " + holder.eventPic);
                Picasso.get().load(event.getImage()).into(holder.eventPic);
            } else {
                holder.eventPic.setImageResource(R.drawable.default_event);
            }

            // SETS EVENT DATE: should look like NOV 29 @ 13:30
            String month = event.getMonth(event.getDateTime());
            String day = event.getDay(event.getDateTime());
            String time = event.getTime(event.getDateTime());
            if (month != null && day != null && time != null) {
                holder.eventDate.setText(month + " " + day + " " + "@" + " " + time);
            } else {
                holder.eventDate.setText("Unknown");
            }


            // CREATES ON CLICK LISTENER FOR OVERFLOW MENU
            holder.overflowImageButton.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(context, holder.overflowImageButton);
                popupMenu.inflate(R.menu.overflow_admin_search_menu);

                // SHOWS THE MENU
                popupMenu.show();

                // CREATES ON CLICK LISTENER FOR OVERFLOW MENU
                popupMenu.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.action_delete_event) {
                        handleDeleteEventAction(event);
                        popupMenu.dismiss();
                        return true;
                    } else if (id == R.id.action_delete_QR_data) {
                        handleDeleteQRDataAction(event);
                            popupMenu.dismiss();
                            return true;
                        } else if (id == R.id.action_delete_poster) {
                            handleDeletePosterAction(event, holder.eventPic);
                            popupMenu.dismiss();
                            return true;
                        }  else {
                        popupMenu.dismiss();
                        return false;
                    }
                });
            });
        } else {
            System.out.println("No events available.");
        }
    }


    /**
     * Returns
     *
     * @return an integer which equals how many events in the event database
     *
     */
    @Override
    public int getItemCount() {
        return allTheEvents == null ? 0 : allTheEvents.size();
    }

    private void handleDeleteEventAction(Event event) {

        // DELETES EVENT FROM DATABASE: should cascade so that the event will not appear in any waitlist/registered/hosting list
        Admin admin = new Admin(context);
        admin.deleteEvent(context, event);
        allTheEvents.remove(event);
        notifyDataSetChanged();
        // OPTIONAL:  SENDS NOTIFICATION TO HOST THAT THEIR EVENT HAS BEEN DELETED ex: Admin has deleted "<Name of event>" on <Date of Event>. Please contact us at <email> in case you have any further questions.
    }

    /**
     * Handles the action of canceling an event.
     *
     * This method is triggered when the "Cancel Event" option in the overflow menu is selected.
     * It will set the QR code data to be a page that says "Sorry! Event is canceled!".
     * Updates the database.
     * It will send a notification to the organizer that their event has been canceled.
     *
     * @param event The {@link Event} object representing the event to be canceled.
     */
    private void handleDeleteQRDataAction(Event event) {
        // GOES INTO DATABASE

        // CHANGES QR CODE OF THE EVENT SO THAT A POSTER ORIGINALLY PUT UP WILL GO TO EVENT CANCELED PAGE
        Admin admin = new Admin(context);
        admin.removeHashData(context, event);

        // OPTIONAL: SENDS NOTIFICATION TO HOST THAT THEIR EVENT HAS BEEN CANCELED ex: Admin has successfully canceled "<Name of event>" on <Date of Event>.
    }

    /**
     * Handles the action of deleting an event poster.
     *
     * This method is triggered when the "Delete Event Poster" option in the overflow menu is selected.
     * It will replace the image with the default event image.
     * Updates the database.
     * It will send a notification to the organizer that their event poster has been taken down.
     *
     * @param event The {@link Event} object representing the event to be canceled.
     * @return returns if the poster was deleted successfully or not
     */
    private void handleDeletePosterAction(Event event, ImageView eventPic) {
        // GOES INTO DATABASE
        String posterFilename = "posters/" + event.getId() + "poster.jpg";
        StorageReference posterStorageReference = FirebaseStorage.getInstance().getReference(posterFilename);

        posterStorageReference.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Poster deleted successfully", Toast.LENGTH_SHORT).show();
                        event.setImage(null);
                        new EventDB().updateEvent(event);
                        eventPic.setImageResource(R.drawable.default_event);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error in deleting poster: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

}







/*

/**
     * Handles the action of deleting an event.
     *
     * This method is triggered when the "Delete Event" option in the overflow menu is selected.
     * It will set the QR code data to be a page that says "Sorry! Event is no longer available!"
     * Updates the database.
     * It will send a notification to the organizer that their event has been deleted.
     * @param event The {@link DocumentReference} object representing the event to be deleted.
     */