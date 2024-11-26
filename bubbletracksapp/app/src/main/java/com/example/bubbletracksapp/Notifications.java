package com.example.bubbletracksapp;

import android.app.Notification;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Class for holding notifications
 * @author Erza
 * @version 1.0
 */
public class Notifications implements Parcelable{

    private String id;

    // Parameters for notification information to builder
    private ArrayList<String> recipients;

    private String title;
    private String smallText;
    private String bigText;
    private Object timestamp;

    /**
     * For initializing notifications
     */
    public Notifications() {
        Log.w("NewNotification", "Notification has empty information.");
        recipients = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Store initial values for notification details
     * @param document document snapshot
     */
    public Notifications(DocumentSnapshot document) {
        this.recipients = (ArrayList<String>)document.getData().get("recipients");
        this.id = document.getString("id");
        this.title = document.getString("title");
        this.smallText = document.getString("smallText");
        this.bigText = document.getString("bigText");
        this.timestamp = document.getString("timestamp");
    }

    /**
     * Initialize notification details
     * @param recipients array of recipients to send notification to
     * @param title title of notification
     * @param smallText small text for notification
     * @param bigText text for when user uses arrow for more notification details
     * @param id id of notification
     */
    public Notifications(ArrayList<String> recipients, String title, String smallText, String bigText, String id) {
        this.recipients = recipients;
        this.title = title;
        this.smallText = smallText;
        this.bigText = bigText;
        this.timestamp = timestamp;
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Parcel version of notification details
     * @param in parcel with notification details
     */
    protected Notifications(Parcel in) {
        recipients = in.createStringArrayList();
        id = in.readString();
        title = in.readString();
        smallText = in.readString();
        bigText = in.readString();
        timestamp = in.readString();
    }


    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        /**
         * create notification in parcel format
         * @param in The Parcel to read the object's data from.
         * @return new notification parcel
         */
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        /**
         * retrieve notification document size
         * @param size Size of the array.
         * @return size of notification
         */
        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    /**
     * Put notification details in a map
     * @return map version of storing notification details
     */
    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("recipients", recipients);
        newMap.put("id", id);
        newMap.put("title", title);
        newMap.put("smallText", smallText);
        newMap.put("bigText", bigText);
        newMap.put("timestamp", timestamp);

        return newMap;
    }

    /**
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write notification details to parcel
     * @param parcel The Parcel in which the object should be written.
     * @param i Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringList(recipients);
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(smallText);
        parcel.writeString(bigText);
        parcel.writeString((String) timestamp);
    }

    /**
     * get id of notification
     * @return notification id
     */
    public String getId() {
        return id;
    }

    /**
     * set id of notification
     * @param id notification id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get array of recipients
     * @return recipients as an array
     */
    public ArrayList<String> getRecipients() {
        return recipients;
    }

    /**
     * set array of recipients to send notification to
     * @param recipients array of recipients
     */
    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    /**
     * get title of notification
     * @return title of notification
     */
    public String getTitle() {
        return title;
    }

    /**
     * set title of notification
     * @param title title of notification
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get small text of notification.
     * @return notification text seen on user device
     */
    public String getSmallText() {
        return smallText;
    }

    /**
     * set small text of notification
     * @param smallText notification text seen on user device
     */
    public void setSmallText(String smallText) {
        this.smallText = smallText;
    }

    /**
     * get big text of notification. Extra notification details user sees.
     * @return extra notification text
     */
    public String getBigText() {
        return bigText;
    }

    /**
     * set big text of notification. Extra notification details user sees.
     * @param bigText extra notification text
     */
    public void setBigText(String bigText) {
        this.bigText = bigText;
    }

    /**
     * get timestamp of when organizer sent out notification to database
     * @return timestamp of notification sent to database
     */
    public Object getTimestamp() {
        return timestamp;
    }

    /**
     * set timestamp of when organizer sent out notification to database
     * @param timestamp timestamp of notification sent to database
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
