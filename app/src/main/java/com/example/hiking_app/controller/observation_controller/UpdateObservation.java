package com.example.hiking_app.controller.observation_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.observation_controller.ViewListObservations;
import com.example.hiking_app.databinding.ActivityUpdateObservationBinding;
import com.example.hiking_app.model.Observations;

public class UpdateObservation extends AppCompatActivity {
    private ActivityUpdateObservationBinding binding;
    private  int observationId;
    private Observations observation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);
        binding = ActivityUpdateObservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        observationId = getIntent().getIntExtra("observationId", -1);
        setData(observationId);
        setListener();
    }

    private void setListener() {
        binding.BtnUpdateObservation.setOnClickListener( v ->{
            updateObservation(observationId);
        });
    }

    private void updateObservation(int observationId) {
        String name = binding.observationName.getText().toString();
        String date = binding.observationDate.getText().toString();
        String comment = binding.observationComment.getText().toString();
        observation.setName(name);
        observation.setComment(comment);
        observation.setDateTime(date);
        DbContext.getInstance(this.getApplicationContext()).appDao().updateObservation(observation);
        Intent intent = new Intent(this, ViewListObservations.class);
        intent.putExtra("hike_id", observation.getHikeId());
        startActivity(intent);
    }

    private void setData(int observationId) {
        observation = DbContext.getInstance(this.getApplicationContext()).appDao().getObservationById(observationId);
        binding.observationName.setText(observation.getName().toString());
        binding.observationComment.setText(observation.getComment().toString());
        binding.observationDate.setText(observation.getComment().toString());
    }
}