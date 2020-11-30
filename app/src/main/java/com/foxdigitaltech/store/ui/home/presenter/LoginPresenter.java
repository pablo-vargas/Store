package com.foxdigitaltech.store.ui.home.presenter;

import com.foxdigitaltech.store.ui.home.contract.LoginContract;
import com.foxdigitaltech.store.ui.home.interactor.LoginInteractor;

public class LoginPresenter implements LoginContract.Listener {

    private LoginContract.View view;
    private LoginInteractor interactor;


    public LoginPresenter(LoginContract.View view) {
        this.view = view;
        interactor = new LoginInteractor(this);
    }

    public void signIn(String email,String password){
        view.showLoader();
        interactor.signIn(email,password);
    }

    @Override
    public void errorEmail(String error) {
        view.errorEmail(error);
    }

    @Override
    public void errorPassword(String error) {
        view.errorPassword(error);
    }

    @Override
    public void hasError(String message) {
        view.hideLoader();
        view.hasError(message);
    }

    @Override
    public void success() {
        view.hideLoader();
        view.success();
    }
}
