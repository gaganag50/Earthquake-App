package com.example.android.quakereport;

public class Earthquake {
    private String mLocation;
    private long mDate;
    private double mMagnitude;
    private String mUrl;

    public Earthquake(double magnitude, String location, long  date,String url){
        mLocation = location;
        mDate = date;
        mMagnitude = magnitude;
        mUrl = url;
    }

    public String getLocation(){
        return mLocation;
    }

    public long getDate(){
        return mDate;
    }

    public double getMagnitude(){
        return mMagnitude;
    }

    public String getUrl(){
        return mUrl;
    }

}
