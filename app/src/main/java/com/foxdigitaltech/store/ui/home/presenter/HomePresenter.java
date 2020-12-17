package com.foxdigitaltech.store.ui.home.presenter;

import com.foxdigitaltech.store.shared.model.PriceDelivery;
import com.foxdigitaltech.store.ui.home.contract.HomeContract;
import com.foxdigitaltech.store.ui.home.interactor.HomeInteractor;
import com.foxdigitaltech.store.shared.model.Category;
import com.foxdigitaltech.store.shared.model.Product;

import java.util.List;

public class HomePresenter implements HomeContract.Listener {

    private HomeContract.View view;
    private HomeInteractor interactor;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        interactor = new HomeInteractor(this);
    }

    public void init(){
        interactor.init();
    }
    public void verifyAccount(){
        interactor.verifyAccount();
    }


    @Override
    public void listCategories(List<Category> categories) {
        view.listCategories(categories);
    }

    @Override
    public void listOffers(List<Product> products) {
        view.listOffers(products);
    }

    @Override
    public void listBestSellers(List<Product> products) {
        view.listBestSellers(products);
    }

    @Override
    public void listPrice(List<PriceDelivery> priceDeliveries) {
        view.listPrice(priceDeliveries);
    }

    @Override
    public void successData() {
        view.successData();
        view.hideLoader();
    }

    @Override
    public void verifyAccount(boolean flag) {
        view.verifyAccount(flag);
    }

    @Override
    public void isVersion(boolean valid){
        view.isVersion(valid);
    }
}
