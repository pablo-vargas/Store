package com.foxdigitaltech.store.shared.model;

import com.google.firebase.database.DataSnapshot;

public class Category {

    private String key;
    private String name;
    private String detail;
    private String image;
    private String slug;

    public Category(String key, String name, String detail, String image,String slug) {
        this.key = key;
        this.name = name;
        this.detail = detail;
        this.image = image;
        this.slug = slug;
    }

    public Category(DataSnapshot snapshot){
        this.key = snapshot.getKey();
        this.detail = snapshot.child("detail").getValue().toString();
        this.name =  snapshot.child("name").getValue().toString();
        this.image =  snapshot.child("image").getValue().toString();
        this.slug = snapshot.child("slug").getValue().toString();
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
