package com.foxdigitaltech.store.ui.about.presenter;

import com.foxdigitaltech.store.ui.about.contract.ContactContract;
import com.foxdigitaltech.store.ui.about.interactor.ContactInteractor;

public class ContactPresenter implements ContactContract.Listener {

    private ContactInteractor interactor ;
    private ContactContract.View view;

    public ContactPresenter(ContactContract.View view) {
        this.view = view;
        interactor = new ContactInteractor(this);
    }

    public void sendMessage(String name,String email,String phone,String message){
        view.showLoader();
        interactor.sendMessage(name,email,phone,message);
    }

    @Override
    public void errorName(String error) {
        view.errorName(error);
    }

    @Override
    public void errorEmail(String error) {
        view.errorEmail(error);
    }

    @Override
    public void errorMessage(String error) {
        view.errorMessage(error);
    }

    @Override
    public void hasError(String error) {
        view.hideLoader();
        view.hasError(error);
    }

    @Override
    public void successful() {
        view.hideLoader();
        view.successful();
    }
}
