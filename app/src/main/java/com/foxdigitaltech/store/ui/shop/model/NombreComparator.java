package com.foxdigitaltech.store.ui.shop.model;

import com.foxdigitaltech.store.shared.model.Product;

import java.util.Comparator;

public class NombreComparator implements Comparator<Product> {
    @Override
    public int compare(Product product, Product t1) {
        return product.getName().compareTo(t1.getName());
    }
}
