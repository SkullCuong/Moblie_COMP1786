package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.model.Hikes;

import java.util.List;

public class ViewHike extends AppCompatActivity {
    private List<Hikes> hikes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hikes);
        hikes = DbContext.getInstance(this).appDao().getListHike();
        ListHikeAdapter listAdapter = new ListHikeAdapter(hikes,this);
        RecyclerView recyclerView = findViewById(R.id.listHikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
        setListener();
    }

    private void setListener() {
    }

}