package com.example.bubbletracksapp;

import android.location.Location;
import android.media.Image;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

public class Event {
    private String name;
    private String description;
    private Calendar date;
    //QRCode should probably be another type of field INCOMPLETE
    private String QRCode;
    private Location geolocation;
    private Image image;
    private boolean needsGeolocation;
    private ArrayList<Entrant> waitList = new ArrayList<>();
    private ArrayList<Entrant> invitedList = new ArrayList<>();
    private ArrayList<Entrant> cancelledList = new ArrayList<>();
    private ArrayList<Entrant> rejectedList = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(Location geolocation) {
        this.geolocation = geolocation;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public boolean isNeedsGeolocation() {
        return needsGeolocation;
    }

    public void setNeedsGeolocation(boolean needsGeolocation) {
        this.needsGeolocation = needsGeolocation;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getID() {
        return QRCode;
    }

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
