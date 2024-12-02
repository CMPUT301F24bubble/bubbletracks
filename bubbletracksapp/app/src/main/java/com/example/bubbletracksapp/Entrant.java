package com.example.bubbletracksapp;


import static java.util.Map.entry;

import android.util.Log;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

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
    private LatLng geolocation;
    private String role;
    private String facility;
    private String profilePicture;
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
     * @param role Denotes role; takes on either 'admin', 'organizer', or ''
     * @param facility facility ID to determine which facility belongs to this entrant
     * @param geolocation Denotes the geolocation when the entrant last updated their profile
     * @param profilePicture stores the download link for the profile picture of the entrant
     * @param eventsOrganized events from the organizer
     * @param eventsInvited events entrant is invited to
     * @param eventsEnrolled events entrant is enrolled in
     * @param eventsWaitlist events entrant is in waitlist for
     */
    public Entrant(String[] name, String email, String phone, String deviceID, Boolean notification, String role, String facility, LatLng geolocation, String profilePicture, ArrayList<String> eventsOrganized, ArrayList<String> eventsInvited, ArrayList<String> eventsEnrolled, ArrayList<String> eventsWaitlist) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deviceID = deviceID;
        this.notification = notification;
        this.geolocation = geolocation;
        this.role = role;
        this.facility = facility;
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
        this.role = "";
        this.facility = "";
        this.notification = false;
        this.profilePicture = "";
        this.geolocation = new LatLng(0,0);
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
        this.role = "";
        this.facility = "";
        this.notification = false;
        this.profilePicture = "";
        this.geolocation = new LatLng(0,0);
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
        this.role = document.getString("role");
        this.geolocation = geoPointToLatLng(document.getGeoPoint("geolocation"));
        this.role = document.getString("role");
        this.facility = document.getString("facility");
        this.profilePicture = document.getString("profilePicture");
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
        geolocation = in.readParcelable(LatLng.class.getClassLoader());
        role = in.readString();
        facility = in.readString();
        profilePicture = in.readString();
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
        map.put("role", role);
        map.put("facility", facility);
        map.put("geolocation", latLngToGeoPoint(geolocation));
        map.put("notification", notification);
        map.put("ID", deviceID);
        map.put("profilePicture", profilePicture);
        map.put("organized", eventsOrganized);
        map.put("invited", eventsInvited);
        map.put("enrolled", eventsEnrolled);
        map.put("waitlist", eventsWaitlist);

        return map;
    }

    /**
     * Retrieve role (organizer, admin, null) or entrant
     * @return role of entrant as a string
     */
    public String getRole() { return role; }

    /**
     * retrieve id of entrant's facility
     * @return id of entrant's facility
     */
    public String getFacility() { return facility; }

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
     * set the entrant's facility
     * @param facility id of the entrant's facility
     */
    public void setFacility(String facility) { this.facility = facility; }

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
     * Gets the download link for the entrant's profile picture
     * @return download link for the entrant's profile picture
     */
    public String getProfilePicture() { return profilePicture; }


    /**
     * From a map of urls that contain default profile pictures with letters, it selects one from the
     * first name and sets the URL to the firebase.
     * Credits of profile pictures: <a href="https://github.com/eladnava/material-letter-icons/tree/master/dist/png">...</a>
     * @return String of the new profile picture URL
     */
     public String setDefaultPicture() {
         String firstLetter = "other";
         if(!Objects.equals(name[0], ""))
         {
             firstLetter = String.valueOf(name[0].charAt(0));
             if (Character.isLetter(firstLetter.charAt(0))) {
                 firstLetter = firstLetter.toUpperCase();
             }
             else
             {
                 firstLetter = "other";
             }
         }
        Map<String,String> alphabetMap = generateAlphabetMap();
        this.profilePicture = alphabetMap.get(firstLetter);
        return profilePicture;
    }

    /**
     * Sets whether entrant allows for notification or not
     * @param notification boolean if entrant allows for notification
     */
    public void setNotification(Boolean notification) {
        this.notification = notification;
    }

    /**
     * Get the user's hosted events.
     * @return A list of the user's hosted events.
     */
    public ArrayList<String> getOrganized() {
        return eventsOrganized;
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
        parcel.writeString(deviceID);
        parcel.writeByte((byte) (notification == null ? 0 : notification ? 1 : 2));
        parcel.writeParcelable(geolocation, i);
        parcel.writeString(role);
        parcel.writeString(facility);
        parcel.writeString(profilePicture);
        parcel.writeStringList(eventsOrganized);
        parcel.writeStringList(eventsInvited);
        parcel.writeStringList(eventsEnrolled);
        parcel.writeStringList(eventsWaitlist);
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
        return Objects.deepEquals(name, entrant.name) && Objects.equals(email, entrant.email) && Objects.equals(phone, entrant.phone) && Objects.equals(deviceID, entrant.deviceID) && Objects.equals(role, entrant.role) && Objects.equals(profilePicture, entrant.profilePicture);
    }
    /**
     * get hash code of entrant
     * @return has code of entrant
     */
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(name), email, phone, deviceID, profilePicture);
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
     * sets the download link for the profile picture
     * @param profilePicture download link for the profile picture
     */
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
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
     * set the role of the entrant; must be "entrant", "admin", or "organizer"
     * @param role
     */
    public void setRole(String role) {
        if (role.equalsIgnoreCase("entrant") || role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("organizer"))
        { this.role = role; } else {
            throw new IllegalArgumentException("Entrant role must equal \"entrant\", \"admin\", or \"organizer\"");
        }
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


    /**
     * Get the geolocation coordinates
     * @return The latitude and longitude of the entrant
     */
    public LatLng getGeolocation() {
        return geolocation;
    }

    /**
     * Set the geolocation coordinates
     * @param geolocation the geolocation point in latitude and longitude
     */
    public void setGeolocation(LatLng geolocation) {
        this.geolocation = geolocation;
    }

    /**
     * Transforms a LatLng point to a geoPoint
     * Used to store in database
     * @param geolocation LatLng point to be transformed
     * @return geoPoint from the given LatLng point
     */
    private GeoPoint latLngToGeoPoint(LatLng geolocation) {
        double lat = geolocation.latitude;
        double lng = geolocation.longitude;
        return new GeoPoint(lat,lng);
    }

    /**
     * Transforms a geoPoint to a LatLng point
     * Used to get from database
     * @param geolocation geoPoint point to be transformed
     * @return LatLng point from the given geoPoint
     */
    private LatLng geoPointToLatLng(GeoPoint geolocation) {
        double lat = geolocation.getLatitude();
        double lng = geolocation.getLongitude();
        return new LatLng(lat,lng);
    }

    /**
     * Removes the event from the users list
     * @param event The event's ID being rejected
     */
    public void rejectEvent(String event) {
        eventsWaitlist.remove(event);
        eventsInvited.remove(event);
        eventsEnrolled.remove(event);
        updateEntrantFirebase();
    }

    /**
     * Adds the event into the users enrolled list
     * @param event The event's ID being accepted
     */
    public void acceptEvent(String event) {
        eventsEnrolled.add(event);
        updateEntrantFirebase();
    }

    /**
     * Creates a Map with the URL's related to each letter.
     * It is not the best implementation but it is easy and we don't expect to change the URL.
     * @return The map with the letters and URLs.
     */
    private Map<String,String> generateAlphabetMap() {
        Map<String, String> alphabetMap = Map.ofEntries(entry("A", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FA.png?alt=media&token=ea317f60-a0a9-4980-8e62-b08ac6c7b333"),
                entry("B", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FB.png?alt=media&token=9553b0b7-9c3f-49d3-89d9-1a64252c11c4"),
                entry("C", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FC.png?alt=media&token=30986a09-bcba-44df-a10f-3f48a44d22d1"),
                entry("D", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FD.png?alt=media&token=deb2730c-8eda-4d48-811c-91db15d00041"),
                entry("E", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FE.png?alt=media&token=72f4c2ee-75f3-4c07-859e-7bb9189fc078"),
                entry("F", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FF.png?alt=media&token=1867d9d8-9d7a-458f-bcf6-4436b5247771"),
                entry("G", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FG.png?alt=media&token=26278ec2-e0e3-4970-9608-650ce580990a"),
                entry("H", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FH.png?alt=media&token=7192d433-638a-440d-844c-e488b8027d84"),
                entry("I", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FI.png?alt=media&token=8773b341-df57-4443-8790-6a52c63e70d0"),
                entry("J", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FJ.png?alt=media&token=d1725572-d79f-41dc-ab88-322baba3c088"),
                entry("K", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FK.png?alt=media&token=1e8b48ca-2c76-41b3-8df3-7aeadb88e858"),
                entry("L", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FL.png?alt=media&token=8c7bf98f-b596-4954-9a06-50ab53e5967a"),
                entry("M", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FM.png?alt=media&token=7c670671-3912-47e8-8e2d-4322691d4863"),
                entry("N", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FN.png?alt=media&token=4f4c330d-4388-49e9-bead-9fbaca940ca8"),
                entry("O", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FO.png?alt=media&token=7265da6e-0a65-444b-ac9e-c504f61bfbdc"),
                entry("P", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FP.png?alt=media&token=d1cd089b-969c-42ea-9f6a-8705953e8011"),
                entry("Q", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FQ.png?alt=media&token=dc027cd6-ab70-403d-b7e2-b715bb145e3e"),
                entry("R", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FR.png?alt=media&token=f6970e54-edff-4472-a991-a38446d38381"),
                entry("S", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FS.png?alt=media&token=5db2210b-1835-47d6-a2f8-e1e8331246b6"),
                entry("T", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FT.png?alt=media&token=bb42bcb3-56b9-41de-b420-8bc057ab546d"),
                entry("U", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FU.png?alt=media&token=62a923e3-05b2-48c7-91c2-dc7605939ddf"),
                entry("V", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FV.png?alt=media&token=463cb3dd-2229-4888-b367-283be378466b"),
                entry("W", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FW.png?alt=media&token=d63e78f3-a0f7-4d0c-be84-f1df3333e027"),
                entry("X", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FX.png?alt=media&token=6001425f-69af-43f7-a8ee-327d1c97a55c"),
                entry("Y", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FY.png?alt=media&token=e4323e24-b0b6-44f1-bdf7-e7cd00c758b4"),
                entry("Z", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2FZ.png?alt=media&token=9638cf7d-3c91-4822-af1f-76ce04332402"),
                entry("other", "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/profile-pictures%2Fother.png?alt=media&token=72b8e1a2-c619-4c8c-b98b-656a899a9e36"));
        return alphabetMap;
    }
    /**
     * Deletes profile pic of entrant
     */
    public void deleteProfilePic() {
        EntrantDB db = new EntrantDB();
        db.deleteProfilePic(deviceID);

    }

}
