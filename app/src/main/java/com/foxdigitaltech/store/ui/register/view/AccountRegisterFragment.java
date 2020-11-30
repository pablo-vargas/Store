package com.foxdigitaltech.store.ui.register.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.register.AccountRegisterActivity;
import com.foxdigitaltech.store.ui.register.contract.RegisterContract;
import com.foxdigitaltech.store.ui.register.model.AccountViewModel;
import com.foxdigitaltech.store.ui.register.presenter.RegisterPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AccountRegisterFragment extends Fragment implements RegisterContract.View {

    RegisterPresenter presenter;

    public AccountRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    TextInputEditText textFirstName,textLastName,textPhone,textEmail,textPassword;
    MaterialButton cancel,next;
    LinearLayout layoutView,layoutLoader;

    AccountViewModel accountViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_register, container, false);
        textFirstName = view.findViewById(R.id.editTextFirsName);
        textLastName = view.findViewById(R.id.editTextLastName);
        textPhone = view.findViewById(R.id.editTextPhone);
        textEmail = view.findViewById(R.id.editTextEmail);
        textPassword = view.findViewById(R.id.editTextPassword);

        layoutLoader = view.findViewById(R.id.layoutLoader);
        layoutView = view.findViewById(R.id.layoutView);

        cancel = view.findViewById(R.id.btnCancel);
        next = view.findViewById(R.id.btnNext);

        presenter = new RegisterPresenter(this);
        presenter.setAccountRegisterActivity((AccountRegisterActivity) getActivity());
        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.validateRegister(
                        textFirstName.getText().toString(),
                        textLastName.getText().toString(),
                        textPhone.getText().toString(),
                        textEmail.getText().toString(),
                        textPassword.getText().toString()
                );
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

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
    public void errorFirstName(String error) {
        textFirstName.setError(error);
    }

    @Override
    public void errorLastName(String error) {
        textLastName.setError(error);
    }

    @Override
    public void errorPhone(String error) {
        textPhone.setError(error);
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
        accountViewModel.setViewFragment("VERIFICATION");
    }

    @Override
    public void hasError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void verificationID(String verificationId) {
        accountViewModel.setPhoneNumber(textPhone.getText().toString().trim());
        accountViewModel.setVerificationID(verificationId);
    }
}