package com.nsn.sportal;

/**
 * Created by nihalpradeep on 24/06/16.
 */
public class ListModel {
    String name = "";
    String id = "";
    String country = "";
    String sport = "";

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getSport() {
        return sport;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
}
