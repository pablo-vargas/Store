package com.foxdigitaltech.store.ui.register.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.register.model.AccountViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationPhoneFragment extends Fragment {


    public VerificationPhoneFragment() {
        // Required empty public constructor
    }
    private int seconds = 59;
    private AccountViewModel accountViewModel;
    private String phone,verificationId;
    FirebaseAuth firebaseAuth;

    TextInputLayout textInputLayout;
    TextInputEditText textInputEditText;
    MaterialButton btnSendCode,btnVerificar;

    LinearLayout layoutView,layoutLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_verification_phone, container, false);

        textInputLayout = view.findViewById(R.id.code_text_input);
        textInputEditText= view.findViewById(R.id.editTextCodeVerification);
        btnSendCode = view.findViewById(R.id.btnBack);
        btnVerificar = view.findViewById(R.id.btnNext);
        layoutLoader = view.findViewById(R.id.layoutLoader);
        layoutView = view.findViewById(R.id.layoutView);

        firebaseAuth = FirebaseAuth.getInstance();

        accountViewModel = new ViewModelProvider(getActivity()).get(AccountViewModel.class);
        verificationId = accountViewModel.verificationID().getValue();
        phone = accountViewModel.phoneNumber().getValue();

        btnVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode(textInputEditText.getText().toString().trim());
            }
        });
        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyPhone();
            }
        });

        btnSendCode.setEnabled(false);

        verifiedSuccess();
        return  view;
    }

    private void sendCode(String code){
        layoutView.setVisibility(View.GONE);
        layoutLoader.setVisibility(View.VISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                layoutView.setVisibility(View.VISIBLE);
                layoutLoader.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    if(seconds <= 1){
                        getActivity().onBackPressed();
                    }
                }else{
                    Toast.makeText(getActivity(), "Error al verificar el código\nIntentelo nuevamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifiedSuccess(){
        FirebaseUser user  = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            if(user.getPhoneNumber() != null){
                getActivity().onBackPressed();
            }else{
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(seconds >0) {
                            seconds--;
                            textInputLayout.setHelperText("(591)"+phone+" "+seconds+" s.");
                            verifiedSuccess();
                        }
                        else {
                            btnSendCode.setEnabled(true);
                            textInputLayout.setHelperText("Tiempo Excedido.");
                        }
                        //verifiedListener.timeExceded();
                    }
                },1000);
            }
        }
    }
    private void verifyPhone(){
        AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
        aBuilder.setTitle("Enviar Código");
        aBuilder.setMessage("Verifica tu número");
        final EditText editText = new EditText(getActivity());
        editText.setText(phone);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        aBuilder.setView(editText);
        aBuilder.setIcon(R.drawable.ic_bolivia_icon);
        aBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                phone = editText.getText().toString().trim();
                if(phone.length()!=8){
                    Toast.makeText(getContext(), "Número inválido", Toast.LENGTH_SHORT).show();
                }else{
                    dialogInterface.dismiss();
                    sendCodeVerificacion(phone);
                }
            }
        });
        aBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        aBuilder.create().show();
    }
    private void sendCodeVerificacion(String phoneNumber){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+591"+phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(getActivity())
                .setCallbacks(callback())
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        seconds = 60;
        verifiedSuccess();
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback(){
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                firebaseAuth.getCurrentUser().linkWithCredential(phoneAuthCredential);
                Log.d("PHONECODE",phoneAuthCredential.toString());
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException)
                Toast.makeText(getContext(), "El número es inválido", Toast.LENGTH_SHORT).show();
                else if (e instanceof FirebaseTooManyRequestsException)
                    Toast.makeText(getContext(), "La cuota de código de verificación ha sido excedida.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String id, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(id, forceResendingToken);
                //token = forceResendingToken;
                //accountListener.sendCode(verificationId);
                Toast.makeText(getContext(), "Codigo enviado", Toast.LENGTH_SHORT).show();
                verificationId = id;
            }
        };
    }

}