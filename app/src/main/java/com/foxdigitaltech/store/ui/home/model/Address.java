package com.foxdigitaltech.store.ui.home.model;

import com.google.firebase.database.DataSnapshot;

public class Address {
    private String key;
    private String street;
    private String phone;
    private String name;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double streetLongitude;
    private Double streetLatitude;

    public Address(){}
    public Address(String key, String street, String phone, String name, Double currentLatitude, Double currentLongitude, Double streetLongitude, Double streetLatitude) {
        this.key = key;
        this.street = street;
        this.phone = phone;
        this.name = name;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.streetLongitude = streetLongitude;
        this.streetLatitude = streetLatitude;
    }

    public Address(String street, String phone, String name, Double currentLatitude, Double currentLongitude, Double streetLongitude, Double streetLatitude) {
        this.street = street;
        this.phone = phone;
        this.name = name;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.streetLongitude = streetLongitude;
        this.streetLatitude = streetLatitude;
    }
    public Address(DataSnapshot snapshot){
        this.key = snapshot.getKey();
        this.street = snapshot.child("street").getValue().toString();
        this.name = snapshot.child("name").getValue().toString();
        this.currentLatitude = Double.parseDouble(snapshot.child("currentLatitude").getValue().toString());
        this.currentLongitude = Double.parseDouble(snapshot.child("currentLongitude").getValue().toString());
        this.streetLongitude = Double.parseDouble(snapshot.child("streetLongitude").getValue().toString());
        this.streetLatitude = Double.parseDouble(snapshot.child("streetLatitude").getValue().toString());
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getStreetLongitude() {
        return streetLongitude;
    }

    public void setStreetLongitude(Double streetLongitude) {
        this.streetLongitude = streetLongitude;
    }

    public Double getStreetLatitude() {
        return streetLatitude;
    }

    public void setStreetLatitude(Double streetLatitude) {
        this.streetLatitude = streetLatitude;
    }
}
