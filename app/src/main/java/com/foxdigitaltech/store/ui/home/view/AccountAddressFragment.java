package com.foxdigitaltech.store.ui.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.model.Address;
import com.foxdigitaltech.store.ui.home.model.RouteDatabase;
import com.foxdigitaltech.store.ui.home.view.adapter.AddressAdapter;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.foxdigitaltech.store.ui.maps.AddressAddActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AccountAddressFragment extends Fragment {

    LinearLayout layoutLoader;
    RecyclerView recyclerView;
    AddressAdapter addressAdapter;
    ImageButton btnBack,btnAdd;
    HomeViewModel viewModel;

    public AccountAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account_address, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        btnAdd = view.findViewById(R.id.btnAddressAdd);
        btnBack = view.findViewById(R.id.btnBack);
        layoutLoader = view.findViewById(R.id.layoutLoader);
        recyclerView = view.findViewById(R.id.recycler_view_address);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddressAddActivity.class));
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getData();
    }

    private void getData(){
        layoutLoader.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Address> list = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    list.add(new Address(dataSnapshot));
                }
                layoutLoader.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                addressAdapter = new AddressAdapter(viewModel,list);
                recyclerView.setAdapter(addressAdapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error de red.", Toast.LENGTH_SHORT).show();
                layoutLoader.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        };
        FirebaseDatabase
                .getInstance()
                .getReference(new RouteDatabase().LIST_ADDRESS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(valueEventListener);
    }

}