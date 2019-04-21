package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class Trip {

    private String mId;
    private String mCountry;
    private String mCreater;
    private String mMainImage;
    private boolean isOnTrip;
    private boolean isComplete;
    private String mTitle;
    private String mDescribe;
    private long mTripStart;
    private long mTripEnd;
    private int mTripDay;
    private ArrayList<String> mOwners;
    private int mAddPointTimes;
    private String mCreaterImage;
    private int mCollectionNumber;

    public Trip() {
        mId = "";
        mCountry = "";
        mCreater = "";
        mMainImage = "";
        isOnTrip = false;
        isComplete = false;
        mTitle = "";
        mDescribe = "";
        mTripStart = 0;
        mTripEnd = 0;
        mTripDay = 0;
        mOwners = new ArrayList<>();
        mAddPointTimes = -1;
        mCreaterImage = "";
        mCollectionNumber = -1;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public String getCreater() {
        return mCreater;
    }

    public void setCreater(String creater) {
        mCreater = creater;
    }

    public String getMainImage() {
        return mMainImage;
    }

    public void setMainImage(String mainImage) {
        mMainImage = mainImage;
    }

    public boolean isOnTrip() {
        return isOnTrip;
    }

    public void setOnTrip(boolean onTrip) {
        isOnTrip = onTrip;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }

    public long getTripStart() {
        return mTripStart;
    }

    public void setTripStart(long tripStart) {
        mTripStart = tripStart;
    }

    public long getTripEnd() {
        return mTripEnd;
    }

    public void setTripEnd(long tripEnd) {
        mTripEnd = tripEnd;
    }

    public int getTripDay() {
        return mTripDay;
    }

    public void setTripDay(int tripDay) {
        mTripDay = tripDay;
    }

    public ArrayList<String> getOwners() {
        return mOwners;
    }

    public void setOwners(ArrayList<String> owners) {
        mOwners = owners;
    }

    public int getAddPointTimes() {
        return mAddPointTimes;
    }

    public void setAddPointTimes(int addPointTimes) {
        mAddPointTimes = addPointTimes;
    }

    public String getCreaterImage() {
        return mCreaterImage;
    }

    public void setCreaterImage(String createrImage) {
        mCreaterImage = createrImage;
    }

    public int getCollectionNumber() {
        return mCollectionNumber;
    }

    public void setCollectionNumber(int collectionNumber) {
        mCollectionNumber = collectionNumber;
    }
}
