package com.foxdigitaltech.store.ui.shop.contract;

import com.foxdigitaltech.store.shared.model.Brand;
import com.foxdigitaltech.store.shared.model.Product;

import java.util.List;

public interface ProductsContract {
    interface View{
        void showLoader();
        void hideLoader();
        void products(List<Product> productList);
        void brands(List<Brand> brandList);
        void setProductsFilter(List<Product> productList);
    }

    interface Listener{
        void products(List<Product> productList);
        void brands(List<Brand> brandList);
        void setProductsFilter(List<Product> productList);
    }
}
