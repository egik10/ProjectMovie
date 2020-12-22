package com.marcellinus.projectmovie;

import com.marcellinus.projectmovie.repository.MovieRepository;

public class Application extends android.app.Application {
    public MovieRepository movieRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        movieRepository = new MovieRepository(this);
    }
}
