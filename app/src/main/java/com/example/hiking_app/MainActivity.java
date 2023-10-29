package com.example.hiking_app;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hiking_app.Fragment.AddHikeFragment;
import com.example.hiking_app.Fragment.DetailsHikeFragment;
import com.example.hiking_app.Fragment.HomeFragment;
import com.example.hiking_app.Fragment.ViewHikeFragment;
import com.example.hiking_app.controller.hike_controller.InsertHikeActivity;
import com.example.hiking_app.controller.observation_controller.InsertObservation;
import com.example.hiking_app.controller.hike_controller.ViewHike;
import com.example.hiking_app.controller.user_controller.LoginActivity;
import com.example.hiking_app.controller.user_controller.ProfileActivity;
import com.example.hiking_app.controller.user_controller.RegistrationActivity;
import com.example.hiking_app.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    // View Binding
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
        Button buttonGoToProfile = findViewById(R.id.buttonGoToProfile);
        buttonGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to ProfileActivity when the button is clicked
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                // Pass the necessary user data to the ProfileActivity if needed
                // intent.putExtra("key", value);
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