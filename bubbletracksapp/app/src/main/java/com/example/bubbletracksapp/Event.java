package com.example.bubbletracksapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;

public class Event implements Parcelable{
    private String id;
    private String name;
    private Date dateTime;
    private String description;
    private String geolocation;
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


    public Event(){
        this.id = UUID.randomUUID().toString();
    }

    public Event(DocumentSnapshot document) {
        this.id = document.getString("id");
        this.name = document.getString("name");
        this.dateTime = document.getTimestamp("dateTime").toDate();
        this.description = document.getString("description");
        this.geolocation=  document.getString("geolocation");
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

    public Event(String id, String name, Date dateTime, String description, String geolocation, Date registrationOpen, Date registrationClose, int maxCapacity, int price, int waitListLimit, boolean needsGeolocation, String image, String QRCode, ArrayList<String> waitList, ArrayList<String> invitedList, ArrayList<String> cancelledList, ArrayList<String> rejectedList, ArrayList<String> enrolledList) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.description = description;
        this.geolocation = geolocation;
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


    protected Event(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        geolocation = in.readString();
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
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(geolocation);
        dest.writeInt(maxCapacity);
        dest.writeInt(price);
        dest.writeInt(WaitListLimit);
        dest.writeByte((byte) (needsGeolocation ? 1 : 0));
        dest.writeString(image);
        dest.writeString(QRCode);
        dest.writeStringList(waitList);
        dest.writeStringList(invitedList);
        dest.writeStringList(cancelledList);
        dest.writeStringList(rejectedList);
        dest.writeStringList(enrolledList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

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

    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("id", id);
        newMap.put("name", name);
        newMap.put("dateTime", dateTime);
        newMap.put("description", description);
        newMap.put("geolocation", geolocation);
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


    public String getId() { return id; }

    public void setId(String id) {this.id = id;}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Date getDateTime() { return dateTime; }

    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getGeolocation() { return geolocation; }

    public void setGeolocation(String geoLocation) {
        this.geolocation = geoLocation;
    }

    public Date getRegistrationOpen() { return registrationOpen; }

    public void setRegistrationOpen(Date registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public Date getRegistrationClose() { return registrationClose; }

    public void setRegistrationClose(Date registrationClose) {
        this.registrationClose = registrationClose;
    }

    public int getMaxCapacity() { return maxCapacity; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public int getWaitListLimit() { return WaitListLimit;}

    public void setWaitListLimit(int waitListLimit) { WaitListLimit = waitListLimit; }

    public boolean getNeedsGeolocation() { return needsGeolocation; }

    public void setNeedsGeolocation(boolean needsGeolocation) { this.needsGeolocation = needsGeolocation; }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public String getQRCode() { return QRCode; }

    public void setQRCode(String QRCode) { this.QRCode = QRCode; }

    public String getYear(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy", Locale.getDefault());
        return formatter.format(date);
    }

    public String getMonth(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM", Locale.getDefault());
        return formatter.format(date);
    }

    public String getDay(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd", Locale.getDefault());
        return formatter.format(date);
    }

    public String getTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatter.format(date);
    }

    public ArrayList<String> getWaitList() {
        return waitList;
    }

    public void setWaitList(ArrayList<String> waitList) {
        this.waitList = waitList;
    }

    public void setWaitListWithEvents(ArrayList<Entrant> waitList) {
        this.waitList = entrantListToStringList(waitList);
    }

    public void addToWaitList(String entrant) {
        this.waitList.add(entrant);
    }

    public void deleteFromWaitList(String entrant) {
        this.waitList.remove(entrant);
    }

    public void clearWaitList() {
        this.waitList.clear();
    }

    public ArrayList<String> getInvitedList() {
        return invitedList;
    }

    public void setInvitedList(ArrayList<String> invitedList) {
        this.invitedList = invitedList;
    }

    public void setInvitedListWithEvents(ArrayList<Entrant> invitedList) {
        this.invitedList = entrantListToStringList(invitedList);
    }

    public void addToInvitedList(String entrant) {
        this.invitedList.add(entrant);
    }

    public void deleteFromInvitedList(String entrant) {
        this.invitedList.remove(entrant);
    }

    public void clearInvitedList() {
        this.invitedList.clear();
    }

    public ArrayList<String> getCancelledList() {
        return cancelledList;
    }

    public void setCancelledList(ArrayList<String> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public void setCancelledListWithEvents(ArrayList<Entrant> cancelledList) {
        this.cancelledList = entrantListToStringList(cancelledList);
    }

    public void addToCancelledList(String entrant) {
        this.cancelledList.add(entrant);
    }

    public void deleteFromCancelledList(String entrant) {
        this.cancelledList.remove(entrant);
    }

    public void clearCancelledList() {
        this.cancelledList.clear();
    }

    public ArrayList<String> getRejectedList() {
        return rejectedList;
    }

    public void setRejectedList(ArrayList<String> rejectedList) {
        this.rejectedList = rejectedList;
    }

    public void setRejectedListWithEvents(ArrayList<Entrant> rejectedList) {
        this.rejectedList = entrantListToStringList(rejectedList);
    }

    public void addToRejectedList(String entrant) {
        this.rejectedList.add(entrant);
    }

    public void deleteFromRejectedList(String entrant) {
        this.rejectedList.remove(entrant);
    }

    public void clearRejectedList() {
        this.rejectedList.clear();
    }

    public ArrayList<String> getEnrolledList() {
        return enrolledList;
    }

    public void setEnrolledList(ArrayList<String> enrolledList) {
        this.enrolledList = enrolledList;
    }

    public void setEnrolledListWithEvents(ArrayList<Entrant> enrolledList) {
        this.enrolledList = entrantListToStringList(enrolledList);
    }

    public void addToEnrolledList(String entrant) {
        this.enrolledList.add(entrant);
    }

    public void deleteFromEnrolledList(String entrant) {
        this.enrolledList.remove(entrant);
    }

    public void clearEnrolledList() {
        this.enrolledList.clear();
    }

    public boolean isInEnrolledList(String entrant){
        return this.enrolledList.contains(entrant);
    }

    private ArrayList<String> entrantListToStringList(ArrayList<Entrant> entrants){
        ArrayList<String> IDs = new ArrayList<>();
        for (Entrant entrant: entrants) {
            IDs.add(entrant.getID());
        }
        return IDs;
    }
}
