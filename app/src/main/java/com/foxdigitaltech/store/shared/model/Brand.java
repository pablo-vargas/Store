package com.foxdigitaltech.store.shared.model;

import com.google.firebase.database.DataSnapshot;

public class Brand {
    private String key;
    private String name;
    private String slug;
    private String image;
    private String category;

    public Brand(String name, String slug, String image) {
        this.name = name;
        this.slug = slug;
        this.image = image;
    }

    public Brand() {
    }

    public Brand(DataSnapshot snapshot){
        this.key = snapshot.getKey();
        this.name = snapshot.child("name").getValue().toString();
        this.image = snapshot.child("image").getValue().toString();
        this.slug = snapshot.child("slug").getValue().toString();
        this.category = snapshot.child("category").getValue().toString();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
