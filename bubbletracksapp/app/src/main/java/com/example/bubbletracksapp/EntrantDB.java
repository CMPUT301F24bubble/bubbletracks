package com.example.bubbletracksapp;

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

public class EntrantDB {

    FirebaseFirestore db;
    CollectionReference entrantsRef;

    public EntrantDB() {
        db = FirebaseFirestore.getInstance();
        entrantsRef = db.collection("entrants");
    }

    public void addEntrant(Entrant entrant)
    {
        //Maybe this should be in Entrant class INCOMPLETE
        Map<String, Object> newEntrant = entrantToMap(entrant);

        String docID = entrant.getID();


        entrantsRef.document(docID)
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
                        Log.w("addEntrant", "Error writing entrant", e);
                    }
                });
    }

    public void deleteEntrant(Entrant entrant)
    {
        String docID = entrant.getID();

        entrantsRef.document(docID)
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

    public void updateEntrant(Entrant newEntrant)
    {
        //Maybe this should be in Entrant class INCOMPLETE
        Map<String, Object> newEntrantMap = entrantToMap(newEntrant);

        String docID = newEntrant.getID();

        entrantsRef.document(docID)
                .update(newEntrantMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateEntrant", "Entrant successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateEntrant", "Error updating entrant", e);
                    }
                });
    }


    public CompletableFuture<Entrant> getEntrant(String ID)
    {
        CompletableFuture<Entrant> returnCode = new CompletableFuture<>();
        DocumentReference entrantRef = entrantsRef.document(ID);

        entrantRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> newEntrantMap = document.getData();
                        Entrant newEntrant = mapToEntrant(newEntrantMap);

                        returnCode.complete(newEntrant);
                        Log.d("EntrantDB", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("EntrantDB", "No such document");
                        returnCode.complete(null);
                    }
                } else {
                    Log.d("EntrantDB", "get failed with ", task.getException());
                }
            }
        });
        return returnCode;
    }

    // These two should be in Entrant
    //Update with required fields INCOMPLETE
    private Map<String, Object> entrantToMap(Entrant entrant) {
        Map<String, Object> newEntrant = new HashMap<>();

        newEntrant.put("name", entrant.getNameAsList());
        newEntrant.put("email", entrant.getEmail());
        newEntrant.put("phone", entrant.getPhone());
        newEntrant.put("notification", entrant.getNotification());

        return newEntrant;
    }

    //Update with required fields INCOMPLETE
    private static Entrant mapToEntrant(Map<String, Object> map) {
        Entrant newEntrant = new Entrant();

        ArrayList<String> name = (ArrayList<String>)map.get("name");

        newEntrant.setName(name.get(0), name.get(1));
        newEntrant.setEmail(map.get("email").toString());
        newEntrant.setPhone(map.get("phone").toString());
        newEntrant.setNotification((Boolean) map.get("notification"));

        return newEntrant;
    }


}
