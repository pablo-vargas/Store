package com.foxdigitaltech.store.ui.shop.interactor;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.Order;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.foxdigitaltech.store.ui.register.model.UserProfile;
import com.foxdigitaltech.store.ui.shop.contract.CartContract;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Date;
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
            sendTokenNotification();
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


    public void checkOrder(int quantity,Double total,int delivery,Address address,List<ProductCart> productCarts){
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            if(user.getPhoneNumber() != null){
                if(user.getPhoneNumber().isEmpty()){
                    listener.hasError("Debe verificar su numero de telefono, perfil inválido");
                }else{
                    UserProfile userProfile = new UserProfile(user.getDisplayName(),"",user.getPhoneNumber(),user.getEmail());
                    Order order = new Order(userProfile,productCarts,address,quantity,delivery,total,new Date().getTime(),"pending");
                    databaseReference.child(routeDatabase.CREATE_ORDER).child(user.getUid()).push().setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.successOrder();
                            databaseReference.child(routeDatabase.CART).child(user.getUid()).removeValue();
                            listener.products(new ArrayList<ProductCart>());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.hasError("Error al realizar orden del pedido");
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            listener.hasError("Error de conexion");
                        }
                    });
                }
            }else{
                listener.hasError("Debe verificar su numero de telefono, perfil inválido");
            }

        }else{
            listener.hasError("No se encontro el usuario.");
        }
    }

    private void sendTokenNotification(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(runnable -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("notify/client").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.child("token").setValue(runnable.getToken());
        });
    }

}
