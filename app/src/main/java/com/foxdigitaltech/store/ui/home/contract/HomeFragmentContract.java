package com.foxdigitaltech.store.ui.home.contract;

import com.foxdigitaltech.store.ui.home.model.Category;
import com.foxdigitaltech.store.ui.home.model.Product;

import java.util.List;

public interface HomeFragmentContract {
    interface View{
        void showLoader();
        void hideLoader();
        void hasError(String message);
    }

    interface Listener{
        void hasError(String message);
    }
}
