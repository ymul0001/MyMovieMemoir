package com.example.mymoviememoir.entities;

public class Cinema {
    private int cinemaId;
    private String cinemaName;
    private String cinemaAddress;
    private int cinemaPostcode;

    public Cinema(int cinemaId, String cinemaName, String cinemaAddress, int cinemaPostcode) {
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.cinemaPostcode = cinemaPostcode;
    }

    public int getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(int cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaAddress() {
        return cinemaAddress;
    }

    public void setCinemaAddress(String cinemaAddress) {
        this.cinemaAddress = cinemaAddress;
    }

    public int getCinemaPostcode() {
        return cinemaPostcode;
    }

    public void setCinemaPostcode(int cinemaPostcode) {
        this.cinemaPostcode = cinemaPostcode;
    }
}
