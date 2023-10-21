package com.example.hiking_app.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    long insertUser(Users user);
    @Query("SELECT * FROM users WHERE userName = :username")
    Users getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email")
    Users getUserByEmail(String email);
    @Insert
    long insertHike(Hikes hike);
    @Query("SELECT * FROM hikes ORDER BY id DESC ")
    List<Hikes> getListHike();
    @Insert
    long insertReview(Reviews review);
    @Insert
    long insertObservations(Observations observations);



}
