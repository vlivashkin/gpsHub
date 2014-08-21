package com.gpshub.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class Tracker {
    private static final String TAG = Tracker.class.getSimpleName();

    private static final long GPS_UPDATE_TIME = 1000;  // 1 sec;
    private static final float GPS_UPDATE_DISTANCE = 2;  // 2 meters
    private final LocationManager locationManager;
    private boolean collecting = false;
    Context context;

    private Location currentLocation = null;
    private final LocationListener _listener = new GpsProviderListener();


    public Tracker(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void start() {
        if (collecting)
            return;
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_TIME, GPS_UPDATE_DISTANCE, _listener);
        collecting = true;
        currentLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        Log.d(TAG, "GPSMonitor started.");
    }

    public void stop() {
        collecting = false;
        locationManager.removeUpdates(_listener);
        Log.d(TAG, "GPSMonitor stopped.");
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    private class GpsProviderListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "location changed: lat="+location.getLatitude() + ", lng=" + location.getLongitude());
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            locationManager.removeUpdates(_listener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_UPDATE_TIME, GPS_UPDATE_DISTANCE, _listener);
            Log.i(TAG, "gps turned on; reload");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "gps turned off");
        }
    }
}