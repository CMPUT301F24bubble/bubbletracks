package com.example.bubbletracksapp;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * this tracks the information of a given facility
 */
public class Facility {

    private String id;
    private String name;
    private String location;
    private String organizer;
    private ArrayList<String> eventList;

    /**
     * constructor that sets the id and initializes the event list array
     */
    public Facility(){
        this.id = UUID.randomUUID().toString();
        this.eventList = new ArrayList<>();
    }

    /**
     * constructor that creates a facility from a document snapshot
     * @param document document snapshot from the database
     */
    public Facility(DocumentSnapshot document){
        this.id = document.getString("id");
        this.name = document.getString("name");
        this.location = document.getString("location");
        this.organizer = document.getString("organizer");
        this.eventList = (ArrayList<String>)document.getData().get("events");
    }

    /**
     * converts the facility to a map
     * @return the converted map
     */
    public Map<String, Object> toMap() {
        Map<String, Object> newMap = new HashMap<>();

        newMap.put("id", id);
        newMap.put("name", name);
        newMap.put("location", location);
        newMap.put("organizer", organizer);
        newMap.put("events", eventList);

        return newMap;
    }

    /**
     * getter for the id of the facility
     * @return id of the facility
     */
    public String getId() { return id; }

    /**
     * setter for the name of the facility
     * @return name of the facility
     */
    public String getName() { return name; }

    /**
     * getter for the location of the facility
     * @return location of the facility
     */
    public String getLocation() { return location; }

    /**
     * getter for the id of organizer associated with the facility
     * @return id of organizer associated with the facility
     */
    public String getOrganizer() { return organizer; }

    /**
     * getter for array of ids of events associated with the facility
     * @return array of ids of events associated with the facility
     */
    public ArrayList<String> getEventList() { return eventList; }

    /**
     * setter for the id of the facility
     * @param id id to be set
     */
    public void setId(String id) { this.id = id; }

    /**
     * setter for the location of the facility
     * @param location location to be set
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * setter for the name of the facility
     * @param name name to be set
     */
    public void setName(String name) { this.name = name; }

    /**
     * setter for the organizer associated with the facility
     * @param organizer organizer to be set
     */
    public void setOrganizer(String organizer) { this.organizer = organizer; }

    /**
     * setter for array of ids of events associated with the facility
     * @param eventList array of ids of events associated with the facility
     */
    public void setEventList(ArrayList<String> eventList) { this.eventList = eventList; }

    /**
     * adds event's id to the event list
     * @param event event's id to be added
     */
    public void addToEventList(String event) {
        this.eventList.add(event);
    }

    /**
     * removes event's id to the event list
     * @param event event's id to be removed
     */
    public void deleteFromEventList(String event) {
        this.eventList.remove(event);
    }

    /**
     * clears event list
     */
    public void clearEventList() {
        this.eventList.clear();
    }

}