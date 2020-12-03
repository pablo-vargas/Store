package com.foxdigitaltech.store.ui.account.model;

import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.Product;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.ui.register.model.UserProfile;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

public class UserHistory implements Comparable<UserHistory>{
    private String key;
    private String image;
    private int quantity;
    private int delivery;
    private Double total;
    private Long createdAt;
    private String status;

    public UserHistory() {
    }
    public UserHistory(DataSnapshot snapshot) {
        this.key = snapshot.getKey();
        this.image = snapshot.child("image").getValue().toString();
        this.quantity = Integer.parseInt(snapshot.child("quantity").getValue().toString());
        this.delivery = Integer.parseInt(snapshot.child("priceDelivery").getValue().toString());
        this.total = Double.valueOf(snapshot.child("total").getValue().toString());
        this.createdAt = Long.valueOf(snapshot.child("createdAt").getValue().toString());
        this.status = snapshot.child("status").getValue().toString();

    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(UserHistory order) {
        if(this.createdAt< order.getCreatedAt()){
            return 1;
        }else if(this.createdAt > order.getCreatedAt()){
            return  -1;
        }else{
            return 0;
        }
    }
}
