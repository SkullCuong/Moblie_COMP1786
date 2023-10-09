package com.example.hiking_app.POJO;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

import java.util.List;

public class HikesWithUsers {
    @Embedded
    Hikes hike;

    @Relation(entity = Users.class,parentColumn = "id",entityColumn = "userId")
    List<Users> userList;
}
