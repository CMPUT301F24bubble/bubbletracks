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

public class Notifications implements Parcelable{

    private String id;

    // Parameters for notification information to builder
    private ArrayList<String> recipients;

    private String title;
    private String smallText;
    private String bigText;
    private Object timestamp;

    public Notifications() {
        Log.w("NewNotification", "Notification has empty information.");
        recipients = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
    }

    public Notifications(DocumentSnapshot document) {
        this.recipients = (ArrayList<String>)document.getData().get("recipients");
        this.id = document.getString("id");
        this.title = document.getString("title");
        this.smallText = document.getString("smallText");
        this.bigText = document.getString("bigText");
        this.timestamp = document.getString("timestamp");
    }

    public Notifications(ArrayList<String> recipients, String title, String smallText, String bigText, String id) {
        this.recipients = recipients;
        this.title = title;
        this.smallText = smallText;
        this.bigText = bigText;
        this.timestamp = timestamp;
        this.id = generateUniqueId();
    }

    protected Notifications(Parcel in) {
        recipients = in.createStringArrayList();
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

        newMap.put("recipients", recipients);
        newMap.put("id", id);
        newMap.put("title", title);
        newMap.put("smallText", smallText);
        newMap.put("bigText", bigText);
        newMap.put("timestamp", timestamp);

        return newMap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringList(recipients);
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(smallText);
        parcel.writeString(bigText);
        parcel.writeString((String) timestamp);
    }

    public String getId() {
        return id;
    }

    private String generateUniqueId() {
        return String.valueOf(System.currentTimeMillis());  // Using the current time as an ID
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

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
