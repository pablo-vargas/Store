package com.foxdigitaltech.store.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.account.view.RegisterFragment;
import com.foxdigitaltech.store.ui.register.model.AccountViewModel;
import com.foxdigitaltech.store.ui.register.view.AccountRegisterFragment;
import com.foxdigitaltech.store.ui.register.view.VerificationPhoneFragment;

public class AccountRegisterActivity extends AppCompatActivity {
    FragmentManager fragmentManager;
    AccountRegisterFragment registerFragment;

    AccountViewModel accountViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);
        fragmentManager = this.getSupportFragmentManager();
        registerFragment = new AccountRegisterFragment();

        fragmentManager.beginTransaction().replace(R.id.content_view,registerFragment).commit();

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        changeFragment();
    }

    private void changeFragment(){
        accountViewModel.viewFragment().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                switch (s){
                    case "VERIFICATION":
                        fragmentManager.beginTransaction().replace(R.id.content_view,new VerificationPhoneFragment()).commit();
                        break;
                    case "REGISTER":
                        fragmentManager.beginTransaction().replace(R.id.content_view,registerFragment).commit();
                        break;
                }
            }
        });
    }

}