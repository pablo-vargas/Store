package com.foxdigitaltech.store.ui.register.contract;

public interface RegisterContract {
    interface View{
        void showLoader();
        void hideLoader();
        void errorFirstName(String error);
        void errorLastName(String error);
        void errorPhone(String error);
        void errorEmail(String error);
        void errorPassword(String error);
        void success();
        void hasError(String message);
        void verificationID(String verificationId);
    }
    interface Listener{
        void errorFirstName(String error);
        void errorLastName(String error);
        void errorPhone(String error);
        void errorEmail(String error);
        void errorPassword(String error);
        void success();
        void hasError(String message);
        void verificationID(String verificationId);
    }
}
