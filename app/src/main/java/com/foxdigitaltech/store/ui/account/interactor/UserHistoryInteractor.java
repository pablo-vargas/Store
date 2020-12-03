package com.foxdigitaltech.store.ui.account.interactor;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.foxdigitaltech.store.ui.account.contract.UserHistoryContract;
import com.foxdigitaltech.store.ui.account.model.UserHistory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserHistoryInteractor {

    UserHistoryContract.Listener listener;
    RouteDatabase routeDatabase;
    DatabaseReference databaseReference;

    public UserHistoryInteractor(UserHistoryContract.Listener listener) {
        this.listener = listener;
        routeDatabase = new RouteDatabase();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void start(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<UserHistory> userHistories = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    userHistories.add(new UserHistory(dataSnapshot));
                }
                Collections.sort(userHistories);
                listener.orders(userHistories);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child(routeDatabase.USER_HISTORY).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(valueEventListener);

    }

    public void getProducts(String keyOrder){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ProductCart> carts = new ArrayList<>();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    carts.add(new ProductCart(dataSnapshot));
                }
                listener.products(carts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child(routeDatabase.CREATE_ORDER).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(keyOrder).child("products").addListenerForSingleValueEvent(valueEventListener);

    }
}
