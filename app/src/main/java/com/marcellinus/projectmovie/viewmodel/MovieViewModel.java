package com.marcellinus.projectmovie.viewmodel;

import com.marcellinus.projectmovie.Utils;
import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MovieViewModel extends ViewModel {
    public boolean arePopularMoviesLoaded, areUpcomingMoviesLoaded;
    private MutableLiveData<Integer> popularMoviesStatus, upcomingMoviesStatus;
    private MovieRepository movieRepository;
    private List<Movie> popularMovies, upcomingMovies, favoriteMovies;

    MovieViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        popularMovies = new ArrayList<>();
        upcomingMovies = new ArrayList<>();
        favoriteMovies = new ArrayList<>();
        popularMoviesStatus = new MutableLiveData<>();
        upcomingMoviesStatus = new MutableLiveData<>();
    }

    public void requestPopularMovies(String regionCode, int page) {
        movieRepository.loadPopularMovies(regionCode, page, new MovieViewModel.Callback() {
            @Override
            public void onSuccess(List<Movie> response) {
                if (response == null) {
                    popularMoviesStatus.postValue(Utils.STATUS_ERROR);
                } else {
                    popularMovies.clear();
                    popularMovies.addAll(response);
                    loadFavoriteMovies(1);
                    arePopularMoviesLoaded = true;
                }
            }
        });
    }

    public void requestUpcomingMovies(String regionCode, int page) {
        movieRepository.loadUpcomingMovies(regionCode, page, new MovieViewModel.Callback() {
            @Override
            public void onSuccess(List<Movie> response) {
                if (response == null) {
                    upcomingMoviesStatus.postValue(Utils.STATUS_ERROR);
                } else {
                    upcomingMovies.clear();
                    upcomingMovies.addAll(response);
                    loadFavoriteMovies(2);
                    areUpcomingMoviesLoaded = true;
                }
            }
        });
    }

    // callerId (1 = /movie/popular, 2 = /movie/upcoming)
    private void loadFavoriteMovies(final int callerId) {
        movieRepository.loadFavoriteMovies(new MovieViewModel.Callback() {
            @Override
            public void onSuccess(List<Movie> response) {
                if (response == null) response = new ArrayList<>();
                favoriteMovies.clear();
                favoriteMovies.addAll(response);

                if (callerId == 1) popularMoviesStatus.postValue(Utils.STATUS_SUCCESS);
                else if (callerId == 2) upcomingMoviesStatus.postValue(Utils.STATUS_SUCCESS);
            }
        });
    }

    public List<Movie> getPopularMovies() {
        return popularMovies;
    }

    public List<Movie> getUpcomingMovies() {
        return upcomingMovies;
    }

    public List<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }

    public MutableLiveData<Integer> getPopularMoviesStatus() {
        return popularMoviesStatus;
    }

    public MutableLiveData<Integer> getUpcomingMoviesStatus() {
        return upcomingMoviesStatus;
    }

    public interface Callback {
        void onSuccess(List<Movie> response);
    }
}
