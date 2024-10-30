package com.example.bubbletracksapp;


import android.location.Location;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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

        String docID = event.getID();

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
        String docID = event.getID();

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

    public void updateEvent(Event newEvent)
    {
        //Maybe this should be in Entrant class INCOMPLETE
        Map<String, Object> newEventMap = eventToMap(newEvent);

        String docID = newEvent.getID();

        eventsRef.document(docID)
                .update(newEventMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateEvent", "Event successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateEvent", "Error updating event", e);
                    }
                });
    }


    public CompletableFuture<Event> getEvent(String ID)
    {
        CompletableFuture<Event> returnCode = new CompletableFuture<>();
        DocumentReference eventRef = eventsRef.document(ID);

        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> newEventMap = document.getData();
                        Event newEvent = mapToEvent(newEventMap);

                        returnCode.complete(newEvent);
                        Log.d("EventDB", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("EventDB", "No such document");
                    }
                } else {
                    Log.d("EventDB", "get failed with ", task.getException());
                }
            }
        });
        return returnCode;
    }



    // These two should be in Event
    //Update with required fields INCOMPLETE
    private Map<String, Object> eventToMap(Event event) {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("name", event.getName());
        newMap.put("description", event.getDescription());
        newMap.put("QRCode", event.getQRCode());
        newMap.put("geolocation", event.getGeolocation());
        newMap.put("image", event.getImage());
        newMap.put("needsGeolocation", event.isNeedsGeolocation());

        return newMap;
    }

    //Update with required fields INCOMPLETE
    private Event mapToEvent(Map<String, Object> map) {
        Event newEvent = new Event();

        ArrayList<String> name = (ArrayList<String>)map.get("name");

        newEvent.setName(map.get("name").toString());
        newEvent.setDescription(map.get("description").toString());
        newEvent.setQRCode(map.get("QRCode").toString());
        Location location = toLocation(map.get("geolocation"));
        newEvent.setGeolocation(location);
        Image image = toImage(map.get("image"));
        newEvent.setImage(image);
        newEvent.setNeedsGeolocation((Boolean) map.get("needsGeolocation"));

        return newEvent;
    }

    private Location toLocation(Object geolocation) {
        //Need to figure out how to handle location
        return null;
    }

    private Image toImage(Object image) {
        //Need to figure out how to handle image
        return null;
    }

}
