package com.gpshub.service.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.gpshub.service.ServiceTempPrefs;

public class LocationTracker {
    private static final String TAG = LocationTracker.class.getSimpleName();

    private final LocationManager locationManager;
    private boolean collecting = false;

    private Location currentLocation = null;
    private final LocationListener _listener = new GpsProviderListener();


    public LocationTracker(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void start() {
        if (collecting)
            return;

        long updateTime = ServiceTempPrefs.getInstance().getUpdateTime();
        float updateDistance = ServiceTempPrefs.getInstance().getUpdateDistance();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, updateTime, updateDistance, _listener);
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
            Log.i(TAG, "gps turned on");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "gps turned off");
        }
    }
}