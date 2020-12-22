package com.marcellinus.projectmovie.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.marcellinus.projectmovie.Application;
import com.marcellinus.projectmovie.R;
import com.marcellinus.projectmovie.Utils;
import com.marcellinus.projectmovie.dialog.LoadingDialog;
import com.marcellinus.projectmovie.model.Movie;
import com.marcellinus.projectmovie.repository.MovieRepository;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class DetailsActivity extends AppCompatActivity {

    public static final String MOVIE_DATA = "movie_data";

    private ImageView imgBackdrop, imgPoster, imgTMDb;
    private ImageButton imgbtnBack, imgbtnMarkAsFavorite;
    private TextView tvName, tvReleaseDate, tvOverview, tvAvgUserScore, tvUserCount;
    private Movie movie;

    private MovieRepository movieRepository = new MovieRepository((Application) getApplication());

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_details);

        final LoadingDialog loadingDialog = new LoadingDialog(DetailsActivity.this);
        loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        loadingDialog.show();

        findViews();
        setOnClickListeners();
        movie = getIntent().getParcelableExtra(MOVIE_DATA);
        movie.setReleaseDate(Utils.toDate2(movie.getStringReleaseDate()));

        if (movie.getIsFavorite() == 1) {
            imgbtnMarkAsFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star, null));
        }

        final String BACKDROP_IMAGE_URL = "https://image.tmdb.org/t/p/w342/" + movie.getBackdropPath(), POSTER_IMAGE_URL = "https://image.tmdb.org/t/p/w342/" + movie.getPosterPath();
        Glide.with(this)
                .load(BACKDROP_IMAGE_URL)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(5, 3)))
                .listener(
                        new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismiss();
                                    }
                                }, 200);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismiss();
                                    }
                                }, 200);
                                return false;
                            }
                        })
                .placeholder(R.drawable.backdrop_placeholder)
                .into(imgBackdrop);

        Glide.with(this)
                .load(POSTER_IMAGE_URL)
                .listener(
                        new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismiss();
                                    }
                                }, 200);

                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingDialog.dismiss();
                                    }
                                }, 200);
                                return false;
                            }
                        })
                .placeholder(R.drawable.poster_placeholder)
                .into(imgPoster);


        tvName.setText(movie.getTitle());
        String releaseDateText = "Released " + movie.getStringReleaseDate();
        tvReleaseDate.setText(releaseDateText);
        String overview = movie.getOverview();
        tvOverview.setText(overview.equals("") ? getResources().getString(R.string.no_overview_provided) : overview);
        String avgUserScoreText = movie.getVoteAverage() > 0
                ? "" + movie.getVoteAverage()
                : "-";
        tvAvgUserScore.setText(avgUserScoreText);
        tvAvgUserScore.setTextColor(getResources().getColor(Utils.getRatingColor(movie.getVoteAverage()), null));
        String userCountText = "voted by " + movie.getVoteCount() + " users";
        tvUserCount.setText(userCountText);
    }

    private void findViews() {
        imgBackdrop = findViewById(R.id.img_details_backdrop);
        imgPoster = findViewById(R.id.img_details_movieposter);
        imgTMDb = findViewById(R.id.img_details_tmdb);

        imgbtnBack = findViewById(R.id.imgbtn_details_back);
        imgbtnMarkAsFavorite = findViewById(R.id.imgbtn_details_markasfavorite);

        tvName = findViewById(R.id.tv_details_name);
        tvReleaseDate = findViewById(R.id.tv_details_releasedate);
        tvOverview = findViewById(R.id.tv_details_overview);
        tvAvgUserScore = findViewById(R.id.tv_details_avguserscore);
        tvUserCount = findViewById(R.id.tv_details_usercount);
    }

    private void setOnClickListeners() {
        imgbtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgbtnMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
            }
        });

        imgTMDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.themoviedb.org/movie/" + movie.getId())));
            }
        });
    }

    private void toggleFavorite() {
        if (movie.getIsFavorite() == 1) {
            movie.setIsFavorite(0);
            movieRepository.deleteFromDB(movie);
            Toast.makeText(this, getResources().getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show();
            imgbtnMarkAsFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_border, null));
        } else {
            movie.setIsFavorite(1);
            movieRepository.insertIntoDB(movie);
            Toast.makeText(this, getResources().getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();
            imgbtnMarkAsFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_star, null));
        }
    }
}
