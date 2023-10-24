package com.example.hiking_app.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

import java.util.List;

@Dao
public interface AppDao {
    @Insert
    long insertUser(Users user);
    @Query("SELECT * FROM users WHERE id = :userId")
    Users findUserById(int userId);
    @Query("SELECT * FROM users WHERE userName = :username")
    Users getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email")
    Users getUserByEmail(String email);
    @Insert
    long insertHike(Hikes hike);
    @Query("SELECT * FROM hikes ORDER BY id DESC ")
    List<Hikes> getListHike();
    @Query("SELECT * FROM hikes WHERE id = :hikeId")
    Hikes findHikeById(int hikeId);
    @Query("DELETE FROM hikes WHERE id = :hikeId")
    void deleteHikeById(int hikeId);
    @Update
    void updateHike(Hikes hike);



    @Insert
    long insertReview(Reviews review);
    @Query("SELECT * FROM reviews WHERE hikeId = :hikeId ORDER BY id DESC ")
    List<Reviews> getListReviewOfHike(int hikeId);


    // Observation Start
    @Insert
    long insertObservations(Observations observations);

    @Query("SELECT * FROM observations WHERE hikeId =:hikeId")
    List<Observations> getObservationsByHikeId(int hikeId);

    @Query("SELECT * FROM observations WHERE id =:observationId")
    Observations getObservationById(int observationId);

    @Update
    void updateObservation(Observations observation);

    @Delete
    void deleteObservation(Observations observation);

    // Observation End

}
