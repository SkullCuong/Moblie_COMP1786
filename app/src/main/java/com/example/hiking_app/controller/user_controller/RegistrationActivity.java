package com.example.hiking_app.controller.user_controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


import androidx.appcompat.app.AppCompatActivity;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.model.Users;

import org.mindrot.jbcrypt.BCrypt;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextAddress;
    private ImageView imageViewProfile;
    private String encodedImage;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        Button buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open image picker when the button is clicked
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String plainPassword = editTextPassword.getText().toString();
                String email = editTextEmail.getText().toString();
                String address = editTextAddress.getText().toString();

                // Validate input fields (add your own validation logic)

                // Hash the password using BCrypt
                String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

                // Check if the user with the same username or email already exists
                if (isUserExists(username, email)) {
                    Toast.makeText(RegistrationActivity.this, "User with the same username or email already exists.", Toast.LENGTH_SHORT).show();
                } else {
                    // Create a new user object with the hashed password and encoded image
                    Users user = new Users(username, hashedPassword, email, address, encodedImage);

                    // Insert the user into the database
                    long userId = DbContext.getInstance(getApplicationContext()).appDao().insertUser(user);

                    // Handle registration success
                    if (userId > 0) {
                        Toast.makeText(RegistrationActivity.this, "Registration successful! Confirmation email sent.", Toast.LENGTH_SHORT).show();
                        // Navigate to another activity if needed
                    } else {
                        Toast.makeText(RegistrationActivity.this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        TextView signUpTextView = findViewById(R.id.signInTextView);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isUserExists(String username, String email) {
        // Query the database to check if the user with the given username or email already exists
        Users userByUsername = DbContext.getInstance(getApplicationContext()).appDao().getUserByUsername(username);
        Users userByEmail = DbContext.getInstance(getApplicationContext()).appDao().getUserByEmail(email);
        return userByUsername != null || userByEmail != null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Get the selected image as bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Display the selected image in ImageView
                imageViewProfile.setImageBitmap(bitmap);
                // Convert bitmap to Base64 string
                encodedImage = encodeImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

}

