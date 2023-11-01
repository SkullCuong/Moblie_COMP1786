package com.example.hiking_app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hiking_app.Dao.AppDao;
import com.example.hiking_app.DbContext;
import com.example.hiking_app.MainActivity;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.ConfirmInsert;
import com.example.hiking_app.controller.hike_controller.InsertHikeActivity;
import com.example.hiking_app.controller.hike_controller.MapsActivity;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.databinding.ActivityInsertHikeBinding;
import com.example.hiking_app.model.Hikes;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddHikeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddHikeFragment extends Fragment  implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePickerDialog datePickerDialog;
    private ActivityInsertHikeBinding binding;
    int hikeId;
    Hikes foundHike;
    double latitude,longitude;

    GoogleMap mMap;

    SupportMapFragment mapFragment;

    private  final static int REQUEST_CODE = 100;

    FusedLocationProviderClient fusedLocationProviderClient ;

    public AddHikeFragment() {
        // Required empty public constructor
    }
    public static AddHikeFragment newInstance(String param1, String param2) {
        AddHikeFragment fragment = new AddHikeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = ActivityInsertHikeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());
        String address = "";
        Bundle data = getArguments();
        if(data != null){
            address = data.getString("address");
            hikeId = data.getInt("hike_id", -1);
        }
        checkAddressIsExisted(address);
        setListener();
        renderHikeIfExist();
        return view;
    }

    private void renderHikeIfExist() {
        foundHike = DbContext.getInstance(requireContext().getApplicationContext()).appDao().findHikeById(hikeId);
        CheckBox parkingAvailableCheckBox = binding.hikeParkingAvailable;
        if (hikeId != -1 && foundHike != null) {
            binding.hikeName.setText(foundHike.getName());
            binding.hikeLocation.setText(foundHike.getLocation());
            binding.hikeDate.setText(foundHike.getDate());
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
    private void putHikeDetails(Intent intent){
            intent.putExtra("name", binding.hikeName.getText().toString());
            intent.putExtra("date", binding.hikeDate.getText().toString());
            intent.putExtra("parkingAvailable", binding.hikeParkingAvailable.isChecked());
            intent.putExtra("length", binding.hikeLength.getText().toString());
            intent.putExtra("difficulty", binding.hikeDifficulty.getText().toString());
            intent.putExtra("description", binding.hikeDescription.getText().toString());
            intent.putExtra("equipment", binding.hikeEquipment.getText().toString());
            intent.putExtra("quality", binding.hikeEquipment.getText().toString());
    }
    private void getHikeDetails(Bundle data){
        latitude = data.getDouble("latitude",-1);
        longitude = data.getDouble("longitude",-1);
        binding.hikeName.setText(data.getString("name"));
        binding.hikeDate.setText(data.getString("date"));
        binding.hikeParkingAvailable.setChecked(data.getBoolean("parkingAvailable",false));
        binding.hikeLength.setText(data.getString("length"));
        binding.hikeDifficulty.setText(data.getString("difficulty"));
        binding.hikeDescription.setText(data.getString("description"));
        binding.hikeEquipment.setText(data.getString("equipment"));
        binding.hikeQuality.setText(data.getString("quality"));
    }

    private void checkAddressIsExisted(String address){
        if (address == ""){
            getLastLocation();
        } else {
            Bundle data = getArguments();
            getHikeDetails(data);
            binding.hikeLocation.setText(address);
            binding.openMap.setVisibility(View.VISIBLE);
        }
    }
    private void getCalendar() {
        Calendar calendar = Calendar.getInstance();
        int currentDay  = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth  = calendar.get(Calendar.MONTH);
        datePickerDialog = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.hikeDate.setText(day + "/" + (month + 1) + "/" +year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }
    private  void openMap(double latitude, double longitude){
        Intent intent = new Intent(requireActivity(), MapsActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude",longitude);
        putHikeDetails(intent);
        startActivity(intent);
    }
    public void getLastLocation(){
        binding.progressBar.setVisibility(View.VISIBLE);
        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                                if (location != null) {
                                    Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    try {
                                        Address addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                                        String address = addresses.getAddressLine(0);
                                        binding.progressBar.setVisibility(View.INVISIBLE);
                                        binding.openMap.setVisibility(View.VISIBLE);
                                        binding.hikeLocation.setText(address);
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                }

                        }
                    });
        } else {
            askPermisson();
        }
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
            DbContext.getInstance(requireContext().getApplicationContext()).appDao().updateHike(foundHike);
            //Start fragment
            Intent intent = new Intent(requireActivity(), ConfirmInsert.class);
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

            SessionManager sessionManager = new SessionManager(getContext());
            int userIdSession = sessionManager.getKeyUserid();
            int userId = userIdSession;

            Hikes hike = new Hikes(name, location, date, parkingAV, length, difficulty, description, equipment, quality, userId);
            AppDao appDao = DbContext.getInstance(requireContext().getApplicationContext()).appDao();

            long hikeId = appDao.insertHike(hike);

            Intent intent = new Intent(requireActivity(), ConfirmInsert.class);
            intent.putExtra("hike_id", Integer.parseInt(String.valueOf(hikeId)));
            startActivity(intent);
        }
        //showMessage("Add successful");
    }

    private void askPermisson() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
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
    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}