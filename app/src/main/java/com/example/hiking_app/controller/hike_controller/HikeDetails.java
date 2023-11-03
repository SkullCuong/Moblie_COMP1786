package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.observation_controller.InsertObservation;
import com.example.hiking_app.controller.observation_controller.ListObservationAdapter;
import com.example.hiking_app.controller.observation_controller.ViewListObservations;
import com.example.hiking_app.controller.review_controller.ListReviewAdapter;
import com.example.hiking_app.controller.review_controller.ViewReview;
import com.example.hiking_app.controller.review_controller.insertReview;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.databinding.ActivityHikeDetailsBinding;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

import java.util.List;

public class HikeDetails extends AppCompatActivity {

    private ActivityHikeDetailsBinding binding;
    int hikeId;
    int userId;
    Users user;
    private List<Observations> observations;
    private List<Reviews> reviews;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_details);
        binding = ActivityHikeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();

        SessionManager sessionManager = new SessionManager(HikeDetails.this);
        int userIdSession = sessionManager.getKeyUserid();
        userId = userIdSession;
        user = DbContext.getInstance(HikeDetails.this).appDao().findUserById(userId);

        hikeId = getIntent().getIntExtra("hike_id", -1); // -1 is a default value if the ID is not found
        Hikes foundHike = DbContext.getInstance(this.getApplicationContext()).appDao().findHikeById(hikeId);
        //List Obs
        observations = DbContext.getInstance(this).appDao().getObservationsByHikeId(hikeId);
        ListObservationAdapter listAdapter = new ListObservationAdapter(observations,this);

        RecyclerView recyclerView = findViewById(R.id.listObservationsAtHikeDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(listAdapter);
        //List Reviews
        reviews = DbContext.getInstance(this).appDao().getListReviewOfHike(hikeId);
        ListReviewAdapter listReviewAdapter = new ListReviewAdapter(reviews,this);

        RecyclerView recyclerReviewsView = findViewById(R.id.listReviewsAtHikeDetails);
        recyclerReviewsView.setLayoutManager(new LinearLayoutManager(this));
        recyclerReviewsView.setAdapter(listReviewAdapter);


        if (hikeId != -1) {

            binding.profileImageView.setImageBitmap(getUserImage(user.getProfile_Picture()));
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

    private void setListener() {
        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HikeDetails.this, UpdateHike.class);
                intent.putExtra("hike_id", hikeId);
                startActivity(intent);
            }
        });
        binding.arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HikeDetails.this, MainActivity2.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "HikeDetailsToListHikes");
                startActivity(intent);
            }
        });
        binding.addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HikeDetails.this, insertReview.class);
                intent.putExtra("hike_id", hikeId);
                startActivity(intent);
            }
        });

        binding.AddOservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(HikeDetails.this, InsertObservation.class);
                intent.putExtra("hike_id", hikeId);
                HikeDetails.this.startActivity(intent);
            }
        });

        binding.hikeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HikeDetails.this, DeleteHike.class);
                intent.putExtra("hike_id", hikeId);
                HikeDetails.this.startActivity(intent);
            }
        });
    }

    private Bitmap getUserImage(String image) {
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