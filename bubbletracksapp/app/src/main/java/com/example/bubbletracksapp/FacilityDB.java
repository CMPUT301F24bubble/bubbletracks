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

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Holds database for Facility
 * @author Samyak
 */
public class FacilityDB {

    FirebaseFirestore db;
    CollectionReference facilitiesRef;

    /**
     * Retrieves facility collection from firebase
     */
    public FacilityDB() {
        db = FirebaseFirestore.getInstance();
        facilitiesRef = db.collection("facilities");
    }

    /**
     * Adds facility details to the database
     * @param facility facility to be uploaded
     */
    public void addFacility(Facility facility)
    {
        Map<String, Object> newFacility = facility.toMap();

        String docID = facility.getId();

        facilitiesRef.document(docID)
                .set(newFacility)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that facility has been added
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addFacility", "Facility successfully added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addFacility", "Error writing Facility", e);
                    }
                });
    }

    /**
     * Deletes facility from database
     * @param facility facility to be deleted
     */
    public void deleteFacility(Facility facility)
    {
        String docID = facility.getId();

        facilitiesRef.document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that facility has been deleted
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteFacility", "Facility successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteFacility", "Error deleting Facility", e);
                    }
                });
    }

    /**
     * Updates facility details to the database
     * @param facility new facility details
     */
    public void updateFacility(Facility facility)
    {
        Map<String, Object> newFacilityMap = facility.toMap();

        String docID = facility.getId();

        facilitiesRef.document(docID)
                .update(newFacilityMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that Facility has been updated
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateFacility", "Facility successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateFacility", "Error updating Facility", e);
                    }
                });
    }

    /**
     * Allows to get facility document from database
     * @param ID facility ID
     * @return return code
     */
    public CompletableFuture<Facility> getFacility(String ID)
    {
        CompletableFuture<Facility> returnCode = new CompletableFuture<>();
        DocumentReference facilityRef = facilitiesRef.document(ID);

        facilityRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            /**
             * tries to retrieve event document from database
             * @param task document snapshot
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Facility newFacility = new Facility(document);

                        returnCode.complete(newFacility);
                        Log.d("FacilityDB", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("FacilityDB", "No such document");
                        returnCode.complete(null);
                    }
                } else {
                    Log.d("FacilityDB", "get failed with ", task.getException());
                }
            }
        });
        return returnCode;
    }

}