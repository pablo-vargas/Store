package com.foxdigitaltech.store.ui.home.interactor;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.contract.VerifyPhoneContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneInteractor {

    private    VerifyPhoneContract.Listener callBack;
    private DatabaseReference databaseReference;
    private HomeActivity homeActivity;
    private FirebaseAuth firebaseAuth;

    public VerifyPhoneInteractor(VerifyPhoneContract.Listener callBack) {
        this.callBack = callBack;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }
    public  void start(HomeActivity homeActivity){
        this.homeActivity = homeActivity;
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    callBack.dataProfile(snapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        databaseReference.child("users/client").child(firebaseAuth.getCurrentUser().getUid()).child("phoneNumber").addListenerForSingleValueEvent(valueEventListener);
    }


    public void sendCodeVerificacion(String phoneNumber){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+591"+phoneNumber)
                .setTimeout(100L, TimeUnit.SECONDS)
                .setActivity(homeActivity)
                .setCallbacks(callback(phoneNumber))
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback(final String phoneNumber){
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                firebaseAuth.getCurrentUser().linkWithCredential(phoneAuthCredential);
                databaseReference.child("users/client").child(firebaseAuth.getCurrentUser().getUid()).child("phoneNumer").setValue(phoneNumber);
                callBack.successful();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                if (e instanceof FirebaseAuthInvalidCredentialsException)
                    callBack.errorPhone("El número es inválido");
                else if (e instanceof FirebaseTooManyRequestsException)
                    callBack.errorPhone("La cuota de código de verificación ha sido excedida.");
                else
                    callBack.errorPhone(e.getMessage());
                callBack.hasError("Error al verificar el teléfono.");
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                //token = forceResendingToken;
                //accountListener.sendCode(verificationId);
                callBack.verificationId(verificationId);
                callBack.phoneNumberValid();
            }
        };
    }
    public void verifySendCode(String verificationId, String code){

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   callBack.successful();
                }else{
                    callBack.errorCode("Error al verificar el código\nIntentelo nuevamente.");
                }
            }
        });
    }
}
