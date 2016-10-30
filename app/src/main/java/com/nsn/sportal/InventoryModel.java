package com.nsn.sportal;

/**
 * Created by nihalpradeep on 28/06/16.
 */
public class InventoryModel {
    String name,sport;
    int quantity,slno;

    public String getName() {
        return name;
    }

    public String getSport() {
        return sport;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSlno() {
        return slno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSlno(int slno) {
        this.slno = slno;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }
}
