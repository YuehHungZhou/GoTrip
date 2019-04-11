package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class Point {

    private String mIconType;
    private int mCost;
    private int mSorte;
    ArrayList<String> mImages;
    private long mArrivalTime;
    private int mDay;
    private String mDescribe;
    private String mTitle;
    private Double mLatitude;
    private Double mLongitude;


    public Point() {
        mCost = -1;
        mIconType = "";
        mSorte = -1;
        mImages = new ArrayList<>();
        mArrivalTime = 0;
        mDay = -1;
        mDescribe = "";
        mTitle = "";
        mLatitude = null;
        mLongitude = null;


    }

    public String getIconType() {
        return mIconType;
    }

    public void setIconType(String iconType) {
        mIconType = iconType;
    }

    public int getCost() {
        return mCost;
    }

    public void setCost(int cost) {
        mCost = cost;
    }

    public int getSorte() {
        return mSorte;
    }

    public void setSorte(int sorte) {
        mSorte = sorte;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }

    public long getArrivalTime() {
        return mArrivalTime;
    }

    public void setArrivalTime(long arrivalTime) {
        mArrivalTime = arrivalTime;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public String getDescribe() {
        return mDescribe;
    }

    public void setDescribe(String describe) {
        mDescribe = describe;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
