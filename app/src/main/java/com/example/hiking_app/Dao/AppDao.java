package com.example.hiking_app.Dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

@Dao
public interface AppDao {
    @Insert
    long insertUser(Users user);

}
