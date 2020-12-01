package com.foxdigitaltech.store.ui.shop.presenter;

import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.ui.shop.contract.CartContract;
import com.foxdigitaltech.store.ui.shop.interactor.CartInteractor;

import java.util.List;

public class CartPresenter implements CartContract.Listener {

    CartContract.View view;
    CartInteractor interactor;

    public CartPresenter(CartContract.View view) {
        this.view = view;
        interactor = new CartInteractor(this);
    }
    public void start(){
        view.showLoader();
        interactor.start();
    }

    public void removeProduct(String key){
        interactor.removeProduct(key);
    }
    public void addSubProduct(ProductCart productCart,int value){
        interactor.addSubProduct(productCart,value);
    }

    @Override
    public void products(List<ProductCart> list) {
        view.hideLoader();
        view.products(list);
    }

    @Override
    public void address(List<Address> addresses) {
        view.address(addresses);
    }
}
