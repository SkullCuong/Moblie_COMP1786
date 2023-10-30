package com.example.hiking_app;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.example.hiking_app.Fragment.AddHikeFragment;
import com.example.hiking_app.Fragment.DetailsHikeFragment;
import com.example.hiking_app.Fragment.HomeFragment;
import com.example.hiking_app.Fragment.ProfileFragment;
import com.example.hiking_app.Fragment.SettingFragment;
import com.example.hiking_app.Fragment.ViewHikeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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

public class MainActivity2 extends AppCompatActivity {
    // Menu Bar Navigation
    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    private ActivityMain2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

        replaceFragment(new HomeFragment());
        // Bottom Menu Fragment
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawer(GravityCompat.START);
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    replaceFragment(new HomeFragment());
                } else if (itemId == R.id.nav_settings) {
                    replaceFragment(new SettingFragment());
                } else if (itemId == R.id.nav_share) {
                    replaceFragment(new DetailsHikeFragment());
                } else if (itemId == R.id.nav_about) {
                    replaceFragment(new ProfileFragment());
                } else if (itemId == R.id.nav_logout) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.subscriptions) {
                replaceFragment(new DetailsHikeFragment());
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

    // Menu bar Outside Oncreate
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}