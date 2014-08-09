package com.gpshub.utils;

import android.content.ServiceConnection;

public class TempSettings {
    private static TempSettings instance;
    private ServiceConnection mConnection;
    private boolean gpsEnabled = false;
    private boolean isBusy = false;

    public static synchronized TempSettings getInstance() {
        if (instance == null) {
            instance = new TempSettings();
        }
        return instance;
    }

    public ServiceConnection getConnection() {
        return mConnection;
    }

    public void setConnection(ServiceConnection mConnection) {
        this.mConnection = mConnection;
    }

    public boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public void setGpsEnabled(boolean gpsEnabled) {
        System.out.println("setGpsEnabled: " + gpsEnabled);
        this.gpsEnabled = gpsEnabled;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean isBusy) {
        this.isBusy = isBusy;
    }

    public void wipeData() {
        gpsEnabled = false;
        isBusy = false;
    }
}