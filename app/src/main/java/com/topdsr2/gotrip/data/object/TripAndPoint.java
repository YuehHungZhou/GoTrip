package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class TripAndPoint {

    private ArrayList<Point> mPoints;
    private Trip mTrip;

    public TripAndPoint() {
        mPoints = new ArrayList<>();
        mTrip = new Trip();
    }

    public ArrayList<Point> getPoints() {
        return mPoints;
    }

    public void setPoints(ArrayList<Point> points) {
        mPoints = points;
    }

    public Trip getTrip() {
        return mTrip;
    }

    public void setTrip(Trip trip) {
        mTrip = trip;
    }
}
