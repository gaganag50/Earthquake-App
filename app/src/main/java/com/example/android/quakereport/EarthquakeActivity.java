/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;

import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private EarthquakeAdapter mAdapter;
    private ListView earthquakeListView;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        Log.i(LOG_TAG, "ON CREATE ");

        textView = (TextView) findViewById(R.id.empty);


        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


        earthquakeListView = (ListView) findViewById(R.id.list);
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);


        if (isConnected == true) {
//        EarthquakeAsyncTask earthquakeAsyncTask = new EarthquakeAsyncTask();
//        earthquakeAsyncTask.execute(USGS_REQUEST_URL);
            Log.i(LOG_TAG, "AFTER THE LISTVIEW AND ADAPTER HAS BEEN INITIALISED ");

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, this);
            Log.i(LOG_TAG, "INIT LOADER HAS BEEN CALLED");

            earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Earthquake earthquake = mAdapter.getItem(position);

                    Uri earthquakeUri = Uri.parse(earthquake.getUrl());

                    Intent launchWeb = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                    startActivity(launchWeb);
                }
            });
        } else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            textView.setText(R.string.no_internet_connectivity);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {
//        Log.i(LOG_TAG, "ON CREATE LOADER HAS BEEN CALLED");
//
//        return new EarthquakeLoader(this, USGS_REQUEST_URL);
//    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

//        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        String minMagnitude = sharedPrefs.getString(
//                getString(R.string.settings_min_magnitude_key),
//                getString(R.string.settings_min_magnitude_default));
//        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
//        Uri.Builder uriBuilder = baseUri.buildUpon();
//
//        uriBuilder.appendQueryParameter("format", "geojson");
//        uriBuilder.appendQueryParameter("limit", "10");
//        uriBuilder.appendQueryParameter("minmag", minMagnitude);
//        uriBuilder.appendQueryParameter("orderby", "time");
//
//        return new EarthquakeLoader(this, uriBuilder.toString());

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());


    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        View loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.GONE);
        textView.setText(R.string.no_earthquakes_found);

        mAdapter.clear();
        if (earthquakes != null && !earthquakes.isEmpty())
            mAdapter.addAll(earthquakes);

        earthquakeListView.setEmptyView(findViewById(R.id.empty));


    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "ONLOADRESET HAS BEEN CALLED");
        mAdapter.clear();
    }


}

