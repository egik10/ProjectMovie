package com.marcellinus.projectmovie.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marcellinus.projectmovie.Application;
import com.marcellinus.projectmovie.R;
import com.marcellinus.projectmovie.activity.DetailsActivity;
import com.marcellinus.projectmovie.adapter.MovieAdapter;
import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.viewmodel.FavoriteViewModel;
import com.marcellinus.projectmovie.viewmodel.ViewModelFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteFragment extends Fragment {
    private RecyclerView rvFavoriteMovies;
    private ProgressBar pbLoading;
    private TextView tvNoFavorite;
    private FavoriteViewModel favoriteViewModel;
    private MovieAdapter movieAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findViews(view);
        favoriteViewModel = new ViewModelProvider(this, new ViewModelFactory(((Application) getActivity().getApplication()).movieRepository)).get(FavoriteViewModel.class);

        movieAdapter = new MovieAdapter(view.getContext(), new MovieAdapter.Callback() {
            @Override
            public void onItemClick(Movie movie) {
                Intent gotoDetails = new Intent(getContext(), DetailsActivity.class);
                gotoDetails.putExtra(DetailsActivity.MOVIE_DATA, movie);
                startActivity(gotoDetails);
            }
        });
        rvFavoriteMovies.setLayoutManager(new GridLayoutManager(view.getContext(), 2, RecyclerView.VERTICAL, false));
        rvFavoriteMovies.setNestedScrollingEnabled(false);
        rvFavoriteMovies.setAdapter(movieAdapter);

        tvNoFavorite.setVisibility(View.GONE);
        showLoading(true);

        favoriteViewModel.loadFavoriteMovies();
        favoriteViewModel.getFavoriteMovies().observe(getViewLifecycleOwner(), new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if (movies.size() != 0) movieAdapter.setMovies(movies);
                setVisibilities();
                showLoading(false);
            }
        });
    }

    private void setVisibilities() {
        tvNoFavorite.setVisibility(favoriteViewModel.getFavoriteMovieCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private void findViews(View view) {
        rvFavoriteMovies = view.findViewById(R.id.rv_favorites_movies);
        pbLoading = view.findViewById(R.id.pb_favorites_loading);
        tvNoFavorite = view.findViewById(R.id.tv_favorites_nofavorite);
    }

    private void showLoading(boolean state) {
        pbLoading.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();

        favoriteViewModel.loadFavoriteMovies();
    }
}
