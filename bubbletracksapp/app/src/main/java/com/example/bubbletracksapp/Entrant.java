package com.example.bubbletracksapp;


import android.util.Log;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

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

    public Entrant(String[] newName, String newEmail, String newPhone, String newDevice, Boolean notificationPermission) {
        this.name = newName;
        this.email = newEmail;
        this.phone = newPhone;
        this.deviceID = newDevice;
        this.notification = notificationPermission;
    }

    public Entrant(){
        Log.w("NewEntrant", "Entrant has empty strings for information.");
        this.name = new String[]{"",""};
        this.email = "";
        this.phone = "";
        this.deviceID = "";
        this.notification = false;
    }

    protected Entrant(Parcel in) {
        name = in.createStringArray();
        email = in.readString();
        phone = in.readString();
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

    // MUST BE CHANGED, should be the phoneID but for now the ID is the name INCOMPLETE
    public String getID() {
        return name[0]+name[1];
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
}
