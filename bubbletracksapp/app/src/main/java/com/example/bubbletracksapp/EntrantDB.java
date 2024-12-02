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

/**
 * Holds database for entrant
 * @author Chester
 */
public class EntrantDB {

    FirebaseFirestore db;
    CollectionReference entrantsRef;

    /**
     * Retrieves entrant from firebase
     */
    public EntrantDB() {
        db = FirebaseFirestore.getInstance();
        entrantsRef = db.collection("entrants");
    }

    /**
     * Adds entrant details to the database
     * @param entrant entrant that organizer is holding
     */
    public void addEntrant(Entrant entrant)
    {
        Map<String, Object> newEntrant = entrant.toMap();

        String docID = entrant.getID();
        
        entrantsRef.document(docID)
                .set(newEntrant)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that entrant has been added
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addEntrant", "Entrant successfully added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addEntrant", "Error writing entrant", e);
                    }
                });
    }

    /**
     * Deletes entrant from database
     * @param entrant entrant that organizer holds
     */
    public void deleteEntrant(Entrant entrant)
    {
        String docID = entrant.getID();

        entrantsRef.document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that entrant has been deleted
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteWaitEntrant", "Entrant successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteWaitEntrant", "Error deleting entrant", e);
                    }
                });
    }

    /**
     * Updates entrant details to the database
     * @param newEntrant new entrant details
     */
    public void updateEntrant(Entrant newEntrant)
    {
        Map<String, Object> newEntrantMap = newEntrant.toMap();

        String docID = newEntrant.getID();

        entrantsRef.document(docID)
                .update(newEntrantMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that entrant has been deleted
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateEntrant", "Entrant successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateEntrant", "Error updating entrant", e);
                    }
                });
    }

    /**
     * updates the entrant within the given entrant map
     * @param newEntrantMap entrant map
     */
    public void updateEntrant(Map<String, Object> newEntrantMap)
    {
        String docID = newEntrantMap.get("ID").toString();

        entrantsRef.document(docID)
                .update(newEntrantMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that entrant has been updated
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateEntrant", "Entrant successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that entrant has been deleted
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateEntrant", "Error updating entrant", e);
                    }
                });
    }


    /**
     * Allows to get entrant document from database
     * @param ID entrant ID
     * @return return code
     */
    public CompletableFuture<Entrant> getEntrant(String ID)
    {
        CompletableFuture<Entrant> returnCode = new CompletableFuture<>();
        DocumentReference entrantRef = entrantsRef.document(ID);

        entrantRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            /**
             * tries to retrieve entrant document from database
             * @param task document snapshot
             */
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
                        returnCode.complete(null);
                    }
                } else {
                    Log.d("EntrantDB", "get failed with ", task.getException());
                }
            }
        });
        return returnCode;
    }

    /**
     * Allows to retrieve entrant list details
     * @param IDs list of entrant ids
     * @return
     */
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
            /**
             * Indicates either that the document was found or not
             * if it is, transform it to entrant and retrieve it
             * @param querySnapshot query snapshot
             */
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()) {
                    Log.d("getEntrantList", "No documents found: " + IDs.toString());
                    returnCode.complete(null);
                    return;
                }
                // Go through each document and get the Entrant information.
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Entrant newEntrant = new Entrant(document);

                    entrants.add(newEntrant);
                }
                Log.d("getEntrantList", "Found Entrants: " + entrants.toString());

                returnCode.complete(entrants);
            }
        }).addOnFailureListener(new OnFailureListener() {
            /**
             * logs that there has been an error retrieving entrants list
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error if it does not work
                Log.d("Database error", "Error getting all entrants", e);
            }
        });
        return returnCode;
    }

    /**
     * Retrieves all entrants that exist in the database
     * @return a completable future with a list of all the entrants or null if there are none
     */
    public CompletableFuture<ArrayList<Entrant>> getAllEntrants()
    {
        CompletableFuture<ArrayList<Entrant>> returnCode = new CompletableFuture<>();
        ArrayList<Entrant> entrants = new ArrayList<>();

        entrantsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            /**
             * Indicates either that the document was found or not
             * if it is, transform it to entrant and retrieve it
             * @param querySnapshot query snapshot
             */
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()) {
                    Log.d("getAllEntrants", "No entrants found in database");
                    returnCode.complete(null);
                    return;
                }
                // Go through each document and get the Entrant information.
                for (QueryDocumentSnapshot document : querySnapshot) {
                    try {
                        Entrant newEntrant = new Entrant(document);
                        entrants.add(newEntrant);
                    } catch (Exception e) {
                        Log.d("getAllEntrants", "An entrant could not be added," +
                                " check if all entrants contain the correct fields");
                    }
                }
                Log.d("getAllEntrants", "Found Entrants: " + entrants.toString());

                returnCode.complete(entrants);
            }
        }).addOnFailureListener(new OnFailureListener() {
            /**
             * logs that there has been an error retrieving entrants list
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error if it does not work
                Log.d("Database error", "Error getting all entrants", e);
            }
        });
        return returnCode;
    }


    public void deleteProfilePic(String ID, String defaultImage) {

        entrantsRef.document(ID)
                .update("image", defaultImage)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that image has been changed if needed to be changed
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteProfilePhoto", "Profile photo successfully changed if necessary!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteProfilePhoto", "Error deleting profile photo", e);
                    }
                });
    }
}
