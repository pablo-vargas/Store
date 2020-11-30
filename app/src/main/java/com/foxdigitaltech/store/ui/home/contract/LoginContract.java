package com.foxdigitaltech.store.ui.home.contract;

public interface LoginContract {

    interface View{
        void showLoader();
        void hideLoader();

        void errorEmail(String error);
        void errorPassword(String error);

        void success();
        void hasError(String message);
    }
    interface Listener{
        void errorEmail(String error);
        void errorPassword(String error);

        void hasError(String message);
        void success();
    }

}
