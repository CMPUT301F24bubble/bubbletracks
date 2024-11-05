package com.example.bubbletracksapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WaitlistDB extends Entrant{
    FirebaseFirestore db;
    CollectionReference waitlistRef;

    public WaitlistDB() {
        db = FirebaseFirestore.getInstance();
        waitlistRef = db.collection("waitlists");
    }

    public void addWaitEntrant(Entrant entrant)
    {
        Map<String, Object> newEntrant = waitEntrantToMap(entrant);
        String docID = entrant.getID();


        waitlistRef.document(docID)
                .set(newEntrant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addEntrant", "Entrant successfully added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addEntrant", "Error adding Entrant", e);
                    }
                });


    }
    public void deleteWaitEntrant(Event event)
    {
        String docID = event.getID();

        waitlistRef.document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteWaitEntrant", "Entrant successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteWaitEntrant", "Error deleting entrant", e);
                    }
                });
    }

/*

    // Make it clear when it returns nothing INCOMPLETE
    public Event getEvent(String ID)
    {
        // WIP INCOMPLETE
        return null;
    }
*/

    private Map<String, Object> waitEntrantToMap(Entrant entrant) {
        Map<String, Object> newMap = new HashMap<>();
        newMap.put("ent_id", entrant.getPhone());

        return newMap;
    }


    //Update with required fields INCOMPLETE
    private Entrant mapToWaitEntrant(Map<String, Object> map) {
        Entrant newEntrant = new Entrant();

        ArrayList<String> name = (ArrayList<String>) map.get("ent_id");

        newEntrant.setPhone(map.get("ent_id").toString());

        return newEntrant;
    }
}
