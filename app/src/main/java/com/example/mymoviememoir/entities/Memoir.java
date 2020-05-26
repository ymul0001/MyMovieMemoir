package com.example.mymoviememoir.entities;

public class Memoir {
    private int memoirId;
    private String movieName;
    private String movieReleasedate;
    private String movieWatchdate;
    private String movieWatchtime;
    private String movieComment;
    private float movieRating;
    private PersonId personId;
    private CinemaId cinemaId;

    public Memoir(int memoirId, String movieName, String movieReleasedate, String movieWatchdate, String movieWatchtime, String movieComment, float movieRating, PersonId personId, CinemaId cinemaId) {
        this.memoirId = memoirId;
        this.movieName = movieName;
        this.movieReleasedate = movieReleasedate;
        this.movieWatchdate = movieWatchdate;
        this.movieWatchtime = movieWatchtime;
        this.movieComment = movieComment;
        this.movieRating = movieRating;
        this.personId = personId;
        this.cinemaId = cinemaId;
    }

    public int getMemoirId() {
        return memoirId;
    }

    public void setMemoirId(int memoirId) {
        this.memoirId = memoirId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieReleasedate() {
        return movieReleasedate;
    }

    public void setMovieReleasedate(String movieReleasedate) {
        this.movieReleasedate = movieReleasedate;
    }

    public String getMovieWatchdate() {
        return movieWatchdate;
    }

    public void setMovieWatchdate(String movieWatchdate) {
        this.movieWatchdate = movieWatchdate;
    }

    public String getMovieWatchtime() {
        return movieWatchtime;
    }

    public void setMovieWatchtime(String movieWatchtime) {
        this.movieWatchtime = movieWatchtime;
    }

    public String getMovieComment() {
        return movieComment;
    }

    public void setMovieComment(String movieComment) {
        this.movieComment = movieComment;
    }

    public float getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(float movieRating) {
        this.movieRating = movieRating;
    }

    public PersonId getPersonId() {
        return personId;
    }

    public void setPersonId(PersonId personId) {
        this.personId = personId;
    }

    public CinemaId getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(CinemaId cinemaId) {
        this.cinemaId = cinemaId;
    }
}
