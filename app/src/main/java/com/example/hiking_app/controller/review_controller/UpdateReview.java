package com.example.hiking_app.controller.review_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.HikeDetails;
import com.example.hiking_app.controller.hike_controller.ViewHike;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.databinding.ActivityUpdateHikeBinding;
import com.example.hiking_app.databinding.ActivityUpdateReviewBinding;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

public class UpdateReview extends AppCompatActivity {
    private ActivityUpdateReviewBinding binding;
    int reviewId;
    int userId;
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

        //take user
        SessionManager sessionManager = new SessionManager(UpdateReview.this);
        int userIdSession = sessionManager.getKeyUserid();
        userId = userIdSession;

        Users user = DbContext.getInstance(UpdateReview.this).appDao().findUserById(userId);
        binding.userImage.setImageBitmap(getUserImage(user.getProfile_Picture()));
        binding.userName.setText(user.getUserName());

        if (reviewId != -1) {
            //binding.rating.setText(String.valueOf(foundReview.getRating()));
            binding.comment.setText(foundReview.getComment());
        }else {
            binding.comment.setText("Not fount");
        }
    }

    private void setListener() {
        binding.reviewUpdateBtn.setOnClickListener(view ->{
            UpdateReviewActivity();
        } );
    }

    private void UpdateReviewActivity() {
        //int updateRating = Integer.parseInt(binding.rating.getText().toString());
        String updatedComment = binding.comment.getText().toString();

        // Update data
        //foundReview.setRating(updateRating);
        foundReview.setComment(updatedComment);

        // Save to db
        DbContext.getInstance(this.getApplicationContext()).appDao().updateReview(foundReview);
        //send message successful
        showMessage("Update successful!");



    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
        //Start activity
        Intent intent = new Intent(this, HikeDetails.class);
        intent.putExtra("hike_id", foundReview.getHikeId());
        startActivity(intent);
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