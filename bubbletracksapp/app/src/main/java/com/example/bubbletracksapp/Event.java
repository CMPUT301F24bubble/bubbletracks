package com.example.bubbletracksapp;

import android.location.Location;
import java.util.Calendar;
import java.util.UUID;

public class Event {
    private String id;
    private String name;
    private Calendar dateTime;
    private String description;
    //QRCode should probably be another type of field INCOMPLETE
    private String QRCode;
    private Location geolocation;
    private Calendar registrationOpen;
    private Calendar registrationClose;
    private String image;
    private boolean needsGeolocation;
    private int maxCapacity;
    private int Price;

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

    public Location getGeolocation() { return geolocation; }

    public Calendar getRegistrationOpen() { return registrationOpen; }

    public void setRegistrationOpen(Calendar registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public Calendar getRegistrationClose() { return registrationClose; }

    public void setRegistrationClose(Calendar registrationClose) {
        this.registrationClose = registrationClose;
    }

    public void setGeolocation(Location geolocation) {
        this.geolocation = geolocation;
    }

    public String getImage() { return image; }

    public void setImage(String image) { this.image = image; }

    public boolean isNeedsGeolocation() { return needsGeolocation; }

    public void setNeedsGeolocation(boolean needsGeolocation) { this.needsGeolocation = needsGeolocation; }

    public String getQRCode() { return QRCode; }

    public void setQRCode(String QRCode) { this.QRCode = QRCode; }

    public int getMaxCapacity() { return maxCapacity; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getPrice() { return Price; }

    public void setPrice(int price) { Price = price; }
}
