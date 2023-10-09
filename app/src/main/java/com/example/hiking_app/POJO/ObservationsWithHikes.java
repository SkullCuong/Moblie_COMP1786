package com.example.hiking_app.POJO;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;
import com.example.hiking_app.model.Users;

import java.util.List;

public class ObservationsWithHikes {
    @Embedded
    Observations observation;

    @Relation(entity = Hikes.class,parentColumn = "id",entityColumn = "hikeId")
    List<Hikes> hikeList;
}
