package com.example.mymoviememoir.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.util.Date;

@Entity(tableName = "watchlist")
public class WatchList {

    @PrimaryKey
    private int movieId;
    @ColumnInfo(name = "movie_name")
    private String movieName;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "added_date")
    private String addedDate;
    @ColumnInfo(name = "added_time")
    private String addedTime;

    public WatchList(int movieId, String movieName, String releaseDate, String addedDate, String addedTime) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.addedDate = addedDate;
        this.addedTime = addedTime;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }
}
