package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class Point {

    private String mIconType;
    private int mCost;
    private int mSorte;
    private ArrayList<String> mImages;
    private ArrayList<String> mAgree;
    private ArrayList<String> mDisagree;
    private long mArrivalTime;
    private int mDay;
    private String mDescribe;
    private String mTitle;
    private Double mLatitude;
    private Double mLongitude;
    private String mId;
    private String mVoteStatus;


    public Point() {
        mCost = -1;
        mIconType = "";
        mSorte = -1;
        mImages = new ArrayList<>();
        mAgree = new ArrayList<>();
        mDisagree = new ArrayList<>();
        mArrivalTime = 0;
        mDay = -1;
        mDescribe = "";
        mTitle = "";
        mLatitude = 0.0;
        mLongitude = 0.0;
        mId = "";
        mVoteStatus = "";

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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public ArrayList<String> getAgree() {
        return mAgree;
    }

    public void setAgree(ArrayList<String> agree) {
        mAgree = agree;
    }

    public ArrayList<String> getDisagree() {
        return mDisagree;
    }

    public void setDisagree(ArrayList<String> disagree) {
        mDisagree = disagree;
    }

    public String getVoteStatus() {
        return mVoteStatus;
    }

    public void setVoteStatus(String voteStatus) {
        mVoteStatus = voteStatus;
    }
}
