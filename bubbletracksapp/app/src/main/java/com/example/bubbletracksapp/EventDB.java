package com.example.bubbletracksapp;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Calendar;
import java.util.Date;
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

    public void updateEvent(Event newEvent)
    {
        //Maybe this should be in Entrant class INCOMPLETE
        Map<String, Object> newEventMap = eventToMap(newEvent);

        String docID = newEvent.getId();

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

                        Event newEvent = new Event(document);

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

        newMap.put("id", event.getId());
        newMap.put("name", event.getName());
        newMap.put("dateTime", event.getDateTime());
        newMap.put("description", event.getDescription());
        newMap.put("geolocation", event.getGeolocation());
        newMap.put("registrationOpen", event.getRegistrationOpen());
        newMap.put("registrationClose", event.getRegistrationClose());
        newMap.put("maxCapacity", event.getMaxCapacity());
        newMap.put("price", event.getPrice());
        newMap.put("waitListLimit", event.getWaitListLimit());
        newMap.put("needsGeolocation", event.getNeedsGeolocation());
        newMap.put("image", event.getImage());
        newMap.put("QRCode", event.getQRCode());

        return newMap;
    }

    //Update with required fields INCOMPLETE
    /*private Event mapToEvent(Map<String, Object> map) {
        Event newEvent = new Event();

        newEvent.setId(map.get("id").toString());
        newEvent.setName(map.get("name").toString());
        newEvent.setDateTime(toDate(map.get("dateTime")));
        newEvent.setDescription(map.get("description").toString());
        newEvent.setGeolocation(map.get("geolocation").toString());
        newEvent.setRegistrationOpen(toDate(map.get("registrationOpen")));
        newEvent.setRegistrationClose(toDate(map.get("registrationClose")));
        newEvent.setMaxCapacity(Integer.parseInt(map.get("maxCapacity").toString()));
        newEvent.setPrice(Integer.parseInt(map.get("price").toString()));
        newEvent.setWaitListLimit(Integer.parseInt(map.get("waitListLimit").toString()));
        newEvent.setNeedsGeolocation((Boolean) map.get("needsGeolocation"));
        newEvent.setImage(map.get("image").toString());
        newEvent.setQRCode(map.get("QRCode").toString());

        return newEvent;
    }*/

    private Date toDate(Object dateTime){
        Timestamp timestamp = (Timestamp) dateTime;
        return timestamp.toDate();
    }

}
