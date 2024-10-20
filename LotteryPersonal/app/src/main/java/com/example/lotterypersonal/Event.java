package com.example.lotterypersonal;


import java.sql.Time;
import java.util.Date;

public class Event {
    private String eventId;
    private String userId;
    private String status; // "pending", "accepted", "declined"
    private String eventName;
    private Date eventDate;
    private Time eventTime;
    private String eventHostId;

    // Constructor
    public Event() {
        // Required empty constructor for Firestore
    }

    //GETTERS and setters
    public String getEventId() { return eventId; }
    public String getUserId() { return userId; }
    public String getStatus() { return status; }
    public String getEventName() { return eventName; }
    public Date getEventDate(){return eventDate;}
    public Time getEventTime() { return eventTime; }
    public String getEventHostId() { return eventHostId; }

    // SETTERS
    public void setEventId(String eventId) {  this.eventId=eventId; }
    public void setUserId(String userId) {  this.userId=userId; }
    public void setStatus(String status) { this.status=status; }
    public void getEventName(String eventName) { this.eventName=eventName; }
    public void getEventDate(Date eventDate){ this.eventDate=eventDate;}
    public void getEventTime(Time eventTime) { this.eventTime=eventTime; }
    public void getEventHost(String eventHost) { this.eventHostId =eventHost; }

}
