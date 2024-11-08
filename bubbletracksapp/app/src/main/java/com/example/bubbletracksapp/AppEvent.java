package com.example.bubbletracksapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppEvent {


    // ATTRIBUTES
    private String eventID;
    private String eventTitle;
    private Integer eventPic;
    private LocalDateTime eventDate;
    private LocalDateTime eventTime;
    private LocalDateTime waitListCloses;
    private List<AppUser> waitlist = new ArrayList<>();
    private List<AppUser> invited = new ArrayList<>();
    private List<AppUser> registered = new ArrayList<>();
    private List<AppUser> allUsers = new ArrayList<>();


    // CONSTRUCTOR
    public AppEvent() {
    }

    public AppEvent(String eventTitle, Integer eventPic, LocalDateTime eventDate, LocalDateTime eventTime, LocalDateTime waitListCloses) {
        this.eventTitle = eventTitle;
        this.eventPic = eventPic;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.waitListCloses = waitListCloses;
        //eventID should be assigned a specific way
        this.eventID = "abc";
    }


    // GETTERS AND SETTERS
    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Integer getEventPic() {
        return eventPic;
    }

    public void setEventPic(Integer eventPic) {
        this.eventPic = eventPic;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public LocalDateTime getWaitListCloses() {
        return waitListCloses;
    }

    public void setWaitListCloses(LocalDateTime waitListCloses) {
        this.waitListCloses = waitListCloses;
    }

    public List<AppUser> getWaitlist() {
        return waitlist;
    }

    public void setWaitlist(List<AppUser> waitlist) {
        this.waitlist = waitlist;
    }

    public List<AppUser> getInvited() {
        return invited;
    }

    public void setInvited(List<AppUser> invited) {
        this.invited = invited;
    }

    public List<AppUser> getRegistered() {
        return registered;
    }

    public void setRegistered(List<AppUser> registered) {
        this.registered = registered;
    }

    public List<AppUser> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<AppUser> allUsers) {
        this.allUsers = allUsers;
    }

    // METHODS

    // ADDS USER TO EVENT LISTS
    public void addtoWL(AppUser u) {
        AppUser userInList = new AppUser(u.getName(), u.getDevice_id());
        if (!waitlist.contains(userInList)) {
            u.addWLEvent(this);
            waitlist.add(userInList);
            allUsers.add(userInList);
        }
    }

    public void delFromWL(AppUser u) {
        if (waitlist.contains(u)){
            u.removeWLEvent(this);
            waitlist.remove(u);
            deletefromAllUsers(u);
        }

    }


    public synchronized void addToInvtd(AppUser u) {
        if (!invited.contains(u)) {
            invited.add(u);
            u.addInvtdEvent(this);
            waitlist.remove(u);
            u.removeWLEvent(this);
        }
        updateAllUsers(u);
    }

    public void delFromInvtd(AppUser u) {
        if (invited.contains(u)){
            u.removeInvtdEvent(this);
            invited.remove(u);
            deletefromAllUsers(u);
        }

    }

    public void addToReg(AppUser u) {
        if (invited.contains(u) && u.getRegStatus(this).equals("accepted invite")) {

            if (!registered.contains(u)) {
                registered.add(u);
                u.addRegEvent(this);
                invited.remove(u);
                u.removeInvtdEvent(this);
            }
            updateAllUsers(u);
        }
    }

    public void delFromReg(AppUser u) {
        if (registered.contains(u)) {
            registered.remove(u);
            u.removeRegEvent(this);
            deletefromAllUsers(u);
        }

    }

    // GETS REGISTRATION STATUS OF ONE USER
    public String getRegistrationStatus(String device_id) {
        for (AppUser user : allUsers) {
            if (user.getDevice_id().equals(device_id)) {
                return user.getRegStatus(this);
            }
        }
        return "unknown";
    }

    public AppUser getUserFromAllUsers(AppUser user){
        for (AppUser userInList: allUsers) {
            if (userInList.getDevice_id().equals(user.getDevice_id())) {
                return userInList;
            }
        }
        return null;
    }

    public void updateAllUsers(AppUser user) {

        if (user != null) {
            allUsers.removeIf(u -> u.getDevice_id().equals(user.getDevice_id())); // Remove existing instance
            allUsers.add(user);
        } else {
            allUsers.add(user);
        }


    }

    private void deletefromAllUsers(AppUser u){
        allUsers.remove(u);
    }




}
