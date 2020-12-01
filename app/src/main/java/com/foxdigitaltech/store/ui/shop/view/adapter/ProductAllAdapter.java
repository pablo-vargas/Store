package com.foxdigitaltech.store.ui.shop.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.model.Product;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAllAdapter extends RecyclerView.Adapter<ProductAllAdapter.ViewHolder>{

    List<Product> products;
    ProductAllAdapter.Listener listener;

    public ProductAllAdapter(List<Product> products, ProductAllAdapter.Listener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_product_all,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Product product = products.get(position);
        holder.labelSale.setVisibility(View.GONE);
        holder.labelOffer.setVisibility(View.GONE);
        holder.labelNew.setVisibility(View.GONE);
        holder.comparePrice.setVisibility(View.GONE);

        if(product.getBadge().equals("hot")){
            holder.labelOffer.setVisibility(View.VISIBLE);
        }else if(product.getBadge().equals("sale")){
            holder.labelSale.setVisibility(View.VISIBLE);
        }else if(product.getBadge().equals("new")){
            holder.labelNew.setVisibility(View.VISIBLE);
        }
        if(product.getComparePrice()> 0){
            holder.comparePrice.setVisibility(View.VISIBLE);
            holder.comparePrice.setText(product.getComparePrice()+"0");
        }
        holder.price.setText(product.getPrice()+"0");
        holder.name.setText(product.getName());
        Picasso.get().load(product.getImages().get(0)).into(holder.image);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addCart(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,price,comparePrice,labelOffer,labelNew,labelSale;
        ImageView image;
        MaterialCardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            labelNew = itemView.findViewById(R.id.labelNew);
            labelOffer = itemView.findViewById(R.id.labelOffer);
            labelSale = itemView.findViewById(R.id.labelSale);
            price = itemView.findViewById(R.id.textViewPrice);
            comparePrice = itemView.findViewById(R.id.textViewComparePrice);
            name = itemView.findViewById(R.id.nameTextView);
            image = itemView.findViewById(R.id.profilePhotoImageView);
            cardView = itemView.findViewById(R.id.cardViewProduct);


        }
    }

    public interface Listener{
        void addCart(Product product);
    }
}
