package com.foxdigitaltech.store.ui.maps.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{

    List<Address> list;
    Listener listener ;

    public AddressAdapter(List<Address> list, Listener listener) {
        this.list = list;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_address,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Address address = list.get(position);
        holder.name.setText(address.getName());
        holder.street.setText(address.getStreet());
        holder.btnTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.delete(address);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,street;
        ImageButton btnTrash;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewNameAddress);
            street = itemView.findViewById(R.id.textViewStreet);
            btnTrash = itemView.findViewById(R.id.btnTrashAddress);
        }
    }
    public interface Listener{
        void delete(Address address);
    }
}
