package com.example.bubbletracksapp;

import android.location.Location;
import android.media.Image;

import java.sql.Time;
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
}
