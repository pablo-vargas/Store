package com.foxdigitaltech.store.ui.account.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.account.model.ProductEntry;

import java.util.List;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.ProductCardViewHolder>{

    private List<ProductEntry> productList;

    public ProductCardAdapter(List<ProductEntry> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product,parent,false);
        return new ProductCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductCardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 11;
    }

    class ProductCardViewHolder extends RecyclerView.ViewHolder{
        public ProductCardViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
