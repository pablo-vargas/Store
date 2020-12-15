package com.foxdigitaltech.store.ui.about.view;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.CustomToast;
import com.foxdigitaltech.store.ui.about.contract.ContactContract;
import com.foxdigitaltech.store.ui.about.presenter.ContactPresenter;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ContactFragment extends Fragment implements OnMapReadyCallback, ContactContract.View {


    TextInputEditText textName,textEmail,textPhone,textMessage;
    MaterialButton btnSend;
    ProgressBar progressBar;
    ImageButton btnFacebook,btnWhatsApp;

    TextView privacy;

    private ContactPresenter presenter;

    public ContactFragment() {
        // Required empty public constructor
    }
    private GoogleMap map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_contact, container, false);
        SupportMapFragment sMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(view.findViewById(R.id.map).getId());
        sMapFragment.getMapAsync(this);
        presenter = new ContactPresenter(this);
        init(view);
        return  view;
    }

    private void init(View view){
        textEmail = view.findViewById(R.id.editTextContactEmail);
        textMessage = view.findViewById(R.id.editTextMessage);
        textPhone = view.findViewById(R.id.editTextPhone);
        textName = view.findViewById(R.id.editTextContactName);
        btnSend = view.findViewById(R.id.btnSend);
        progressBar = view.findViewById(R.id.layoutLoader);
        btnFacebook = view.findViewById(R.id.btnFacebook);
        btnWhatsApp = view.findViewById(R.id.btnWhatsApp);
        privacy = view.findViewById(R.id.textViewPrivacy);
        privacy.setOnClickListener(view1 -> {

            Uri uri = Uri.parse("https://foxdigital-store.web.app/politicas-de-privacidad.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        btnFacebook.setOnClickListener(v->{
            Uri uri = Uri.parse("https://www.facebook.com/ZinZaliSucre/");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            try{
                startActivity(intent);
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        btnWhatsApp.setOnClickListener(v->{

            Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=59178675978");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        btnSend.setOnClickListener(view1 -> {
            presenter.sendMessage(textName.getText().toString(),textEmail.getText().toString(),textPhone.getText().toString(),textMessage.getText().toString());
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(true);
        LatLng latLng = new LatLng(-19.05166, -65.26678);
        map.addMarker(new MarkerOptions().position(latLng).title("Zinzali"));

        map.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(latLng)
                        .zoom(15f)
                        .build()
        ));

    }

    @Override
    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
        btnSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void errorName(String error) {
        textName.setError(error);
    }

    @Override
    public void errorEmail(String error) {
        textEmail.setError(error);
    }

    @Override
    public void errorMessage(String error) {
        textMessage.setError(error);
    }

    @Override
    public void hasError(String error) {
        Toast toast = new CustomToast().custom(getContext(),"error",error);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void successful() {
        Toast toast = new CustomToast().custom(getContext(),"success","Enviado correctamente\n");
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        resetForm();
    }

    private void resetForm(){
        textMessage.setText("");
        textName.setText("");
        textPhone.setText("");
        textEmail.setText("");
    }
}