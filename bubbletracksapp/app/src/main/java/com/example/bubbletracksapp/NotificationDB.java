package com.example.bubbletracksapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    static CollectionReference notifsRef;
    private Entrant entrant;
    private final String channelID = "channel_id";

    public NotificationDB() {
        db = FirebaseFirestore.getInstance();
        notifsRef = db.collection("notifications");
    }

    public void addNotification(Notifications notifications) {
        Map<String, Object> newNotif = notifications.toMap();
        String docID = notifications.getId();
        if (docID == null || docID.isEmpty()) {
            Log.e("addNotification", "Document ID is null or empty, unable to add notification.");
            return;  // Exit early if the ID is invalid
        }
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
     * @param notifications notification made by organizer to entrant
     */
    public void deleteNotification(Notifications notifications)
    {
        String docID = notifications.getId();

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
     * @param newNotifications new notification details
     */
    public void updateNotification(Notifications newNotifications)
    {
        Map<String, Object> newNotificationMap = newNotifications.toMap();

        String docID = newNotifications.getId();

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
    public CompletableFuture<Notifications> getNotification(String ID)
    {
        CompletableFuture<Notifications> returnCode = new CompletableFuture<>();
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

                        Notifications newNotifications = new Notifications(document);

                        returnCode.complete(newNotifications);
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
    public CompletableFuture<ArrayList<Notifications>> getNotifList(ArrayList<String> IDs)
    {
        CompletableFuture<ArrayList<Notifications>> returnCode = new CompletableFuture<>();
        ArrayList<Notifications> notifications = new ArrayList<>();

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
                    Notifications newNotifications = new Notifications(document);

                    notifications.add(newNotifications);
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
    public void listenForNotifications(String deviceID, MainActivity mainActivity) {
        notifsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("listenForNewNotifications", "Listen failed.", e);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        Notifications newNotification = dc.getDocument().toObject(Notifications.class);

                        // Check if this notification is for the receiver
                        if (newNotification.getRecipients().contains(deviceID)) {
                            Log.d("FirestoreListener", "New notification received: " + newNotification.getTitle());

                            // Call the method from MainActivity
                            mainActivity.showNotification(newNotification);
                        }
                    }
                }
            }
        });
    }


}
