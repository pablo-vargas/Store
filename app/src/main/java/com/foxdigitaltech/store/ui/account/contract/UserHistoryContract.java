package com.foxdigitaltech.store.ui.account.contract;

import com.foxdigitaltech.store.shared.model.Order;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.ui.account.model.UserHistory;

import java.util.List;

public interface UserHistoryContract {
    interface View{
        void showLoader();
        void hideLoader();
        void orders(List<UserHistory> orders);
        void products(List<ProductCart> cartList);
    }
    interface Listener{
        void orders(List<UserHistory> orders);
        void products(List<ProductCart> cartList);

    }
}
