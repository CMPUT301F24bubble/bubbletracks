package com.example.bubbletracksapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Notification {

    private String id;

    // Parameters for notification information to builder
    private ArrayList<String> recipients;

    private String title;
    private String smallText;
    private String bigText;
    private String timestamp;

    public Notification() {
        Log.w("NewNotification", "Notification has empty information.");
        recipients = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public Notification(DocumentSnapshot document) {
        this.id = document.getString("id");
        this.title = document.getString("title");
        this.smallText = document.getString("smallText");
        this.bigText = document.getString("bigText");
        this.timestamp = document.getString("timestamp");
    }

    public Notification(String title, String smallText, String bigText, String timestamp) {
        this.id = id;
        this.title = title;
        this.smallText = smallText;
        this.bigText = bigText;
        this.timestamp = timestamp;
    }

    protected Notification(Parcel in) {
        id = in.readString();
        title = in.readString();
        smallText = in.readString();
        bigText = in.readString();
        timestamp = in.readString();
    }

    public static final Parcelable.Creator<Notification> CREATOR = new Parcelable.Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("id", id);
        newMap.put("title", title);
        newMap.put("smallText", smallText);
        newMap.put("bigText", bigText);
        newMap.put("timestamp", timestamp);

        return newMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(ArrayList<String> recipients) {
        this.recipients = recipients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSmallText() {
        return smallText;
    }

    public void setSmallText(String smallText) {
        this.smallText = smallText;
    }

    public String getBigText() {
        return bigText;
    }

    public void setBigText(String bigText) {
        this.bigText = bigText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
