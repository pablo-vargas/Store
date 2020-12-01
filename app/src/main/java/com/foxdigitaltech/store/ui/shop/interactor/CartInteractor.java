package com.foxdigitaltech.store.ui.shop.interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.foxdigitaltech.store.ui.shop.contract.CartContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartInteractor {

    CartContract.Listener listener;
    RouteDatabase routeDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

    public CartInteractor(CartContract.Listener listener) {
        this.listener = listener;
        routeDatabase = new RouteDatabase();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void start(){
        if(user != null) {
            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<ProductCart> productCarts = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        productCarts.add(new ProductCart(dataSnapshot));
                    }
                    listener.products(productCarts);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.d("USERNULL", error.getMessage());
                }
            };
            databaseReference.child(routeDatabase.CART).child(user.getUid()).child("products").addListenerForSingleValueEvent(valueEventListener);
            getAddress();
        }else{
            listener.products(new ArrayList<ProductCart>());
        }
    }

    public void removeProduct(String key){
        databaseReference.child(routeDatabase.CART).child(user.getUid()).child("products").child(key).removeValue();
    }
    public void addSubProduct(ProductCart productCart,int value){
        int val = productCart.getQuantity() + value;
        if(val <= 0){
            removeProduct(productCart.getKey());
            return;
        }
        databaseReference.child(routeDatabase.CART).child(user.getUid()).child("products").child(productCart.getKey()).child("quantity").setValue(val);
    }

    public void getAddress(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Address> addresses = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    addresses.add(new Address(dataSnapshot));
                }
                Log.d("ADDRESS",snapshot.toString());
                listener.address(addresses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child(routeDatabase.LIST_ADDRESS).child(user.getUid()).addListenerForSingleValueEvent(valueEventListener);
    }

}
