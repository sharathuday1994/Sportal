package com.nsn.sportal;

/**
 * Created by nihalpradeep on 27/06/16.
 */
public class EventModel {
    String eventID,eventDate,eventCountry,eventName,eventUser,eventSport,eventUserID;

    public String getEventCountry() {
        return eventCountry;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventSport() {
        return eventSport;
    }

    public String getEventUserID() {
        return eventUserID;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventUser() {
        return eventUser;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventCountry(String eventCountry) {
        this.eventCountry = eventCountry;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventUser(String eventUser) {
        this.eventUser = eventUser;
    }

    public void setEventSport(String eventSport) {
        this.eventSport = eventSport;
    }

    public void setEventUserID(String eventUserID) {
        this.eventUserID = eventUserID;
    }
}
