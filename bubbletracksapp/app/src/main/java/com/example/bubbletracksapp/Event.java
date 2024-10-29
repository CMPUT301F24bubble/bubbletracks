package com.example.bubbletracksapp;

import android.location.Location;
import android.media.Image;

import com.google.type.DateTime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Event {
    private String id;
    private String name;
    private Date dateTime;
    private String description;
    //QRCode should probably be another type of field INCOMPLETE
    private String QRCode;
    private Location geolocation;
    private Date registrationOpen;
    private Date registrationClose;
    private Image image;
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

    public Date getDateTime() { return dateTime; }

    public void setDateTime(Date dateTime) { this.dateTime = dateTime; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Location getGeolocation() { return geolocation; }

    public Date getRegistrationOpen() { return registrationOpen; }

    public void setRegistrationOpen(Date registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public Date getRegistrationClose() { return registrationClose; }

    public void setRegistrationClose(Date registrationClose) {
        this.registrationClose = registrationClose;
    }

    public void setGeolocation(Location geolocation) {
        this.geolocation = geolocation;
    }

    public Image getImage() { return image; }

    public void setImage(Image image) { this.image = image; }

    public boolean isNeedsGeolocation() { return needsGeolocation; }

    public void setNeedsGeolocation(boolean needsGeolocation) { this.needsGeolocation = needsGeolocation; }

    public String getQRCode() { return QRCode; }

    public void setQRCode(String QRCode) { this.QRCode = QRCode; }

    public int getMaxCapacity() { return maxCapacity; }

    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public int getPrice() { return Price; }

    public void setPrice(int price) { Price = price; }
}
