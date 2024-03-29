package com.foxdigitaltech.store.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.model.PriceDelivery;
import com.foxdigitaltech.store.ui.account.view.UserHistoryFragment;
import com.foxdigitaltech.store.ui.home.contract.HomeContract;
import com.foxdigitaltech.store.shared.model.Category;
import com.foxdigitaltech.store.shared.model.Product;
import com.foxdigitaltech.store.ui.home.presenter.HomePresenter;
import com.foxdigitaltech.store.ui.maps.view.AccountAddressFragment;
import com.foxdigitaltech.store.ui.home.view.AccountLoginFragment;
import com.foxdigitaltech.store.ui.home.view.AccountProfileFragment;
import com.foxdigitaltech.store.ui.home.view.AccountVerifyPhoneFragment;
import com.foxdigitaltech.store.ui.home.view.HomeFragment;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.foxdigitaltech.store.ui.shop.view.CartFragment;
import com.foxdigitaltech.store.ui.shop.view.ProductsFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    HomeViewModel viewModel;

    HomePresenter presenter;

    LottieAnimationView layoutLoader;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment;

    private List<Category> categoryList;
    private List<Product> listOffers,listBestSellers;
    FragmentManager fragmentManager;

    //Fragments
    AccountLoginFragment accountLoginFragment;
    AccountProfileFragment accountProfileFragment;
    AccountAddressFragment accountAddressFragment;
    AccountVerifyPhoneFragment accountVerifyPhoneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.nav_view);
        layoutLoader = findViewById(R.id.animation_view);
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.navigation_cart);
        badge.setVisible(true);
        accountLoginFragment = new AccountLoginFragment();
        accountProfileFragment = new AccountProfileFragment();
        accountAddressFragment = new AccountAddressFragment();
        accountVerifyPhoneFragment = new AccountVerifyPhoneFragment();

        presenter = new HomePresenter(this);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        presenter.init();
        presenter.verifyAccount();


        fragmentManager = this.getSupportFragmentManager();

        viewModel.getBottomNavigation().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                bottomNavigationView.setVisibility(integer);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            switch (id){
                case R.id.navigation_home:
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction().replace(R.id.content_view,homeFragment,"main").commit();
                    break;
                case R.id.navigation_cart:
                    changeFragment(13,"main");
                    break;
                case R.id.navigation_account:
                    //
                    if(viewModel.getUserAuth().getValue()){
                        fragmentManager.beginTransaction().replace(R.id.content_view,accountProfileFragment,"profile").addToBackStack("main").commit();
                    }else{
                        fragmentManager.beginTransaction().replace(R.id.content_view,accountLoginFragment,"login").addToBackStack("main").commit();
                    }
                    break;
            }
            return true;
        });
    }

    @Override
    public void showLoader() {
        layoutLoader.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoader() {
        layoutLoader.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void successData() {
        List<Product> sales = new ArrayList<>();
         for (int i =listBestSellers.size()-1; i>=0 ; i--){
            sales.add(listBestSellers.get(i));
        }
        homeFragment = new HomeFragment(categoryList,listOffers,sales);
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content_view,homeFragment,"main");
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void listCategories(List<Category> categories) {
        this.categoryList = categories;
    }

    @Override
    public void listOffers(List<Product> products) {
        listOffers = products;
    }

    @Override
    public void listPrice(List<PriceDelivery> priceDeliveries) {
        viewModel.serPrice(priceDeliveries);
    }

    @Override
    public void listBestSellers(List<Product> products) {
        listBestSellers = products;
    }

    @Override
    public void verifyAccount(boolean flag) {
        viewModel.setUser(flag);
    }

    @Override
    public void isVersion(boolean valid) {
        if(!valid){
            new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert)
                    .setIcon(R.drawable.ic_baseline_update_24)
                    .setTitle("Actualización")
                    .setMessage("Existe una actualización disponible.")
                    .setPositiveButton("Actualizar", (dialogInterface, i) -> {
                        Intent intentNavegador = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()));
                        startActivity(intentNavegador);
                    })
                    .setCancelable(false)
                    .create().show();
        }
    }

    /**
     *
     * @param fragment view Fragment
     *
     *                 9: Verify Phone
     *                 10: Profile
     *                 11: Address
     *                 12: Products Category
     *                 13: Cart
     *                 14: Order History
     *                 101: Login
     * @param tag backStack
     */
    public void changeFragment(int fragment,String tag){
        switch (fragment){
            case 9:
                fragmentManager.beginTransaction().replace(R.id.content_view,accountVerifyPhoneFragment,"verify_phone").addToBackStack(tag).commit();
                break;
            case 10:
                fragmentManager.beginTransaction().replace(R.id.content_view,accountProfileFragment,"profile").addToBackStack(tag).commit();
                break;
            case 11:
                fragmentManager.beginTransaction().replace(R.id.content_view,accountAddressFragment,"address").addToBackStack(tag).commit();
                break;
            case 12:
                fragmentManager.beginTransaction().replace(R.id.content_view,new ProductsFragment(),"products").addToBackStack(tag).commit();
                break;
            case 13:
                fragmentManager.beginTransaction().replace(R.id.content_view,new CartFragment(),"cart").addToBackStack(tag).commit();
                break;
            case 14:
                fragmentManager.beginTransaction().replace(R.id.content_view,new UserHistoryFragment(),"order").addToBackStack(tag).commit();
                break;
            case 101:
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.content_view,accountLoginFragment,"login").commit();
                break;
        }
    }
}