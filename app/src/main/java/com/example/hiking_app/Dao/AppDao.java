package com.example.hiking_app.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

@Dao
public interface AppDao {
    @Insert
    long insertUser(Users user);

    @Query("SELECT * FROM users WHERE userName = :username")
    Users getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email")
    Users getUserByEmail(String email);


}
