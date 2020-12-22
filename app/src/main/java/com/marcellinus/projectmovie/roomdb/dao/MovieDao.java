package com.marcellinus.projectmovie.roomdb.dao;

import android.database.Cursor;

import com.marcellinus.projectmovie.model.Movie;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM " + Movie.TABLE_NAME)
    List<Movie> getAll();

    @Query("SELECT * FROM " + Movie.TABLE_NAME)
    Cursor getAllCursor();

    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);
}
