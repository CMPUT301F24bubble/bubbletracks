package com.example.bubbletracksapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

/**
 * this tracks the information of a given event
 */
public class Event implements Parcelable{
    private String id;
    private String name;
    private Date dateTime;
    private String description;
    private String geolocation;
    private String facility;
    private Date registrationOpen;
    private Date registrationClose;
    private int maxCapacity;
    private int price;
    private int WaitListLimit;
    private boolean needsGeolocation;
    private String image;
    private String QRCode;
    private ArrayList<String> waitList = new ArrayList<>();
    private ArrayList<String> invitedList = new ArrayList<>();
    private ArrayList<String> cancelledList = new ArrayList<>();
    private ArrayList<String> rejectedList = new ArrayList<>();
    private ArrayList<String> enrolledList = new ArrayList<>();

    /**
     * constructor that sets the id and initializes the arrays
     */
    public Event(){
        Log.w("NewEvent", "Event has empty information.");

        this.id = UUID.randomUUID().toString();
        waitList = new ArrayList<>();
        invitedList = new ArrayList<>();
        cancelledList = new ArrayList<>();
        rejectedList = new ArrayList<>();
        enrolledList = new ArrayList<>();
    }

    /**
     * Constructor that creates an event from a document snapshot
     * @param document document snapshot from the database
     */
    public Event(DocumentSnapshot document) {
        this.id = document.getString("id");
        this.name = document.getString("name");
        this.dateTime = document.getTimestamp("dateTime").toDate();
        this.description = document.getString("description");
        this.geolocation=  document.getString("geolocation");
        this.facility = document.getString("facility");
        this.registrationOpen = document.getTimestamp("registrationOpen").toDate();
        this.registrationClose = document.getTimestamp("registrationClose").toDate();
        this.maxCapacity = document.getLong("maxCapacity").intValue();
        this.price = document.getLong("price").intValue();
        this.WaitListLimit =  document.getLong("waitListLimit").intValue();
        this.needsGeolocation = document.getBoolean("needsGeolocation");
        this.image = document.getString("image");
        this.QRCode = document.getString("QRCode");
        this.waitList = (ArrayList<String>)document.getData().get("wait");
        this.invitedList = (ArrayList<String>)document.getData().get("invited");
        this.cancelledList = (ArrayList<String>)document.getData().get("cancelled");
        this.rejectedList = (ArrayList<String>)document.getData().get("rejected");
        this.enrolledList = (ArrayList<String>)document.getData().get("enrolled");
    }

    /**
     * constructor given all the attributes of the event
     * @param id id of the event
     * @param name name of the event
     * @param dateTime date and time of the event
     * @param description description for the event
     * @param geolocation location of the event
     * @param facility id of the facility where the event will be
     * @param registrationOpen registration open date of the event
     * @param registrationClose registration close date of the event
     * @param maxCapacity capacity of the event
     * @param price price of the event
     * @param waitListLimit limit of the waitlist
     * @param needsGeolocation geolocation requirement for the event
     * @param image download link for the event's poster
     * @param QRCode download link for the event's QR code
     * @param waitList array of ids of entrants that are in the waitlist
     * @param invitedList array of ids of entrants that are invited to the event
     * @param cancelledList array of ids of entrants that are cancelled from the event
     * @param rejectedList array of ids of entrants that are rejected from the waitlist
     * @param enrolledList array of ids of entrants that are enrolled for the event
     */
    public Event(String id, String name, Date dateTime, String description, String geolocation, String facility, Date registrationOpen, Date registrationClose, int maxCapacity, int price, int waitListLimit, boolean needsGeolocation, String image, String QRCode, ArrayList<String> waitList, ArrayList<String> invitedList, ArrayList<String> cancelledList, ArrayList<String> rejectedList, ArrayList<String> enrolledList) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.geolocation = geolocation;
        this.facility = facility;
        this.registrationOpen = registrationOpen;
        this.registrationClose = registrationClose;
        this.maxCapacity = maxCapacity;
        this.price = price;
        WaitListLimit = waitListLimit;
        this.needsGeolocation = needsGeolocation;
        this.image = image;
        this.QRCode = QRCode;
        this.waitList = waitList;
        this.invitedList = invitedList;
        this.cancelledList = cancelledList;
        this.rejectedList = rejectedList;
        this.enrolledList = enrolledList;
    }


    /**
     *
     * @param in
     */
    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        geolocation = in.readString();
        facility = in.readString();
        maxCapacity = in.readInt();
        price = in.readInt();
        WaitListLimit = in.readInt();
        needsGeolocation = in.readByte() != 0;
        image = in.readString();
        QRCode = in.readString();
        waitList = in.createStringArrayList();
        invitedList = in.createStringArrayList();
        cancelledList = in.createStringArrayList();
        rejectedList = in.createStringArrayList();
        enrolledList = in.createStringArrayList();
        dateTime = new Date(in.readLong());
        registrationOpen = new Date(in.readLong());
        registrationClose = new Date(in.readLong());
    }

    /**
     *
     */
    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    /**
     * converts the event to a map
     * @return the converted map
     */
    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("id", id);
        newMap.put("name", name);
        newMap.put("dateTime", dateTime);
        newMap.put("description", description);
        newMap.put("geolocation", geolocation);
        newMap.put("facility", facility);
        newMap.put("registrationOpen", registrationOpen);
        newMap.put("registrationClose", registrationClose);
        newMap.put("maxCapacity", maxCapacity);
        newMap.put("price", price);
        newMap.put("waitListLimit", WaitListLimit);
        newMap.put("needsGeolocation", needsGeolocation);
        newMap.put("image", image);
        newMap.put("QRCode", QRCode);
        newMap.put("wait", waitList);
        newMap.put("invited", invitedList);
        newMap.put("cancelled", cancelledList);
        newMap.put("rejected", rejectedList);
        newMap.put("enrolled", enrolledList);

        return newMap;
    }

    /**
     * getter for the id of the event
     * @return id of the event
     */
    public String getId() { return id; }

    /**
     * setter for the id of the event
     * @param id id to be set
     */
    public void setId(String id) {this.id = id;}

    /**
     * Getter for the name of the event
     * @return name of the event
     */
    public String getName() { return name; }

    /**
     * Setter for the name of the event
     * @param name name to be set
     */
    public void setName(String name) { this.name = name; }

    /**
     * Getter for the date and time of the event
     * @return date and time of the event
     */
    public Date getDateTime() { return dateTime; }

    /**
     * Setter for the date and time of the event
     * @param dateTime date and time to be set
     */
    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    /**
     * Getter for the description of the event
     * @return description of the event
     */
    public String getDescription() { return description; }

    /**
     * Setter for the description of the event
     * @param description description to be set
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Getter for the location of the event
     * @return location of the event
     */
    public String getGeolocation() { return geolocation; }

    /**
     * Setter for the location of the event
     * @param geoLocation location to be set
     */
    public void setGeolocation(String geoLocation) { this.geolocation = geoLocation; }

    /**
     * Getter for the id of facility associated with the event
     * @return id of facility of the event
     */
    public String getFacility() { return facility; }

    /**
     * Setter for the id of facility associated with the event
     * @param facility id of facility to be set
     */
    public void setFacility(String facility) { this.facility = facility; }

    /**
     * Getter for the registration open date
     * @return registration open date
     */
    public Date getRegistrationOpen() { return registrationOpen; }

    /**
     * Setter for the registration open date
     * @param registrationOpen registration open date to be set
     */
    public void setRegistrationOpen(Date registrationOpen) { this.registrationOpen = registrationOpen; }

    /**
     * Getter for the registration close date
     * @return registration close date
     */
    public Date getRegistrationClose() { return registrationClose; }

    /**
     * Setter for the registration close date
     * @param registrationClose registration close date to be set
     */
    public void setRegistrationClose(Date registrationClose) { this.registrationClose = registrationClose; }

    /**
     * Getter for the capacity of the event
     * @return capacity of the event
     */
    public int getMaxCapacity() { return maxCapacity; }

    /**
     * Setter for the capacity of the event
     * @param maxCapacity capacity to be set
     */
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    /**
     * Getter for the price of the event
     * @return price of the event
     */
    public int getPrice() { return price; }

    /**
     * Setter for the price of the event
     * @param price price to be set
     */
    public void setPrice(int price) { this.price = price; }

    /**
     * Getter for the waitlist limit of the event
     * @return waitlist limit of the event
     */
    public int getWaitListLimit() { return WaitListLimit; }

    /**
     * Setter for the waitlist limit of the event
     * @param waitListLimit waitlist limit to be set
     */
    public void setWaitListLimit(int waitListLimit) { WaitListLimit = waitListLimit; }

    /**
     * Getter for the event's geolocation requirement
     * @return true if the event needs geolocation, false otherwise
     */
    public boolean getNeedsGeolocation() { return needsGeolocation; }

    /**
     * Setter for the event's geolocation requirement
     * @param needsGeolocation true if the event needs geolocation, false otherwise
     */
    public void setNeedsGeolocation(boolean needsGeolocation) { this.needsGeolocation = needsGeolocation; }

    /**
     * Getter for the poster's download link for the event
     * @return download link of the poster for the event
     */
    public String getImage() { return image; }

    /**
     * Setter for the poster's download link for the event
     * @param image  download link of the poster to be set
     */
    public void setImage(String image) { this.image = image; }

    /**
     * Getter for the QR code's download link for the event
     * @return download link of the QR code for the event
     */
    public String getQRCode() { return QRCode; }

    /**
     * Setter for the QR code's download link for the event
     * @param QRCode download link of the QR code to be set
     */
    public void setQRCode(String QRCode) { this.QRCode = QRCode; }

    /**
     * extracts the year of the given date object
     * @param date date object for which the year needs to be extracted
     * @return extracted year
     */
    public String getYear(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * extracts the month of the given date object
     * @param date date object for which the month needs to be extracted
     * @return extracted month
     */
    public String getMonth(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM", Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * extracts the day of the given date object
     * @param date date object for which the day needs to be extracted
     * @return extracted day
     */
    public String getDay(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd", Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * extracts the time of the given date object
     * @param date date object for which the time needs to be extracted
     * @return extracted time
     */
    public String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatter.format(date);
    }

    /**
     * Getter for the array of ids of entrants that are in the waitlist of the event
     * @return array of ids of entrants in the waitlist for the event
     */
    public ArrayList<String> getWaitList() {
        return waitList;
    }

    /**
     * Setter for the array of ids of entrants that are in the waitlist of the event
     * @param waitList array of ids of entrants in the waitlist to be set
     */
    public void setWaitList(ArrayList<String> waitList) {
        this.waitList = waitList;
    }

    /**
     * sets the waitlist of the event given an array of entrants
     * @param waitList array of entrants to be in the waitlist
     */
    public void setWaitListWithEvents(ArrayList<Entrant> waitList) {
        this.waitList = entrantListToStringList(waitList);
    }

    /**
     * adds an entrant to the waitlist
     * @param entrant entrant to be added
     */
    public void addToWaitList(String entrant) {
        this.waitList.add(entrant);
    }

    /**
     * removes an entrant to the waitlist
     * @param entrant entrant entrant to be removed
     */
    public void deleteFromWaitList(String entrant) {
        this.waitList.remove(entrant);
    }

    /**
     * clears the waitlist
     */
    public void clearWaitList() {
        this.waitList.clear();
    }

    /**
     * Getter for the array of ids of entrants invited to the event
     * @return array of ids of entrants invited to the event
     */
    public ArrayList<String> getInvitedList() {
        return invitedList;
    }

    /**
     * Setter for the array of ids of entrants invited to the event
     * @param invitedList array of ids of entrants invited to the event to be set
     */
    public void setInvitedList(ArrayList<String> invitedList) {
        this.invitedList = invitedList;
    }

    /**
     * Sets the invited list of the event given an array of entrants
     * @param invitedList array of entrants invited to the event
     */
    public void setInvitedListWithEvents(ArrayList<Entrant> invitedList) {
        this.invitedList = entrantListToStringList(invitedList);
    }

    /**
     * Adds an entrant to the invited list.
     * @param entrant entrant to be added to the invited list
     */
    public void addToInvitedList(String entrant) {
        this.invitedList.add(entrant);
    }

    /**
     * Removes an entrant from the invited list.
     * @param entrant entrant to be removed from the invited list
     */
    public void deleteFromInvitedList(String entrant) {
        this.invitedList.remove(entrant);
    }

    /**
     * Clears the invited list.
     */
    public void clearInvitedList() {
        this.invitedList.clear();
    }

    /**
     * Getter for the array of ids of entrants cancelled from the event
     * @return array of ids of entrants cancelled from the event
     */
    public ArrayList<String> getCancelledList() {
        return cancelledList;
    }

    /**
     * Setter for the array of ids of entrants cancelled from the event
     * @param cancelledList array of ids of entrants cancelled from the event to be set
     */
    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = cancelledList;
    }

    /**
     * Sets the cancelled list of the event given an array of entrants.
     * @param cancelledList array of entrants to be marked as cancelled for the event
     */
    public void setCancelledListWithEvents(ArrayList<Entrant> cancelledList) {
        this.cancelledList = entrantListToStringList(cancelledList);
    }

    /**
     * Adds an entrant to the cancelled list
     * @param entrant entrant to be added to the cancelled list
     */
    public void addToCancelledList(String entrant) {
        this.cancelledList.add(entrant);
    }

    /**
     * Removes an entrant from the cancelled list
     * @param entrant entrant to be removed from the cancelled list
     */
    public void deleteFromCancelledList(String entrant) {
        this.cancelledList.remove(entrant);
    }

    /**
     * Clears the cancelled list.
     */
    public void clearCancelledList() {
        this.cancelledList.clear();
    }

    /**
     * Getter for the array of ids of entrants rejected from the event
     * @return array of ids of entrants rejected from the event
     */
    public ArrayList<String> getRejectedList() {
        return rejectedList;
    }

    /**
     * Setter for the array of ids of entrants rejected from the event
     * @param rejectedList array of ids of entrants rejected from the event to be set
     */
    public void setRejectedList(ArrayList<String> rejectedList) {
        this.rejectedList = rejectedList;
    }

    /**
     * Sets the rejected list of the event given an array of entrants
     * @param rejectedList array of entrants to be marked as rejected for the event
     */
    public void setRejectedListWithEvents(ArrayList<Entrant> rejectedList) {
        this.rejectedList = entrantListToStringList(rejectedList);
    }

    /**
     * Adds an entrant to the rejected list
     * @param entrant entrant to be added to the rejected list
     */
    public void addToRejectedList(String entrant) {
        this.rejectedList.add(entrant);
    }

    /**
     * Removes an entrant from the rejected list
     * @param entrant entrant to be removed from the rejected list
     */
    public void deleteFromRejectedList(String entrant) {
        this.rejectedList.remove(entrant);
    }

    /**
     * Clears the rejected list
     */
    public void clearRejectedList() {
        this.rejectedList.clear();
    }

    /**
     * Getter for the array of ids of entrants enrolled in the event.
     * @return array of ids of entrants enrolled in the event
     */
    public ArrayList<String> getEnrolledList() {
        return enrolledList;
    }

    /**
     * Setter for the array of ids of entrants enrolled in the event
     * @param enrolledList array of ids of entrants enrolled in the event to be set
     */
    public void setEnrolledList(ArrayList<String> enrolledList) {
        this.enrolledList = enrolledList;
    }

    /**
     * Sets the enrolled list of the event given an array of entrants.
     * @param enrolledList array of entrants to be enrolled in the event
     */
    public void setEnrolledListWithEvents(ArrayList<Entrant> enrolledList) {
        this.enrolledList = entrantListToStringList(enrolledList);
    }

    /**
     * Adds an entrant to the enrolled list.
     * @param entrant entrant to be added to the enrolled list
     */
    public void addToEnrolledList(String entrant) {
        this.enrolledList.add(entrant);
    }

    /**
     * Removes an entrant from the enrolled list.
     * @param entrant entrant to be removed from the enrolled list
     */
    public void deleteFromEnrolledList(String entrant) {
        this.enrolledList.remove(entrant);
    }

    /**
     * Clears the enrolled list.
     */
    public void clearEnrolledList() {
        this.enrolledList.clear();
    }

    /**
     * Checks if an entrant is in the enrolled list
     * @param entrant entrant to be checked
     * @return true if the entrant is in the enrolled list, false otherwise
     */
    public boolean isInEnrolledList(String entrant) {
        return this.enrolledList.contains(entrant);
    }

    /**
     * Updates the event information in Firebase.
     */
    public void updateEventFirebase() {
        new EventDB().updateEvent(toMap());
    }

    /**
     * converts entrant array to an array of entrant ids
     * @param entrants entrants to be converted
     * @return array of entrant ids
     */
    private ArrayList<String> entrantListToStringList(ArrayList<Entrant> entrants){
        ArrayList<String> IDs = new ArrayList<>();
        for (Entrant entrant: entrants) {
            IDs.add(entrant.getID());
        }
        return IDs;
    }

    /**
     *
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     *
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeString(geolocation);
        parcel.writeString(facility);
        parcel.writeInt(maxCapacity);
        parcel.writeInt(price);
        parcel.writeInt(WaitListLimit);
        parcel.writeByte((byte) (needsGeolocation ? 1 : 0));
        parcel.writeString(image);
        parcel.writeString(QRCode);
        parcel.writeStringList(waitList);
        parcel.writeStringList(invitedList);
        parcel.writeStringList(cancelledList);
        parcel.writeStringList(rejectedList);
        parcel.writeStringList(enrolledList);
        parcel.writeLong(dateTime.getTime());
        parcel.writeLong(registrationOpen.getTime());
        parcel.writeLong(registrationClose.getTime());
    }

    /**
     * Removes the given entrant from the event
     * @param entrant The id of the entrant being removed from the event
     */
    public void cancelEntrant(String entrant) {
        waitList.remove(entrant);
        invitedList.remove(entrant);
        enrolledList.remove(entrant);
        rejectedList.remove(entrant);
        cancelledList.add(entrant);
        updateEventFirebase();
    }
}
