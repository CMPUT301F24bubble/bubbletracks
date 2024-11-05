package com.example.bubbletracksapp;

import android.location.Location;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;

public class Event implements Parcelable {
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
    private ArrayList<Entrant> enrolledList = new ArrayList<>();


    public Event(){
        Log.w("NewEvent", "Event has empty strings for information.");
    }

    public Event(String name, String description, Calendar date, String QRCode, Location geolocation, Image image, boolean needsGeolocation, ArrayList<Entrant> waitList, ArrayList<Entrant> invitedList, ArrayList<Entrant> cancelledList, ArrayList<Entrant> rejectedList, ArrayList<Entrant> enrolledList) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.QRCode = QRCode;
        this.geolocation = geolocation;
        this.image = image;
        this.needsGeolocation = needsGeolocation;
        this.waitList = waitList;
        this.invitedList = invitedList;
        this.cancelledList = cancelledList;
        this.rejectedList = rejectedList;
        this.enrolledList = enrolledList;
    }


    protected Event(Parcel in) {
        name = in.readString();
        description = in.readString();
        QRCode = in.readString();
        geolocation = in.readParcelable(Location.class.getClassLoader());
        needsGeolocation = in.readByte() != 0;
        waitList = in.createTypedArrayList(Entrant.CREATOR);
        invitedList = in.createTypedArrayList(Entrant.CREATOR);
        cancelledList = in.createTypedArrayList(Entrant.CREATOR);
        rejectedList = in.createTypedArrayList(Entrant.CREATOR);
        enrolledList = in.createTypedArrayList(Entrant.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(QRCode);
        dest.writeParcelable(geolocation, flags);
        dest.writeByte((byte) (needsGeolocation ? 1 : 0));
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
