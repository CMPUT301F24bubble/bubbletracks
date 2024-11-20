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
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Hold database for notification sending and receiving
 * @author Erza
 */
public class NotificationDB {
    FirebaseFirestore db;
    CollectionReference notifsRef;

    public NotificationDB() {
        db = FirebaseFirestore.getInstance();
        notifsRef = db.collection("notifications");
    }

    public void addNotification(Notification notification) {
        Map<String, Object> newNotif = notification.toMap();
        String docID = notification.getId();

        notifsRef.document(docID)
                .set(newNotif)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that notification has been added
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("addNotif", "Notification successfully added!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("addNotification", "Error writing notification", e);
                    }
                });
    }

    /**
     * Deletes notification from database
     * @param notification notification made by organizer to entrant
     */
    public void deleteNotification(Notification notification)
    {
        String docID = notification.getId();

        notifsRef.document(docID)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that notification has been deleted
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("deleteNotif", "Notification successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("deleteNotification", "Error deleting notification", e);
                    }
                });
    }

    /**
     * Updates notification details to the database
     * @param newNotification new notification details
     */
    public void updateNotification(Notification newNotification)
    {
        Map<String, Object> newNotificationMap = newNotification.toMap();

        String docID = newNotification.getId();

        notifsRef.document(docID)
                .update(newNotificationMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that notification has been deleted
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateNotification", "Notification successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that there's been an error
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateNotification", "Error updating notifiation", e);
                    }
                });
    }

    /**
     * Update the notification within the given notification map
     * @param newNotificationMap notification map
     */
    public void updateNotification(Map<String, Object> newNotificationMap)
    {
        String docID = newNotificationMap.get("id").toString();

        notifsRef.document(docID)
                .update(newNotificationMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    /**
                     * Logs that notification has been updated
                     * @param aVoid void
                     */
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("updateNotif", "Notification successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    /**
                     * Logs that notification has been deleted
                     * @param e exception
                     */
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("updateNotification", "Error updating notification", e);
                    }
                });
    }

    /**
     * Allows to get notification document from database
     * @param ID notification ID
     * @return return code
     */
    public CompletableFuture<Notification> getNotification(String ID)
    {
        CompletableFuture<Notification> returnCode = new CompletableFuture<>();
        DocumentReference notifRef = notifsRef.document(ID);

        notifRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            /**
             * tries to retrieve notification document from database
             * @param task document snapshot
             */
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Notification newNotification = new Notification(document);

                        returnCode.complete(newNotification);
                        Log.d("NotifDB", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("NotifDB", "No such document");
                    }
                } else {
                    Log.d("NotifDB", "get failed with ", task.getException());
                }
            }
        });
        return returnCode;
    }

    /**
     * Allows to retrieve notification list details
     * @param IDs list of notification ids
     * @return return code
     */
    public CompletableFuture<ArrayList<Notification>> getNotifList(ArrayList<String> IDs)
    {
        CompletableFuture<ArrayList<Notification>> returnCode = new CompletableFuture<>();
        ArrayList<Notification> notifications = new ArrayList<>();

        if (IDs.isEmpty()) {
            returnCode.complete(null);
            Log.d("Is this array empty", IDs.toString());
            return returnCode;
        }

        Query query = notifsRef.whereIn("id", IDs);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            /**
             * Indicates either that the document was found or not
             * @param querySnapshot query snapshot
             */
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                if (querySnapshot.isEmpty()) {
                    Log.d("getNotifList", "No documents found: " + IDs.toString());
                    returnCode.complete(null);
                    return;
                }
                // Go through each document and get the Notification information.
                for (QueryDocumentSnapshot document : querySnapshot) {
                    Notification newNotification = new Notification(document);

                    notifications.add(newNotification);
                }
                Log.d("getNotifList", "Found Notifications: " + notifications.toString());

                returnCode.complete(notifications);
            }
        }).addOnFailureListener(new OnFailureListener() {
            /**
             * logs that there has been an error retrieving  s list
             */
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error if it does not work
                Log.d("Database error", "Error getting all notifications", e);
            }
        });
        return returnCode;
    }

}
