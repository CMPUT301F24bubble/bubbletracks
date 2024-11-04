package com.example.bubbletracksapp;


import android.location.Location;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EventDB {

    FirebaseFirestore db;
    CollectionReference eventsRef;

    public EventDB() {
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
    }

    public void addEvent(Event event)
    {
        //Maybe this should be in Entrant class INCOMPLETE
        Map<String, Object> newEntrant = eventToMap(event);

        String docID = event.getId();

        eventsRef.document(docID)
                .set(newEntrant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addEvent", "Event successfully added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addEvent", "Error writing event", e);
                    }
                });
    }

    public void deleteEvent(Event event)
    {
        String docID = event.getId();

        eventsRef.document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteEvent", "Event successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteEvent", "Error deleting event", e);
                    }
                });
    }

    // Make it clear when it returns nothing INCOMPLETE
    public Event getEvent(String ID)
    {
        // WIP INCOMPLETE
        return null;
    }



    // These two should be in Event
    //Update with required fields INCOMPLETE
    private Map<String, Object> eventToMap(Event event) {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("id", event.getId());
        newMap.put("name", event.getName());
        newMap.put("dateTime", event.getDateTime().getTime());
        newMap.put("description", event.getDescription());
        newMap.put("QRCode", event.getQRCode());
        newMap.put("geolocation", event.getGeolocation().getAddress());
        newMap.put("registrationOpen", event.getRegistrationOpen().getTime());
        newMap.put("registrationClose", event.getRegistrationClose().getTime());
        newMap.put("image", event.getImage());
        newMap.put("needsGeolocation", event.isNeedsGeolocation());
        newMap.put("maxCapacity", event.getMaxCapacity());
        newMap.put("price", event.getPrice());
        newMap.put("waitListLimit", event.getWaitListLimit());

        return newMap;
    }

    //Update with required fields INCOMPLETE
    private Event mapToEvent(Map<String, Object> map) {
        Event newEvent = new Event();

        newEvent.setId(map.get("id").toString());
        newEvent.setName(map.get("name").toString());
        newEvent.setDateTime(toCalendar(map.get("dateTime")));
        newEvent.setDescription(map.get("description").toString());
        newEvent.setQRCode(map.get("QRCode").toString());
        Place location = toLocation(map.get("geolocation"));
        newEvent.setRegistrationOpen(toCalendar(map.get("registrationOpen")));
        newEvent.setRegistrationClose(toCalendar(map.get("registrationClose")));
        newEvent.setGeolocation(location);
        newEvent.setImage(map.get("image").toString());
        newEvent.setNeedsGeolocation((Boolean) map.get("needsGeolocation"));
        newEvent.setMaxCapacity(Integer.parseInt(map.get("maxCapacity").toString()));
        newEvent.setPrice(Integer.parseInt(map.get("price").toString()));
        newEvent.setWaitListLimit(Integer.parseInt(map.get("waitListLimit").toString()));

        return newEvent;
    }

    private Calendar toCalendar(Object dateTime){
        //Need to figure out how to handle LocalDateTime
        return null;
    }

    private Place toLocation(Object geolocation) {
        //Need to figure out how to handle location
        return null;
    }

    private Image toImage(Object image) {
        //Need to figure out how to handle image
        return null;
    }

}
