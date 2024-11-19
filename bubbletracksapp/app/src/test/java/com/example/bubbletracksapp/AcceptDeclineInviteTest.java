/**
 *
 * @author Sarah, Chester
 * @version 1.0
 */

package com.example.bubbletracksapp;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Class that tests functionality of AcceptDeclineInvite tools
 */
public class AcceptDeclineInviteTest {

        // CREATES A MOCK EVENT
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
            ArrayList<String> invitedList = new ArrayList<>();
            ArrayList<String> cancelledList = new ArrayList<>();
            ArrayList<String> rejectedList = new ArrayList<>();
            ArrayList<String> enrolledList = new ArrayList<>();
            Event event = new Event(id, name, dateTime, description, geolocation, registrationOpen,
                    registrationClose, maxCapacity, price, WaitListLimit, needsGeolocation, image,
                    QRCode, waitList, invitedList, cancelledList, rejectedList, enrolledList);

            return event;
        }

        // CREATES A MOCK USER
        private Entrant mockEntrant() {
            String[] name = new String[]{"The","Name"};
            String email = "test@gmail.com";
            String phone = "8253302549";
            String deviceID = "TheID tested";
            Boolean notification = false;
            ArrayList<String> eventsOrganized = new ArrayList<>();
            ArrayList<String> eventsInvited = new ArrayList<>();
            ArrayList<String> eventsEnrolled = new ArrayList<>();
            ArrayList<String> eventsWaitlist = new ArrayList<>();
            Entrant newEntrant = new Entrant(name, email, phone, deviceID, notification,
                    eventsOrganized, eventsInvited, eventsEnrolled, eventsWaitlist);
            return newEntrant;
        }

        // ADDS EVENT TO USER'S WAITLIST
        @Test
        void testAcceptInvite(){
            Entrant entrant1 = new mockEntrant();
            Event event1 = new mockEvent();

            // ADDS EVENT TO USER'S WAITLIST

            // USER IS INVITED TO EVENT

            // USER ACCEPTS INVITE

            // CHECKS THAT EVENT IS IN USER'S ENROLLED
            assert(entrant1.getEventsWaitlist(contains(event1.getId())));
        }





    }

