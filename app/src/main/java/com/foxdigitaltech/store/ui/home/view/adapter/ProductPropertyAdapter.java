package com.foxdigitaltech.store.ui.home.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.home.model.ProductPropertyCart;
import com.google.android.material.chip.Chip;

import java.util.List;

public class ProductPropertyAdapter extends RecyclerView.Adapter<ProductPropertyAdapter.ViewHolder> {

    List<ProductPropertyCart> list;

    public ProductPropertyAdapter(List<ProductPropertyCart> list) {
        this.list = list;
    }

    private void notifyData(){
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_charasteristic,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.charac.setText(list.get(position).getProductCart().getProperty());
        holder.charac.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                list.get(position).setSelect(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        Chip charac;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            charac = itemView.findViewById(R.id.chipChar);
        }
    }
}
