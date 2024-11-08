package com.example.bubbletracksapp;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
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
        Map<String, Object> newEvent = event.toMap();

        String docID = event.getId();

        eventsRef.document(docID)
                .set(newEvent)
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
        Map<String, Object> newEventMap = newEvent.toMap();

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

    public void updateEvent(Map<String, Object> newEventMap)
    {
        String docID = newEventMap.get("id").toString();

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

    public CompletableFuture<ArrayList<Event>> getEventList(ArrayList<String> IDs)
    {
        CompletableFuture<ArrayList<Event>> returnCode = new CompletableFuture<>();
        ArrayList<Event> events = new ArrayList<>();

        Query query = eventsRef.whereIn("id", IDs);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()) {
                    Log.d("getEventList", "No documents found: " + IDs.toString());
                    returnCode.complete(null);
                    return;
                }
                // Go through each document and get the Event information.
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Event newEvent = new Event(document);

                    events.add(newEvent);
                }
                Log.d("getEventList", "Found Events: " + events.toString());

                returnCode.complete(events);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error if it does not work
                Log.d("Database error", "Error getting all events", e);
            }
        });
        return returnCode;

    }
}
