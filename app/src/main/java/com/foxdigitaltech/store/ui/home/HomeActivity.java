package com.foxdigitaltech.store.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.account.view.LoginFragment;
import com.foxdigitaltech.store.ui.account.view.RegisterFragment;
import com.foxdigitaltech.store.ui.home.contract.HomeContract;
import com.foxdigitaltech.store.ui.home.model.Category;
import com.foxdigitaltech.store.ui.home.model.Product;
import com.foxdigitaltech.store.ui.home.presenter.HomePresenter;
import com.foxdigitaltech.store.ui.home.view.AccountAddressFragment;
import com.foxdigitaltech.store.ui.home.view.AccountLoginFragment;
import com.foxdigitaltech.store.ui.home.view.AccountProfileFragment;
import com.foxdigitaltech.store.ui.home.view.AccountVerifyPhoneFragment;
import com.foxdigitaltech.store.ui.home.view.HomeFragment;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeContract.View {

    HomeViewModel viewModel;

    HomePresenter presenter;

    RelativeLayout layoutLoader;
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
        layoutLoader = findViewById(R.id.layoutLoader);
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


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.navigation_home:
                        fragmentManager.popBackStack();
                        fragmentManager.beginTransaction().replace(R.id.content_view,homeFragment,"main").commit();
                        break;
                    case R.id.navigation_cart:
                        fragmentManager.beginTransaction().replace(R.id.content_view,new RegisterFragment()).addToBackStack("main").commit();
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
            }
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
    public void listBestSellers(List<Product> products) {
        listBestSellers = products;
    }

    @Override
    public void verifyAccount(boolean flag) {
        viewModel.setUser(flag);
    }

    /**
     *
     * @param fragment view Fragment
     *
     *                 9: Verify Phone
     *                 10: Profile
     *                 11: Address
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
            case 101:
                fragmentManager.popBackStack();
                fragmentManager.beginTransaction().replace(R.id.content_view,accountLoginFragment,"login").commit();
                break;
        }
    }
}