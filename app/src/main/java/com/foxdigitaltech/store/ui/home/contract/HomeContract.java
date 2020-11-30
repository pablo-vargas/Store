package com.foxdigitaltech.store.ui.home.contract;

import com.foxdigitaltech.store.ui.home.model.Category;
import com.foxdigitaltech.store.ui.home.model.Product;

import java.util.List;

public interface HomeContract {
    interface View{
        void showLoader();
        void hideLoader();
        void successData();
        void listCategories(List<Category> categories);
        void listOffers(List<Product> products);
        void listBestSellers(List<Product> products);
        void verifyAccount(boolean flag);
    }

    interface Listener{
        void listCategories(List<Category> categories);
        void listOffers(List<Product> products);
        void listBestSellers(List<Product> products);
        void successData();
        void verifyAccount(boolean flag);
    }
}
