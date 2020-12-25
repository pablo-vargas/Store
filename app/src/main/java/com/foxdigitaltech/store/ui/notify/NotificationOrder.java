package com.foxdigitaltech.store.ui.notify;

import com.google.firebase.database.DataSnapshot;

public class NotificationOrder {

    private String title;
    private String detail;
    private Long date;

    public NotificationOrder(DataSnapshot dataSnapshot) {
        this.title = dataSnapshot.child("title").getValue().toString();
        this.detail = dataSnapshot.child("message").getValue().toString();
        this.date = Long.valueOf(dataSnapshot.child("createdAt").getValue().toString());
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
