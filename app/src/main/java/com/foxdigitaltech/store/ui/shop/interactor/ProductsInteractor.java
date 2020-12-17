package com.foxdigitaltech.store.ui.shop.interactor;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.shared.model.Brand;
import com.foxdigitaltech.store.shared.model.Category;
import com.foxdigitaltech.store.shared.model.Product;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.foxdigitaltech.store.ui.shop.contract.ProductsContract;
import com.foxdigitaltech.store.ui.shop.model.NombreComparator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProductsInteractor {
    private ProductsContract.Listener listener;
    private RouteDatabase routeDatabase;
    private DatabaseReference databaseReference;

    public ProductsInteractor(ProductsContract.Listener listener) {
        this.listener = listener;
        routeDatabase = new RouteDatabase();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public void start(Category category){
        getProducts(category);
        getBrands(category);
    }
    public void addCart(ProductCart productCart){
        String key = productCart.getCode()+"-"+productCart.getProperty().replace(" ","-").toLowerCase();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference.child(routeDatabase.CART).child(uid).child("products").child(key).setValue(productCart);
    }

    public void filter(List<Product> products,List<Brand> brands,String compareBrand,String orderBy){
        List<Product> productList = new ArrayList<>();

        for (Product product : products){
            if(!compareBrand.isEmpty()){
                if(compareBrand.contains(product.getBrand())){
                    productList.add(product);
                }
            }
            else{
                productList.add(product);
            }
        }

        if(orderBy.equals("Precio")){
            Collections.sort(productList);
        }else if(orderBy.equals("Nombre")){
            Collections.sort(productList,new NombreComparator());
        }
        listener.setProductsFilter(productList);

    }
    private void getProducts(Category category){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Product> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(new Product(dataSnapshot));
                }
                listener.products(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child(routeDatabase.LIST_PRODUCTS).child(category.getSlug()).addListenerForSingleValueEvent(valueEventListener);
    }
    private void getBrands(Category category){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Brand> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    list.add(new Brand(dataSnapshot));
                }
                listener.brands(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child(routeDatabase.LIST_BRANDS).child(category.getSlug()).addListenerForSingleValueEvent(valueEventListener);
    }
}
