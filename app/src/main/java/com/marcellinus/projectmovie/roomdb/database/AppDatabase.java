package com.marcellinus.projectmovie.roomdb.database;

import android.content.Context;

import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.roomdb.dao.MovieDao;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "app_database";
    private static volatile AppDatabase INSTANCE;

    public static synchronized AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME).build();
        }
        return INSTANCE;
    }

    public abstract MovieDao movieDao();
}
