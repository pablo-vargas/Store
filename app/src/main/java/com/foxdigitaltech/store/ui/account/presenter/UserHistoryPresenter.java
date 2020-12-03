package com.foxdigitaltech.store.ui.account.presenter;

import com.foxdigitaltech.store.shared.model.Order;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.ui.account.contract.UserHistoryContract;
import com.foxdigitaltech.store.ui.account.interactor.UserHistoryInteractor;
import com.foxdigitaltech.store.ui.account.model.UserHistory;

import java.util.List;

public class UserHistoryPresenter implements UserHistoryContract.Listener {
    UserHistoryContract.View view;
    UserHistoryInteractor interactor;

    public UserHistoryPresenter(UserHistoryContract.View view) {
        this.view = view;
        interactor = new UserHistoryInteractor(this);
    }
    public void start(){
        view.showLoader();
        interactor.start();
    }
    public void showProducts(String keyOrder){
        view.showLoader();
        interactor.getProducts(keyOrder);
    }

    @Override
    public void orders(List<UserHistory> orders) {
        view.hideLoader();
        view.orders(orders);
    }

    @Override
    public void products(List<ProductCart> cartList) {
        view.hideLoader();
        view.products(cartList);
    }
}
