package com.foxdigitaltech.store.ui.account.view;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.model.Order;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.ui.account.contract.UserHistoryContract;
import com.foxdigitaltech.store.ui.account.model.UserHistory;
import com.foxdigitaltech.store.ui.account.presenter.UserHistoryPresenter;
import com.foxdigitaltech.store.ui.account.view.adapter.HistoryCardAdapter;
import com.foxdigitaltech.store.ui.account.view.adapter.ProductOrderAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class UserHistoryFragment extends Fragment implements UserHistoryContract.View,HistoryCardAdapter.Listener {

    UserHistoryPresenter presenter;
    RecyclerView recyclerView,recyclerViewOrders;
    LinearLayout layoutLoader;

    AlertDialog alertDialog;
    View viewProducts;
    public UserHistoryFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_user_history, container, false);
        presenter = new UserHistoryPresenter(this);

        init(view);
        presenter.start();
        initFilter();
        return view;
    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        layoutLoader = view.findViewById(R.id.layoutLoader);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

    }
    private void initFilter(){
        viewProducts = LayoutInflater.from(getContext()).inflate(R.layout.layout_product_order,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ImageButton btnClose;

        btnClose = viewProducts.findViewById(R.id.btnClose);
        recyclerViewOrders = viewProducts.findViewById(R.id.recyclerViewProducts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewOrders.setLayoutManager(layoutManager);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        builder.setView(viewProducts);
        alertDialog = builder.create();
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
    public void orders(List<UserHistory> orders) {
        recyclerView.setAdapter(new HistoryCardAdapter(orders,this));
    }

    @Override
    public void products(List<ProductCart> cartList) {
        recyclerViewOrders.setAdapter(new ProductOrderAdapter(cartList));
        alertDialog.show();
    }

    @Override
    public void showProducts(String codeOrder) {
        presenter.showProducts(codeOrder);
    }
}