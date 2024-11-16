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
/**
 *
 * @author Zoe, Chester, Erza
 * @version 1.0
 */

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

    /**
     *
     * @param name A string of the entrants name
     * @param email Entrants email
     * @param phone Entrants phone number
     * @param deviceID Entrants device ID to determine who the entrant is
     * @param notification Entrants declaration of allowing notification
     * @param eventsOrganized events from the organizer
     * @param eventsInvited evened entrant is invited to
     * @param eventsEnrolled event entrant is enrolled in
     * @param eventsWaitlist event entrant is in waitlist for
     */
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

    /**
     * Store initial values for entrant
     * @param newDeviceID device ID of entrant
     */
    public Entrant(String newDeviceID){
        this.name = new String[]{"",""};
        this.email = "";
        this.phone = "";
        this.deviceID = newDeviceID;
        this.notification = false;
        this.eventsOrganized = new ArrayList<>();
        this.eventsInvited = new ArrayList<>();
        this.eventsEnrolled = new ArrayList<>();
        this.eventsWaitlist = new ArrayList<>();
    }

    /**
     * To log entrant information
     */
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

    /**
     * To retrieve entrant information from document
     * @param document document snapshot
     */
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
    /**
     * For the entrant information to be used in a parcel
     * @param in
     *      parcel with entrant information
     */

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
    /**
     * Makes creator of entrant
     */

    public static final Creator<Entrant> CREATOR = new Creator<Entrant>() {
        @Override
        public Entrant createFromParcel(Parcel in) {
            return new Entrant(in);
        }
        /**
         * Create new array of the entrants details
         * @param size Size of the array.
         * @return array of the entrants details
         */

        @Override
        public Entrant[] newArray(int size) {
            return new Entrant[size];
        }
    };

    /**
     * Hash map of entrant information
     * @return hash map of entrant
     */
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
    /**
     * Retrieve name of entrant
     * @return the name of the entrant
     */

    public String[] getName() {
        return name;
    }
    /**
     *Retrieve name of entrant in list form
     * @return name of entrant as a list
     */

    public List<String> getNameAsList() {
        return Arrays.asList(name);
    }

    /**
     * Get entrant name as a string
     * @return string format of name of entrant
     */
    public String getNameAsString() {return String.format("%s %s",name[0],name[1]); };

    /**
     * Set the name of the entrant
     * @param first a string of the entrants first name
     * @param last a a string of the entrants last name
     */
    public void setName(String first, String last) {
        this.name = new String[]{first, last};
    }
    /**
     * retrieve the entrants email
     * @return the entrants email
     */

    public String getEmail() {
        return email;
    }
    /**
     * Set the email of the entrant
     * @param email a string of the entrants new email
     */
    public void setEmail(String email) {
        // should enforce a format for the email
        this.email = email;
    }

    /**
     * Get the phone number of the entrant
     * @return a string format of the entrants phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set the entrants phone number
     * @param phone a string format of the entrants new phone number
     */
    public void setPhone(String phone) {
        // should enforce a format for the phone number
        this.phone = phone;
    }

    /**
     * Gets whether the entrant allows notifications or not
     * @return boolean if entrant allows notification
     */
    public Boolean getNotification() { return notification;}

    /**
     * Sets whether entrant allows for notification or not
     * @param notification boolean if entrant allows for notification
     */
    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    /**
     * describes the content of the entrant
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes to the parcel that holds the entrants details
     * @param parcel The Parcel in which the object should be written.
     * @param i Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeStringArray(name);
        parcel.writeString(email);
        parcel.writeString(phone);
    }

    // Will need to be updated after changing the fields. May be easy to just use the device ID.\
    /**
     * Find the entrant in list of entrants (checks for id)
     * @param o entrant object
     * @return a boolean to find the entrant
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrant entrant = (Entrant) o;
        return Objects.deepEquals(name, entrant.name) && Objects.equals(email, entrant.email) && Objects.equals(phone, entrant.phone) && Objects.equals(deviceID, entrant.deviceID);
    }
    /**
     * get hash code of entrant
     * @return has code of entrant
     */
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(name), email, phone, deviceID);
    }

    /**
     * Get device ID of entrant
     * @return device ID of entrant
     */
    public String getID() {
        return deviceID;
    }

    /**
     * Set the device ID of entrant
     * @param deviceID string of device ID
     */
    public void setID(String deviceID) {
        this.deviceID = deviceID;
    }

    /**
     * Get the event entrant is interacting with
     * @return Array of the events entrants is a part of
     */
    public ArrayList<String> getEventsOrganized() {
        return eventsOrganized;
    }

    /**
     * set the array of the event entrant is interacting with
     * @param eventsOrganized
     */
    public void setEventsOrganized(ArrayList<String> eventsOrganized) {
        this.eventsOrganized = eventsOrganized;
    }

    /**
     * Update the entrant firebase
     */
    public void updateEntrantFirebase() {
        new EntrantDB().updateEntrant(toMap());
    }

    /**
     * Get the events that the entrant is invited to
     * @return list of events entrant is invited to
     */
    public ArrayList<String> getEventsInvited() {
        return eventsInvited;
    }

    /**
     * Set the events that the entrant is invited to
     * @param eventsInvited array of events entrant is invited to
     */
    public void setEventsInvited(ArrayList<String> eventsInvited) {
        this.eventsInvited = eventsInvited;
    }

    /**
     * get the events the entrants are enrolled in
     * @return list of events entrant is enrolled to
     */
    public ArrayList<String> getEventsEnrolled() {
        return eventsEnrolled;
    }

    /**
     * set the events that the entrant is enrolled in
     * @param eventsEnrolled list of events entrant is enrolled
     */
    public void setEventsEnrolled(ArrayList<String> eventsEnrolled) {
        this.eventsEnrolled = eventsEnrolled;
    }

    /**
     * get and set the events the entrant is in the waitlist for
     * @return
     */
    public ArrayList<String> getEventsWaitlist() {
        return eventsWaitlist;
    }

    public void setEventsWaitlist(ArrayList<String> eventsWaitlist) {
        this.eventsWaitlist = eventsWaitlist;
    }

    /**
     * Add entrant to event
     * @param event string of event
     */
    public void addToEventsOrganized(String event){ this.eventsOrganized.add(event); }

    /**
     * Delete entrant from event
     * @param event string of event
     */
    public void deleteFromEventsOrganized(String event){ this.eventsOrganized.remove(event); }

    /**
     * Add entrant to invited list
     * @param event string of event
     */
    public void addToEventsInvited(String event){ this.eventsInvited.add(event); }

    /**
     * Delete entrant from invited list
     * @param event string of event
     */
    public void deleteFromEventsInvited(String event){ this.eventsInvited.remove(event); }

    /**
     * add entrant to enrolled list
     * @param event string of event
     */
    public void addToEventsEnrolled(String event){ this.eventsEnrolled.add(event); }

    /**
     * delete entrant from enrolled list
     * @param event string of event
     */
    public void deleteFromEventsEnrolled(String event){ this.eventsEnrolled.remove(event); }

    /**
     * Add entrant to waitlist
     * @param event string of event
     */
    public void addToEventsWaitlist(String event){ this.eventsWaitlist.add(event); }

    /**
     * Delete entrant from waitlist
     * @param event string of waitlist
     */
    public void deleteFromEventsWaitlist(String event){ this.eventsWaitlist.remove(event); }
}
