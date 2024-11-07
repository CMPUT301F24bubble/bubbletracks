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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
        Map<String, Object> newEntrant = entrant.toMap();

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
        Map<String, Object> newEntrantMap = newEntrant.toMap();

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
                        Entrant newEntrant = new Entrant(document);

                        returnCode.complete(newEntrant);
                        Log.d("EntrantDB", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("EntrantDB", "No such document");
                    }
                } else {
                    Log.d("EntrantDB", "get failed with ", task.getException());
                }
            }
        });
        return returnCode;
    }

    public CompletableFuture<ArrayList<Entrant>> getEntrantList(ArrayList<String> IDs)
    {
        CompletableFuture<ArrayList<Entrant>> returnCode = new CompletableFuture<>();
        ArrayList<Entrant> entrants = new ArrayList<>();

        if (IDs.isEmpty())
        {
            returnCode.complete(null);
            return returnCode;
        }

        Query query = entrantsRef.whereIn("ID", IDs);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()) {
                    Log.d("getEntrantList", "No documents found: " + IDs.toString());
                    returnCode.complete(null);
                    return;
                }
                // Go through each document and get the Event information.
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Entrant newEntrant = new Entrant(document);

                    entrants.add(newEntrant);
                }
                Log.d("getEntrantList", "Found Entrants: " + entrants.toString());

                returnCode.complete(entrants);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error if it does not work
                Log.d("Database error", "Error getting all entrants", e);
            }
        });
        return returnCode;

    }

}
