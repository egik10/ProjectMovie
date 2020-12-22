package com.marcellinus.projectmovie.repository;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.marcellinus.projectmovie.Application;
import com.marcellinus.projectmovie.BuildConfig;
import com.marcellinus.projectmovie.R;
import com.marcellinus.projectmovie.Utils;
import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.roomdb.dao.MovieDao;
import com.marcellinus.projectmovie.roomdb.database.AppDatabase;
import com.marcellinus.projectmovie.viewmodel.MovieViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MovieRepository {
    private final String TAG_REQUEST_ERROR = "[ERROR] GET REQUEST", TAG_RELEASE_DATE_ERROR = "[ERROR] RELEASE DATE", TAG_INSERT_ERROR = "[ERROR] DB INSERT", TAG_DELETE_ERROR = "[ERROR] DB DELETE";
    private final String API_KEY = BuildConfig.TMDB_API_KEY;
    private final String BASE_TMDB_API_URL = "https://api.themoviedb.org/3/";
    private final MovieDao movieDao;
    private com.marcellinus.projectmovie.Application application;

    public MovieRepository(Application application) {
        this.application = application;
        AppDatabase db = AppDatabase.getDatabase(application);
        movieDao = db.movieDao();
    }

    public void loadFavoriteMovies(final MovieViewModel.Callback callback) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(movieDao.getAll());
            }
        });
    }

    // countryCode must be in ISO 3166-1 format (Indonesia = "IN", USA = "US", China = "CN", etc..)
    // page must be between 1-1000

    public void loadPopularMovies(String countryCode, int page, final MovieViewModel.Callback callback) {
        final String REQUEST_URL = BASE_TMDB_API_URL + "movie/popular" + "?api_key=" + API_KEY + "&region=" + countryCode + "&page=" + page;
        request(REQUEST_URL, callback);
    }

    public void loadUpcomingMovies(String countryCode, int page, final MovieViewModel.Callback callback) {
        final String REQUEST_URL = BASE_TMDB_API_URL + "movie/upcoming" + "?api_key=" + API_KEY + "&region=" + countryCode + "&page=" + page;
        request(REQUEST_URL, callback);
    }

    public void loadMoviesByQuery(String countryCode, int page, String query, final MovieViewModel.Callback callback) {
        final String REQUEST_URL = BASE_TMDB_API_URL + "search/movie" + "?api_key=" + API_KEY + "&region=" + countryCode + "&page=" + page + "&query=" + query;
        request(REQUEST_URL, callback);
    }

    // TODO: Do pagination
    private void request(String url, final MovieViewModel.Callback callback) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("results");

                            ArrayList<Movie> movies = new ArrayList<>();
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject curr = results.getJSONObject(i);

                                Date releaseDate = null;
                                String stringReleaseDate = application.getResources().getString(R.string.unknown);
                                try {
                                    releaseDate = Utils.toDate(curr.getString("release_date"));
                                    stringReleaseDate = Utils.toStringFormat2(releaseDate);
                                } catch (Exception e) {
                                    Log.e(TAG_RELEASE_DATE_ERROR, Objects.requireNonNull(e.getMessage()));
                                }
                                movies.add(new Movie(curr.getInt("id"), curr.getString("original_title"), curr.getString("title"), releaseDate,
                                        curr.getString("original_language"), curr.getBoolean("adult") ? 1 : 0, curr.getString("overview"),
                                        curr.getString("poster_path"), curr.getString("backdrop_path"), curr.getDouble("popularity"),
                                        curr.getDouble("vote_average"), curr.getInt("vote_count"), stringReleaseDate, 0)
                                );
                            }
                            callback.onSuccess(movies);

                        } catch (JSONException e) {
                            Log.e(TAG_REQUEST_ERROR, Objects.requireNonNull(e.getMessage()));
                            callback.onSuccess(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.e(TAG_REQUEST_ERROR, Objects.requireNonNull(e.getMessage()));
                        callback.onSuccess(null);
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(application.getApplicationContext());
        requestQueue.add(request);
    }

    public void insertIntoDB(final Movie movie) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    movieDao.insert(movie);
                } catch (Exception e) {
                    Log.e(TAG_INSERT_ERROR, Objects.requireNonNull(e.getMessage()));
                }
            }
        });
    }

    public void deleteFromDB(final Movie movie) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    movieDao.delete(movie);
                } catch (Exception e) {
                    Log.e(TAG_DELETE_ERROR, Objects.requireNonNull(e.getMessage()));
                }
            }
        });
    }
}
