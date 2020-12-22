package com.marcellinus.projectmovie.activity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marcellinus.projectmovie.Application;
import com.marcellinus.projectmovie.R;
import com.marcellinus.projectmovie.Utils;
import com.marcellinus.projectmovie.adapter.MovieAdapter;
import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.viewmodel.SearchViewModel;
import com.marcellinus.projectmovie.viewmodel.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SearchActivity extends AppCompatActivity {
    private ImageButton imgbtnBack;
    private SearchView svSearch;
    private ProgressBar pbLoading;
    private TextView tvNoResultFound;
    private RecyclerView rvFoundMovies;

    private SearchViewModel searchViewModel;
    private MovieAdapter movieAdapter;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_search);

        findViews();
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            svSearch.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            svSearch.setIconifiedByDefault(false);
            svSearch.setMaxWidth(Integer.MAX_VALUE);
            svSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    svSearch.clearFocus();
                    if (query.length() > 0) {
                        tvNoResultFound.setVisibility(View.GONE);
                        showLoading(true);

                        searchViewModel.searchMovies(Utils.INDONESIA_ISO_3166_1_COUNTRY_CODE, 1, query);
                        searchViewModel.getSearchMoviesStatus().observe(SearchActivity.this, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                if (integer == Utils.STATUS_SUCCESS) {
                                    List<Movie> movies = searchViewModel.getFoundMovies();
                                    List<Movie> favoriteMovies = searchViewModel.getFavoriteMovies();

                                    for (Movie i : movies) {
                                        for (Movie j : favoriteMovies) {
                                            if (i.getId() == j.getId()) {
                                                i.setIsFavorite(1);
                                                favoriteMovies.remove(j);
                                                break;
                                            }
                                        }
                                    }

                                    movieAdapter.setMovies(movies);
                                }

                                setVisibilities();
                                showLoading(false);
                            }
                        });
                    } else {
                        movieAdapter.setMovies(new ArrayList<Movie>());
                    }

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }

        searchViewModel = new ViewModelProvider(this, new ViewModelFactory(((Application) getApplication()).movieRepository)).get(SearchViewModel.class);

        movieAdapter = new MovieAdapter(this, new MovieAdapter.Callback() {
            @Override
            public void onItemClick(Movie movie) {
                Intent gotoDetails = new Intent(SearchActivity.this, DetailsActivity.class);
                gotoDetails.putExtra(DetailsActivity.MOVIE_DATA, movie);
                startActivity(gotoDetails);
            }
        });
        rvFoundMovies.setLayoutManager(new GridLayoutManager(this, 2, RecyclerView.VERTICAL, false));
        rvFoundMovies.setAdapter(movieAdapter);
    }

    private void findViews() {
        imgbtnBack = findViewById(R.id.imgbtn_search_back);
        svSearch = findViewById(R.id.sv_search);
        rvFoundMovies = findViewById(R.id.rv_search_movies);
        pbLoading = findViewById(R.id.pb_search_loading);
        tvNoResultFound = findViewById(R.id.tv_search_noresultfound);
    }

    private void setVisibilities() {
        tvNoResultFound.setVisibility(movieAdapter.getItemCount() > 0 ? View.GONE : View.VISIBLE);
    }

    private void showLoading(boolean state) {
        pbLoading.setVisibility(state ? View.VISIBLE : View.GONE);
    }
}

