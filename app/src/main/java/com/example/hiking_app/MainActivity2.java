package com.example.hiking_app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.hiking_app.Fragment.AddHikeFragment;
import com.example.hiking_app.Fragment.DetailsHikeFragment;
import com.example.hiking_app.Fragment.HomeFragment;
import com.example.hiking_app.Fragment.ProfileFragment;
import com.example.hiking_app.Fragment.SettingFragment;
import com.example.hiking_app.Fragment.ViewHikeFragment;
import com.example.hiking_app.controller.user_controller.SessionManager;
import com.example.hiking_app.model.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hiking_app.databinding.ActivityMain2Binding;
import com.makeramen.roundedimageview.RoundedImageView;

public class MainActivity2 extends AppCompatActivity {
    // Menu Bar Navigation
    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    private ActivityMain2Binding binding;
    private DbContext dbContext;
    private SessionManager sessionManager;
    private TextView emailEditText;
    private TextView usernameEditText;
    private RoundedImageView profileImageView;

    private String city ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // user Image
        dbContext = DbContext.getInstance(this.getApplicationContext());
        sessionManager = new SessionManager(this);
        // Header Nav bar
        usernameEditText = findViewById(R.id.userNameNavbar);
        emailEditText = findViewById(R.id.emailNavbar);
        profileImageView = findViewById(R.id.imageNavbar);
        city = getIntent().getStringExtra("city") == null ? "":getIntent().getStringExtra("city") ;
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


        // Menu Bar Navigation Bottom
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Nav Bar Left side
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        initPage();


        // Bottom Menu Fragment
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    replaceFragment(new HomeFragment());
                }
                else if (itemId == R.id.nav_weather) {
                    replaceFragment(new  WeatherForecast());
                }
                else if (itemId == R.id.nav_hiking) {
                    replaceFragment(new AddHikeFragment());
                }
                else if (itemId == R.id.nav_settings) {
                    replaceFragment(new SettingFragment());
                }
                else if (itemId == R.id.nav_support) {
                    replaceFragment(new SettingFragment());
                }
                else if (itemId == R.id.nav_logout) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.weather) {
                replaceFragment(new WeatherForecast());
            } else if (itemId == R.id.add) {
                replaceFragment(new AddHikeFragment());
            } else if (itemId == R.id.settings) {
                replaceFragment(new SettingFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }
            return true;
        });
    }

    private void populateUI(Users user) {
        usernameEditText.setText(user.getUserName());
        emailEditText.setText(user.getEmail());

        if (user.getProfile_Picture() != null && !user.getProfile_Picture().isEmpty()) {
            byte[] decodedString = Base64.decode(user.getProfile_Picture(), Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageView.setImageBitmap(decodedBitmap);
        }
    }
    private void  initPage(){
        Intent intent = getIntent();
        if (intent != null) {
            String fragmentToLoad = intent.getStringExtra("FRAGMENT_TO_LOAD");
            if ("AddHikeFragment".equals(fragmentToLoad)) {
                AddHikeFragment addHikeFragment = new AddHikeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle hike = new Bundle();
                hike.putString("address",intent.getStringExtra("address"));
                hike.putDouble("latitude",intent.getDoubleExtra("latitude",1000));
                hike.putDouble("longitude",intent.getDoubleExtra("longitude",1000));
                hike.putString("name",getIntent().getStringExtra("name"));
                hike.putString("date",intent.getStringExtra("date"));
                hike.putBoolean("parkingAvailable",getIntent().getBooleanExtra("parkingAvailable",false));
                hike.putString("length",intent.getStringExtra("length"));
                hike.putString("difficulty",intent.getStringExtra("difficulty"));
                hike.putString("description",intent.getStringExtra("description"));
                hike.putString("equipment",intent.getStringExtra("equipment"));
                hike.putString("quality",intent.getStringExtra("quality"));
                addHikeFragment.setArguments(hike);
                fragmentTransaction.replace(R.id.frame_layout, addHikeFragment).commit();
            }else if("AddHikeFragmentForConfirm".equals(fragmentToLoad)){
                AddHikeFragment addHikeFragment = new AddHikeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle hike = new Bundle();
                hike.putInt("hike_id", intent.getIntExtra("hike_id",-1));
                addHikeFragment.setArguments(hike);
                fragmentTransaction.replace(R.id.frame_layout, addHikeFragment).commit();
            }else if("HikeDetailsToListHikes".equals(fragmentToLoad)){
                DetailsHikeFragment detailsHikeFragment = new DetailsHikeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                Bundle hike = new Bundle();
                hike.putInt("hike_id", intent.getIntExtra("hike_id",-1));
                detailsHikeFragment.setArguments(hike);
                fragmentTransaction.replace(R.id.frame_layout, detailsHikeFragment).commit();
            } else if (!city.isEmpty()) {
                WeatherForecast weatherForecast = new WeatherForecast();
                Bundle weather = new Bundle();
                weather.putString("city",city);
                weatherForecast.setArguments(weather);
                replaceFragment(weatherForecast);
            } else{
               replaceFragment(new HomeFragment());
            }
        }

    }
    // Menu bar Outside Oncreate
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}