package com.foxdigitaltech.store.ui.home.contract;

public interface HomeFragmentContract {
    interface View{
        void showLoader();
        void hideLoader();
        void hasError(String message);
    }

    interface Listener{
        void hasError(String message);
    }
}
