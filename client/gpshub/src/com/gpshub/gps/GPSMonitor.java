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
    private static final long GPS_UPDATE_TIME = 1000;  // 1 sec;
    private static final float GPS_UPDATE_DISTANCE = 2;  // 2 meters
    private final LocationManager locationManager;
    private boolean collecting = false;
    Context context;

    private Location currentLocation = null;
    private final LocationListener _listener = new Listener();


    public GPSMonitor(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public static boolean isGPSEnabled(Activity activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void start() {
        if (collecting)
            return;
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                GPS_UPDATE_TIME,
                GPS_UPDATE_DISTANCE,
                _listener
        );
        collecting = true;
        currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        System.out.println("GPSMonitor started.");
    }

    public void stop() {
        collecting = false;
        locationManager.removeUpdates(_listener);
        System.out.println("GPSMonitor stopped.");
    }

    private class Listener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged(): lat="+location.getLatitude() + ", long=" + location.getLongitude());
            currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(context, "Gps turned on", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(context, "Gps turned off", Toast.LENGTH_LONG).show();
        }
    }
}