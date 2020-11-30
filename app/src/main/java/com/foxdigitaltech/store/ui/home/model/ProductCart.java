package com.foxdigitaltech.store.ui.home.model;

import java.util.List;

public class ProductCart {
    private String key;
    private String code;
    private String name;
    private String slug;
    private String image;
    private String brand;
    private String badge;
    private String category;
    private String property;
    private int quantity;
    private Double price;


    public ProductCart(String code, String name, String slug, String image, String brand, String badge, String category, int quantity, Double price) {
        this.code = code;
        this.name = name;
        this.slug = slug;
        this.image = image;
        this.brand = brand;
        this.badge = badge;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }
    public ProductCart(Product product,String property){
        this.code = product.getKey();
        this.name = product.getName();
        this.slug = product.getSlug();
        this.image  = product.getImages().get(0);
        this.brand = product.getBrand();
        this.badge = product.getBadge();
        this.category = product.getCategory();
        this.quantity = 1;
        this.price = product.getPrice();
        this.property = property;

    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
