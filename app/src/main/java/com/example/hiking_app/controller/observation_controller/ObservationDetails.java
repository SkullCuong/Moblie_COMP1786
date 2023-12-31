package com.example.hiking_app.controller.observation_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.DeleteHike;
import com.example.hiking_app.controller.hike_controller.HikeDetails;
import com.example.hiking_app.controller.hike_controller.UpdateHike;
import com.example.hiking_app.databinding.ActivityConfirmInsertBinding;
import com.example.hiking_app.databinding.ActivityObservationDetailsBinding;
import com.example.hiking_app.model.Observations;

public class ObservationDetails extends AppCompatActivity {
    private ActivityObservationDetailsBinding binding;
    int observationId;
    Observations observation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_details);
        binding = ActivityObservationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        observationId = getIntent().getIntExtra("observationId", -1);
        observation = DbContext.getInstance(this.getApplicationContext()).appDao().findObsById(observationId);
        if (observationId != -1) {
            binding.ObsName.setText(observation.getName());
            binding.ObsDate.setText(observation.getDateTime());
            binding.ObsComment.setText(observation.getComment());
            binding.ObsPhoto.setImageBitmap(getImage(observation.getPhoto()));
        } else {
            // Handle the case where the ID is not found
            binding.ObsName.setText("Not found");
        }
        setListenter();
    }

    private void setListenter() {
        binding.arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObservationDetails.this, HikeDetails.class);
                intent.putExtra("hike_id", observation.getHikeId());
                startActivity(intent);
            }
        });
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObservationDetails.this, UpdateObservation.class);
                intent.putExtra("observationId", observation.getId());
                startActivity(intent);
            }
        });
        binding.BtnDeleteObservation.setOnClickListener(v -> {
            DbContext.getInstance(this.getApplicationContext()).appDao().deleteObservation(observation);
            showMessage(observation.getHikeId(), "Delete success!");

        });
    }
    private void showMessage(int hikeId,String message) {
        Toast.makeText(this.getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this.getApplicationContext(), HikeDetails.class);
        intent.putExtra("hike_id", hikeId);
        startActivity(intent);
    }
    private Bitmap getImage(String image) {
        try {
            byte[] decodedBytes = Base64.decode(image, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (IllegalArgumentException e) {
            Log.e("ImageError", "IllegalArgumentException when converting the image (at getImage): " + e.getMessage());
        } catch (OutOfMemoryError e) {
            Log.e("ImageError", "OutOfMemoryError when converting the image (at getImage): " + e.getMessage());
        } catch (Exception e) {
            Log.e("ImageError", "Error when converting the image (at getImage): " + e.getMessage());
        }
        return null;
    }

}