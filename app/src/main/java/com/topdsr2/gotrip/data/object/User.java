package com.topdsr2.gotrip.data.object;

import java.util.ArrayList;

public class User {

    private String mEmail;
    private String mName;
    private String mUserImage;
    ArrayList<String> mFriends;
    ArrayList<String> mTripCollection;

    public User() {
        mEmail = "";
        mName = "";
        mUserImage = "";
        mFriends = new ArrayList<>();
        mTripCollection = new ArrayList<>();
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUserImage() {
        return mUserImage;
    }

    public void setUserImage(String userImage) {
        mUserImage = userImage;
    }

    public ArrayList<String> getFriends() {
        return mFriends;
    }

    public void setFriends(ArrayList<String> friends) {
        mFriends = friends;
    }

    public ArrayList<String> getTripCollection() {
        return mTripCollection;
    }

    public void setTripCollection(ArrayList<String> tripCollection) {
        mTripCollection = tripCollection;
    }
}
