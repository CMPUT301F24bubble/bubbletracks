package com.example.bubbletracksapp;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Facility {

    private String id;
    private String name;
    private String location;
    private String organizer;
    private ArrayList<String> eventList;

    public Facility(){
        this.id = UUID.randomUUID().toString();
        this.eventList = new ArrayList<>();
    }

    public Facility(DocumentSnapshot document){
        this.id = document.getString("id");
        this.name = document.getString("name");
        this.location = document.getString("location");
        this.organizer = document.getString("organizer");
        this.eventList = (ArrayList<String>)document.getData().get("events");
    }

    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("id", id);
        newMap.put("name", name);
        newMap.put("location", location);
        newMap.put("organizer", organizer);
        newMap.put("events", eventList);

        return newMap;
    }

    public String getId() { return id; }

    public String getName() { return name; }

    public String getLocation() { return location; }

    public String getOrganizer() { return organizer; }

    public ArrayList<String> getEventList() { return eventList; }

    public void setId(String id) { this.id = id; }

    public void setLocation(String location) { this.location = location; }

    public void setName(String name) { this.name = name; }

    public void setOrganizer(String organizer) { this.organizer = organizer; }

    public void setEventList(ArrayList<String> eventList) { this.eventList = eventList; }

    public void addToEventList(String event) {
        this.eventList.add(event);
    }

    public void deleteFromEventList(String event) {
        this.eventList.remove(event);
    }

    public void clearEventList() {
        this.eventList.clear();
    }

}