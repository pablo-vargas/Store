package com.foxdigitaltech.store.ui.register.presenter;

import com.foxdigitaltech.store.ui.register.AccountRegisterActivity;
import com.foxdigitaltech.store.ui.register.contract.RegisterContract;
import com.foxdigitaltech.store.ui.register.interactor.RegisterInteractor;

public class RegisterPresenter implements RegisterContract.Listener {
    private RegisterContract.View view;
    private RegisterInteractor interactor;


    public RegisterPresenter(RegisterContract.View view) {
        this.view = view;
        interactor = new RegisterInteractor(this);
    }


    public void validateRegister(String firstName,String lastName,String phone,String email,String password){
        this.view.showLoader();
        this.interactor.validateRegister(firstName,lastName,phone,email,password);
    }
    public void setAccountRegisterActivity(AccountRegisterActivity accountRegisterActivity) {
        this.interactor.setAccountRegisterActivity(accountRegisterActivity);
    }


    @Override
    public void errorFirstName(String error) {
        view.errorFirstName(error);
    }

    @Override
    public void errorLastName(String error) {
        view.errorLastName(error);
    }

    @Override
    public void errorPhone(String error) {
        view.errorPhone(error);
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
    public void success() {
        view.hideLoader();
        view.success();
    }

    @Override
    public void hasError(String message) {
        view.hideLoader();
        view.hasError(message);
    }

    @Override
    public void verificationID(String verificationId) {
        view.verificationID(verificationId);
        view.success();
    }
}
