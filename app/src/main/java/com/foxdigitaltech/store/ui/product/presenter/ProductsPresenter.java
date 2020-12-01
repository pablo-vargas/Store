package com.foxdigitaltech.store.ui.product.presenter;

import com.foxdigitaltech.store.shared.model.Brand;
import com.foxdigitaltech.store.shared.model.Category;
import com.foxdigitaltech.store.shared.model.Product;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.ui.product.contract.ProductsContract;
import com.foxdigitaltech.store.ui.product.interactor.ProductsInteractor;

import java.util.List;

public class ProductsPresenter implements ProductsContract.Listener {

    private ProductsContract.View view;
    private ProductsInteractor interactor;

    public ProductsPresenter(ProductsContract.View view) {
        this.view = view;
        interactor = new ProductsInteractor(this);
    }
    public void start(Category category){
        view.showLoader();
        interactor.start(category);
    }

    public void filter(List<Product> products,List<Brand> brands,String compare,String orderBy){
        interactor.filter(products,brands,compare,orderBy);
    }

    public void addCart(ProductCart productCart){
        interactor.addCart(productCart);
    }
    @Override
    public void products(List<Product> productList) {
        view.hideLoader();
        view.products(productList);
        view.setProductsFilter(productList);
    }

    @Override
    public void brands(List<Brand> brandList) {
        view.hideLoader();
        view.brands(brandList);
    }

    @Override
    public void setProductsFilter(List<Product> productList) {
        view.setProductsFilter(productList);
    }
}
