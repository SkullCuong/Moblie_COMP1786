package com.example.hiking_app.controller.hike_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import androidx.appcompat.widget.SearchView;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.model.Hikes;

import java.util.List;

public class ViewHike extends AppCompatActivity {
    private List<Hikes> hikes;
    private SearchView searchView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hikes);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        hikes = DbContext.getInstance(this).appDao().getListHike();
        ListHikeAdapter listAdapter = new ListHikeAdapter(hikes,this);
        RecyclerView recyclerView = findViewById(R.id.listHikes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
        setListener(listAdapter);
    }

    private void setListener(ListHikeAdapter listAdapter ) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newQuery) {
                hikes = DbContext.getInstance(ViewHike.this).appDao().getListHikeByName(newQuery);
                listAdapter.setFilteredList(hikes);
                return false;
            }
        });
    }

}