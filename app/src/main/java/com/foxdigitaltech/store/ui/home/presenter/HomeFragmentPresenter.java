package com.foxdigitaltech.store.ui.home.presenter;



import com.foxdigitaltech.store.ui.home.contract.HomeFragmentContract;
import com.foxdigitaltech.store.ui.home.interactor.HomeFragmentInteractor;
import com.foxdigitaltech.store.shared.model.ProductCart;

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
