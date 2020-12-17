package com.foxdigitaltech.store.shared.model;

import com.google.firebase.database.DataSnapshot;

public class PriceDelivery {
    private int price;
    private int end;
    private int start;

    public PriceDelivery(int price, int end, int start) {
        this.price = price;
        this.end = end;
        this.start = start;
    }

    public PriceDelivery(DataSnapshot snapshot) {
        this.price = Integer.parseInt(snapshot.child("price").getValue().toString());
        this.end = Integer.parseInt(snapshot.child("end").getValue().toString());
        this.start = Integer.parseInt(snapshot.child("start").getValue().toString());
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
