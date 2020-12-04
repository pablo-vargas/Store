package com.foxdigitaltech.store.ui.maps.contract;

import com.foxdigitaltech.store.shared.model.Address;

import java.util.List;

public interface AddressContract {
    interface View{
        void showLoader();
        void hideLoader();
        void hasError(String error);
        void addressList(List<Address> addresses);
        void success();
    }
    interface Listener{
        void hasError(String error);
        void addressList(List<Address> addresses);
        void success();
    }
}
