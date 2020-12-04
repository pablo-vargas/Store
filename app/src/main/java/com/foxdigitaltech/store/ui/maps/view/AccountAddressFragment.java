package com.foxdigitaltech.store.ui.maps.view;

import android.content.DialogInterface;
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
import com.foxdigitaltech.store.shared.CustomToast;
import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.maps.contract.AddressContract;
import com.foxdigitaltech.store.ui.maps.presenter.AddressPresenter;
import com.foxdigitaltech.store.ui.maps.view.adapter.AddressAdapter;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.foxdigitaltech.store.ui.maps.AddressAddActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AccountAddressFragment extends Fragment implements AddressContract.View, AddressAdapter.Listener {

    LinearLayout layoutLoader;
    MaterialButton btnAddAddress;
    RecyclerView recyclerView;
    AddressAdapter addressAdapter;
    ImageButton btnBack,btnAdd;
    HomeViewModel viewModel;

    AddressPresenter presenter;
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
        btnAddAddress = view.findViewById(R.id.btnAddAddressEmpty);

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
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
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

        presenter = new AddressPresenter(this);
        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();
    }


    @Override
    public void showLoader() {
        layoutLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        layoutLoader.setVisibility(View.GONE);
    }

    @Override
    public void hasError(String error) {
        Toast toast = new CustomToast().custom(getContext(),"error",error);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void addressList(List<Address> addresses) {
        addressAdapter = new AddressAdapter(addresses,this);
        recyclerView.setAdapter(addressAdapter);
        if (addresses.size() > 0){
            btnAddAddress.setVisibility(View.GONE);
        }else{
            btnAddAddress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void success() {
        Toast toast = new CustomToast().custom(getContext(),"success","Completado Correctamente");
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        presenter.start();
    }

    @Override
    public void delete(final Address address) {
        new MaterialAlertDialogBuilder(getContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Eliminar Dirección")
                .setIcon(R.drawable.ic_baseline_restore_from_trash_24)
                .setMessage("¿Está seguro de eliminar "+address.getName()+"?")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Si, Eliminar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.delete(address);
                    }
                })
                .create()
                .show();
    }
}