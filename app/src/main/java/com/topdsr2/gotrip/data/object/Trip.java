package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class Trip {

    private int mId;
    private String mCountry;
    private String mCreater;
    private String mMainImage;
    private boolean isOnTrip;
    private boolean isComplete;
    private String mTitle;
    private String mDescribe;
    private Object mTripStart;
    private Object mTripEnd;
    ArrayList<String> mOwners;
    private int mAddPointTimes;
    private String mCreaterImage;
    private int mCollectionNumber;

    public Trip() {
        mId = -1;
        mCountry = "";
        mCreater = "";
        mMainImage = "";
        isOnTrip = false;
        isComplete = false;
        mTitle = "";
        mDescribe = "";
        mTripStart = null;
        mTripEnd = null;
        mOwners = new ArrayList<>();
        mAddPointTimes = -1;
        mCreaterImage = "";
        mCollectionNumber = -1;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
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

    public Object getTripStart() {
        return mTripStart;
    }

    public void setTripStart(Object tripStart) {
        mTripStart = tripStart;
    }

    public Object getTripEnd() {
        return mTripEnd;
    }

    public void setTripEnd(Object tripEnd) {
        mTripEnd = tripEnd;
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
