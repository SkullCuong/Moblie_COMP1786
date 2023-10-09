package com.example.hiking_app.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "reviews",foreignKeys = {
        @ForeignKey(
                entity = Hikes.class,
                parentColumns = "id",
                childColumns = "hikeId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = Users.class,
                parentColumns = "id",
                childColumns = "userId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
})
public class Reviews {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int rating;
    private String comment;
    private int userId;

    private int hikeId;

    public Reviews(int rating, String comment, int userId, int hikeId) {
        this.rating = rating;
        this.comment = comment;
        this.userId = userId;
        this.hikeId = hikeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getHikeId() {
        return hikeId;
    }

    public void setHikeId(int hikeId) {
        this.hikeId = hikeId;
    }
}
