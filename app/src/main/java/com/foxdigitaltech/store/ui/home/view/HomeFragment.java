package com.foxdigitaltech.store.ui.home.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.CustomToast;
import com.foxdigitaltech.store.ui.about.AboutActivity;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.contract.HomeFragmentContract;
import com.foxdigitaltech.store.shared.model.Category;
import com.foxdigitaltech.store.shared.model.Product;
import com.foxdigitaltech.store.shared.model.ProductCart;
import com.foxdigitaltech.store.shared.model.ProductCharacteristics;
import com.foxdigitaltech.store.shared.model.ProductPropertyCart;
import com.foxdigitaltech.store.ui.home.presenter.HomeFragmentPresenter;
import com.foxdigitaltech.store.ui.home.view.adapter.CategoryAdapter;
import com.foxdigitaltech.store.ui.home.view.adapter.ProductAdapter;
import com.foxdigitaltech.store.ui.home.view.adapter.ProductPropertyAdapter;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeFragmentContract.View, ProductAdapter.Listener,CategoryAdapter.Listener{

    //Loader
    RecyclerView recyclerView,recyclerViewOffres,recyclerViewBestSellers;
    CategoryAdapter categoryAdapter;
    ProductAdapter adapterOffers,adapterSales;
    HomeViewModel viewModel;
    TextView notFoundOffers;
    MaterialButton btnAbout;


    private List<Category> categoryList;
    private List<Product> listOffers,listBestSellers;

    public HomeFragment(List<Category> categoryList, List<Product> listOffers, List<Product> listBestSellers) {
        this.categoryList = categoryList;
        this.listOffers = listOffers;
        this.listBestSellers = listBestSellers;
    }

    private HomeFragmentPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        notFoundOffers = view.findViewById(R.id.no_offers_found);
        init(view);
        presenter = new HomeFragmentPresenter(this);
        viewModel  = new ViewModelProvider(getActivity()).get(HomeViewModel.class);

       // presenter.getData();


        return view;

    }

    private void init(View view) {
        recyclerView = view.findViewById(R.id.recycler_view_categorias);
        recyclerViewOffres = view.findViewById(R.id.recycler_view_offers);
        recyclerViewBestSellers = view.findViewById(R.id.recycler_view_bestSellers);
        btnAbout = view.findViewById(R.id.btnAbout);
        btnAbout.setOnClickListener(v->{
            getActivity().startActivity(new Intent(getContext(), AboutActivity.class));
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewBestSellers.setLayoutManager(layoutManager1);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewOffres.setLayoutManager(layoutManager2);
        categoryAdapter = new CategoryAdapter(categoryList,this);
        recyclerView.setAdapter(categoryAdapter);

        adapterOffers = new ProductAdapter(listOffers,this);
        adapterSales = new ProductAdapter(listBestSellers, this);

        recyclerViewOffres.setAdapter(adapterOffers);
        recyclerViewBestSellers.setAdapter(adapterSales);

        if(listOffers.size() > 0){
            recyclerViewOffres.setVisibility(View.VISIBLE);
            notFoundOffers.setVisibility(View.GONE);
        }else{
            recyclerViewOffres.setVisibility(View.GONE);
            notFoundOffers.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void showLoader() {
        //viewModel.setVisibilityBottomNavigation(View.GONE);
    }

    @Override
    public void hideLoader() {
        //viewModel.setVisibilityBottomNavigation(View.VISIBLE);
    }



    @Override
    public void hasError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void addCart(Product product) {
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            if(product.getBadge().equals("sale")){
                Toast customToast = new CustomToast().custom(getContext(),"warning",product.getName()+" se ha agotado");
                customToast.setDuration(Toast.LENGTH_SHORT);
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

    @Override
    public void clickCategory(Category category) {
        viewModel.setCategory(category);
        ((HomeActivity)getActivity()).changeFragment(12,"main");
    }
}