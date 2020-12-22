package com.marcellinus.projectmovie.viewmodel;

import com.marcellinus.projectmovie.Utils;
import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.repository.MovieRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {
    private List<Movie> foundMovies, favoriteMovies;
    private MutableLiveData<Integer> searchMoviesStatus;
    private MovieRepository movieRepository;

    SearchViewModel(MovieRepository movieRepository) {
        foundMovies = new ArrayList<>();
        favoriteMovies = new ArrayList<>();
        searchMoviesStatus = new MutableLiveData<>();
        this.movieRepository = movieRepository;
    }

    public void searchMovies(String countryCode, int page, final String query) {
        movieRepository.loadMoviesByQuery(countryCode, page, query, new MovieViewModel.Callback() {
            @Override
            public void onSuccess(List<Movie> response) {
                if (response == null) {
                    searchMoviesStatus.postValue(Utils.STATUS_ERROR);
                } else {
                    foundMovies.clear();
                    foundMovies.addAll(response);
                    loadFavoriteMovies();
                }
            }
        });
    }

    private void loadFavoriteMovies() {
        movieRepository.loadFavoriteMovies(new MovieViewModel.Callback() {
            @Override
            public void onSuccess(List<Movie> response) {
                if (response == null) response = new ArrayList<>();
                favoriteMovies.clear();
                favoriteMovies.addAll(response);
                searchMoviesStatus.postValue(Utils.STATUS_SUCCESS);
            }
        });
    }

    public MutableLiveData<Integer> getSearchMoviesStatus() {
        return searchMoviesStatus;
    }

    public List<Movie> getFoundMovies() {
        return foundMovies;
    }

    public List<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }
}
