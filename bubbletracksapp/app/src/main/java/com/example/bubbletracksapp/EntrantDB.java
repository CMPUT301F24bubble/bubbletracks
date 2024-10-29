package com.example.bubbletracksapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCanceledListener;
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
import java.util.concurrent.Future;

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
                        Log.d("deleteEntrant", "Entrant successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteEntrant", "Error deleting entrant", e);
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


    public static CompletableFuture<Entrant> getEntrant(String ID)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference entrantsRef = db.collection("entrants");

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
                    }
                } else {
                    Log.d("EntrantDB", "get failed with ", task.getException());
                }
            }

        });

        return returnCode;
    }

    public static CompletableFuture<ArrayList<DocumentReference>> getQrCodes(String currentPlayer) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference scannedBy = db.collection("scannedBy");

// Get owner of account reference
        DocumentReference playerReference = db.collection("Players").document(currentPlayer);
//

        CompletableFuture<ArrayList<DocumentReference>> returnCode = new CompletableFuture<>();
        ArrayList<DocumentReference> qrCodeRefs = new ArrayList<>();
// Query the scannedBy collection to get all documents that reference the player's document
        Query query = scannedBy.whereEqualTo("Player", playerReference);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()) {
                    Log.d("GetQrCodes", "No documents found for player: " + playerReference);
                    return;
                }

// Iterate over the query results and add the QR code references to the ArrayList
                for (QueryDocumentSnapshot document : querySnapshot) {
                    DocumentReference docRef = document.getReference();
                    qrCodeRefs.add(((DocumentReference)document.get("qrCodeScanned")));
                }

                returnCode.complete(qrCodeRefs);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
// Display error
                Log.d("Database error", "Error getting all qrcodes for a player", e);
            }
        });

        return returnCode;
    }


    // Make it clear when it returns nothing INCOMPLETE
//    public Entrant getEntrant(String ID)
//    {
//        final Entrant[] newEntrant = {new Entrant()};
//
////        DocumentReference entrantRef = getDocument(ID);
//        DocumentReference entrantRef = db.collection("entrants").document(ID);
//        entrantRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Map<String, Object> newEntrantMap = document.getData();
//                        Log.d("TAGaa", newEntrantMap.toString());
//                        newEntrant[0] = mapToEntrant(newEntrantMap);
//                        Log.d("TAGa", "DocumentSnapshot data: " + document.getData());
//                    } else {
//                        Log.d("TAGa", "No such document");
//                    }
//                } else {
//                    Log.d("TAGa", "get failed with ", task.getException());
//                }
//            }
//        });
//
//        Log.d("wok", newEntrant[0].getID());
//
//        return newEntrant[0];
//    }


    // These two should be in Entrant
    //Update with required fields INCOMPLETE
    private Map<String, Object> entrantToMap(Entrant entrant) {
        Map<String, Object> newEntrant = new HashMap<>();

        newEntrant.put("name", entrant.getNameAsList());
        newEntrant.put("email", entrant.getEmail());
        newEntrant.put("phone", entrant.getPhone());

        return newEntrant;
    }

    //Update with required fields INCOMPLETE
    private static Entrant mapToEntrant(Map<String, Object> map) {
        Entrant newEntrant = new Entrant();

        ArrayList<String> name = (ArrayList<String>)map.get("name");

        newEntrant.setName(name.get(0), name.get(1));
        newEntrant.setEmail(map.get("email").toString());
        newEntrant.setPhone(map.get("phone").toString());

        return newEntrant;
    }


}
