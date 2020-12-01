package com.foxdigitaltech.store.ui.home.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.foxdigitaltech.store.shared.model.Category;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> bottomNavigation;
    private MutableLiveData<Boolean> isLogged;
    private MutableLiveData<Category> categoryMutableLiveData;

    public HomeViewModel(){
        bottomNavigation = new MutableLiveData<>();
        isLogged = new MutableLiveData<>();
        categoryMutableLiveData = new MutableLiveData<>();
    }

    public void setVisibilityBottomNavigation(int visibility){
        bottomNavigation.postValue(visibility);
    }
    public LiveData<Integer> getBottomNavigation(){ return bottomNavigation;}

    public void setUser(boolean isLogged){
        this.isLogged.postValue(isLogged);
    }
    public LiveData<Boolean> getUserAuth(){return this.isLogged;}

    public void setCategory(Category category){
        this.categoryMutableLiveData.postValue(category);
    }
    public LiveData<Category> getCategory(){
        return this.categoryMutableLiveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
