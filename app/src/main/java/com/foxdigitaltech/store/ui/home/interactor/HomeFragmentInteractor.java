package com.foxdigitaltech.store.ui.home.interactor;

import com.foxdigitaltech.store.ui.home.contract.HomeFragmentContract;
import com.foxdigitaltech.store.shared.model.Category;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeFragmentInteractor {

    /*FIREBASE*/
    private DatabaseReference database;

    private HomeFragmentContract.Listener callBack;
    private List<Category> categoryList;
    private RouteDatabase routeDatabase;


    public HomeFragmentInteractor(HomeFragmentContract.Listener callBack) {
        this.callBack = callBack;
        database = FirebaseDatabase.getInstance().getReference();
        routeDatabase = new RouteDatabase();
    }

    public void addCart(ProductCart productCart){
        String key = productCart.getCode()+"-"+productCart.getProperty().replace(" ","-").toLowerCase();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.child(routeDatabase.CART).child(uid).child("products").child(key).setValue(productCart);
    }




}
