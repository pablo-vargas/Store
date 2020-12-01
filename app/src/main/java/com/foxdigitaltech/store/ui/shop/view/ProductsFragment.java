package com.foxdigitaltech.store.ui.shop.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.CustomToast;
import com.foxdigitaltech.store.shared.model.Brand;
import com.foxdigitaltech.store.shared.model.Product;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.shared.model.ProductCharacteristics;
import com.foxdigitaltech.store.shared.model.ProductPropertyCart;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.view.adapter.ProductPropertyAdapter;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.foxdigitaltech.store.ui.shop.contract.ProductsContract;
import com.foxdigitaltech.store.ui.shop.presenter.ProductsPresenter;
import com.foxdigitaltech.store.ui.shop.view.adapter.ProductAllAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment implements ProductsContract.View, ProductAllAdapter.Listener{

    private HomeViewModel viewModel;
    private ProductsPresenter presenter;

    private LinearLayout layoutLoader;
    private RecyclerView recyclerView;
    private ProductAllAdapter productAdapter;


    View viewFilter;
    ChipGroup chipGroup;
    List<Brand> brandList;
    List<Product> products;
    AlertDialog alertDialog;



    DisplayMetrics metrics = new DisplayMetrics();
    public ProductsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_products, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        presenter = new ProductsPresenter(this);
        initFilter();
        setUpToolbar(view);
        init(view);

        presenter.start(viewModel.getCategory().getValue());
        return view;
    }
    private void init(View view){
        layoutLoader = view.findViewById(R.id.layoutLoader);
        recyclerView = view.findViewById(R.id.recyclerViewProducts);

        int columns = getActivity().getResources().getConfiguration().screenWidthDp/180;
        if(columns == 1){
            columns = 2;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),columns);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
    private void initFilter(){
        viewFilter = LayoutInflater.from(getContext()).inflate(R.layout.layout_product_filters,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        chipGroup = viewFilter.findViewById(R.id.chipGroup);
        final Spinner spinner = viewFilter.findViewById(R.id.spinner);
        MaterialButton btnCancel,btnAplicar;

        btnAplicar = viewFilter.findViewById(R.id.btnAplicar);
        btnCancel = viewFilter.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        chipGroup.setClickable(true);



        final List<String> listSpinner = new ArrayList<>();
        listSpinner.add("Default");
        listSpinner.add("Nombre");
        listSpinner.add("Precio");
        spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,listSpinner));

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               try {
                   String compare = "";
                   for (int pos : chipGroup.getCheckedChipIds()) {
                       compare += brandList.get(pos).getSlug() + " ";
                   }
                   presenter.filter(products, brandList, compare, spinner.getSelectedItem().toString());
                   alertDialog.cancel();
               }catch (IndexOutOfBoundsException e){
                   Log.d("ERRORCHIP",e.getMessage());
               }
            }
        });
        builder.setView(viewFilter);
        alertDialog = builder.create();

    }
    private void setUpToolbar(View view) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            toolbar.setTitle(viewModel.getCategory().getValue().getName());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.filter:
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
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
    public void products(List<Product> productList) {
        this.products = productList;
    }

    @Override
    public void brands(List<Brand> brandList) {
        this.brandList = brandList;
        int c = 0;
        for (Brand brand:brandList){
            Chip chip = new Chip(getContext());
            chip.setId(c);
            c++;
            chip.setText(brand.getName());
            chip.setCheckable(true);
            chipGroup.addView(chip);
        }
    }

    @Override
    public void setProductsFilter(List<Product> productList) {
        productAdapter = new ProductAllAdapter(productList,this);
        recyclerView.setAdapter( productAdapter);
    }

    @Override
    public void addCart(Product product) {
        if(viewModel.getUserAuth().getValue()){
            if(product.getBadge().equals("sale")){
                //Toast toast =  Toast.makeText(getContext(),R.string.product_sale,Toast.LENGTH_SHORT);
                Toast customToast = new CustomToast().custom(getContext(),"warning",product.getName()+" se ha agotado");
                customToast.setDuration(Toast.LENGTH_SHORT);
                //customToast.setGravity(Gravity.TOP|Gravity.RIGHT,0,0);
                customToast.show();
            }else if(product.getCharacteristics() != null){
                selectProperty(product);
            }else{
                presenter.addCart(new ProductCart(product,""));
                Toast customToast = new CustomToast().custom(getContext(),"success",product.getName()+" añadido al carrito correctamente");
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.show();
            }
        }else{
            signIn();
        }
    }
    private void selectProperty(Product productCart){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottombar_product_property,null);
        final List<ProductPropertyCart> list = new ArrayList<>();

        for (ProductCharacteristics productCharacteristics: productCart.getCharacteristics()){
            list.add(new ProductPropertyCart(false,new ProductCart(productCart,productCharacteristics.getValue())));
        }

        TextView name,price,property;
        final ImageView product;
        RecyclerView recyclerViewProperty;

        name = view.findViewById(R.id.textViewNameProduct);
        price = view.findViewById(R.id.textViewPrice);
        property = view.findViewById(R.id.textViewProperty);
        product = view.findViewById(R.id.imageViewProduct);
        recyclerViewProperty = view.findViewById(R.id.recyclerViewCharacteristic);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewProperty.setLayoutManager(layoutManager);

        recyclerViewProperty.setAdapter(new ProductPropertyAdapter(list));

        name.setText(productCart.getName());
        price.setText("Precio: Bs "+productCart.getPrice()+"0");
        property.setText("Seleccione: "+productCart.getCharacteristics().get(0).getSlug());

        Picasso.get().load(productCart.getImages().get(0)).into(product);

        MaterialButton btn_save;
        btn_save = view.findViewById(R.id.btnAddCart);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String addCart ="";
                for(ProductPropertyCart productPropertyCart: list){
                    if(productPropertyCart.isSelect()){
                        presenter.addCart(productPropertyCart.getProductCart());
                        addCart+=productPropertyCart.getProductCart().getName()+" - "+productPropertyCart.getProductCart().getProperty()+"\n";
                    }
                }
                if(!addCart.isEmpty()){
                    Toast customToast = new CustomToast().custom(getContext(),"success",addCart+"\nañadido al carrito correctamente");
                    customToast.setDuration(Toast.LENGTH_SHORT);
                    customToast.show();
                }else{
                    Toast customToast = new CustomToast().custom(getContext(),"error","Ningún producto seleccionado.");
                    customToast.setDuration(Toast.LENGTH_SHORT);
                    customToast.show();
                }
                bottomSheetDialog.cancel();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void signIn(){
        new MaterialAlertDialogBuilder(getContext(),  R.style.ThemeOverlay_App_MaterialAlertDialog)
                .setTitle("Iniciar sesión")
                .setIcon(R.drawable.ic_baseline_account_circle_24)
                .setMessage("Para añadir productos a su carrito \ndebe iniciar sesion")
                .setNegativeButton("Ahora no", null)
                .setPositiveButton("Iniciar sesión", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        ((HomeActivity)getActivity()).changeFragment(101,"main");

                    }
                })
                .create()
                .show();
    }
}