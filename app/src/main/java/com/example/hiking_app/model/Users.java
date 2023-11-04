package com.example.hiking_app.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class Users {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userName;
    private String password;
    private String email;
    private String address;

    public Users(String userName, String password, String email, String address, String profile_Picture) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.profile_Picture = profile_Picture;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_Picture() {
        return profile_Picture;
    }

    public void setProfile_Picture(String profile_Picture) {
        this.profile_Picture = profile_Picture;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    private String profile_Picture;
}
