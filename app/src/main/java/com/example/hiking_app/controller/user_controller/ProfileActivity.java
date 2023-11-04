package com.example.hiking_app.controller.user_controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

public class ProfileActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText addressEditText;
    private ImageView profileImageView;
    private Button updateButton;

    private DbContext dbContext;
    private SessionManager sessionManager;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbContext = DbContext.getInstance(this.getApplicationContext());
        sessionManager = new SessionManager(this);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        addressEditText = findViewById(R.id.addressEditText);
        profileImageView = findViewById(R.id.profileImageView);
        updateButton = findViewById(R.id.updateButton);

        // Retrieve user ID from the session
        userId = sessionManager.getKeyUserid();

        if (userId != -1) {
            Users user = dbContext.appDao().findUserById(userId);
            if (user != null) {
                populateUI(user);
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get updated user information
                updateProfile(); // Call the updateProfile method when the button is clicked
            }
        });
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

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    // Define the updateProfile method to handle the update logic
    public void updateProfile() {
        // Get updated user information
        String updatedUsername = usernameEditText.getText().toString().trim();
        String updatedEmail = emailEditText.getText().toString().trim();
        String updatedAddress = addressEditText.getText().toString().trim();

        // Convert Bitmap to Base64 String for the updated profile picture
        String updatedProfilePicture = "";
        Bitmap profileBitmap = ((BitmapDrawable) profileImageView.getDrawable()).getBitmap();
        if (profileBitmap != null) {
            updatedProfilePicture = bitmapToBase64(profileBitmap);
        }
        else{
            Toast.makeText(ProfileActivity.this, "the image worng", Toast.LENGTH_SHORT).show();
        }


        // Update user profile in the database
        Users updatedUser = new Users(updatedUsername, null, updatedEmail, updatedAddress, updatedProfilePicture);
        updatedUser.setId(userId);

        try {
            dbContext.appDao().updateUser(updatedUser);
            Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("UpdateUser", "Error updating user: " + e.getMessage());
            Toast.makeText(ProfileActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}
