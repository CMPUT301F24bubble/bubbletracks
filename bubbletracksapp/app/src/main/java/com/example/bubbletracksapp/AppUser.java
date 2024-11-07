package com.example.bubbletracksapp;

import java.util.ArrayList;
import java.util.List;

public class AppUser {

    // ATTRIBUTES
    private String name;
    private String appUserType;
    private String device_id;
    private List<AppEvent> waitlistEvents = new ArrayList<>();
    private List<AppEvent> invitedEvents = new ArrayList<>();
    private List<AppEvent> registeredEvents = new ArrayList<>();
    private String regStatus = "WAITLISTED";



    // CONTRUCTOR

    public AppUser(String name, String device_id) {
        this.name = name;
        this.device_id = device_id;
    }

    // GETTERS AND SETTERS
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAppUserType() {
        return appUserType;
    }

    public void setAppUserType(String appUserType) {
        this.appUserType = appUserType;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }
    public List<AppEvent> getWaitlistEvents() {
        return waitlistEvents;
    }

    public void setWaitlistEvents(List<AppEvent> waitlistEvents) {
        this.waitlistEvents = waitlistEvents;
    }

    public List<AppEvent> getInvitedEvents() {
        return invitedEvents;
    }

    public void setInvitedEvents(List<AppEvent> invitedEvents) {
        this.invitedEvents = invitedEvents;
    }

    public List<AppEvent> getRegisteredEvents() {
        return registeredEvents;
    }

    public void setRegisteredEvents(List<AppEvent> registeredEvents) {
        this.registeredEvents = registeredEvents;
    }
    // GETTERS AND SETTERS
    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    // METHODS
    public void addWLEvent(AppEvent wlEvent) {
        waitlistEvents.add(wlEvent);
    }
    public void removeWLEvent(AppEvent wlEvent) {
        waitlistEvents.remove(wlEvent);
    }

    public void addInvtdEvent(AppEvent iEvent) {
        invitedEvents.add(iEvent);
    }
    public void removeInvtdEvent(AppEvent iEvent) {
        invitedEvents.remove(iEvent);
    }

    public void addRegEvent(AppEvent rEvent) {
        registeredEvents.add(rEvent);
    }

    public void removeRegEvent(AppEvent rEvent) {
        registeredEvents.remove(rEvent);
    }

    // METHODS
    public void acceptInvite(AppEvent event){
        for (AppUser user : event.getInvited()) {
            if (device_id.equals(user.getDevice_id())){
                user.setRegStatus("accepted invite");
                event.addToReg(user);
                break;
            }
        }
    }

}

