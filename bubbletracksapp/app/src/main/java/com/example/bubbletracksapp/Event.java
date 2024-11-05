package com.example.bubbletracksapp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.ArrayList;

public class Event {
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

    public Event(){
        this.id = UUID.randomUUID().toString();
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
        SimpleDateFormat formatter = new SimpleDateFormat("MM", Locale.getDefault());
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
