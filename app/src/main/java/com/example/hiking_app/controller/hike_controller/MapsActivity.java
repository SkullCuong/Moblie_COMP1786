package com.example.hiking_app.controller.hike_controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.hiking_app.Fragment.AddHikeFragment;
import com.example.hiking_app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.hiking_app.databinding.ActivityMapsBinding;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double latitude = getIntent().getDoubleExtra("latitude", -1);;
        double longitude = getIntent().getDoubleExtra("longitude", -1);;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title("You are in"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,15));

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("You are in"));
                Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try{
                    Address addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1).get(0);
                    String address = addresses.getAddressLine(0) ;
                    System.out.println("81");
                    Intent intent = new Intent();

                    //Intent intent = new Intent(MapsActivity.this, AddHikeFragment.class);
                    hikeDetails(intent,address,latLng);

                    //startActivity(intent);
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });
    }

    private void hikeDetails(Intent intent, String address , LatLng latLng){
        System.out.println("97");
        intent.putExtra("address", address);
        intent.putExtra("latitude", latLng.latitude);
        intent.putExtra("longitude", latLng.longitude);
        intent.putExtra("name",getIntent().getStringExtra("name"));
        intent.putExtra("date",getIntent().getStringExtra("date"));
        intent.putExtra("parkingAvailable",getIntent().getBooleanExtra("parkingAvailable",false));
        intent.putExtra("length",getIntent().getStringExtra("length"));
        intent.putExtra("difficulty",getIntent().getStringExtra("difficulty"));
        intent.putExtra("description",getIntent().getStringExtra("description"));
        intent.putExtra("equipment",getIntent().getStringExtra("equipment"));
        intent.putExtra("quality",getIntent().getStringExtra("quality"));

        //AddHikeFragment addHikeFragment = AddHikeFragment.newInstance("param1","param2");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,new AddHikeFragment());
        fragmentTransaction.commit();
    }
}