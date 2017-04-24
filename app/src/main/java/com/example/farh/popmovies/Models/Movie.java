package com.example.farh.popmovies.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by farh on 17/10/2016.
 */

public class Movie implements Parcelable{
    private String posterPath;
    private String overview;
    private String id;
    private String title;
    private String backdrop;
    private String voteAverage;
    private String date;

    public Movie() {
    }

    public Movie(String posterPath, String overview, String id, String title, String backdrop, String voteAverage, String date) {
        this.posterPath = posterPath;
        this.overview = overview;
        this.id = id;
        this.title = title;
        this.backdrop = backdrop;
        this.voteAverage = voteAverage;
        this.date = date;
    }

    protected Movie(Parcel in) {
        posterPath = in.readString();
        overview = in.readString();
        id = in.readString();
        title = in.readString();
        backdrop = in.readString();
        voteAverage = in.readString();
        date = in.readString();
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

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(backdrop);
        dest.writeString(date);
        dest.writeString(voteAverage);

    }
}
