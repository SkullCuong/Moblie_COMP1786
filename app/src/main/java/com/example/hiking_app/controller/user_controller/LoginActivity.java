package com.example.hiking_app.controller.user_controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.Fragment.ProfileFragment;
import com.example.hiking_app.MainActivity;
import com.example.hiking_app.MainActivity2;
import com.example.hiking_app.R;
import com.example.hiking_app.model.Users;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextUsername;

    private EditText editTextPassword;
    private DbContext dbContext;
    private SessionManager sessionManager;
    private Button buttonLogin;
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // Initialize DbContext and SessionManager
        dbContext = DbContext.getInstance(this.getApplicationContext());
        sessionManager = new SessionManager(this);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);



        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogout = findViewById(R.id.buttonLogout);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                // Validate username and password against the database
                if (isValidCredentials(username, password) != 0) {
                    int userId = isValidCredentials(username, password);
                    sessionManager.createLoginSession(userId);

                    setLoginStatus(true);

                    // Redirect to the next activity or perform other actions after successful login
                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class)
                    Intent profileIntent = new Intent(LoginActivity.this, ProfileActivity.class);

                    Intent profileIntent = new Intent(LoginActivity.this, ProfileFragment.class);

                    profileIntent.putExtra("userId", userId); // Pass the user ID to fetch user data in ProfileActivity
                    startActivity(intent);
                    finish(); // Close the LoginActivity

                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                } else {
                    // Login failed, show an error message
                    Toast.makeText(LoginActivity.this, "Invalid credentials. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout the user and clear the session
                sessionManager.logoutUser();
                

                // Redirect to the LoginActivity or perform other actions after logout
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Close the current activity
            }
        });
    }

    private int isValidCredentials(String username, String password) {
        // Query the database to check if the user exists with the provided username
        Users user = dbContext.appDao().getUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user.getId();
        }
       return 0;
    }
    private void setLoginStatus(boolean isLoggedIn) {
        SharedPreferences preferences = getSharedPreferences("login_status", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoggedIn", isLoggedIn);
        editor.apply();
    }
}

