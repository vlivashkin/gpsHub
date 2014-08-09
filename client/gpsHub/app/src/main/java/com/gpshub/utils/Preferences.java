package com.gpshub.utils;

import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
    private static ServiceConnection mConnection;
    private static boolean gpsEnabled = false;
    private static boolean isBusy = false;

    public static String getPreference(String key) {
        return getPreference(key, null);
    }

    public static String getPreference(String key, String defaultValue) {
        Context context = ContextHack.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, defaultValue);
    }

    public static void setPreference(String key, String value) {
        Context context = ContextHack.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }

    public static void removePreference(String key) {
        Context context = ContextHack.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().remove(key).apply();
    }

    public static ServiceConnection getConnection() {
        return mConnection;
    }

    public static void setConnection(ServiceConnection mConnection) {
        Preferences.mConnection = mConnection;
    }

    public static boolean isGpsEnabled() {
        return gpsEnabled;
    }

    public static void setGpsEnabled(boolean gpsEnabled) {
        Preferences.gpsEnabled = gpsEnabled;
    }

    public static boolean isBusy() {
        return isBusy;
    }

    public static void setBusy(boolean isBusy) {
        Preferences.isBusy = isBusy;
    }

    public static void wipeTempSettings() {
        mConnection = null;
        gpsEnabled = false;
        isBusy = false;
    }
}
