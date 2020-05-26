package com.example.mymoviememoir.entities;

public class FullMemoir {
    private String memoirName;
    private String memoirImagePath;
    private String releaseDate;
    private String watchDate;
    private int postCode;
    private float userRating;
    private float publicRating;
    private String comments;

    public FullMemoir(String memoirName, String memoirImagePath, String releaseDate, String watchDate, int postCode, float userRating, float publicRating,String comments) {
        this.memoirName = memoirName;
        this.memoirImagePath = memoirImagePath;
        this.releaseDate = releaseDate;
        this.watchDate = watchDate;
        this.postCode = postCode;
        this.userRating = userRating;
        this.publicRating = publicRating;
        this.comments = comments;
    }

    public String getMemoirName() {
        return memoirName;
    }

    public void setMemoirName(String memoirName) {
        this.memoirName = memoirName;
    }

    public String getMemoirImagePath() {
        return memoirImagePath;
    }

    public void setMemoirImagePath(String memoirImagePath) {
        this.memoirImagePath = memoirImagePath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getWatchDate() {
        return watchDate;
    }

    public void setWatchDate(String watchDate) {
        this.watchDate = watchDate;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public float getUserRating() {
        return userRating;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public float getPublicRating() {
        return publicRating;
    }

    public void setPublicRating(float publicRating) {
        this.publicRating = publicRating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
