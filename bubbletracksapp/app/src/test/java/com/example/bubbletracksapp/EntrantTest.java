package com.example.bubbletracksapp;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntrantTest {
    Entrant entrant;

    private Entrant mockEntrant() {
        String[] name = new String[]{"The","Name"};
        String email = "test@gmail.com";
        String phone = "8253302549";
        String deviceID = "TheID tested";
        Boolean notification = false;
        ArrayList<String> eventsOrganized = new ArrayList<>();
        eventsOrganized.add("Event1");
        eventsOrganized.add("Event2");
        eventsOrganized.add("Event3");
        ArrayList<String> eventsInvited = new ArrayList<>();
        eventsInvited.add("Event4");
        eventsInvited.add("Event5");
        ArrayList<String> eventsEnrolled = new ArrayList<>();
        eventsEnrolled.add("Event4");
        ArrayList<String> eventsWaitlist = new ArrayList<>();
        eventsWaitlist.add("Event6");
        eventsWaitlist.add("Event7");

        Entrant newEntrant = new Entrant(name, email, phone, deviceID, notification,
                eventsOrganized, eventsInvited, eventsEnrolled, eventsWaitlist);
        return newEntrant;
    }


    private Event mockEvent(String ID) {
        Event event = new Event();
        event.setId(ID);
        return event;
    }


    @Test
    public void toMapTest() {
        entrant = mockEntrant();
        Map<String, Object> map = entrant.toMap();

        String[] name = new String[]{"The","Name"};
        String email = "test@gmail.com";
        String phone = "8253302549";
        String deviceID = "TheID tested";
        Boolean notification = false;
        ArrayList<String> eventsOrganized = new ArrayList<>();
        eventsOrganized.add("Event1");
        eventsOrganized.add("Event2");
        eventsOrganized.add("Event3");
        ArrayList<String> eventsInvited = new ArrayList<>();
        eventsInvited.add("Event4");
        eventsInvited.add("Event5");
        ArrayList<String> eventsEnrolled = new ArrayList<>();
        eventsEnrolled.add("Event4");
        ArrayList<String> eventsWaitlist = new ArrayList<>();
        eventsWaitlist.add("Event6");
        eventsWaitlist.add("Event7");

        assertEquals(map.get("name"), entrant.getNameAsList());
        assertEquals(map.get("email"), email);
        assertEquals(map.get("phone"), phone);
        assertEquals(map.get("notification"), notification);
        assertEquals(map.get("ID"), deviceID);
        assertEquals(map.get("organized"), eventsOrganized);
        assertEquals(map.get("invited"), eventsInvited);
        assertEquals(map.get("enrolled"), eventsEnrolled);
        assertEquals(map.get("waitlist"), eventsWaitlist);
    }

    /**
     * Check if the Name of the mockEvent is the same as the Name we set.
     */
    @Test
    public void getNameTest() {
        entrant = mockEntrant();
        assertEquals(entrant.getName(), new String[]{"The","Name"});
    }

    /**
     * Check if the Name of the mockEvent is the same as the Name we set.
     */
    @Test
    public void getNameAsListTest() {
        entrant = mockEntrant();
        List<String> nameList = new ArrayList<>();
        nameList.add("The");
        nameList.add("Name");
        assertEquals(entrant.getNameAsList(), nameList);
        assertEquals(entrant.getNameAsList().getClass(), List.class);
    }

    /**
     * Check if the Name of the mockEvent is the same as the Name we set.
     */
    @Test
    public void getNameAsStringTest() {
        entrant = mockEntrant();
        assertEquals(entrant.getNameAsString(), "The Name");
    }

    /**
     * Change the Name of the mockEvent and check if it changed.
     */
    @Test
    public void setNameTest() {
        entrant = mockEntrant();
        String[] newName = new String[]{"New","Name!"};
        entrant.setName(newName[0],newName[1]);
        String[] theName = entrant.getName();
        assertEquals(newName[0], theName[0]);
        assertEquals(newName[1], theName[1]);
    }

    /**
     * Check if the Email of the mockEvent is the same as the ID we set.
     */
    @Test
    public void getEmailTest() {
        entrant = mockEntrant();
        assertEquals(entrant.getEmail(), "test@gmail.com");
    }

    /**
     * Change the Email of the mockEvent and check if it changed.
     */
    @Test
    public void setEmailTest() {
        entrant = mockEntrant();
        entrant.setEmail("mynewemail@gmail.com");
        assertEquals(entrant.getEmail(), "mynewemail@gmail.com");
    }

    /**
     * Check if the Email of the mockEvent is the same as the Email we set.
     */
    @Test
    public void getPhoneTest() {
        entrant = mockEntrant();
        assertEquals(entrant.getEmail(), "8253302549");
    }

    /**
     * Change the Phone of the mockEvent and check if it changed.
     */
    @Test
    public void setPhoneTest() {
        entrant = mockEntrant();
        entrant.setPhone("24537432");
        assertEquals(entrant.getEmail(), "24537432");
    }

    /**
     * Check if the Notification of the mockEvent is the same as the Notification we set.
     */
    @Test
    public void getNotificationTest() {
        entrant = mockEntrant();
        assertEquals(entrant.getNotification(), false);
    }

    /**
     * Change the Notification of the mockEvent and check if it changed.
     */
    @Test
    public void setNotificationTest() {
        entrant = mockEntrant();
        entrant.setNotification(true);
        assertEquals(entrant.getNotification(), true);
    }

    /**
     * Check if the ID of the mockEvent is the same as the ID we set.
     */
    @Test
    public void getIDTest() {
        entrant = mockEntrant();
        assertEquals(entrant.getID(), "TheID tested");
    }

    /**
     * Change the ID of the mockEvent and check if it changed.
     */
    @Test
    public void setIDTest() {
        entrant = mockEntrant();
        entrant.setID("newID tested");
        assertEquals(entrant.getID(), "newID tested");
    }

    /**
     * Check if the EventsOrganized list of the mockEvent is the same as the EventsOrganized list we set.
     */
    @Test
    public void getEventsOrganizedTest() {
        entrant = mockEntrant();
        ArrayList<String> eventsOrganized = new ArrayList<>();
        eventsOrganized.add("Event1");
        eventsOrganized.add("Event2");
        eventsOrganized.add("Event3");
        assertEquals(entrant.getEventsOrganized(), eventsOrganized);
    }

    /**
     * Change the EventsOrganized list of the mockEvent and check if it changed.
     */
    @Test
    public void setEventsOrganizedTest() {
        entrant = mockEntrant();
        ArrayList<String> eventsOrganized = new ArrayList<>();
        eventsOrganized.add("newEvent1");
        eventsOrganized.add("newEvent2");
        eventsOrganized.add("newEvent3");
        entrant.setEventsOrganized(eventsOrganized);
        assertEquals(entrant.getEventsOrganized(), eventsOrganized);
    }

    /**
     * Check if the EventsInvited list of the mockEvent is the same as the EventsInvited list we set.
     */
    @Test
    public void getEventsInvitedTest() {
        entrant = mockEntrant();
        ArrayList<String> eventsInvited = new ArrayList<>();
        eventsInvited.add("Event4");
        eventsInvited.add("Event5");
        assertEquals(entrant.getEventsInvited(), eventsInvited);
    }

    /**
     * Change the EventsInvited list of the mockEvent and check if it changed.
     */
    @Test
    public void setEventsInvitedTest() {
        entrant = mockEntrant();
        ArrayList<String> eventsInvited = new ArrayList<>();
        eventsInvited.add("newEvent4");
        eventsInvited.add("newEvent5");
        assertEquals(entrant.getEventsInvited(), eventsInvited);

    }

    /**
     * Check if the EventsEnrolled list of the mockEvent is the same as the EventsEnrolled list we set.
     */
    @Test
    public void getEventsEnrolledTest() {
        entrant = mockEntrant();
        ArrayList<String> eventsEnrolled = new ArrayList<>();
        eventsEnrolled.add("Event4");
        assertEquals(entrant.getEventsEnrolled(), eventsEnrolled);
    }

    /**
     * Change the EventsEnrolled list of the mockEvent and check if it changed.
     */
    @Test
    public void setEventsEnrolledTest() {
        entrant = mockEntrant();
        ArrayList<String> eventsEnrolled = new ArrayList<>();
        eventsEnrolled.add("NewEvent4");
        entrant.setEventsEnrolled(eventsEnrolled);
        assertEquals(entrant.getEventsEnrolled(), eventsEnrolled);

    }

    /**
     * Check if the EventsWaitlist list of the mockEvent is the same as the EventsWaitlist list we set.
     */
    @Test
    public void getEventsWaitlistTest() {
        entrant = mockEntrant();
        ArrayList<String> eventsWaitlist = new ArrayList<>();
        eventsWaitlist.add("Event6");
        eventsWaitlist.add("Event7");

    }

    /**
     * Change the EventsWaitlist list of the mockEvent and check if it changed.
     */
    @Test
    public void setEventsWaitlistTest() {
        entrant = mockEntrant();

    }

    /**
     * Add to List.
     * Check the that the List contains the new ID
     */
    @Test
    public void addToEventsOrganizedTest() {
        entrant = mockEntrant();

    }

    /**
     * Delete from list.
     * Check the list does not contain the new ID
     */
    @Test
    public void deleteFromEventsOrganizedTest() {
        entrant = mockEntrant();

    }

    /**
     * Add to List.
     * Check the that the List contains the new ID
     */
    @Test
    public void addToEventsInvitedTest() {
        entrant = mockEntrant();

    }

    /**
     * Delete from list.
     * Check the list does not contain the new ID
     */
    @Test
    public void deleteFromEventsInvitedTest() {
        entrant = mockEntrant();

    }

    /**
     * Add to List.
     * Check the that the List contains the new ID
     */
    @Test
    public void addToEventsEnrolledTest() {
        entrant = mockEntrant();

    }

    /**
     * Delete from list.
     * Check the list does not contain the new ID
     */
    @Test
    public void deleteFromEventsEnrolledTest() {
        entrant = mockEntrant();

    }

    /**
     * Add to List.
     * Check the that the List contains the new ID
     */
    @Test
    public void addToEventsWaitlistTest() {
        entrant = mockEntrant();

    }

    /**
     * Delete from list.
     * Check the list does not contain the new ID
     */
    @Test
    public void deleteFromEventsWaitlistTest() {
        entrant = mockEntrant();

    }
}