package com.example.fellowtraveller;

public class Notification {
    private int id;
    private UserB user;
    private Trip trip;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserB getUser() {
        return user;
    }

    public void setUser(UserB user) {
        this.user = user;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
