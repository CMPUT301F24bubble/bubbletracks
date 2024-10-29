package com.example.bubbletracksapp;

import android.location.Location;
import android.media.Image;

import java.time.LocalDateTime;
import java.util.UUID;

public class Event {
    private String id;
    private String name;
    private LocalDateTime dateTime;
    private String description;
    //QRCode should probably be another type of field INCOMPLETE
    private String QRCode;
    private Location geolocation;
    private LocalDateTime registrationOpen;
    private LocalDateTime registrationClose;
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

    public LocalDateTime getDateTime() { return dateTime; }

    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Location getGeolocation() { return geolocation; }

    public LocalDateTime getRegistrationOpen() { return registrationOpen; }

    public void setRegistrationOpen(LocalDateTime registrationOpen) {
        this.registrationOpen = registrationOpen;
    }

    public LocalDateTime getRegistrationClose() { return registrationClose; }

    public void setRegistrationClose(LocalDateTime registrationClose) {
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
