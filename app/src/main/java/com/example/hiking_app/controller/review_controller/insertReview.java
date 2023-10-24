package com.example.hiking_app.controller.review_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.ViewHike;
import com.example.hiking_app.databinding.ActivityInsertHikeBinding;
import com.example.hiking_app.databinding.ActivityInsertReviewBinding;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Reviews;

public class insertReview extends AppCompatActivity {
    private ActivityInsertReviewBinding binding;
    private DatePickerDialog datePickerDialog;
    int HikeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);
        binding = ActivityInsertReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        HikeId = getIntent().getIntExtra("hike_id", -1);
    }


    private void setListener() {
        binding.reviewAddBtn.setOnClickListener(v ->{
            insertReviewActivity();
        });
    }

    private void insertReviewActivity() {
        int rating = Integer.parseInt(binding.rating.getText().toString().trim());
        String comment = binding.comment.getText().toString().trim();

        int userId = 1;
        int hikeId = HikeId;

        Reviews review = new Reviews(rating, comment,userId, hikeId );

        DbContext.getInstance(this.getApplicationContext()).appDao().insertReview(review);
        showMessage("Add review successful");
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, ViewReview.class);
        intent.putExtra("hike_id", HikeId);
        startActivity(intent);
    }
}