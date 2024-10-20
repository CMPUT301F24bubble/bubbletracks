import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DbUtil {

    // Get a Firestore connection
    public static Firestore getConnection() {
        return FirestoreClient.getFirestore();  // Fetches Firestore connection from Firebase SDK
    }

    // GETS STATUS ("waitlisted", "invited", "register") OF USER FOR SPECIFIC EVENT
    public static String getStatusForUserInEvent(String eventId, String userId) {
        Firestore db = getConnection();
        ApiFuture<DocumentSnapshot> future = db.collection("events")
                .document(eventId)
                .collection("invited")
                .document(userId)
                .get();

        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.getString("status");  // Assuming the field is named 'status'
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();  // Handle exceptions
        }

        return null;  // Return null if no status found
    }

    // Update the status of a user for a specific event (eventId and userId as String)
    public static boolean updateUserStatus(String eventId, String userId, String newStatus) {
        Firestore db = getConnection();
        ApiFuture<Void> future = db.collection("events")
                .document(eventId)
                .collection("invited")
                .document(userId)
                .update("status", newStatus);  // Update the 'status' field

        try {
            future.get();  // Wait for the operation to complete
            return true;   // Success
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();  // Handle exceptions
        }

        return false;  // Return false if update failed
    }

    // Add a new invite for a user to an event (eventId and userId as String)
    public static boolean addInviteToEvent(String eventId, String userId, String status) {
        Firestore db = getConnection();
        ApiFuture<Void> future = db.collection("events")
                .document(eventId)
                .collection("invited")
                .document(userId)
                .set(new Invite(status));  // Create an Invite object to store in Firestore

        try {
            future.get();  // Wait for the operation to complete
            return true;   // Success
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();  // Handle exceptions
        }

        return false;  // Return false if add failed
    }

    // Remove an invite from the event (eventId and userId as String)
    public static boolean removeInviteFromEvent(String eventId, String userId) {
        Firestore db = getConnection();
        ApiFuture<Void> future = db.collection("events")
                .document(eventId)
                .collection("invited")
                .document(userId)
                .delete();  // Delete the invite

        try {
            future.get();  // Wait for the operation to complete
            return true;   // Success
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();  // Handle exceptions
        }

        return false;  // Return false if remove failed
    }

    // Get a list of all users invited to a specific event (eventId as String)
    public static List<String> getUsersInvitedToEvent(String eventId) {
        Firestore db = getConnection();
        ApiFuture<com.google.cloud.firestore.QuerySnapshot> future = db.collection("events")
                .document(eventId)
                .collection("invited")
                .get();  // Get all documents in the "invites" collection

        try {
            List<String> userIds = new ArrayList<>();
            for (QueryDocumentSnapshot document : future.get()) {
                userIds.add(document.getId());  // Add the document ID (which is the user ID) to the list
            }
            return userIds;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();  // Handle exceptions
        }

        return null;  // Return null if there was an error or no invites
    }
}


