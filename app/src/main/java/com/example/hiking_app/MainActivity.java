package com.example.hiking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.hiking_app.model.Users;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Users user = new Users("123123","12312","12312","12312","12312");
        DbContext.getInstance(this.getApplicationContext()).appDao().insertUser(user);
    }
}