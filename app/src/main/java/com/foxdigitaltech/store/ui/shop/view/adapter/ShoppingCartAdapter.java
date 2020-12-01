package com.foxdigitaltech.store.ui.shop.view.adapter;

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

import org.w3c.dom.Text;

import java.util.List;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder>{

    List<ProductCart> cartList;
    Listener listener;

    public ShoppingCartAdapter(List<ProductCart> cartList, Listener listener) {
        this.cartList = cartList;
        this.listener = listener;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_shopping_cart,parent,false);
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
        holder.count.setText(cart.getQuantity()+"");

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.remove(cart.getKey());
                cartList.remove(position);
                notifyCart();
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addSubProduct(cart,1);
                cart.setQuantity(cart.getQuantity()+1);
                notifyCart();
            }
        });
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.addSubProduct(cart,-1);
                if(cart.getQuantity() == 1){
                    cartList.remove(position);
                }else{
                    cart.setQuantity(cart.getQuantity()-1);
                }
                notifyCart();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,price,count,total;
        ImageButton add,sub,remove;
        ImageView imageProduct;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewNameProduct);
            price = itemView.findViewById(R.id.textViewPrice);
            count = itemView.findViewById(R.id.textViewCount);
            total = itemView.findViewById(R.id.textViewTotal);
            add = itemView.findViewById(R.id.btnAdd);
            sub = itemView.findViewById(R.id.btnSub);
            remove = itemView.findViewById(R.id.btnRemove);
            imageProduct = itemView.findViewById(R.id.imageView);
        }
    }

    public interface Listener{
        void remove(String key);
        void addSubProduct(ProductCart productCart,int value);
    }
}
