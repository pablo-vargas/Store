package com.foxdigitaltech.store.ui.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> bottomNavigation;
    private MutableLiveData<Boolean> isLogged;

    public HomeViewModel(){
        bottomNavigation = new MutableLiveData<>();
        isLogged = new MutableLiveData<>();
    }

    public void setVisibilityBottomNavigation(int visibility){
        bottomNavigation.postValue(visibility);
    }
    public LiveData<Integer> getBottomNavigation(){ return bottomNavigation;}

    public void setUser(boolean isLogged){
        this.isLogged.postValue(isLogged);
    }
    public LiveData<Boolean> getUserAuth(){return this.isLogged;}

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
