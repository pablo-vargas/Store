package com.foxdigitaltech.store.ui.shop.view;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.shop.contract.CartContract;
import com.foxdigitaltech.store.ui.shop.presenter.CartPresenter;
import com.foxdigitaltech.store.ui.shop.view.adapter.ShoppingCartAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements ShoppingCartAdapter.Listener, CartContract.View {

    CartPresenter presenter;

    RecyclerView recyclerView;
    ShoppingCartAdapter cartAdapter;
    LinearLayout layoutLoader;
    TextView cartEmpty;
    MaterialButton btnCheckOrder;

    List<Address> addressList;
    View viewCheckOrder;
    BottomSheetDialog bottomSheetDialog;
    Spinner spinnerAddress;
    TextView count,subTotal,total,delivery;
    MaterialButton btnAddAddress,btnOrder;
    ProgressBar checkLoader;
    Location locationStore = new Location("Punto A");
    Location locationDelivery = new Location("Punto B");

    //COORDENADAS A CALCULAR ,
    //PAGE
    public CartFragment() {
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
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);
        locationStore.setLatitude(-19.05163);
        locationStore.setLongitude(-65.26676);
        init(view);
        initCheckOrder();
        presenter = new CartPresenter(this);
        presenter.start();
        return view;
    }
    private void init(View view){
        recyclerView = view.findViewById(R.id.recyclerViewCart);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        layoutLoader = view.findViewById(R.id.layoutLoader);
        cartEmpty = view.findViewById(R.id.textViewCartEmpty);
        btnCheckOrder = view.findViewById(R.id.materialButton);
        btnCheckOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartAdapter.get().size() > 0){
                    calculatePrice();
                }else{
                    checkCart();
                }
            }
        });
    }
    private void initCheckOrder(){
        bottomSheetDialog = new BottomSheetDialog(getContext());
        viewCheckOrder = LayoutInflater.from(getContext()).inflate(R.layout.bottomshet_check_order,null);
        spinnerAddress = viewCheckOrder.findViewById(R.id.spinner_address);
        btnOrder = viewCheckOrder.findViewById(R.id.btnCheckOrder);
        btnAddAddress = viewCheckOrder.findViewById(R.id.btnAddressAdd);
        checkLoader = viewCheckOrder.findViewById(R.id.checkOrderLoader);
        count = viewCheckOrder.findViewById(R.id.textViewCountProduct);
        subTotal = viewCheckOrder.findViewById(R.id.textViewSubTotal);
        total = viewCheckOrder.findViewById(R.id.textViewTotal);
        delivery = viewCheckOrder.findViewById(R.id.textViewPriceDelivery);

        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).changeFragment(11,"cart");
                bottomSheetDialog.cancel();
            }
        });

        spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Double subTotalPrice = Double.valueOf(subTotal.getText().toString());
                locationDelivery.setLatitude(addressList.get(spinnerAddress.getSelectedItemPosition()).getStreetLatitude());
                locationDelivery.setLongitude(addressList.get(spinnerAddress.getSelectedItemPosition()).getStreetLongitude());
                float distance = locationStore.distanceTo(locationDelivery);
                if(distance <= 600){
                    delivery.setText("5");
                    total.setText((subTotalPrice+5)+"0");
                }else if(distance > 600 && distance <=2000){
                    delivery.setText("8");
                    total.setText((subTotalPrice+8)+"0");
                }else if(distance >2000 && distance <3500){
                    delivery.setText("12");
                    total.setText((subTotalPrice+12)+"0");
                }else {
                    delivery.setText("15");
                    total.setText((subTotalPrice+15)+"0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void remove(String key) {
        presenter.removeProduct(key);
    }

    @Override
    public void addSubProduct(ProductCart productCart,int value) {
        presenter.addSubProduct(productCart,value);
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
    public void products(List<ProductCart> list) {
        cartAdapter = new ShoppingCartAdapter(list,this);
        recyclerView.setAdapter(cartAdapter);
        checkCart();
    }

    @Override
    public void address(List<Address> addresses) {

        addressList = addresses;
        if(addresses.size()> 0){
            spinnerAddress.setVisibility(View.VISIBLE);
            btnAddAddress.setVisibility(View.GONE);
            List<String> list = new ArrayList<>();
            for(Address address : addresses){
                list.add(address.getName());
            }
            spinnerAddress.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list));
        }else{
            spinnerAddress.setVisibility(View.GONE);
            btnAddAddress.setVisibility(View.VISIBLE);
        }
    }

    private void checkCart(){
        if (cartAdapter.get().size() <=0){
            cartEmpty.setVisibility(View.VISIBLE);
            btnCheckOrder.setVisibility(View.GONE);
        }else{
            cartEmpty.setVisibility(View.GONE);
            btnCheckOrder.setVisibility(View.VISIBLE);
        }
    }

    private void calculatePrice(){

        Double subTotalPrice = 0d;
        int countProduct =0 ;
        for (ProductCart productCart: cartAdapter.get()){
            subTotalPrice += (productCart.getPrice() * productCart.getQuantity());
            countProduct += productCart.getQuantity();
        }
        count.setText(countProduct+"");
        subTotal.setText(subTotalPrice+"0");
        total.setText((subTotalPrice+10)+"0");

        if(addressList.size() > 0) {
            btnOrder.setEnabled(true);
            locationDelivery.setLatitude(addressList.get(spinnerAddress.getSelectedItemPosition()).getStreetLatitude());
            locationDelivery.setLongitude(addressList.get(spinnerAddress.getSelectedItemPosition()).getStreetLongitude());
            float distance = locationStore.distanceTo(locationDelivery);

            if(distance <= 600){
                delivery.setText("5");
                total.setText((subTotalPrice+5)+"0");
            }else if(distance > 600 && distance <=2000){
                delivery.setText("8");
                total.setText((subTotalPrice+8)+"0");
            }else if(distance >2000 && distance <3500){
                delivery.setText("12");
                total.setText((subTotalPrice+12)+"0");
            }else {
                delivery.setText("15");
                total.setText((subTotalPrice+15)+"0");
            }
        }else{
            btnOrder.setEnabled(false);
        }

        bottomSheetDialog.setContentView(viewCheckOrder);
        bottomSheetDialog.show();
    }
}