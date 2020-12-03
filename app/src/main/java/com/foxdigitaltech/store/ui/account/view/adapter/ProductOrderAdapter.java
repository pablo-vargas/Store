package com.foxdigitaltech.store.ui.account.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductOrderAdapter extends RecyclerView.Adapter<ProductOrderAdapter.ViewHolder>{

    List<ProductCart> cartList;

    public ProductOrderAdapter(List<ProductCart> cartList) {
        this.cartList = cartList;
    }
    private void notifyCart(){
        this.notifyDataSetChanged();
    }
    public List<ProductCart> get(){
        return cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_product_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final ProductCart cart = cartList.get(position);
        Picasso.get().load(cart.getImage()).into(holder.imageProduct);
        String property = cart.getProperty().isEmpty()? "":" - "+cart.getProperty();
        holder.name.setText(cart.getName()+property);
        holder.price.setText("Bs"+cart.getPrice()+"0");
        holder.total.setText("Total: Bs "+(cart.getPrice()*cart.getQuantity())+"0");
        holder.count.setText("Cantidad: "+cart.getQuantity()+"");

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,count,total;

        ImageView imageProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewNameProduct);
            price = itemView.findViewById(R.id.textViewPrice);
            count = itemView.findViewById(R.id.textViewCount);
            total = itemView.findViewById(R.id.textViewTotal);

            imageProduct = itemView.findViewById(R.id.imageView);
        }
    }

}
