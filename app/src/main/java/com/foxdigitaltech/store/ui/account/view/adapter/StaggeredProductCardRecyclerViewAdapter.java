package com.foxdigitaltech.store.ui.account.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.account.model.ProductEntry;

import java.util.List;

    public class StaggeredProductCardRecyclerViewAdapter extends RecyclerView.Adapter<StaggeredProductCardRecyclerViewAdapter.StaggeredProductCardViewHolder> {

    private List<ProductEntry> productList;
   // private ImageRequester imageRequester;

    public StaggeredProductCardRecyclerViewAdapter(List<ProductEntry> productList) {
        this.productList = productList;
        //imageRequester = ImageRequester.getInstance();
    }

    @Override
    public int getItemViewType(int position) {
        return position % 3;
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    @NonNull
    @Override
    public StaggeredProductCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = R.layout.card_product_first;
        if (viewType == 1) {
            layoutId = R.layout.card_product_second;
        } else if (viewType == 2) {
            layoutId = R.layout.card_product_third;
        }
        Log.d("TYPECARD",viewType+"");
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new StaggeredProductCardViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull StaggeredProductCardViewHolder holder, int position) {
        if (productList != null && position < productList.size()) {
          /* ProductEntry product = productList.get(position);
            holder.productTitle.setText(product.title);
            holder.productPrice.setText(product.price);
            imageRequester.setImageFromUrl(holder.productImage, product.url);*/
        }
    }

    public class StaggeredProductCardViewHolder extends RecyclerView.ViewHolder {

        public TextView productTitle;
        public TextView productPrice;

        StaggeredProductCardViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

}
