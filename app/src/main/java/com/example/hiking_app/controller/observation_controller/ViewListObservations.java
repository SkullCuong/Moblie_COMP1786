package com.example.hiking_app.controller.observation_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.observation_controller.ListObservationAdapter;
import com.example.hiking_app.model.Observations;

import java.util.List;

public class ViewListObservations extends AppCompatActivity {
    private List<Observations> observations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_observations);
        int hikeId = getIntent().getIntExtra("hike_id", -1);;
        observations = DbContext.getInstance(this).appDao().getObservationsByHikeId(hikeId);
        ListObservationAdapter listAdapter = new ListObservationAdapter(observations,this);
        RecyclerView recyclerView = findViewById(R.id.listObservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}