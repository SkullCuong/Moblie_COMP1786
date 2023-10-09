package com.example.hiking_app.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "hikes", foreignKeys = {
        @ForeignKey(
                entity = Users.class,
                parentColumns = {"id"},
                childColumns ={"userId"},
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})
public class Hikes {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String location;
    private String date;
    private Boolean parking_available;
    private float length;
    private int difficulty;
    @Nullable
    private String description;
    @Nullable
    private String equipment;
    @Nullable
    private String quality;
    private int userId;

    public Hikes(String name, String location, String date, Boolean parking_available, float length, int difficulty, @Nullable String description, @Nullable String equipment, @Nullable String quality, int userId) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking_available = parking_available;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.equipment = equipment;
        this.quality = quality;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getParking_available() {
        return parking_available;
    }

    public void setParking_available(Boolean parking_available) {
        this.parking_available = parking_available;
    }

    public float getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = length;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(@Nullable String equipment) {
        this.equipment = equipment;
    }

    @Nullable
    public String getQuality() {
        return quality;
    }

    public void setQuality(@Nullable String quality) {
        this.quality = quality;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
