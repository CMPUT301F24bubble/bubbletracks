package com.example.lotterypersonal;


import java.sql.Time;
import java.util.Date;

public class Event {
    private String eventId;
    private User host;
    private String eventName;
    private Date eventDate;
    private Time eventTime;

    // Constructor
    public Event() {
        // Required empty constructor for Firestore
    }

    //GETTERS and setters
    public String getEventId() { return eventId; }
    public User getHost(){return host;}
    public String getEventName() { return eventName; }
    public Date getEventDate(){return eventDate;}
    public Time getEventTime() { return eventTime; }

    // SETTERS
    public void setEventId(String eventId) {  this.eventId=eventId; }
    public void setHost(User host) {this.host = host;}
    public void setEventName(String eventName) { this.eventName=eventName; }
    public void setEventDate(Date eventDate){ this.eventDate=eventDate;}
    public void setEventTime(Time eventTime) { this.eventTime=eventTime; }


    // METHODS
}
