package com.example.bubbletracksapp;


import android.util.Log;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Entrant implements Parcelable {
    /**
     * This tracks the information for a given entrant.
     * INCOMPLETE:
     * This class currently lacks a way to store profile pictures
     * It also doesn't track any contact info besides email and phone
     * Also, there's no fancy data types for email or phone.
     */
    private String[] name;
    private String email;
    private String phone;
    private String deviceID;
    private Boolean notification;
    private ArrayList<String> eventsOrganized = new ArrayList<>();

    public Entrant(String[] newName, String newEmail, String newPhone, String newDevice, Boolean notificationPermission, ArrayList<String> eventsOrganized) {
        this.name = newName;
        this.email = newEmail;
        this.phone = newPhone;
        this.deviceID = newDevice;
        this.notification = notificationPermission;
        this.eventsOrganized = eventsOrganized;
    }

    public Entrant(){
        Log.w("NewEntrant", "Entrant has empty strings for information.");
        this.name = new String[]{"",""};
        this.email = "";
        this.phone = "";
        this.deviceID = "";
        this.notification = false;
        this.eventsOrganized = new ArrayList<>();
    }

    public Entrant(DocumentSnapshot document) {
        ArrayList<String> name = (ArrayList<String>)document.getData().get("name");

        this.name = new String[]{name.get(0), name.get(1)};
        this.email = document.getString("email");
        this.phone = document.getString("phone");
        this.deviceID = document.getString("ID");
        this.notification = document.getBoolean("notification");
        this.eventsOrganized = (ArrayList<String>)document.getData().get("events");
    }

    protected Entrant(Parcel in) {
        name = in.createStringArray();
        email = in.readString();
        phone = in.readString();
        deviceID = in.readString();
        byte tmpNotification = in.readByte();
        notification = tmpNotification == 0 ? null : tmpNotification == 1;
        eventsOrganized = in.createStringArrayList();
    }

    public static final Creator<Entrant> CREATOR = new Creator<Entrant>() {
        @Override
        public Entrant createFromParcel(Parcel in) {
            return new Entrant(in);
        }

        @Override
        public Entrant[] newArray(int size) {
            return new Entrant[size];
        }
    };

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("name", getNameAsList());
        map.put("email", email);
        map.put("phone", phone);
        map.put("notification", notification);
        map.put("ID", deviceID);
        map.put("events", eventsOrganized);

        return map;
    }

    public String[] getName() {
        return name;
    }

    public List<String> getNameAsList() {
        return Arrays.asList(name);
    }

    public String getNameAsString() {return String.format("%s %s",name[0],name[1]); };

    public void setName(String first, String last) {
        this.name = new String[]{first, last};
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // should enforce a format for the email
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        // should enforce a format for the phone number
        this.phone = phone;
    }


    public Boolean getNotification() { return notification;}

    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringArray(name);
        parcel.writeString(email);
        parcel.writeString(phone);
    }

    // Will need to be updated after changing the fields. May be easy to just use the device ID.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrant entrant = (Entrant) o;
        return Objects.deepEquals(name, entrant.name) && Objects.equals(email, entrant.email) && Objects.equals(phone, entrant.phone) && Objects.equals(deviceID, entrant.deviceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(name), email, phone, deviceID);
    }

    public String getID() {
        return deviceID;
    }

    public void setID(String deviceID) {
        this.deviceID = deviceID;
    }

    public ArrayList<String> getEventsOrganized() {
        return eventsOrganized;
    }

    public void setEventsOrganized(ArrayList<String> eventsOrganized) {
        this.eventsOrganized = eventsOrganized;
    }
}
