package com.foxdigitaltech.store.ui.home.presenter;



import com.foxdigitaltech.store.ui.home.contract.HomeFragmentContract;
import com.foxdigitaltech.store.ui.home.interactor.HomeFragmentInteractor;
import com.foxdigitaltech.store.ui.home.model.Category;
import com.foxdigitaltech.store.ui.home.model.Product;
import com.foxdigitaltech.store.ui.home.model.ProductCart;

import java.util.List;

public class HomeFragmentPresenter implements HomeFragmentContract.Listener {

    private HomeFragmentContract.View view;
    private HomeFragmentInteractor interactor;


    public HomeFragmentPresenter(HomeFragmentContract.View view) {
        this.view = view;
        interactor = new HomeFragmentInteractor(this);
    }

    public void addCart(ProductCart productCart){
        interactor.addCart(productCart);
    }


    @Override
    public void hasError(String message) {
        view.showLoader();
        view.hasError(message);
    }
}
