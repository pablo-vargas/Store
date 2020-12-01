package com.foxdigitaltech.store.shared.model;

import com.google.firebase.database.DataSnapshot;

public class ProductCharacteristics {
    private String key;
    private String value;
    private String slug;

    public ProductCharacteristics(String value, String slug) {
        this.value = value;
        this.slug = slug;
    }
    public ProductCharacteristics(DataSnapshot snapshot){
        this.key = snapshot.getKey();
        this.value = snapshot.child("value").getValue().toString();
        this.slug = snapshot.child("slug").getValue().toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
