package com.example.bubbletracksapp;


import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import android.os.Parcel;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventTest {
    private Event event;

    private Event mockEvent() {
        String id = "TheID";
        String name = "The Name";
        Date dateTime = new Date("December 17, 1995 03:24:00");
        String description = "The description";
        String geolocation = "10502 79 Ave NW, Edmonton";
        Date registrationOpen = new Date("December 17, 1995 03:24:00");
        Date registrationClose = new Date("December 17, 1995 03:24:00");
        int maxCapacity = 100;
        int price = 50;
        int WaitListLimit = 10;
        boolean needsGeolocation = false;
        String image = "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/posters%2F1731044610966.jpg?alt=media&token=f9daa8db-eba7-43fd-9cca-779bac4bbfbe";
        String QRCode = "https://www.bubbletracks.com/events/bcdc753e-f1ae-440e-97cf-2d13e4d7547c";
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("ID1");
        waitList.add("ID2");
        waitList.add("ID3");
        waitList.add("ID4");
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add("ID1");
        invitedList.add("ID3");
        ArrayList<String> cancelledList = new ArrayList<>();
        cancelledList.add("ID3");
        ArrayList<String> rejectedList = new ArrayList<>();
        rejectedList.add("ID2");
        rejectedList.add("ID4");
        ArrayList<String> enrolledList = new ArrayList<>();
        enrolledList.add("ID1");
        Event event = new Event(id, name, dateTime, description, geolocation, registrationOpen, 
                registrationClose, maxCapacity, price, WaitListLimit, needsGeolocation, image, 
                QRCode, waitList, invitedList, cancelledList, rejectedList, enrolledList);

        return event;
        }


    private Entrant mockEntrant(String ID) {
        return new Entrant(ID);
        }




    /**
     * Transform mock event to map.
     * Set the same values as the event before it was map.
     * Get the values from the map and make sure they did not change.
     */
    @Test
    public void toMapTest() {
        event = mockEvent();
        Map<String, Object> map = event.toMap();

        String id = "TheID";
        String name = "The Name";
        Date dateTime = new Date("December 17, 1995 03:24:00");
        String description = "The description";
        String geolocation = "10502 79 Ave NW, Edmonton";
        Date registrationOpen = new Date("December 17, 1995 03:24:00");
        Date registrationClose = new Date("December 17, 1995 03:24:00");;
        int maxCapacity = 100;
        int price = 50;
        int WaitListLimit = 10;
        boolean needsGeolocation = false;
        String image = "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/posters%2F1731044610966.jpg?alt=media&token=f9daa8db-eba7-43fd-9cca-779bac4bbfbe";
        String QRCode = "https://www.bubbletracks.com/events/bcdc753e-f1ae-440e-97cf-2d13e4d7547c";
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("ID1");
        waitList.add("ID2");
        waitList.add("ID3");
        waitList.add("ID4");
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add("ID1");
        invitedList.add("ID3");
        ArrayList<String> cancelledList = new ArrayList<>();
        cancelledList.add("ID3");
        ArrayList<String> rejectedList = new ArrayList<>();
        rejectedList.add("ID2");
        rejectedList.add("ID4");
        ArrayList<String> enrolledList = new ArrayList<>();
        enrolledList.add("ID1");



        assertEquals(map.get("id"), id);
        assertEquals(map.get("name"), name);
        assertEquals(map.get("dateTime"), dateTime);
        assertEquals(map.get("description"), description);
        assertEquals(map.get("geolocation"), geolocation);
        assertEquals(map.get("registrationOpen"), registrationOpen);
        assertEquals(map.get("registrationClose"), registrationClose);
        assertEquals(map.get("maxCapacity"), maxCapacity);
        assertEquals(map.get("price"), price);
        assertEquals(map.get("waitListLimit"), WaitListLimit);
        assertEquals(map.get("needsGeolocation"), needsGeolocation);
        assertEquals(map.get("image"), image);
        assertEquals(map.get("QRCode"), QRCode);
        assertEquals(map.get("wait"), waitList);
        assertEquals(map.get("invited"), invitedList);
        assertEquals(map.get("cancelled"), cancelledList);
        assertEquals(map.get("rejected"), rejectedList);
        assertEquals(map.get("enrolled"), enrolledList);


        }

    /**
     * Check if the ID of the mockEvent is the same as the ID we set.
     */
    @Test
    public void getIdTest() {
        event = mockEvent();
        assertEquals(event.getId(), "TheID");
        }

    /**
     * Change the ID of the mockEvent and check if it changed.
     */
    @Test
    public void setIdTest() {
        event = mockEvent();
        event.setId("NewID");
        assertEquals(event.getId(),"NewID");
        }

    /**
     * Check if the Name of the mockEvent is the same as the Name we set.
     */
    @Test
    public void getNameTest() {
        event = mockEvent();
        assertEquals(event.getName(), "The Name");
        }

    /**
     * Change the Name of the mockEvent and check if it changed.
     */
    @Test
    public void setNameTest() {
        event = mockEvent();
        event.setName("New Name");
        assertEquals(event.getName(),"New Name");
        }

    /**
     * Check if the DateTime of the mockEvent is the same as the DateTime we set.
     */
    @Test
    public void getDateTimeTest() {
        event = mockEvent();
        assertEquals(event.getDateTime(),new Date("December 17, 1995 03:24:00"));
     }

    /**
     * Change the DateTime of the mockEvent and check if it changed.
     */
    @Test
    public void setDateTimeTest() {
        event = mockEvent();
        event.setDateTime(new Date("November 18, 2003 02:47:00"));
        assertEquals(event.getDateTime(),new Date("November 18, 2003 02:47:00"));
     }

    /**
     * Check if the Description of the mockEvent is the same as the Description we set.
     */
    @Test
    public void getDescriptionTest() {
        event = mockEvent();
        assertEquals(event.getDescription(),"The description");
     }

    /**
     * Change the Description of the mockEvent and check if it changed.
     */
    @Test
    public void setDescriptionTest() {
        event = mockEvent();
        event.setDescription("The new description");
        assertEquals(event.getDescription(),"The new description");
     }

    /**
     * Check if the geolocation of the mockEvent is the same as the geolocation we set.
     */
    @Test
    public void getGeolocationTest() {
        event = mockEvent();
        assertEquals(event.getGeolocation(),"10502 79 Ave NW, Edmonton");
     }

    /**
     * Change the geolocation of the mockEvent and check if it changed.
     */
    @Test
    public void setGeolocationTest() {
        event = mockEvent();
        event.setGeolocation("11047 89 Ave NW, Edmonton");
        assertEquals(event.getGeolocation(),"11047 89 Ave NW, Edmonton");
    }

    /**
     * Check if the registrationOpen date of the mockEvent is the same as the registrationOpen date we set.
     */
    @Test
    public void getRegistrationOpenTest() {
        event = mockEvent();
        assertEquals(event.getRegistrationOpen(),new Date("December 17, 1995 03:24:00"));
     }

    /**
     * Change the registrationOpen date of the mockEvent and check if it changed.
     */
    @Test
    public void setRegistrationOpenTest() {
        event = mockEvent();
        event.setRegistrationOpen(new Date("November 18, 2003 02:47:00"));
        assertEquals(event.getRegistrationOpen(),new Date("November 18, 2003 02:47:00"));
    }

    /**
     * Check if the RegistrationClose date of the mockEvent is the same as the RegistrationClose date we set.
     */
    @Test
    public void getRegistrationCloseTest() {
        event = mockEvent();
        assertEquals(event.getRegistrationClose(),new Date("December 17, 1995 03:24:00"));
    }

    /**
     * Change the RegistrationClose of the mockEvent and check if it changed.
     */
    @Test
    public void setRegistrationCloseTest() {
        event = mockEvent();
        event.setRegistrationClose(new Date("November 18, 2003 02:47:00"));
        assertEquals(event.getRegistrationClose(),new Date("November 18, 2003 02:47:00"));
    }

    /**
     * Check if the MaxCapacity date of the mockEvent is the same as the MaxCapacity date we set.
     */
    @Test
    public void getMaxCapacityTest() {
        event = mockEvent();
        assertEquals(event.getMaxCapacity(),100);
    }

    /**
     * Change the MaxCapacity of the mockEvent and check if it changed.
     */
    @Test
    public void setMaxCapacityTest() {
        event = mockEvent();
        event.setMaxCapacity(66);
        assertEquals(event.getMaxCapacity(),66);
    }

    /**
     * Check if the Price date of the mockEvent is the same as the Price date we set.
     */
    @Test
    public void getPriceTest() {
        event = mockEvent();
        assertEquals(event.getPrice(),50);
    }

    /**
     * Change the Price of the mockEvent and check if it changed.
     */
    @Test
    public void setPriceTest() {
        event = mockEvent();
        event.setPrice(33);
        assertEquals(event.getPrice(),33);
    }


    /**
     * Check if the WaitListLimit date of the mockEvent is the same as the WaitListLimit date we set.
     */
    @Test
    public void getWaitListLimitTest() {
        event = mockEvent();
        assertEquals(event.getWaitListLimit(),10);
    }
    
    /**
     * Change the WaitListLimit of the mockEvent and check if it changed.
     */
    @Test
    public void setWaitListLimitTest() {
        event = mockEvent();
        event.setWaitListLimit(5);
        assertEquals(event.getWaitListLimit(),5);
    }


    /**
     * Check if the NeedsGeolocation date of the mockEvent is the same as the NeedsGeolocation date we set.
     */
    @Test
    public void getNeedsGeolocationTest() {
        event = mockEvent();
        assertFalse(event.getNeedsGeolocation());
    }

    /**
     * Change the NeedsGeolocation of the mockEvent and check if it changed.
     */
    @Test
    public void setNeedsGeolocationTest() {
        event = mockEvent();
        event.setNeedsGeolocation(true);
        assertTrue(event.getNeedsGeolocation());
    }

    /**
     * Check if the Image of the mockEvent is the same as the Image we set.
     */
    @Test
    public void getImageTest() {
        event = mockEvent();
        String image = "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/posters%2F1731044610966.jpg?alt=media&token=f9daa8db-eba7-43fd-9cca-779bac4bbfbe";
        assertEquals(event.getImage(),image);
    }

    /**
     * Change the Image of the mockEvent and check if it changed.
     */
    @Test
    public void setImageTest() {
        event = mockEvent();
        String image = "https://firebasestorage.googleapis.com/v0/b/bubbletracks-bubble.firebasestorage.app/o/posters%2F1730969059833.jpg?alt=media&token=b57acd19-ef08-4293-83ba-d7a119dd92a2";
        event.setImage(image);
        assertEquals(event.getImage(),image);
    }

    /**
     * Check if the QRCode of the mockEvent is the same as the QRCode we set.
     */
    @Test
    public void getQRCodeTest() {
        event = mockEvent();
        String QRCode = "https://www.bubbletracks.com/events/bcdc753e-f1ae-440e-97cf-2d13e4d7547c";
        assertEquals(event.getImage(),QRCode);
    }

    /**
     * Change the QRCode of the mockEvent and check if it changed.
     */
    @Test
    public void setQRCodeTest() {
        event = mockEvent();
        String QRCode = "https://www.bubbletracks.com/events/0d0d9557-dcf8-45ce-9b37-83a183f339cf";
        event.setQRCode(QRCode);
        assertEquals(event.getImage(),QRCode);
    }

    /**
     * Make sure it gets the year in the correct format.
     */
    @Test
    public void getYearTest(){
        event = mockEvent();
        assertEquals(event.getYear(event.getDateTime()), "1995");
    }

    /**
     * Make sure it gets the Month in the correct format.
     */
    @Test
    public void getMonthTest() {
        event = mockEvent();
        assertEquals(event.getMonth(event.getDateTime()), "Dec");
    }

    /**
     * Make sure it gets the Day in the correct format.
     */
    @Test
    public void getDayTest() {
        event = mockEvent();
        assertEquals(event.getDay(event.getDateTime()), "17");
    }

    /**
     * Make sure it gets the Time in the correct format.
     */
    @Test
    public void getTimeTest() {
        event = mockEvent();
        assertEquals(event.getTime(event.getDateTime()), "03:24");
    }

    /**
     * Check if the WaitList of the mockEvent is the same as the WaitList we set.
     */
    @Test
    public void getWaitListTest() {
        event = mockEvent();
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("ID1");
        waitList.add("ID2");
        waitList.add("ID3");
        waitList.add("ID4");
        assertEquals(event.getWaitList(), waitList);
    }

    /**
     * Change the WaitList of the mockEvent and check if it changed.
     */
    @Test
    public void setWaitListTest() {
        event = mockEvent();
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("NewID1");
        waitList.add("NewID2");
        waitList.add("NewID3");
        waitList.add("NewID4");
        event.setWaitList(waitList);
        assertEquals(event.getWaitList(), waitList);
    }

    /**
     * Set the waitlist with entrants with the same ID.
     * Get waitlist and compare with IDs
     */
    @Test
    public void setWaitListWithEventsTest() {
        event = mockEvent();
        ArrayList<Entrant> waitList = new ArrayList<>();
        waitList.add(mockEntrant("ID1"));
        waitList.add(mockEntrant("ID2"));
        waitList.add(mockEntrant("ID3"));
        waitList.add(mockEntrant("ID4"));
        ArrayList<String> waitListID = new ArrayList<>();
        waitListID.add("ID1");
        waitListID.add("ID2");
        waitListID.add("ID3");
        waitListID.add("ID4");

        event.setWaitListWithEvents(waitList);
        assertEquals(event.getWaitList(), waitListID);
    }

    /**
     * Add to waitlist.
     * Check the waitlist contains the new ID
     */
    @Test
    public void addToWaitListTest() {
        event = mockEvent();
        event.addToWaitList("newID");
        assertTrue(event.getWaitList().contains("newID"));
    }

    /**
     * Delete from waitlist.
     * Check the waitlist does not contain the new ID
     */
    @Test
    public void deleteFromWaitListTest() {
        event = mockEvent();
        event.deleteFromWaitList("ID1");
        assertFalse(event.getWaitList().contains("ID1"));
    }

    /**
     * Clear list
     * Check list size is 0
     */
    @Test
    public void clearWaitListTest() {
        event = mockEvent();
        event.clearWaitList();
        assertEquals(event.getWaitList().size(), 0);
    }

    /**
     * Check if the invitedList of the mockEvent is the same as the invitedList we set.
     */
    @Test
    public void getInvitedListTest() {
        event = mockEvent();
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add("ID1");
        invitedList.add("ID3");
        assertEquals(event.getInvitedList(), invitedList);
    }

    /**
     * Change the invitedList of the mockEvent and check if it changed.
     */
    @Test
    public void setInvitedListTest() {
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add("NewID1");
        invitedList.add("NewID2");
        invitedList.add("NewID3");
        invitedList.add("NewID4");
        event.setInvitedList(invitedList);
        assertEquals(event.getInvitedList(), invitedList);
    }

    /**
     * Set the InvitedList with entrants with the same ID.
     * Get InvitedList and compare with IDs
     */
    @Test
    public void setInvitedListWithEventsTest() {
        event = mockEvent();
        ArrayList<Entrant> invitedList = new ArrayList<>();
        invitedList.add(mockEntrant("ID1"));
        invitedList.add(mockEntrant("ID3"));
        ArrayList<String> invitedListID = new ArrayList<>();
        invitedListID.add("ID1");
        invitedListID.add("ID3");

        event.setWaitListWithEvents(invitedList);
        assertEquals(event.getInvitedList(), invitedListID);
    }

    /**
     * Add to InvitedList.
     * Check the InvitedList contains the new ID
     */
    @Test
    public void addToInvitedListTest() {
        event = mockEvent();
    }

    /**
     * Delete from InvitedList.
     * Check the InvitedList does not contain the new ID
     */
    @Test
    public void deleteFromInvitedListTest() {
        event = mockEvent();
    }

    /**
     * Clear list
     * Check list size is 0
     */
    @Test
    public void clearInvitedListTestTest() {
        event = mockEvent();
    }

    /**
     * Check if the Name of the mockEvent is the same as the Name we set.
     */
    @Test
    public void getCancelledListTest() {
        event = mockEvent();
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("ID1");
        waitList.add("ID2");
        waitList.add("ID3");
        waitList.add("ID4");
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add("ID1");
        invitedList.add("ID3");
        ArrayList<String> cancelledList = new ArrayList<>();
        cancelledList.add("ID3");
        ArrayList<String> rejectedList = new ArrayList<>();
        rejectedList.add("ID2");
        rejectedList.add("ID4");
        ArrayList<String> enrolledList = new ArrayList<>();
        enrolledList.add("ID1");
    }

    /**
     * Set the CancelledList with entrants with the same ID.
     * Get CancelledList and compare with IDs
     */
    @Test
    public void setCancelledListTest() {
     }

    /**
     * Set the CancelledList with entrants with the same ID.
     * Get CancelledList and compare with IDs
     */
    @Test
    public void setCancelledListWithEventsTest() {
        event = mockEvent();
    }

    /**
     * Add to CancelledList.
     * Check the CancelledList contains the new ID
     */
    @Test
    public void addToCancelledListTest() {
        event = mockEvent();
    }

    /**
     * Delete from CancelledList.
     * Check the CancelledList does not contain the new ID
     */
    @Test
    public void deleteFromCancelledListTest() {
        event = mockEvent();
    }

    /**
     * Clear list
     * Check list size is 0
     */
    @Test
    public void clearCancelledListTest() {
        event = mockEvent();
    }

    /**
     * Check if the Name of the mockEvent is the same as the Name we set.
     */
    @Test
    public void getRejectedListTest() {
        event = mockEvent();
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("ID1");
        waitList.add("ID2");
        waitList.add("ID3");
        waitList.add("ID4");
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add("ID1");
        invitedList.add("ID3");
        ArrayList<String> cancelledList = new ArrayList<>();
        cancelledList.add("ID3");
        ArrayList<String> rejectedList = new ArrayList<>();
        rejectedList.add("ID2");
        rejectedList.add("ID4");
        ArrayList<String> enrolledList = new ArrayList<>();
        enrolledList.add("ID1");

    }

    /**
     * Set the RejectedList with entrants with the same ID.
     * Get RejectedList and compare with IDs
     */
    @Test
    public void setRejectedListTest() {
        event = mockEvent();
    }

    /**
     * Set the RejectedList with entrants with the same ID.
     * Get RejectedList and compare with IDs
     */
    @Test
    public void setRejectedListWithEventsTest() {
        event = mockEvent();
    }

    /**
     * Add to RejectedList.
     * Check the RejectedList contains the new ID
     */
    @Test
    public void addToRejectedListTest() {
        event = mockEvent();
    }

    /**
     * Delete from RejectedList.
     * Check the RejectedList does not contain the new ID
     */
    @Test
    public void deleteFromRejectedListTest() {
        event = mockEvent();
    }

    /**
     * Clear list
     * Check list size is 0
     */
    @Test
    public void clearRejectedListTest() {
        event = mockEvent();
    }

    /**
     * Check if the Name of the mockEvent is the same as the Name we set.
     */
    @Test
    public void getEnrolledListTest() {
        event = mockEvent();
        ArrayList<String> waitList = new ArrayList<>();
        waitList.add("ID1");
        waitList.add("ID2");
        waitList.add("ID3");
        waitList.add("ID4");
        ArrayList<String> invitedList = new ArrayList<>();
        invitedList.add("ID1");
        invitedList.add("ID3");
        ArrayList<String> cancelledList = new ArrayList<>();
        cancelledList.add("ID3");
        ArrayList<String> rejectedList = new ArrayList<>();
        rejectedList.add("ID2");
        rejectedList.add("ID4");
        ArrayList<String> enrolledList = new ArrayList<>();
        enrolledList.add("ID1");

    }

    /**
     * Set the EnrolledList with entrants with the same ID.
     * Get EnrolledList and compare with IDs
     */
    @Test
    public void setEnrolledListTest() {
        event = mockEvent();
    }

    /**
     * Set the EnrolledList with entrants with the same ID.
     * Get EnrolledList and compare with IDs
     */
    @Test
    public void setEnrolledListWithEventsTest() {
        event = mockEvent();
    }

    /**
     * Add to EnrolledList.
     * Check the EnrolledList contains the new ID
     */
    @Test
    public void addToEnrolledListTest() {
        event = mockEvent();
    }

    /**
     * Delete from EnrolledList.
     * Check the EnrolledList does not contain the new ID
     */
    @Test
    public void deleteFromEnrolledListTest() {
        event = mockEvent();
    }

    /**
     * Clear list
     * Check list size is 0
     */
    @Test
    public void clearEnrolledListTest() {
        event = mockEvent();
    }


    public void isInEnrolledListTest(){
   }
   
}
