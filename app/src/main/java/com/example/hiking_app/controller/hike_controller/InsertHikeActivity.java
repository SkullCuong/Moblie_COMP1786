package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.databinding.ActivityInsertHikeBinding;
import com.example.hiking_app.model.Hikes;

import java.util.Calendar;

public class InsertHikeActivity extends AppCompatActivity {

    private ActivityInsertHikeBinding binding;
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_hike);
        binding = ActivityInsertHikeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.HikeAdd.setOnClickListener(v ->{
            insertHike();
        });
        binding.hikeDate.setOnClickListener(v ->{
            getCalendar();
        });
    }
    private void getCalendar() {
        Calendar calendar = Calendar.getInstance();
        int currentDay  = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth  = calendar.get(Calendar.MONTH);
        datePickerDialog = new DatePickerDialog(InsertHikeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.hikeDate.setText(day + "/" + (month + 1) + "/" +year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }

    private void insertHike() {
        String name = binding.hikeName.getText().toString().trim();
        String location = binding.hikeLocation.getText().toString().trim();
        String date = binding.hikeDate.getText().toString().trim();
        boolean parkingAV = true;
        float length = Float.parseFloat(binding.hikeLength.getText().toString().trim());
        int difficulty = Integer.parseInt(binding.hikeDifficulty.getText().toString().trim());
        String description = binding.hikeDescription.getText().toString().trim();
        String equipment = binding.hikeEquipment.getText().toString().trim();
        String quality = binding.hikeQuality.getText().toString().trim();
        int userId = 1;

        Hikes hike = new Hikes(name,location,date,parkingAV,length,difficulty,description,equipment,quality,userId);

        DbContext.getInstance(this.getApplicationContext()).appDao().insertHike(hike);
        showMessage("Add successful");
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}