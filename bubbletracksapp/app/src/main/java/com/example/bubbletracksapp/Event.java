package com.example.bubbletracksapp;

import android.location.Location;

import com.google.android.libraries.places.api.model.Place;

import java.util.Calendar;
import java.util.UUID;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

public class Event {
    private String id;
    private String name;
    private String description;
    private Calendar date;
    //QRCode should probably be another type of field INCOMPLETE
    private String QRCode;
    private Place geolocation;
    private Calendar registrationOpen;
    private Calendar registrationClose;
    private String image;
    private boolean needsGeolocation;
    private ArrayList<Entrant> waitList = new ArrayList<>();
    private ArrayList<Entrant> invitedList = new ArrayList<>();
    private ArrayList<Entrant> cancelledList = new ArrayList<>();
    private ArrayList<Entrant> rejectedList = new ArrayList<>();
    private int maxCapacity;
    private int price;
    private int WaitListLimit;

    public Event(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() { return id; }

    public void setId(String id) {this.id = id;}

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Calendar getDateTime() { return dateTime; }

    public void setDateTime(Calendar dateTime) { this.dateTime = dateTime; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Place getGeolocation() { return geolocation; }

    public void setGeolocation(Place geoLocation) {
        this.geolocation = geoLocation;
    }

    public Calendar getRegistrationOpen() { return registrationOpen; }

    public void setRegistrationOpen(Calendar registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public Calendar getRegistrationClose() { return registrationClose; }

    public void setRegistrationClose(Calendar registrationClose) {
        this.registrationClose = registrationClose;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public boolean isNeedsGeolocation() { return needsGeolocation; }

    public void setNeedsGeolocation(boolean needsGeolocation) { this.needsGeolocation = needsGeolocation; }

    public String getQRCode() { return QRCode; }

    public void setQRCode(String QRCode) { this.QRCode = QRCode; }

    public int getMaxCapacity() { return maxCapacity; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getPrice() { return price; }

    public void setPrice(int price) { this.price = price; }

    public int getWaitListLimit() { return WaitListLimit;}

    public void setWaitListLimit(int waitListLimit) { WaitListLimit = waitListLimit; }
    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getMonth() {
        Formatter format = new Formatter();
        format.format("%tb", date);
        return format.toString();

    }

    public String getDay() {
        Formatter format = new Formatter();
        format.format("%tm", date);
        return format.toString();

    }

    public String getTime() {
        Formatter format = new Formatter();
        format.format("%tl:%tM", date, date);
        return format.toString();

    }

    //Should return a specific address
    public String getLocation() {
        return "Ualberta 10001";

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
}
