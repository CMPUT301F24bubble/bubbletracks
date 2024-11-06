package com.example.bubbletracksapp;

import android.location.Location;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

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
    private ArrayList<Entrant> waitList = new ArrayList<>();
    private ArrayList<Entrant> invitedList = new ArrayList<>();
    private ArrayList<Entrant> cancelledList = new ArrayList<>();
    private ArrayList<Entrant> rejectedList = new ArrayList<>();
    private ArrayList<Entrant> enrolledList = new ArrayList<>();


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
    }

    public Event(String id, String name, Date dateTime, String description, String geolocation, Date registrationOpen, Date registrationClose, int maxCapacity, int price, int waitListLimit, boolean needsGeolocation, String image, String QRCode, ArrayList<Entrant> waitList, ArrayList<Entrant> invitedList, ArrayList<Entrant> cancelledList, ArrayList<Entrant> rejectedList, ArrayList<Entrant> enrolledList) {
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
        waitList = in.createTypedArrayList(Entrant.CREATOR);
        invitedList = in.createTypedArrayList(Entrant.CREATOR);
        cancelledList = in.createTypedArrayList(Entrant.CREATOR);
        rejectedList = in.createTypedArrayList(Entrant.CREATOR);
        enrolledList = in.createTypedArrayList(Entrant.CREATOR);
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
        dest.writeTypedList(waitList);
        dest.writeTypedList(invitedList);
        dest.writeTypedList(cancelledList);
        dest.writeTypedList(rejectedList);
        dest.writeTypedList(enrolledList);
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

    public ArrayList<Entrant> getWaitList() {
        return waitList;
    }

    public ArrayList<Entrant> setWaitList(ArrayList<Entrant> waitList) {
        this.waitList = waitList;
        return waitList;
    }

    public void addToWaitList(Entrant entrant) {
        this.waitList.add(entrant);
    }

    public void deleteFromWaitList(Entrant entrant) {
        this.waitList.remove(entrant);
    }

    public void clearWaitList() {
        this.waitList.clear();
    }

    public ArrayList<Entrant> getInvitedList() {
        return invitedList;
    }

    public void setInvitedList(ArrayList<Entrant> invitedList) {
        this.invitedList = invitedList;
    }

    public void addToInvitedList(Entrant entrant) {
        this.invitedList.add(entrant);
    }

    public void deleteFromInvitedList(Entrant entrant) {
        this.invitedList.remove(entrant);
    }

    public void clearInvitedList() {
        this.invitedList.clear();
    }

    public ArrayList<Entrant> getCancelledList() {
        return cancelledList;
    }

    public void setCancelledList(ArrayList<Entrant> cancelledList) {
        this.cancelledList = cancelledList;
    }

    public void addToCancelledList(Entrant entrant) {
        this.cancelledList.add(entrant);
    }

    public void deleteFromCancelledList(Entrant entrant) {
        this.cancelledList.remove(entrant);
    }

    public void clearCancelledList() {
        this.cancelledList.clear();
    }

    public ArrayList<Entrant> getRejectedList() {
        return rejectedList;
    }

    public void setRejectedList(ArrayList<Entrant> rejectedList) {
        this.rejectedList = rejectedList;
    }
    public void addToRejectedList(Entrant entrant) {
        this.rejectedList.add(entrant);
    }

    public void deleteFromRejectedList(Entrant entrant) {
        this.rejectedList.remove(entrant);
    }

    public void clearRejectedList() {
        this.rejectedList.clear();
    }

    public ArrayList<Entrant> getEnrolledList() {
        return enrolledList;
    }

    public void setEnrolledList(ArrayList<Entrant> enrolledList) {
        this.enrolledList = enrolledList;
    }

    public void addToEnrolledList(Entrant entrant) {
        this.enrolledList.add(entrant);
    }

    public void deleteFromEnrolledList(Entrant entrant) {
        this.enrolledList.remove(entrant);
    }

    public void clearEnrolledList() {
        this.enrolledList.clear();
    }

    public boolean isInEnrolledList(Entrant entrant){
        return this.enrolledList.contains(entrant);
    }

}
