package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class Point {

    private String mIconType;
    private int mCost;
    private int mSorte;
    private Object mGeo;
    ArrayList<String> mImages;
    private Object mArrivalTime;
    private int mDay;
    private String mDescribe;
    private String mTitle;


    public Point() {
        mCost = -1;
        mIconType = "";
        mSorte = -1;
        mGeo = null;
        mImages = new ArrayList<>();
        mArrivalTime = null;
        mDay = -1;
        mDescribe = "";
        mTitle = "";


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

    public Object getGeo() {
        return mGeo;
    }

    public void setGeo(Object geo) {
        mGeo = geo;
    }

    public ArrayList<String> getImages() {
        return mImages;
    }

    public void setImages(ArrayList<String> images) {
        mImages = images;
    }

    public Object getArrivalTime() {
        return mArrivalTime;
    }

    public void setArrivalTime(Object arrivalTime) {
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
