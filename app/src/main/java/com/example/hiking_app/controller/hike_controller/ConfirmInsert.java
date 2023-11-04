package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.databinding.ActivityConfirmInsertBinding;
import com.example.hiking_app.databinding.ActivityDeleteHikeBinding;
import com.example.hiking_app.model.Hikes;

public class ConfirmInsert extends AppCompatActivity {
    private ActivityConfirmInsertBinding binding;
    int hikeId;
    Hikes foundHike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_insert);
        binding = ActivityConfirmInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        // Inside the "DeleteHike" activity
        hikeId = getIntent().getIntExtra("hike_id", -1); // -1 is a default value if the ID is not found
        foundHike = DbContext.getInstance(this.getApplicationContext()).appDao().findHikeById(hikeId);
        if (hikeId != -1) {
            binding.hikeName.setText(foundHike.getName());
            binding.hikeLocation.setText(foundHike.getLocation());
            binding.hikeDate.setText(foundHike.getDate());
            if(foundHike.getParking_available() == true){
                binding.hikeParkingAvailable.setText("Yes");
            }else {
                binding.hikeParkingAvailable.setText("No");
            }
            if(foundHike.getDifficulty() == 1){
                binding.hikeDifficulty.setText("Easy");
            } else if (foundHike.getDifficulty() == 2) {
                binding.hikeDifficulty.setText("Moderate");
            } else if (foundHike.getDifficulty() == 3) {
                binding.hikeDifficulty.setText("Difficult");
            } else if (foundHike.getDifficulty() == 4) {
                binding.hikeDifficulty.setText("Very Difficult");
            }
            binding.hikeLength.setText(String.valueOf(foundHike.getLength()));
            //binding.hikeDifficulty.setText(String.valueOf(foundHike.getDifficulty()));
            binding.hikeDescription.setText(foundHike.getDescription());
            binding.hikeEquipment.setText(foundHike.getEquipment());
            binding.hikeQuality.setText(foundHike.getQuality());

        } else {
            // Handle the case where the ID is not found
            binding.hikeName.setText("Not found");
        }
    }
    private void setListener() {
        binding.addHike.setOnClickListener(view ->{
            InserHike();
        } );
        binding.CancelBtn.setOnClickListener(view ->{
            DeleteConfirmHike();
        } );
    }

    private void DeleteConfirmHike() {
//        DbContext.getInstance(this.getApplicationContext()).appDao().deleteHikeById(hikeId);
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("hike_id", hikeId);
        intent.putExtra("FRAGMENT_TO_LOAD", "AddHikeFragmentForConfirm");
        startActivity(intent);
    }

    private void InserHike() {
        //DbContext.getInstance(this.getApplicationContext()).appDao().insertHike(hikeId);
        showMessage("Add successful");
//        Intent intent = new Intent(this, ViewHike.class);
//        startActivity(intent);
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("FRAGMENT_TO_LOAD", "ViewHikeFragment");
        startActivity(intent);
    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}