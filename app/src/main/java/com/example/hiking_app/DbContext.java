package com.example.hiking_app;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.hiking_app.Dao.AppDao;
import com.example.hiking_app.model.Hikes;
import com.example.hiking_app.model.Observations;
import com.example.hiking_app.model.Reviews;
import com.example.hiking_app.model.Users;

@Database(entities = {Users.class, Hikes.class, Observations.class, Reviews.class}, version = 1)
public abstract class DbContext extends RoomDatabase {
    private static final String Database_name = "DbContext_v1.db";
    private static DbContext instance;

    public static synchronized DbContext getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),DbContext.class,Database_name)
                    .allowMainThreadQueries()
                    .build();
        };
        return instance;
    }
    public abstract AppDao appDao();

}
