package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class TripAndPoint {

    private ArrayList<Point> mPoints;
    private Trip mTrip;
    private String mDocumentId;

    public TripAndPoint() {
        mPoints = new ArrayList<>();
        mTrip = new Trip();
        mDocumentId = "";
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

    public String getDocumentId() {
        return mDocumentId;
    }

    public void setDocumentId(String documentId) {
        mDocumentId = documentId;
    }
}
