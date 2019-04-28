package com.topdsr2.gotrip.data.object;

public class SearchData {

    private int mYear;
    private int mMonth;
    private int mDay;
    private String mCountry;
    private int mCollection;


    public SearchData() {
        mYear = 0;
        mMonth = 0;
        mDay = 0;
        mCountry = "";
        mCollection = 0;
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getMonth() {
        return mMonth;
    }

    public void setMonth(int month) {
        mMonth = month;
    }

    public int getDay() {
        return mDay;
    }

    public void setDay(int day) {
        mDay = day;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String country) {
        mCountry = country;
    }

    public int getCollection() {
        return mCollection;
    }

    public void setCollection(int collection) {
        mCollection = collection;
    }
}
