package com.marcellinus.projectmovie.viewmodel;

import com.marcellinus.projectmovie.repository.MovieRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private MovieRepository movieRepository;

    public ViewModelFactory(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MovieViewModel.class))
            return (T) new MovieViewModel(movieRepository);
        if (modelClass.isAssignableFrom(FavoriteViewModel.class))
            return (T) new FavoriteViewModel(movieRepository);
        if (modelClass.isAssignableFrom(SearchViewModel.class))
            return (T) new SearchViewModel(movieRepository);

        return null;
    }
}
