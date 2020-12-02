package com.foxdigitaltech.store.ui.shop.contract;

import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.ProductCart;

import java.util.List;

public interface CartContract {

    interface View{
        void showLoader();
        void hideLoader();
        void products(List<ProductCart> list);
        void address(List<Address> addresses);

        void loaderOrder();
        void hideLoaderOrder();
        void successOrder();
        void hasError(String message);
    }
    interface Listener{
        void products(List<ProductCart> list);
        void address(List<Address> addresses);
        void successOrder();
        void hasError(String message);
    }
}
