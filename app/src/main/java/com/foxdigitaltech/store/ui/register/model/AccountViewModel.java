package com.foxdigitaltech.store.ui.register.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<String> verificationID;
    private MutableLiveData<String> viewFragment;
    private MutableLiveData<String> phoneNumber;

    public AccountViewModel() {
        verificationID = new MutableLiveData<>();
        viewFragment = new MutableLiveData<>();
        phoneNumber = new MutableLiveData<>();
    }

    public void setVerificationID(String verificationID){
        this.verificationID.postValue(verificationID);
    }
    public LiveData<String> verificationID(){
        return verificationID;
    }
    public void setViewFragment(String viewFragment){
        this.viewFragment.postValue(viewFragment);
    }
    public LiveData<String> viewFragment(){
        return viewFragment;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber.postValue(phoneNumber);
    }
    public LiveData<String> phoneNumber(){
        return this.phoneNumber;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
