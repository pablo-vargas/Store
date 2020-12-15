package com.foxdigitaltech.store.ui.about.contract;

public interface ContactContract {
    interface View{
        void showLoader();
        void hideLoader();

        void errorName(String error);
        void errorEmail(String error);
        void errorMessage(String error);
        void hasError(String error);
        void successful();
    }

    interface Listener{
        void errorName(String error);
        void errorEmail(String error);
        void errorMessage(String error);
        void hasError(String error);
        void successful();
    }

}
