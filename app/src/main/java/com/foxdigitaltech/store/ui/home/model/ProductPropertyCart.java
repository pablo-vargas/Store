package com.foxdigitaltech.store.ui.home.model;

public class ProductPropertyCart {
    private boolean select ;
    private ProductCart productCart;

    public ProductPropertyCart(boolean select, ProductCart productCart) {
        this.select = select;
        this.productCart = productCart;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public ProductCart getProductCart() {
        return productCart;
    }

    public void setProductCart(ProductCart productCart) {
        this.productCart = productCart;
    }
}
