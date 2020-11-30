package com.foxdigitaltech.store.ui.home.model;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private String key;
    private String name;
    private String slug;
    private String detail;
    private List<String> images;
    private String brand;
    private Double price;
    private Double comparePrice;
    private String badge;
    private String category;
    private int ventas;
    private List<ProductCharacteristics> characteristics;


    public Product(String key, String name, String slug, String detail, List<String> images, String brand, Double price, Double comparePrice, String badge, int ventas,String category) {
        this.key = key;
        this.name = name;
        this.slug = slug;
        this.detail = detail;
        this.images = images;
        this.brand = brand;
        this.price = price;
        this.comparePrice = comparePrice;
        this.badge = badge;
        this.ventas = ventas;
        this.category = category;
    }

    public Product(DataSnapshot snapshot){
        images = new ArrayList<>();
        this.key = snapshot.getKey();
        this.name = snapshot.child("name").getValue().toString();
        this.slug = snapshot.child("slug").getValue().toString();
        this.detail = snapshot.child("detail").getValue().toString();
        this.brand = snapshot.child("brand").getValue().toString();
        this.price = Double.valueOf(snapshot.child("price").getValue().toString());
        this.comparePrice = Double.valueOf(snapshot.child("comparePrice").getValue().toString());
        this.badge = snapshot.child("badge").getValue().toString();
        this.category = snapshot.child("category").getValue().toString();
        this.ventas = Integer.parseInt(snapshot.child("ventas").getValue().toString());
        this.images.add(snapshot.child("images").child("0").getValue().toString());
        if(snapshot.child("characteristics").exists()){
            characteristics = new ArrayList<>();
            for (DataSnapshot dataSnapshot: snapshot.child("characteristics").getChildren()){
                characteristics.add(new ProductCharacteristics(dataSnapshot));
            }
        }



    }

    public Product() {
    }

    public List<ProductCharacteristics> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<ProductCharacteristics> characteristics) {
        this.characteristics = characteristics;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getComparePrice() {
        return comparePrice;
    }

    public void setComparePrice(Double comparePrice) {
        this.comparePrice = comparePrice;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }
}