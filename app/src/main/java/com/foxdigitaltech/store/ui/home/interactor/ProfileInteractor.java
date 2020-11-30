package com.foxdigitaltech.store.ui.home.interactor;

import android.util.Log;

import com.foxdigitaltech.store.ui.home.contract.ProfileContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileInteractor {
    private ProfileContract.Listener callBack;
    private FirebaseAuth firebaseAuth;

    public ProfileInteractor(ProfileContract.Listener callBack) {
        this.callBack = callBack;
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void phoneVerified(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.getPhoneNumber() != null ){
            if(!user.getPhoneNumber().isEmpty()){
                callBack.phoneVerified(true);
            }
            else{
                callBack.phoneVerified(false);
            }
        }else{
            callBack.phoneVerified(false);
        }
        callBack.showEmail(user.getEmail());
    }
    public void exit(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        callBack.signOut();
    }

}
