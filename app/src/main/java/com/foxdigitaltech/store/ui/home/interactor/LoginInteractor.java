package com.foxdigitaltech.store.ui.home.interactor;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.foxdigitaltech.store.ui.home.contract.LoginContract;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.Objects;


public class LoginInteractor {

    private LoginContract.Listener callBack;
    private FirebaseAuth firebaseAuth;

    public LoginInteractor(LoginContract.Listener callBack) {
        this.callBack = callBack;
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void signIn(String email,String password){
        Boolean flag = false;

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
            signInFirebase(email,password);
        }
    }


    private void signInFirebase(String email,String password){

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference("/users/client").child(task.getResult().getUser().getUid()).child("updateAt").setValue(new Date().getTime());
                    callBack.success();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String error="";
                switch (Objects.requireNonNull(e.getMessage())) {
                    case "The password is invalid or the user does not have a password.":
                        error ="La contraseña es incorrecta";
                        callBack.errorPassword(error);
                        break;
                    case "There is no user record corresponding to this identifier. The user may have been deleted.":
                        error= "No hay ningún registro de usuario que corresponda a este email.";
                        callBack.errorEmail(error);
                        break;
                    case "The user account has been disabled by an administrator.":
                        error="Esta cuenta ha sido bloqueada por el administrador.";
                        break;
                }
                callBack.hasError(error);

            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                callBack.hasError("Por favor revise su conexion a internet.");
            }
        });

    }
}
