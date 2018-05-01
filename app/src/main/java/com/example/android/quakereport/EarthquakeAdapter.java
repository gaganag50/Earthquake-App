package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeAdapter(EarthquakeActivity earthquakeActivity, ArrayList<Earthquake> earthquakes) {
        super(earthquakeActivity, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }

        Earthquake currentEarthquake = getItem(position);

        TextView magnitude = (TextView) listItemView.findViewById(R.id.magnitude);
        DecimalFormat formatter = new DecimalFormat("0.0");
        String magnitudeFormatted = formatter.format(currentEarthquake.getMagnitude());
        magnitude.setText(magnitudeFormatted);

        String location = currentEarthquake.getLocation();

        TextView location_offset = (TextView) listItemView.findViewById(R.id.location_offset);
        TextView primary_location = (TextView) listItemView.findViewById(R.id.primary_location);

        if (location.contains("of")) {
            String[] parts = location.split("of");
            location_offset.setText(parts[0] + "of");
            primary_location.setText(parts[1]);
        } else {
            location_offset.setText("NEAR THE");
            primary_location.setText(location);
        }

        Date dateObject = new Date(currentEarthquake.getDate());
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);
        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(formattedDate);
        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(formattedTime);

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;

    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);

        switch (magnitudeFloor) {
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;

            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;

        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
