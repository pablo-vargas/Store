package com.foxdigitaltech.store.ui.home.contract;

public interface ProfileContract {
    interface View{
        void phoneVerified(boolean isValid);
        void signOut();
        void showEmail(String email);
    }
    interface Listener{
        void phoneVerified(boolean isValid);
        void signOut();
        void showEmail(String email);

    }

}
