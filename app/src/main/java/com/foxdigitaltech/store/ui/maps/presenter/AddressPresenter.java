package com.foxdigitaltech.store.ui.maps.presenter;

import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.ui.maps.contract.AddressContract;
import com.foxdigitaltech.store.ui.maps.interactor.AddressInteractor;

import java.util.List;

public class AddressPresenter implements AddressContract.Listener {

    private AddressContract.View view;
    private AddressInteractor interactor;

    public AddressPresenter(AddressContract.View view) {
        this.view = view;
        interactor = new AddressInteractor(this);
    }
    public void start(){
        view.showLoader();
        interactor.start();
    }
    public void delete(Address address){
        interactor.delete(address);
    }

    @Override
    public void hasError(String error) {
        view.hideLoader();
        view.hasError(error);
    }

    @Override
    public void addressList(List<Address> addresses) {
        view.hideLoader();
        view.addressList(addresses);
    }

    @Override
    public void success() {
        view.hideLoader();
        view.success();
    }
}
