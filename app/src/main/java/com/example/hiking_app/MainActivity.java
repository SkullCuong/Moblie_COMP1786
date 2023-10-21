package com.example.hiking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.hiking_app.controller.hike_controller.InsertHikeActivity;
import com.example.hiking_app.controller.hike_controller.ViewHike;
import com.example.hiking_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
        Button buttonGoToRegistration = findViewById(R.id.buttonGoToRegistration);
        buttonGoToRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to RegistrationActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
        Button buttonGoToLogin = findViewById(R.id.buttonGoToLogin);
        buttonGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to LoginActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setListener() {
        binding.InsertHike.setOnClickListener(v ->{
            Intent intent = new Intent(this, InsertHikeActivity.class);
            startActivity(intent);
        });
        binding.InsertObservationButton.setOnClickListener(v ->{
            Intent intent = new Intent(this, InsertObservation.class);
            startActivity(intent);
        });
        binding.ViewHike.setOnClickListener(v ->{
            Intent intent = new Intent(this, ViewHike.class);
            startActivity(intent);
        });
    }
    private void insertObservation(){

    }


    //private Boolean checkValidDate() {
    //}
}