package com.example.hiking_app.controller.review_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.ViewHike;
import com.example.hiking_app.databinding.ActivityUpdateHikeBinding;
import com.example.hiking_app.databinding.ActivityUpdateReviewBinding;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Reviews;

public class UpdateReview extends AppCompatActivity {
    private ActivityUpdateReviewBinding binding;
    int reviewId;
    Reviews foundReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_review);
        binding = ActivityUpdateReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        reviewId = getIntent().getIntExtra("review_id", -1); // -1 is a default value if the ID is not found
        foundReview = DbContext.getInstance(this.getApplicationContext()).appDao().findReviewById(reviewId);
        if (reviewId != -1) {
            binding.rating.setText(String.valueOf(foundReview.getRating()));
            binding.comment.setText(foundReview.getComment());
        }else {
            binding.rating.setText("Not fount");
        }
    }

    private void setListener() {
        binding.reviewUpdateBtn.setOnClickListener(view ->{
            UpdateReviewActivity();
        } );
    }

    private void UpdateReviewActivity() {
        int updateRating = Integer.parseInt(binding.rating.getText().toString());
        String updatedComment = binding.comment.getText().toString();

        // Update data
        foundReview.setRating(updateRating);
        foundReview.setComment(updatedComment);

        // Save to db
        DbContext.getInstance(this.getApplicationContext()).appDao().updateReview(foundReview);
        //send message successful
        showMessage("Update successful!");
        //Start activity
        Intent intent = new Intent(this, ViewReview.class);
        intent.putExtra("hike_id", foundReview.getHikeId());
        startActivity(intent);


    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}