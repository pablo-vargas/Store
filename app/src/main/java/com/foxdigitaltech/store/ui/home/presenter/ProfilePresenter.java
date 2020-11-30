package com.foxdigitaltech.store.ui.home.presenter;

import com.foxdigitaltech.store.ui.home.contract.ProfileContract;
import com.foxdigitaltech.store.ui.home.interactor.ProfileInteractor;

public class ProfilePresenter implements ProfileContract.Listener {

    ProfileContract.View view;
    ProfileInteractor interactor;

    public ProfilePresenter(ProfileContract.View view) {
        this.view = view;
        interactor = new ProfileInteractor(this);
    }
    public void verifiedPhone(){
        interactor.phoneVerified();
    }
    public void exit(){
        interactor.exit();
    }

    @Override
    public void phoneVerified(boolean isValid) {
        view.phoneVerified(isValid);
    }

    @Override
    public void signOut() {
        view.signOut();
    }

    @Override
    public void showEmail(String email) {
        view.showEmail(email);
    }
}
