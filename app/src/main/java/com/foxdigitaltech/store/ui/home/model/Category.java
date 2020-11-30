package com.foxdigitaltech.store.ui.home.model;

import com.google.firebase.database.DataSnapshot;

public class Category {

    private String key;
    private String name;
    private String detail;
    private String image;

    public Category(String key, String name, String detail, String image) {
        this.key = key;
        this.name = name;
        this.detail = detail;
        this.image = image;
    }

    public Category(DataSnapshot snapshot){
        this.key = snapshot.getKey();
        this.detail = snapshot.child("detail").getValue().toString();
        this.name =  snapshot.child("name").getValue().toString();
        this.image =  snapshot.child("image").getValue().toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
