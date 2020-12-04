package com.foxdigitaltech.store.ui.maps.interactor;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.foxdigitaltech.store.ui.maps.contract.AddressContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddressInteractor {
    private AddressContract.Listener listener;

    public AddressInteractor(AddressContract.Listener listener) {
        this.listener = listener;
    }

    public void start(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Address> list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    list.add(new Address(dataSnapshot));
                }
                listener.addressList(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.hasError(error.getMessage());
            }
        };
        FirebaseDatabase
                .getInstance()
                .getReference(new RouteDatabase().LIST_ADDRESS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(valueEventListener);
    }

    public void delete(Address address){
        FirebaseDatabase
                .getInstance()
                .getReference(new RouteDatabase().LIST_ADDRESS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(address.getKey()).removeValue();
        listener.success();
    }
}
