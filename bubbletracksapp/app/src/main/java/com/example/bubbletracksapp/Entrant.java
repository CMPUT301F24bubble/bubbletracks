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
    private ArrayList<String> eventsInvited = new ArrayList<>();
    private ArrayList<String> eventsEnrolled = new ArrayList<>();
    private ArrayList<String> eventsWaitlist = new ArrayList<>();

    public Entrant(String[] name, String email, String phone, String deviceID, Boolean notification, ArrayList<String> eventsOrganized, ArrayList<String> eventsInvited, ArrayList<String> eventsEnrolled, ArrayList<String> eventsWaitlist) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deviceID = deviceID;
        this.notification = notification;
        this.eventsOrganized = eventsOrganized;
        this.eventsInvited = eventsInvited;
        this.eventsEnrolled = eventsEnrolled;
        this.eventsWaitlist = eventsWaitlist;
    }

    public Entrant(String newDeviceID){
        this.name = new String[]{"",""};
        this.email = "";
        this.phone = "";
        this.deviceID = newDeviceID;
        this.notification = false;
    }

    public Entrant(){
        Log.w("NewEntrant", "Entrant has empty strings for information.");
        this.name = new String[]{"",""};
        this.email = "";
        this.phone = "";
        this.deviceID = "";
        this.notification = false;
        this.eventsOrganized = new ArrayList<>();
        this.eventsInvited = new ArrayList<>();
        this.eventsEnrolled = new ArrayList<>();
        this.eventsWaitlist = new ArrayList<>();
    }

    public Entrant(DocumentSnapshot document) {
        ArrayList<String> name = (ArrayList<String>)document.getData().get("name");

        this.name = new String[]{name.get(0), name.get(1)};
        this.email = document.getString("email");
        this.phone = document.getString("phone");
        this.deviceID = document.getString("ID");
        this.notification = document.getBoolean("notification");
        this.eventsOrganized = (ArrayList<String>)document.getData().get("organized");
        this.eventsInvited = (ArrayList<String>)document.getData().get("invited");
        this.eventsEnrolled = (ArrayList<String>)document.getData().get("enrolled");
        this.eventsWaitlist = (ArrayList<String>)document.getData().get("waitlist");

    }

    protected Entrant(Parcel in) {
        name = in.createStringArray();
        email = in.readString();
        phone = in.readString();
        deviceID = in.readString();
        byte tmpNotification = in.readByte();
        notification = tmpNotification == 0 ? null : tmpNotification == 1;
        eventsOrganized = in.createStringArrayList();
        eventsInvited = in.createStringArrayList();
        eventsEnrolled = in.createStringArrayList();
        eventsWaitlist = in.createStringArrayList();
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
        map.put("organized", eventsOrganized);
        map.put("invited", eventsInvited);
        map.put("enrolled", eventsEnrolled);
        map.put("waitlist", eventsWaitlist);

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

    public void updateEntrantFirebase() {
        new EntrantDB().updateEntrant(toMap());
    }

    public ArrayList<String> getEventsInvited() {
        return eventsInvited;
    }

    public void setEventsInvited(ArrayList<String> eventsInvited) {
        this.eventsInvited = eventsInvited;
    }

    public ArrayList<String> getEventsEnrolled() {
        return eventsEnrolled;
    }

    public void setEventsEnrolled(ArrayList<String> eventsEnrolled) {
        this.eventsEnrolled = eventsEnrolled;
    }

    public ArrayList<String> getEventsWaitlist() {
        return eventsWaitlist;
    }

    public void setEventsWaitlist(ArrayList<String> eventsWaitlist) {
        this.eventsWaitlist = eventsWaitlist;
    }
}
