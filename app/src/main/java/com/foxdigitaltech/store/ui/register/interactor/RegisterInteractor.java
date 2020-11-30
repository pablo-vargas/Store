package com.foxdigitaltech.store.ui.register.interactor;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.ui.register.AccountRegisterActivity;
import com.foxdigitaltech.store.ui.register.contract.RegisterContract;
import com.foxdigitaltech.store.ui.register.model.UserProfile;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RegisterInteractor {

    private RegisterContract.Listener callBack;
    private FirebaseAuth firebaseAuth;



    private AccountRegisterActivity accountRegisterActivity;
    public void setAccountRegisterActivity(AccountRegisterActivity accountRegisterActivity) {
        this.accountRegisterActivity = accountRegisterActivity;
    }
    public RegisterInteractor(RegisterContract.Listener listener) {
        this.callBack = listener;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void validateRegister(String firstName,String lastName,String phone,String email,String password){
        Boolean flag = false;
        if(firstName.trim().isEmpty()){
            callBack.errorFirstName("Este campo no puede ir vacío.");
            flag = true;
        }
        if(lastName.trim().isEmpty()){
            callBack.errorLastName("Este campo no puede ir vacío.");
            flag = true;
        }
        if(phone.trim().isEmpty() || phone.trim().length() < 8){
            callBack.errorPhone("Inválido.");
            flag = true;
        }
        if(email.trim().isEmpty()) {
            callBack.errorEmail("Este campo no puede ir vacío.");
            flag = true;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
            callBack.errorEmail("El formato del correo no es válido.");
            flag = true;
        }
        if(password.trim().length() < 8){
            callBack.errorPassword("Mínimo 8 caracteres");
            flag = true;
        }
        if(flag){
            callBack.hasError("Formulario inválido.");
        }else{
            UserProfile userProfile = new UserProfile(firstName,lastName,phone,email);
            userProfile.setCreateAt(new Date().getTime());
            userProfile.setUpdateAt(new Date().getTime());
            singUpEmail(userProfile,password);
        }
    }


    private void singUpEmail(final UserProfile userProfile, String password){
        firebaseAuth.createUserWithEmailAndPassword(userProfile.getEmail(),password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("/users/client/"+task.getResult().getUser().getUid()).setValue(userProfile);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(userProfile.getFirstName()+" "+userProfile.getLastName())
                            .build();
                    FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                    sendCodeVerificacion(userProfile.getPhoneNumber());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if(e.getMessage().equals("The email address is badly formatted.")){
                    callBack.errorEmail("Por favor introduzca un correo válido.");
                }
                else if(e.getMessage().equals("The email address is already in use by another account.")){
                    callBack.errorEmail("El correo que ingreso ya esta en uso\nPor favor use otro email.");

                }else{
                    callBack.errorEmail("Por favor revise su conexion a internet.");
                }
                callBack.hasError("Error de email.");
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                callBack.hasError("Por favor revise su conexion a internet.");
            }
        });
    }
    private void sendCodeVerificacion(String phoneNumber){
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber("+591"+phoneNumber)
                .setTimeout(60L,TimeUnit.SECONDS)
                .setActivity(accountRegisterActivity)
                .setCallbacks(callback())
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
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
                callBack.verificationID(verificationId);
            }
        };
    }






}
