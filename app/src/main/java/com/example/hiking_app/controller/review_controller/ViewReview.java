package com.example.hiking_app.controller.review_controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.hiking_app.DbContext;
import com.example.hiking_app.R;
import com.example.hiking_app.controller.hike_controller.ListHikeAdapter;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Reviews;

import java.util.List;

public class ViewReview extends AppCompatActivity {

    private List<Reviews> reviews;
    int hikeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        hikeId = getIntent().getIntExtra("hike_id", -1);
        reviews = DbContext.getInstance(this).appDao().getListReviewOfHike(hikeId);
        ListReviewAdapter listAdapter = new ListReviewAdapter(reviews,this);
        RecyclerView recyclerView = findViewById(R.id.listReviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
        //setListener();
    }
}