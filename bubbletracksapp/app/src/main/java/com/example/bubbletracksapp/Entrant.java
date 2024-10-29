package com.example.bubbletracksapp;


import android.util.Log;

import java.util.Arrays;
import java.util.List;

public class Entrant {
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

    public Entrant(String[] newName, String newEmail, String newPhone, String newDevice) {
        this.name = newName;
        this.email = newEmail;
        this.phone = newPhone;
        this.deviceID = newDevice;
    }

    public Entrant(){
        Log.w("NewEntrant", "Entrant has empty strings for information.");
        this.name = new String[]{"",""};
        this.email = "";
        this.phone = "";
        this.deviceID = "";
    }

    public String[] getName() {
        return name;
    }

    public List<String> getNameAsList() {
        return Arrays.asList(name);
    }

    public String getNameAsString() {return Arrays.toString(name); };

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
}
