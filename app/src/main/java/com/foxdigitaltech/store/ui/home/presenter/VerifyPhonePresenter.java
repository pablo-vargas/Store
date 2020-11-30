package com.foxdigitaltech.store.ui.home.presenter;

import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.contract.VerifyPhoneContract;
import com.foxdigitaltech.store.ui.home.interactor.VerifyPhoneInteractor;

public class VerifyPhonePresenter implements VerifyPhoneContract.Listener {


    VerifyPhoneContract.View view;
    VerifyPhoneInteractor interactor;

    public VerifyPhonePresenter(VerifyPhoneContract.View view) {
        this.view = view;
        interactor = new VerifyPhoneInteractor(this);
    }

    public void verifyPhone(String phoneNumber){
        view.showLoader();
        interactor.sendCodeVerificacion(phoneNumber);
    }
    public void start(HomeActivity homeActivity){
        view.showLoader();
        interactor.start(homeActivity);
    }
    public void verifySendCode(String verificationId, String code){
        view.showLoader();
        interactor.verifySendCode(verificationId,code);
    }

    @Override
    public void errorPhone(String error) {
        view.hideLoaderPhone();
        view.errorPhone(error);
    }

    @Override
    public void errorCode(String error) {
        view.hideLoaderCode();
        view.errorCode(error);
    }

    @Override
    public void phoneNumberValid() {
        view.hideLoaderCode();
        view.phoneNumberValid();
    }

    @Override
    public void sendCodeVerified() {
        view.hideLoaderCode();
        view.sendCodeVerified();
    }

    @Override
    public void verificationId(String verificationId) {
        view.verificationId(verificationId);
    }

    @Override
    public void successful() {
        view.successful();
    }

    @Override
    public void dataProfile(String phoneNumber) {
        view.hideLoaderPhone();
        view.dataProfile(phoneNumber);
    }

    @Override
    public void hasError(String message) {
        view.hasError(message);
    }
}
