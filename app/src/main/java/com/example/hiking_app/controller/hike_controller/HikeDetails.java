package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.databinding.ActivityHikeDetailsBinding;
import com.example.hiking_app.model.Hikes;

public class HikeDetails extends AppCompatActivity {

    private ActivityHikeDetailsBinding binding;
    int hikeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_details);
        binding = ActivityHikeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setListener();
        // Inside the "DeleteHike" activity
        hikeId = getIntent().getIntExtra("hike_id", -1); // -1 is a default value if the ID is not found
        Hikes foundHike = DbContext.getInstance(this.getApplicationContext()).appDao().findHikeById(hikeId);

        if (hikeId != -1) {
            binding.hikeName.setText(foundHike.getName());
            binding.hikeLocation.setText(foundHike.getLocation());
            binding.hikeDate.setText(foundHike.getDate());
            if(foundHike.getParking_available() == true){
                binding.hikeParkingAvailable.setText("Yes");
            }else {
                binding.hikeParkingAvailable.setText("No");
            }
            binding.hikeLength.setText(String.valueOf(foundHike.getLength()));
            binding.hikeDifficulty.setText(String.valueOf(foundHike.getDifficulty()));
            binding.hikeDescription.setText(foundHike.getDescription());
            binding.hikeEquipment.setText(foundHike.getEquipment());
            binding.hikeQuality.setText(foundHike.getQuality());

        } else {
            // Handle the case where the ID is not found
            binding.hikeName.setText("Not found");
        }
    }

}