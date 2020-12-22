package com.marcellinus.projectmovie.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.marcellinus.projectmovie.Application;
import com.marcellinus.projectmovie.R;
import com.marcellinus.projectmovie.Utils;
import com.marcellinus.projectmovie.activity.DetailsActivity;
import com.marcellinus.projectmovie.activity.SearchActivity;
import com.marcellinus.projectmovie.adapter.MovieAdapter;
import com.marcellinus.projectmovie.dialog.NoInternetDialog;
import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.viewmodel.MovieViewModel;
import com.marcellinus.projectmovie.viewmodel.ViewModelFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MovieFragment extends Fragment {
    private ProgressBar pbLoading;
    private MovieViewModel movieViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pbLoading = view.findViewById(R.id.pb_movies_loading);
        RecyclerView rvPopularMovies = view.findViewById(R.id.rv_movies_popular), rvUpcomingMovies = view.findViewById(R.id.rv_movies_upcoming);
        EditText etSearchBox = view.findViewById(R.id.et_movies_search);

        etSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoSearch = new Intent(getContext(), SearchActivity.class);
                startActivity(gotoSearch);
            }
        });

        movieViewModel = new ViewModelProvider(this, new ViewModelFactory(((Application) requireActivity().getApplication()).movieRepository)).get(MovieViewModel.class);

        final MovieAdapter popularMoviesAdapter = new MovieAdapter(view.getContext(), new MovieAdapter.Callback() {
            @Override
            public void onItemClick(Movie movie) {
                Intent gotoDetails = new Intent(getContext(), DetailsActivity.class);
                gotoDetails.putExtra(DetailsActivity.MOVIE_DATA, movie);
                startActivity(gotoDetails);
            }
        });
        final MovieAdapter upcomingMoviesAdapter = new MovieAdapter(view.getContext(), new MovieAdapter.Callback() {
            @Override
            public void onItemClick(Movie movie) {
                Intent gotoDetails = new Intent(getContext(), DetailsActivity.class);
                gotoDetails.putExtra(DetailsActivity.MOVIE_DATA, movie);
                startActivity(gotoDetails);
            }
        });

        rvPopularMovies.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rvPopularMovies.setNestedScrollingEnabled(false);
        rvPopularMovies.setAdapter(popularMoviesAdapter);

        rvUpcomingMovies.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));
        rvUpcomingMovies.setNestedScrollingEnabled(false);
        rvUpcomingMovies.setAdapter(upcomingMoviesAdapter);

        if (!movieViewModel.arePopularMoviesLoaded) {
            showLoading(true);
            movieViewModel.requestPopularMovies(Utils.INDONESIA_ISO_3166_1_COUNTRY_CODE, 1);
        }
        movieViewModel.getPopularMoviesStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == Utils.STATUS_SUCCESS) {
                    List<Movie> movies = movieViewModel.getPopularMovies();
                    List<Movie> favoriteMovies = movieViewModel.getFavoriteMovies();

                    for (Movie i : movies) {
                        for (Movie j : favoriteMovies) {
                            if (i.getId() == j.getId()) {
                                i.setIsFavorite(1);
                                favoriteMovies.remove(j);
                                break;
                            }
                        }
                    }

                    popularMoviesAdapter.setMovies(movies);
                    showLoading(false);
                } else {
                    NoInternetDialog dialog = new NoInternetDialog(requireContext());

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            requireActivity().moveTaskToBack(true);
                            requireActivity().finish();
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            movieViewModel.requestPopularMovies(Utils.INDONESIA_ISO_3166_1_COUNTRY_CODE, 1);
                        }
                    });

                    dialog.show();
                }
            }
        });

        if (!movieViewModel.areUpcomingMoviesLoaded) {
            showLoading(true);
            movieViewModel.requestUpcomingMovies(Utils.INDONESIA_ISO_3166_1_COUNTRY_CODE, 1);
        }
        movieViewModel.getUpcomingMoviesStatus().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer == Utils.STATUS_SUCCESS) {
                    List<Movie> movies = movieViewModel.getUpcomingMovies();
                    List<Movie> favoriteMovies = movieViewModel.getFavoriteMovies();

                    for (Movie i : movies) {
                        for (Movie j : favoriteMovies) {
                            if (i.getId() == j.getId()) {
                                i.setIsFavorite(1);
                                favoriteMovies.remove(j);
                                break;
                            }
                        }
                    }

                    upcomingMoviesAdapter.setMovies(movies);
                    showLoading(false);
                } else {
                    NoInternetDialog dialog = new NoInternetDialog(requireContext());

                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            requireActivity().moveTaskToBack(true);
                            requireActivity().finish();
                        }
                    });

                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            movieViewModel.requestUpcomingMovies(Utils.INDONESIA_ISO_3166_1_COUNTRY_CODE, 1);
                        }
                    });

                    dialog.show();
                }
            }
        });
    }

    private int loadingStackCounter;

    private void showLoading(boolean state) {
        loadingStackCounter = loadingStackCounter + (state ? 1 : -1);
        if (loadingStackCounter < 0) loadingStackCounter = 0;
        pbLoading.setVisibility(loadingStackCounter == 0 ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        movieViewModel.requestPopularMovies(Utils.INDONESIA_ISO_3166_1_COUNTRY_CODE, 1);
        movieViewModel.requestUpcomingMovies(Utils.INDONESIA_ISO_3166_1_COUNTRY_CODE, 1);
    }
}
