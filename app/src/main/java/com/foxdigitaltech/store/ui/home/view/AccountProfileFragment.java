package com.foxdigitaltech.store.ui.home.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.ui.home.HomeActivity;
import com.foxdigitaltech.store.ui.home.contract.ProfileContract;
import com.foxdigitaltech.store.ui.home.presenter.ProfilePresenter;
import com.foxdigitaltech.store.ui.home.viewmodel.HomeViewModel;
import com.foxdigitaltech.store.ui.notify.NotifyActivity;
import com.google.android.material.card.MaterialCardView;

public class AccountProfileFragment extends Fragment implements ProfileContract.View {

    MaterialCardView cardPhoneVerified,cardPhoneInvalid,cardProfile,cardAddress,cardRecord,cardExit,cardNotify;
    TextView textViewName;

    ProfilePresenter presenter;
    HomeViewModel viewModel;

    public AccountProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_profile, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(HomeViewModel.class);
        cardAddress = view.findViewById(R.id.cardAddress);
        cardExit = view.findViewById(R.id.cardExit);
        cardPhoneInvalid = view.findViewById(R.id.cardPhoneInvalid);
        cardPhoneVerified = view.findViewById(R.id.cardPhoneVerified);
        cardRecord = view.findViewById(R.id.cardRecord);
        cardProfile = view.findViewById(R.id.cardUpdateProfile);
        cardNotify = view.findViewById(R.id.cardNotify);

        textViewName = view.findViewById(R.id.textViewName);


        presenter = new ProfilePresenter(this);

        clickListener();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.verifiedPhone();
    }

    private void clickListener() {
        cardExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setUser(false);
                presenter.exit();

            }
        });
        cardPhoneInvalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).changeFragment(9,"profile");
            }
        });
        cardAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).changeFragment(11,"profile");
            }
        });
        cardRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getActivity()).changeFragment(14,"profile");
            }
        });
        cardNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), NotifyActivity.class));
            }
        });
    }

    @Override
    public void phoneVerified(boolean isValid) {
        if(isValid){
            cardPhoneVerified.setVisibility(View.VISIBLE);
            cardPhoneInvalid.setVisibility(View.GONE);
        }else{
            cardPhoneInvalid.setVisibility(View.VISIBLE);
            cardPhoneVerified.setVisibility(View.GONE);
        }
    }

    @Override
    public void signOut() {
        ((HomeActivity)getActivity()).changeFragment(101,"main");
    }

    @Override
    public void showEmail(String email) {
        textViewName.setText(email);
    }
}