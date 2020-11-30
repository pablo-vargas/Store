package com.foxdigitaltech.store.ui.home.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.contract.VerifyPhoneContract;
import com.foxdigitaltech.store.ui.home.presenter.VerifyPhonePresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class AccountVerifyPhoneFragment extends Fragment implements VerifyPhoneContract.View {

    VerifyPhonePresenter phonePresenter ;


    public AccountVerifyPhoneFragment() {
        // Required empty public constructor
    }
    MaterialButton btnPhone,btnCode;
    LinearLayout layoutLoader;
    TextInputLayout inputCode,inputPhone;
    TextInputEditText textCode,textPhone;
    ImageButton btnBack;

    private String VerificationID="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account_verify_phone, container, false);
        btnBack = view.findViewById(R.id.btnBack);
        btnCode = view.findViewById(R.id.btnVerifyCode);
        btnPhone = view.findViewById(R.id.btnVerifyPhone);
        layoutLoader = view.findViewById(R.id.layoutLoader);
        inputCode = view.findViewById(R.id.inputCode);
        inputPhone = view.findViewById(R.id.inputPhone);
        textCode = view.findViewById(R.id.editTextCode);
        textPhone = view.findViewById(R.id.editTextPhone);


        phonePresenter = new VerifyPhonePresenter(this);
        phonePresenter.start((HomeActivity) getActivity());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonePresenter.verifyPhone(textPhone.getText().toString().trim());
            }
        });
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phonePresenter.verifySendCode(VerificationID,textCode.getText().toString().trim());
            }
        });


        return view;
    }

    @Override
    public void errorPhone(String error) {
        textPhone.setError(error);
    }

    @Override
    public void errorCode(String error) {
        textCode.setError(error);
    }

    @Override
    public void showLoader() {
        btnPhone.setVisibility(View.GONE);
        btnCode.setVisibility(View.GONE);
        layoutLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoaderPhone() {
        btnCode.setVisibility(View.GONE);
        btnPhone.setVisibility(View.VISIBLE);
        inputPhone.setVisibility(View.VISIBLE);
        inputCode.setVisibility(View.GONE);
        layoutLoader.setVisibility(View.GONE);
    }

    @Override
    public void hideLoaderCode() {
        btnCode.setVisibility(View.VISIBLE);
        inputCode.setVisibility(View.VISIBLE);
        btnPhone.setVisibility(View.GONE);
        inputPhone.setVisibility(View.GONE);
        layoutLoader.setVisibility(View.GONE);
    }


    @Override
    public void phoneNumberValid() {
        Toast.makeText(getContext(), "Código enviado correctamente", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendCodeVerified() {

    }

    @Override
    public void verificationId(String verificationId) {
        this.VerificationID = verificationId;
    }

    @Override
    public void successful() {
        ((HomeActivity)getActivity()).changeFragment(10,"main");
    }

    @Override
    public void dataProfile(String phoneNumber) {
        textPhone.setText(phoneNumber);
    }

    @Override
    public void hasError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}