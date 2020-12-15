package com.foxdigitaltech.store.ui.about.interactor;

import android.os.Handler;
import android.os.Looper;
import android.util.Patterns;

import com.foxdigitaltech.store.ui.about.Contact;
import com.foxdigitaltech.store.ui.about.contract.ContactContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class ContactInteractor {
    private ContactContract.Listener listener;

    public ContactInteractor(ContactContract.Listener listener) {
        this.listener = listener;
    }

    public void sendMessage(String name,String email,String phone,String message){
        if(isValid(name,email,message)){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            Contact contact = new Contact(name,email,phone,message);
            if(user != null){
                contact.setEmail(user.getEmail());
                contact.setName(user.getDisplayName());
                contact.setUid(user.getUid());
            }
            FirebaseDatabase.getInstance().getReference("/contact").push().setValue(contact).addOnCompleteListener(task -> {
               if(task.isSuccessful()){
                   listener.successful();
               }else
                   listener.hasError("No se pudo enviar el mensaje");
            });

        }else{
            new Handler(Looper.getMainLooper()).postDelayed(() -> listener.hasError("Error al enviar el mensaje."),800);
        }
    }

    private boolean isValid(String name,String email,String message){
        boolean valid = true;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            if(name.trim().isEmpty()){
                listener.errorName("Este campo no puede ir vacío.");
                valid = false;
            }else if (!Pattern.matches("^[a-zA-Z _]*$",name)){
                listener.errorName("El nombre solo puede contener letras");
                valid = false;
            }

            if(email.trim().isEmpty()) {
                listener.errorEmail("Este campo no puede ir vacío.");
                valid = false;
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()){
                listener.errorEmail("El formato del correo no es válido.");
                valid = false;
            }

        }
        if(message.trim().length() < 6){
            listener.errorMessage("Mensaje demasiado corto");
            valid = false;
        }
        return valid;
    }
}
