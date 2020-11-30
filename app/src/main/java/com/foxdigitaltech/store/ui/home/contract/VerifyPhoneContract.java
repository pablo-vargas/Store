package com.foxdigitaltech.store.ui.home.contract;

public interface VerifyPhoneContract {
    interface View{
        void errorPhone(String error);
        void errorCode(String error);

        void showLoader();
        void hideLoaderPhone();
        void hideLoaderCode();
        void phoneNumberValid();
        void sendCodeVerified();
        void verificationId(String verificationId);
        void successful();
        void dataProfile(String phoneNumber);
        void hasError(String message);

    }
    interface Listener{
        void errorPhone(String error);
        void errorCode(String error);
        void phoneNumberValid();
        void sendCodeVerified();
        void verificationId(String verificationId);
        void successful();
        void dataProfile(String phoneNumber);
        void hasError(String message);
    }
}
