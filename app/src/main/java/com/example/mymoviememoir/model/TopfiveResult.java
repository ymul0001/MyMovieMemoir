package com.example.mymoviememoir.model;

public class TopfiveResult {
    private String movieName;
    private String movieRating;
    private String releaseDate;

    public TopfiveResult(String movieName, String movieRating, String releaseDate){
        this.movieName = movieName;
        this.movieRating = movieRating;
        this.releaseDate = releaseDate;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(String movieRating) {
        this.movieRating = movieRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
