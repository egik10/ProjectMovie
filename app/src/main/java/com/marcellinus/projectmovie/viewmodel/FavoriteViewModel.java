package com.marcellinus.projectmovie.viewmodel;

import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.repository.MovieRepository;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FavoriteViewModel extends ViewModel {
    private MutableLiveData<List<Movie>> favoriteMovies;
    private MovieRepository movieRepository;
    private int favoriteMovieCount;

    FavoriteViewModel(MovieRepository movieRepository) {
        favoriteMovies = new MutableLiveData<>();
        this.movieRepository = movieRepository;
    }

    public void loadFavoriteMovies() {
        movieRepository.loadFavoriteMovies(new MovieViewModel.Callback() {
            @Override
            public void onSuccess(List<Movie> response) {
                getFavoriteMovies().postValue(response);
                favoriteMovieCount = response.size();
            }
        });
    }

    public MutableLiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }

    public int getFavoriteMovieCount() {
        return favoriteMovieCount;
    }
}
