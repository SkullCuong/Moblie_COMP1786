package com.example.hiking_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Properties;


import androidx.appcompat.app.AppCompatActivity;

import com.example.hiking_app.model.Users;

import org.mindrot.jbcrypt.BCrypt;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private EditText editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAddress = findViewById(R.id.editTextAddress);

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
                    // Create a new user object with the hashed password
                    Users user = new Users(username, hashedPassword, email, address, null);

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
    }

    private boolean isUserExists(String username, String email) {
        // Query the database to check if the user with the given username or email already exists
        Users userByUsername = DbContext.getInstance(getApplicationContext()).appDao().getUserByUsername(username);
        Users userByEmail = DbContext.getInstance(getApplicationContext()).appDao().getUserByEmail(email);

        return userByUsername != null || userByEmail != null;
    }

}

