package com.foxdigitaltech.store.ui.home.interactor;

import android.util.Log;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.shared.model.PriceDelivery;
import com.foxdigitaltech.store.ui.home.contract.HomeContract;
import com.foxdigitaltech.store.shared.model.Category;
import com.foxdigitaltech.store.shared.model.Product;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeInteractor {
    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    private HomeContract.Listener callback;
    private int countData;
    private static final int APP_VERSION =2;

    public HomeInteractor(HomeContract.Listener callback) {
        this.callback = callback;
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        countData = 0;
    }

    public void init(){
        categories();
        offers();
        bestSellers();
        price();
    }
    public void verifyAccount(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            callback.verifyAccount(true);
        }else{
            callback.verifyAccount(false);
        }
    }


    private void categories(){

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> categories = new ArrayList<>();

                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        categories.add(new Category(dataSnapshot));
                    }
                }
                callback.listCategories(categories);
                countData++;
                completeInit();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        };
        database.child(new RouteDatabase().LIST_CATEGORIES).addListenerForSingleValueEvent(listener);

    }
    private void offers(){

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    products.add(new Product(dataSnapshot));
                }
                callback.listOffers(products);
                countData++;
                completeInit();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERRORDATABASE",error.getMessage());
            }
        };
        database.child(new RouteDatabase().LIST_OFFERS).addListenerForSingleValueEvent(listener);
    }
    private void bestSellers(){
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> products = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    products.add(new Product(dataSnapshot));
                }
                callback.listBestSellers(products);
                countData++;
                completeInit();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERRORDATABASE1",error.getMessage());
            }
        };
        database.child(new RouteDatabase().LIST_BESTSELLERS).orderByChild("ventas").limitToLast(10).addListenerForSingleValueEvent(listener);
    }
    private void completeInit(){
        if(countData > 2){
            callback.successData();
            validateVersion();
        }
    }

    public void validateVersion(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int playStoreVersion = Integer.parseInt(snapshot.getValue().toString());
                if(playStoreVersion == APP_VERSION){
                    callback.isVersion(true);
                }else{
                    callback.isVersion(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        database.child("version").addListenerForSingleValueEvent(valueEventListener);
    }

    private void price(){
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<PriceDelivery> priceDeliveries = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    priceDeliveries.add(new PriceDelivery(dataSnapshot));
                }
                callback.listPrice(priceDeliveries);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERRORDATABASE1",error.getMessage());
            }
        };
        database.child("price").addValueEventListener(listener);
    }
}
