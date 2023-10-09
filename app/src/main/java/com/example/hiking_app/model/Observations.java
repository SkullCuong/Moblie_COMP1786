package com.example.hiking_app.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "observations", foreignKeys = {
        @ForeignKey(
                entity = Hikes.class,
                parentColumns = {"id"},
                childColumns = "hikeId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})
public class Observations {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String dateTime;
    @Nullable
    private String comment;
    private String photo;

    private int hikeId;

    public Observations(String name, String dateTime, @Nullable String comment, String photo, int hikeId) {
        this.name = name;
        this.dateTime = dateTime;
        this.comment = comment;
        this.photo = photo;
        this.hikeId = hikeId;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Nullable
    public String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }
}
