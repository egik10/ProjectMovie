package com.marcellinus.projectmovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.marcellinus.projectmovie.R;
import com.marcellinus.projectmovie.model.Movie;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private Context context;
    private List<Movie> movies = new ArrayList<>();
    private Callback callback;

    public MovieAdapter(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    public void setMovies(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_movie, viewGroup, false);
        return new MovieAdapter.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int position) {
        movieViewHolder.bind(movies.get(position), position);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMoviePoster;
        private TextView tvMovieName, tvMovieReleaseDate;

        MovieViewHolder(@NonNull final View view) {
            super(view);
            imgMoviePoster = view.findViewById(R.id.img_movie_poster);
            tvMovieName = view.findViewById(R.id.tv_movie_name);
            tvMovieReleaseDate = view.findViewById(R.id.tv_movie_releasedate);
        }

        void bind(final Movie movie, final int position) {
            final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w185/";
            String imageUrl = BASE_IMAGE_URL + movie.getPosterPath();
            Glide.with(context)
                    .load(imageUrl)
                    .apply(new RequestOptions().override(200, 300))
                    .placeholder(R.drawable.poster_placeholder)
                    .into(imgMoviePoster);

            tvMovieName.setText(movie.getTitle());
            tvMovieReleaseDate.setText(movie.getStringReleaseDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onItemClick(movies.get(position));
                }
            });
        }
    }

    public interface Callback {
        void onItemClick(Movie movie);
    }
}
