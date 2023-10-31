package com.example.hiking_app.controller.hike_controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hiking_app.Dao.AppDao;
import com.example.hiking_app.DbContext;

import com.example.hiking_app.controller.observation_controller.InsertObservation;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.review_controller.insertReview;
import com.example.hiking_app.databinding.ActivityInsertHikeBinding;
import com.example.hiking_app.model.Hikes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InsertHikeActivity extends FragmentActivity implements OnMapReadyCallback {

    private ActivityInsertHikeBinding binding;
    private DatePickerDialog datePickerDialog;
    double latitude,longitude;

    GoogleMap mMap;

    SupportMapFragment  mapFragment;


    private  final static int REQUEST_CODE = 100;

    FusedLocationProviderClient fusedLocationProviderClient;
    int hikeId;
    Hikes foundHike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_hike);
        binding = ActivityInsertHikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        String address = getIntent().getStringExtra("address");
        checkAddressIsExisted(address);
        setListener();
        hikeId = getIntent().getIntExtra("hike_id", -1); // -1 is a default value if the ID is not found
        foundHike = DbContext.getInstance(this.getApplicationContext()).appDao().findHikeById(hikeId);
        CheckBox parkingAvailableCheckBox = binding.hikeParkingAvailable;

        if (hikeId != -1 && foundHike != null) {
            binding.hikeName.setText(foundHike.getName());
            binding.hikeLocation.setText(foundHike.getLocation());
            binding.hikeDate.setText(foundHike.getDate());
//            if(foundHike.getParking_available() == true){
//                binding.hikeParkingAvailable.setText("Yes");
//            }else {
//                binding.hikeParkingAvailable.setText("No");
//            }
            if (foundHike.getParking_available() == true) {
                parkingAvailableCheckBox.setChecked(true);
            } else {
                parkingAvailableCheckBox.setChecked(false);
            }
            binding.hikeLength.setText(String.valueOf(foundHike.getLength()));
            binding.hikeDifficulty.setText(String.valueOf(foundHike.getDifficulty()));
            binding.hikeDescription.setText(foundHike.getDescription());
            binding.hikeEquipment.setText(foundHike.getEquipment());
            binding.hikeQuality.setText(foundHike.getQuality());

        }
    }

    private void putHikeDetails(Intent intent){
        intent.putExtra("name",binding.hikeName.getText().toString());
        intent.putExtra("date",binding.hikeDate.getText().toString());
        intent.putExtra("parkingAvailable",binding.hikeParkingAvailable.isChecked());
        intent.putExtra("length",binding.hikeLength.getText().toString());
        intent.putExtra("difficulty",binding.hikeDifficulty.getText().toString());
        intent.putExtra("description",binding.hikeDescription.getText().toString());
        intent.putExtra("equipment",binding.hikeEquipment.getText().toString());
        intent.putExtra("quality",binding.hikeEquipment.getText().toString());
    }
    private void getHikeDetails(){
        latitude = getIntent().getDoubleExtra("latitude",-1);
        longitude = getIntent().getDoubleExtra("longitude",-1);
        binding.hikeName.setText(getIntent().getStringExtra("name"));
        binding.hikeDate.setText(getIntent().getStringExtra("date"));
        binding.hikeParkingAvailable.setChecked(getIntent().getBooleanExtra("parkingAvailable",false));
        binding.hikeLength.setText(getIntent().getStringExtra("length"));
        binding.hikeDifficulty.setText(getIntent().getStringExtra("difficulty"));
        binding.hikeDescription.setText(getIntent().getStringExtra("description"));
        binding.hikeEquipment.setText(getIntent().getStringExtra("equipment"));
        binding.hikeQuality.setText(getIntent().getStringExtra("quality"));
    }

    private void checkAddressIsExisted(String address){
        if (address == null){
            getLastLocation();
        } else {
            getHikeDetails();
            binding.hikeLocation.setText(address);
        }
    }
    private void setListener() {
        binding.HikeAdd.setOnClickListener(v ->{
            insertHike();
        });
        binding.hikeDate.setOnClickListener(v ->{
            getCalendar();
        });
        binding.openMap.setOnClickListener(v ->{
            openMap(latitude,longitude);
        });
    }
    private void getCalendar() {
        Calendar calendar = Calendar.getInstance();
        int currentDay  = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth  = calendar.get(Calendar.MONTH);
        datePickerDialog = new DatePickerDialog(InsertHikeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.hikeDate.setText(day + "/" + (month + 1) + "/" +year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }

    private  void openMap(double latitude, double longitude){
        Intent intent = new Intent(InsertHikeActivity.this, MapsActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude",longitude);
        putHikeDetails(intent);
        InsertHikeActivity.this.startActivity(intent);
    }
    private void insertHike() {
        if(hikeId != -1 && foundHike != null){
            // take data from EditText
            String updatedName = binding.hikeName.getText().toString();
            String updatedLocation = binding.hikeLocation.getText().toString();
            String updatedDate = binding.hikeDate.getText().toString();
            // Update parking_available based on the CheckBox state
            boolean isParkingAvailable = binding.hikeParkingAvailable.isChecked();
            float updateLength = Float.parseFloat(binding.hikeLength.getText().toString());
            int updateDifficulty = Integer.parseInt(binding.hikeDifficulty.getText().toString());
            String updatedDescription = binding.hikeDescription.getText().toString();
            String updatedEquipment = binding.hikeEquipment.getText().toString();
            String updatedQuality = binding.hikeQuality.getText().toString();

            // Update data
            foundHike.setName(updatedName);
            foundHike.setLocation(updatedLocation);
            foundHike.setDate(updatedDate);
            foundHike.setLength(updateLength);
            foundHike.setDifficulty(updateDifficulty);
            foundHike.setDescription(updatedDescription);
            foundHike.setEquipment(updatedEquipment);
            foundHike.setQuality(updatedQuality);
            foundHike.setParking_available(isParkingAvailable);

            // Save to db
            DbContext.getInstance(this.getApplicationContext()).appDao().updateHike(foundHike);
            //Start activity
            Intent intent = new Intent(this, ConfirmInsert.class);
            intent.putExtra("hike_id", Integer.parseInt(String.valueOf(hikeId)));
            startActivity(intent);
        }else {
            String name = binding.hikeName.getText().toString().trim();
            String location = binding.hikeLocation.getText().toString().trim();
            String date = binding.hikeDate.getText().toString().trim();
            boolean parkingAV = true;
            float length = Float.parseFloat(binding.hikeLength.getText().toString().trim());
            int difficulty = Integer.parseInt(binding.hikeDifficulty.getText().toString().trim());
            String description = binding.hikeDescription.getText().toString().trim();
            String equipment = binding.hikeEquipment.getText().toString().trim();
            String quality = binding.hikeQuality.getText().toString().trim();
            int userId = 1;

            Hikes hike = new Hikes(name, location, date, parkingAV, length, difficulty, description, equipment, quality, userId);
            AppDao appDao = DbContext.getInstance(this.getApplicationContext()).appDao();

            long hikeId = appDao.insertHike(hike);

            Intent intent = new Intent(InsertHikeActivity.this, ConfirmInsert.class);
            intent.putExtra("hike_id", Integer.parseInt(String.valueOf(hikeId)));
            startActivity(intent);
        }
        //showMessage("Add successful");
    }
    public void getLastLocation(){
        binding.progressBar.setVisibility(View.VISIBLE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                Geocoder geocoder = new Geocoder(InsertHikeActivity.this, Locale.getDefault());
                                latitude = location.getLatitude();
                                longitude =location.getLongitude();
                               try{
                                   Address addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1).get(0);
                                   String address = addresses.getAddressLine(0);
                                   binding.progressBar.setVisibility(View.INVISIBLE);
                                   binding.openMap.setVisibility(View.VISIBLE);
                                   binding.hikeLocation.setText(address);
                               }
                               catch (Exception e){
                                   System.out.println(e.getMessage());
                               }
                            }
                        }
                    });
        } else {
            askPermisson();
        }
    }

    private void askPermisson() {
        ActivityCompat.requestPermissions(InsertHikeActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            } else {
                showMessage("Permission required");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }
}