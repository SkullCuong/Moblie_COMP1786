package com.example.hiking_app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.hiking_app.databinding.ActivityInsertHikeBinding;
import com.example.hiking_app.databinding.ActivityInsertObservationBinding;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;

import java.util.Calendar;

public class InsertObservation extends AppCompatActivity {
    private ActivityInsertObservationBinding binding;
    private DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_observation);
        binding = ActivityInsertObservationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListener();
    }

    private void setListener() {
        binding.ObAdd.setOnClickListener(v ->{
            insertOb();
        });
        binding.ObDate.setOnClickListener(v ->{
            getCalendar();
        });
    }
    private void insertOb() {
        String name = binding.ObName.getText().toString().trim();
        String date = binding.ObDate.getText().toString().trim();
        String comment = binding.ObComment.getText().toString().trim();
        String photo = binding.ObPhoto.getText().toString().trim();
        int hikeId = 1;

        Observations observation = new Observations(name,date,comment,photo,hikeId);

        DbContext.getInstance(this.getApplicationContext()).appDao().insertObservations(observation);
        showMessage("Add observation successful");
    }

    private void getCalendar() {
        Calendar calendar = Calendar.getInstance();
        int currentDay  = calendar.get(Calendar.DAY_OF_MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth  = calendar.get(Calendar.MONTH);
        datePickerDialog = new DatePickerDialog(InsertObservation.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                binding.ObDate.setText(day + "/" + (month + 1) + "/" +year);
            }
        },currentYear,currentMonth,currentDay);
        datePickerDialog.show();
    }
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
}