package com.example.hiking_app.controller.review_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.HikeDetails;
import com.example.hiking_app.controller.hike_controller.ViewHike;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.databinding.ActivityInsertHikeBinding;
import com.example.hiking_app.databinding.ActivityInsertReviewBinding;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

import java.util.List;

public class insertReview extends AppCompatActivity {
    private ActivityInsertReviewBinding binding;
    private DatePickerDialog datePickerDialog;
    private List<Reviews> reviews;
    int HikeId;
    int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_review);
        binding = ActivityInsertReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        HikeId = getIntent().getIntExtra("hike_id", -1);

        SessionManager sessionManager = new SessionManager(insertReview.this);
        int userIdSession = sessionManager.getKeyUserid();
        userId = userIdSession;

        Users user = DbContext.getInstance(insertReview.this).appDao().findUserById(userId);
        binding.userImage.setImageBitmap(getUserImage(user.getProfile_Picture()));
        binding.userName.setText(user.getUserName());
    }
    private void setListener() {
        binding.reviewAddBtn.setOnClickListener(v ->{
            insertReviewActivity();
        });
        binding.arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(insertReview.this, HikeDetails.class);
                intent.putExtra("hike_id", HikeId);
                startActivity(intent);
            }
        });
    }

    private void insertReviewActivity() {
        //int rating = Integer.parseInt(binding.rating.getText().toString().trim());
        int rating = 5;
        String comment = binding.comment.getText().toString().trim();
        if(comment.isEmpty()){
            showMessage("Please input the comment");
        }

        Reviews review = new Reviews(rating, comment,userId, HikeId);

        DbContext.getInstance(this.getApplicationContext()).appDao().insertReview(review);
        showMessage("Add review successful");
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, HikeDetails.class);
        intent.putExtra("hike_id", HikeId);
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