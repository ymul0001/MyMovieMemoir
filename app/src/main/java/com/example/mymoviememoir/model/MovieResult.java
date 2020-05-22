package com.example.mymoviememoir.model;

public class MovieResult {
    private int movieId;
    private String movieImagePath;
    private String movieTitle;
    private String movieReleaseDate;

    public MovieResult(int movieId, String movieImagePath, String movieTitle, String movieReleaseDate) {
        this.movieId = movieId;
        this.movieImagePath = "https://image.tmdb.org/t/p/w780" + movieImagePath;
        this.movieTitle = movieTitle;
        this.movieReleaseDate = movieReleaseDate;
    }

    public int getMovieId(){
        return movieId;
    }

    public void setMovieId(){
        this.movieId = movieId;
    }

    public String getMovieImagePath() {
        return movieImagePath;
    }

    public void setMovieImagePath(String movieImagePath) {
        this.movieImagePath = movieImagePath;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }
}
