package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    String url;
    public EarthquakeLoader(Context context , String main_url) {
        super(context);
        url = main_url;
    }

    @Override
    public List<Earthquake> loadInBackground() {
        Log.i("EarthquakeLoader.class" , "LOAD IN BACKGROUND");

        return QueryUtils.fetchEarthquakeData(url);
    }

    @Override
    protected void onStartLoading() {
        Log.v("EarthquakeLoader.class" , "onstartloading");

        forceLoad();
    }


}
