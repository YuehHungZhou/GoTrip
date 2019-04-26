package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class Request {

    private ArrayList<User> mUsers;
    private ArrayList<Trip> mTrips;

    public Request() {
        mUsers = new ArrayList<>();
        mTrips = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return mUsers;
    }

    public void setUsers(ArrayList<User> users) {
        mUsers = users;
    }

    public ArrayList<Trip> getTrips() {
        return mTrips;
    }

    public void setTrips(ArrayList<Trip> trips) {
        mTrips = trips;
    }
}
