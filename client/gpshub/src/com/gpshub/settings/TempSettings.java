package com.gpshub.settings;

import android.content.ServiceConnection;

public class TempSettings {
    private static TempSettings instance;

    private TempSettings() {
    }

    public static synchronized TempSettings getInstance() {
        if (instance == null) {
            instance = new TempSettings();
        }
        return instance;
    }

    boolean gpsEnabled = false;
    boolean isBusy = false;

    public ServiceConnection getConnection() {
        return mConnection;
    }

    public void setConnection(ServiceConnection mConnection) {
        this.mConnection = mConnection;
    }

    ServiceConnection mConnection;

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
}