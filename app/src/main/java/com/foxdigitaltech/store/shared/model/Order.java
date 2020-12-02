package com.foxdigitaltech.store.shared.model;

import com.foxdigitaltech.store.ui.register.model.UserProfile;

import java.util.List;

public class Order {
    private String key;
    private UserProfile user;
    private List<ProductCart> products;
    private Address address;
    private int quantity;
    private int delivery;
    private Double total;
    private Long createAt;
    private String status;

    public Order(UserProfile user, List<ProductCart> products, Address address, int quantity, int delivery, Double total, Long createAt,String status) {
        this.user = user;
        this.products = products;
        this.address = address;
        this.quantity = quantity;
        this.delivery = delivery;
        this.total = total;
        this.createAt = createAt;
        this.status = status;
    }



    public Order() {
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public UserProfile getUser() {
        return user;
    }

    public void setUser(UserProfile user) {
        this.user = user;
    }

    public List<ProductCart> getProducts() {
        return products;
    }

    public void setProducts(List<ProductCart> products) {
        this.products = products;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Long createAt) {
        this.createAt = createAt;
    }
}
