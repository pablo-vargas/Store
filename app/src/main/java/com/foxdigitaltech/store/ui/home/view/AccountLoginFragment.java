package com.foxdigitaltech.store.ui.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.contract.LoginContract;
import com.foxdigitaltech.store.ui.home.presenter.LoginPresenter;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.foxdigitaltech.store.ui.register.AccountRegisterActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


public class AccountLoginFragment extends Fragment implements LoginContract.View {

    LoginPresenter presenter;
    HomeViewModel viewModel;

    public AccountLoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    TextInputEditText textEmail,textPassword;
    RelativeLayout layoutView;
    LinearLayout layoutLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account_login, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        MaterialButton btnRegister = view.findViewById(R.id.btnCancel);
        MaterialButton btnLogin = view.findViewById(R.id.btnLogin);
        textEmail = view.findViewById(R.id.editTextEmail);
        textPassword = view.findViewById(R.id.editTextPassword);
        layoutLoader = view.findViewById(R.id.layoutLoader);
        layoutView = view.findViewById(R.id.layoutView);

        presenter = new LoginPresenter(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AccountRegisterActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.signIn(
                        textEmail.getText().toString(),
                        textPassword.getText().toString()
                );
            }
        });
        return view ;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!= null){
            ((HomeActivity)getActivity()).changeFragment(10,"main");
        }
    }

    @Override
    public void showLoader() {
        layoutLoader.setVisibility(View.VISIBLE);
        layoutView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoader() {
        layoutView.setVisibility(View.VISIBLE);
        layoutLoader.setVisibility(View.GONE);
    }

    @Override
    public void errorEmail(String error) {
        textEmail.setError(error);
    }

    @Override
    public void errorPassword(String error) {
        textPassword.setError(error);
    }

    @Override
    public void success() {
        viewModel.setUser(true);
        ((HomeActivity)getActivity()).changeFragment(10,"main");
    }

    @Override
    public void hasError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}