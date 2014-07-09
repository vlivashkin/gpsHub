package com.gpshub.gps;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class GPSMonitor {

    private static final String TAG = GPSMonitor.class.getSimpleName();
    private static final long MIN_TIME_BW_UPDATES = 1000;  // 1 sec;
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 2;  // 2 meters
    private final Context context;
    private final LocationManager locationManager;
    private LocationListener listener;
    private boolean collecting = false;

    private Location currentLocation = null;
    private final LocationListener _listener = new Listener();

    public GPSMonitor(Context context) {
        this(context, null);
    }

    public GPSMonitor(Context context, LocationListener listener) {
        this.listener = listener;
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public boolean isGPSEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isGPSEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Location getLastKnownLocation() {
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    public Context getContext() {
        return context;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public LocationListener getListener() {
        return listener;
    }

    public void setListener(LocationListener listener) {
        this.listener = listener;
    }

    public boolean isCollecting() {
        return collecting;
    }

    public void start() {
        if (collecting)
            return;
        if (isGPSEnabled()) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    _listener
            );
            collecting = true;
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            System.out.println("GPSMonitor started.");
        } else {
            stop();
            Toast.makeText(context, "Please, allow access to GPS location provider.", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop() {
        collecting = false;
        locationManager.removeUpdates(_listener);
        System.out.println("GPSMonitor stopped.");
    }

    private class Listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (listener != null)
                listener.onLocationChanged(location);
            Log.d(TAG, "onLocationChanged(): lat="+location.getLatitude() + ", long=" + location.getLongitude());
            if (location.hasSpeed())
                Log.d(TAG, "onLocationChanged(): speed=" + location.getSpeed());
            if (location.hasBearing())
                Log.d(TAG, "onLocationChanged(): bearing=" + location.getBearing());
            if (location.hasAccuracy())
                Log.d(TAG, "onLocationChanged():  accuracy=" + location.getAccuracy());
            if (location.hasAltitude())
                Log.d(TAG, "onLocationChanged(): alt="+location.getAltitude());

            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (listener != null)
                listener.onStatusChanged(provider, status, extras);
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (listener != null)
                listener.onProviderEnabled(provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (listener != null)
                listener.onProviderDisabled(provider);
        }
    }
}