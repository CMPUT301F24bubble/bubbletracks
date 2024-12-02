package com.example.bubbletracksapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class FacilityTest {
    private Facility facility;

    private Facility mockFacility(){
        String id = "TheId";
        String name = "TheName";
        String location = "TheLocation";
        String organizer = "TheOrganizer";
        ArrayList<String> eventList = new ArrayList<>();
        eventList.add("event1");
        eventList.add("event2");
        eventList.add("event3");
        eventList.add("event4");
        Facility facility = new Facility(id, name, location, organizer, eventList);
        return facility;
    }

    private Entrant mockEntrant(String ID) {
        return new Entrant(ID);
    }

    /**
     * Transform mock facility to map.
     * Set the same values as the facility before it was map.
     * Get the values from the map and make sure they did not change.
     */
    @Test
    public void toMapTest() {
        facility = mockFacility();
        Map<String, Object> map = facility.toMap();
        String id = "TheId";
        String name = "TheName";
        String location = "TheLocation";
        String organizer = "TheOrganizer";
        ArrayList<String> eventList = new ArrayList<>();
        eventList.add("event1");
        eventList.add("event2");
        eventList.add("event3");
        eventList.add("event4");

        assertEquals(map.get("id"), id);
        assertEquals(map.get("name"), name);
        assertEquals(map.get("location"), location);
        assertEquals(map.get("organizer"), organizer);
        assertEquals(map.get("events"), eventList);
    }

    /**
     * Check if the ID of the mockFacility is the same as the ID we set.
     */
    @Test
    public void getIdTest() {
        facility = mockFacility();
        assertEquals(facility.getId(), "TheId");
    }

    /**
     * Change the Id of the mockFacility and check if it changed.
     */
    @Test
    public void setIdTest() {
        facility = mockFacility();
        facility.setId("NewId");
        assertEquals(facility.getId(),"NewId");
    }

    /**
     * Check if the name of the mockFacility is the same as we set
     */
    @Test
    public void getNameTest() {
        facility = mockFacility();
        assertEquals(facility.getName(), "TheName");
    }

    /**
     * Change the name of the mockFacility and check if it changed.
     */
    @Test
    public void setNameTest() {
        facility = mockFacility();
        facility.setName("NewName");
        assertEquals(facility.getName(), "NewName");
    }

    /**
     * Check if the location of the mockFacility is the same as we set
     */
    @Test
    public void getLocationTest() {
        facility = mockFacility();
        assertEquals(facility.getLocation(), "TheLocation");
    }

    /**
     * Change the location of the mockFacility and check if it changed.
     */
    @Test
    public void setLocationTest() {
        facility = mockFacility();
        facility.setLocation("NewLocation");
        assertEquals(facility.getLocation(), "NewLocation");
    }

    /**
     * Check if the organizer of the mockFacility is the same as we set
     */
    @Test
    public void getOrganizerTest() {
        facility = mockFacility();
        assertEquals(facility.getOrganizer(), "TheOrganizer");
    }

    /**
     * Change the organizer of the mockFacility and check if it changed.
     */
    @Test
    public void setOrganizerTest() {
        facility = mockFacility();
        facility.setOrganizer("NewOrganizer");
        assertEquals(facility.getOrganizer(), "NewOrganizer");
    }

    /**
     * Check if the event list of the mockFacility is the same as we set
     */
    @Test
    public void getEventListTest() {
        facility = mockFacility();
        ArrayList<String> EventList = new ArrayList<>();
        EventList.add("event1");
        EventList.add("event2");
        EventList.add("event3");
        EventList.add("event4");
        assertEquals(facility.getEventList(), EventList);
    }

    /**
     * Change the organizer of the mockFacility and check if it changed.
     */
    @Test
    public void setEventsTest() {
        facility = mockFacility();
        ArrayList<String> EventList = facility.getEventList();
        EventList.add("event5");
        EventList.add("event6");
        facility.setEventList(EventList);
        assertEquals(facility.getEventList(), EventList);
    }
}
