package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.databinding.ActivityHikeDetailsBinding;
import com.example.hiking_app.databinding.ActivityUpdateHikeBinding;
import com.example.hiking_app.model.Hikes;

public class UpdateHike extends AppCompatActivity {
    private ActivityUpdateHikeBinding binding;
    int hikeId;
    Hikes foundHike;
    int difficulty;
    String[] difficultyLevels;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hike);
        binding = ActivityUpdateHikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();

        hikeId = getIntent().getIntExtra("hike_id", -1); // -1 is a default value if the ID is not found
        foundHike = DbContext.getInstance(this.getApplicationContext()).appDao().findHikeById(hikeId);
        RadioButton parkingAvailableCheckBox = binding.hikeParkingAvailable;

        //Spinner
        difficultyLevels = new String[]{"Easy", "Moderate", "Difficult", "Very Difficult"};
        adapter = new ArrayAdapter<>(UpdateHike.this, android.R.layout.simple_spinner_item, difficultyLevels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.hikeDifficulty.setAdapter(adapter);

        binding.hikeDifficulty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedChoice = difficultyLevels[position];

                if (selectedChoice.equals("Easy")) {
                    difficulty = adapter.getPosition("Easy") + 1;

                } else if (selectedChoice.equals("Moderate")) {
                    difficulty = adapter.getPosition("Moderate") + 1;
                } else if (selectedChoice.equals("Difficult")) {
                    difficulty = adapter.getPosition("Difficult") + 1;
                } else if (selectedChoice.equals("Very Difficult")) {
                    difficulty = adapter.getPosition("Very Difficult") + 1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


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
            int difficultyValue = foundHike.getDifficulty();
            if (difficultyValue >= 1 && difficultyValue <= 4) {
                // Subtract 1 to get the index in the adapter
                binding.hikeDifficulty.setSelection(difficultyValue - 1);
            }
            //binding.hikeDifficulty.setText(String.valueOf(foundHike.getDifficulty()));
            binding.hikeDescription.setText(foundHike.getDescription());
            binding.hikeEquipment.setText(foundHike.getEquipment());
            binding.hikeQuality.setText(foundHike.getQuality());
    }
}

    private void setListener() {
        binding.BtnUpdateHike.setOnClickListener(view ->{
            UpdateHikeActivity();
        } );
        binding.arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateHike.this, HikeDetails.class);
                intent.putExtra("hike_id", hikeId);
                startActivity(intent);
            }
        });
    }
    private void UpdateHikeActivity() {
        // take data from EditText
        String updatedName = binding.hikeName.getText().toString();
        String updatedLocation = binding.hikeLocation.getText().toString();
        String updatedDate = binding.hikeDate.getText().toString();
        // Update parking_available based on the CheckBox state
        boolean isParkingAvailable = binding.hikeParkingAvailable.isChecked();
        float updateLength = Float.parseFloat(binding.hikeLength.getText().toString());
        //int updateDifficulty = Integer.parseInt(binding.hikeDifficulty.getText().toString());
        int difficultyValue = foundHike.getDifficulty();
        if (difficultyValue >= 1 && difficultyValue <= 4) {
            // Subtract 1 to get the index in the adapter
            binding.hikeDifficulty.setSelection(difficultyValue - 1);
        }
        String updatedDescription = binding.hikeDescription.getText().toString();
        String updatedEquipment = binding.hikeEquipment.getText().toString();
        String updatedQuality = binding.hikeQuality.getText().toString();

        // Update data
        foundHike.setName(updatedName);
        foundHike.setLocation(updatedLocation);
        foundHike.setDate(updatedDate);
        foundHike.setLength(updateLength);
        //foundHike.setDifficulty(updateDifficulty);
        foundHike.setDifficulty(difficulty);
        foundHike.setDescription(updatedDescription);
        foundHike.setEquipment(updatedEquipment);
        foundHike.setQuality(updatedQuality);
        foundHike.setParking_available(isParkingAvailable);

        // Save to db
        DbContext.getInstance(this.getApplicationContext()).appDao().updateHike(foundHike);
        //send message successful
        showMessage("Update successful!");
        //Start activity
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("FRAGMENT_TO_LOAD", "HikeDetailsToListHikes");
        startActivity(intent);

    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}