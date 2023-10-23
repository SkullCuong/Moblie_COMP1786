package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.databinding.ActivityHikeDetailsBinding;
import com.example.hiking_app.databinding.ActivityUpdateHikeBinding;
import com.example.hiking_app.model.Hikes;

public class UpdateHike extends AppCompatActivity {
    private ActivityUpdateHikeBinding binding;
    int hikeId;
    Hikes foundHike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hike);
        binding = ActivityUpdateHikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        // Inside the "DeleteHike" activity
        hikeId = getIntent().getIntExtra("hike_id", -1); // -1 is a default value if the ID is not found
        foundHike = DbContext.getInstance(this.getApplicationContext()).appDao().findHikeById(hikeId);
        CheckBox parkingAvailableCheckBox = binding.hikeParkingAvailable;
        if (hikeId != -1) {
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
        binding.BtnUpdateHike.setOnClickListener(view ->{
            UpdateHikeActivity();
        } );
    }
    private void UpdateHikeActivity() {
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

        // Uodate data
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

        //send message successful
        showMessage("Update successful!");
    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}