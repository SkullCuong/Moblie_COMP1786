package com.example.hiking_app.controller.user_controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.model.Users;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private ImageView profileImageView; // Declare profileImageView

    private DbContext dbContext;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbContext = DbContext.getInstance(this.getApplicationContext());
        sessionManager = new SessionManager(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        profileImageView = findViewById(R.id.profileImageView); // Initialize profileImageView


        // Retrieve user ID from the session
        int userId = sessionManager.getKeyUserid();

        if (userId != -1) {
            Users user = dbContext.appDao().findUserById(userId);
            if (user != null) {
                populateUI(user);
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                // Redirect to LoginActivity or handle the situation accordingly
                finish();
            }
        } else {
            // User is not logged in, redirect to LoginActivity
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            // Redirect to LoginActivity or handle the situation accordingly
            finish();
        }
    }

    private void populateUI(Users user) {
        usernameEditText.setText(user.getUserName());
        emailEditText.setText(user.getEmail());
        addressEditText.setText(user.getAddress());

        if (user.getProfile_Picture() != null && !user.getProfile_Picture().isEmpty()) {
            byte[] decodedString = Base64.decode(user.getProfile_Picture(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageView.setImageBitmap(decodedBitmap);
        }


    }
}
