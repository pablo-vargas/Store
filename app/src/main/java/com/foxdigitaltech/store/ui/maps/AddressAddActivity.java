package com.foxdigitaltech.store.ui.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.foxdigitaltech.store.R;
import com.foxdigitaltech.store.shared.model.Address;
import com.foxdigitaltech.store.shared.model.RouteDatabase;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AddressAddActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private boolean isMoveCamera =false;
    private GoogleMap map;
    private SupportMapFragment sMapFragment;

    View view;
    AlertDialog alertDialog;

    Address currentAddress;

    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            //map.clear();
            for (Location location : locationResult.getLocations()) {
                if (getApplicationContext() != null) {
                    //map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Estoy aqui.").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_person_pin_24)));
                    if(isMoveCamera){

                    }else{
                        map.moveCamera(CameraUpdateFactory.newCameraPosition(
                                new CameraPosition.Builder()
                                        .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                        .zoom(13f)
                                        .build()
                        ));
                        currentAddress.setCurrentLatitude(location.getLatitude());
                        currentAddress.setCurrentLongitude(location.getLongitude());
                        isMoveCamera = true;
                    }

                }
            }
        }
    };

    MaterialButton btnAddLocation,btnSave,btnSaveLocation;
    TextInputEditText textName,textStreet;
    LinearLayout layoutLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);

        currentAddress = new Address();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        view = getLayoutInflater().inflate(R.layout.card_add_address,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        alertDialog  = builder.create();
        alertDialog.setView(view);
        int id = view.findViewById(R.id.map).getId();
        sMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(id);

        btnSaveLocation = view.findViewById(R.id.btnSaveLocation);
        sMapFragment.getMapAsync(this);
        //mapFragmentMuestra.getMapAsync(this);
        layoutLoader = findViewById(R.id.layoutLoader);
        textName = findViewById(R.id.editTextName);
        textStreet = findViewById(R.id.editTextStreet);
        btnAddLocation = findViewById(R.id.btnAddLocation);
        btnAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLocation();
            }
        });
        btnSave = findViewById(R.id.btnAddAddress);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAddress();
            }
        });
        btnSaveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


    }

    private void saveAddress() {
        if(textStreet.getText().toString().trim().isEmpty()){
            textStreet.setError("El campo no puede ir vacío.");
            return;
        }
        if(textName.getText().toString().trim().isEmpty()){
            textName.setError("Este campo no puede ir vacío");
            return;
        }
        if(currentAddress.getCurrentLatitude() == null){
            startLocation();
            Toast.makeText(this, "Debe seleccionar la ubicacion en el mapa", Toast.LENGTH_SHORT).show();
            return;
        }
        if(currentAddress.getStreetLatitude() == null){
            startLocation();
            Toast.makeText(this, "Debe seleccionar la ubicacion en el mapa", Toast.LENGTH_SHORT).show();
            return;
        }
        layoutLoader.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.GONE);
        currentAddress.setName(textName.getText().toString().trim());
        currentAddress.setStreet(textStreet.getText().toString().trim());

        if(currentAddress.getKey() == null){
            FirebaseDatabase.getInstance()
                    .getReference().child(new RouteDatabase().LIST_ADDRESS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .push().setValue(currentAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddressAddActivity.this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else{
                        Toast.makeText(AddressAddActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        layoutLoader.setVisibility(View.GONE);
                        btnSave.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else{
            FirebaseDatabase.getInstance()
                    .getReference().child(new RouteDatabase().LIST_ADDRESS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(currentAddress.getKey()).setValue(currentAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(AddressAddActivity.this, "Modificado correctamente", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }else{
                        Toast.makeText(AddressAddActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        layoutLoader.setVisibility(View.GONE);
                        btnSave.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (gpsActived()) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                         map.setMyLocationEnabled(true);
                    }
                    else {
                        showAlertDialogNOGPS();
                    }
                }
                else {
                    checkLocationPermisions();
                }
            }
            else {

                checkLocationPermisions();
            }
        }
    }

    private boolean gpsActived() {
        boolean isActive = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void checkLocationPermisions(){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(AddressAddActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){

                new AlertDialog.Builder(this)
                        .setTitle("Proporcione los permisos continuar")
                        .setMessage("Esta aplicacion requiere de los permisos de ubicacion para poder utilizarse")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(AddressAddActivity.this,new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                },LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }else{
                ActivityCompat.requestPermissions(AddressAddActivity.this,new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                },LOCATION_REQUEST_CODE);
            }
        }else {

            ActivityCompat.requestPermissions(AddressAddActivity.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
            },LOCATION_REQUEST_CODE);
        }
    }
    private void startLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(AddressAddActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (gpsActived()) {
                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
                    map.setMyLocationEnabled(true);
                    alertDialog.show();
                }
                else {
                    showAlertDialogNOGPS();
                }
            }
            else {
                checkLocationPermisions();
            }
        } else {
            if (gpsActived()) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
            }
            else {
                showAlertDialogNOGPS();
            }
        }
    }
    private void showAlertDialogNOGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressAddActivity.this);

        builder.setMessage("Por favor activa tu ubicación para continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL|GoogleMap.MAP_TYPE_NONE);
        //map.setBuildingsEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(new LatLng(-19.0429, -65.2554))
                        .zoom(11f)
                        .build()
        ));
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(5);
        map.setTrafficEnabled(true);
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title(""));
                currentAddress.setStreetLatitude(latLng.latitude);
                currentAddress.setStreetLongitude(latLng.longitude);
            }
        });

    }
}