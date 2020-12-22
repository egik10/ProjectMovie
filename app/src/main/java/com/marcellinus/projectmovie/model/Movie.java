package com.marcellinus.projectmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = Movie.TABLE_NAME)
public class Movie implements Parcelable {
    public static final String TABLE_NAME = "movies";

    @PrimaryKey
    private int id;

    // booleans (isAdult and hasVideo) is changed to int (0 = false, 1 = true)
    // genreIds and hasVideo are removed
    private String originalTitle;
    private String title;
    private Date releaseDate;
    private String originalLanguage;
    private int isAdult;
    private String overview;
    private String posterPath;
    private String backdropPath;
    private double popularity;
    private double voteAverage;
    private int voteCount;

    // Added fields
    private String stringReleaseDate;
    private int isFavorite;

    public Movie(int id, String originalTitle, String title, Date releaseDate, String originalLanguage, int isAdult, String overview, String posterPath, String backdropPath, double popularity, double voteAverage, int voteCount, String stringReleaseDate, int isFavorite) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.isAdult = isAdult;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.stringReleaseDate = stringReleaseDate;
        this.isFavorite = isFavorite;
    }

    // region Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public int isAdult() {
        return isAdult;
    }

    public void setAdult(int adult) {
        isAdult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getStringReleaseDate() {
        return stringReleaseDate;
    }

    public void setStringReleaseDate(String stringReleaseDate) {
        this.stringReleaseDate = stringReleaseDate;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    //endregion

    public int getReleaseYear() {
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(getReleaseDate());
        } catch (Exception e) {
            return 0;
        }

        return calendar.get(Calendar.YEAR);
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        title = in.readString();
        originalLanguage = in.readString();
        isAdult = in.readInt();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        popularity = in.readDouble();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        stringReleaseDate = in.readString();
        isFavorite = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // releaseDate is skipped (no writeDate function)
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(title);
        dest.writeString(originalLanguage);
        dest.writeInt(isAdult);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeDouble(voteAverage);
        dest.writeInt(voteCount);
        dest.writeString(stringReleaseDate);
        dest.writeInt(isFavorite);
    }
}
