package com.gpshub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {
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

    public static void setBusy(boolean isBusy) {
        Preferences.isBusy = isBusy;
    }

    public static boolean isBusy() {
        return isBusy;
    }

    public static void wipeTempSettings() {
        isBusy = false;
    }

    public static boolean tryMigration() {
        Context context = ContextHack.getAppContext();
        String PREFS_NAME = "gpshubprefs";
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String driverID = settings.getString("driver_id", null);
        if (driverID != null) {
            settings.edit().remove("driver_id").apply();
            Preferences.setPreference("server_url", "http://javafiddle.org/gpsHub");
            Preferences.setPreference("driver_id", driverID);
            return true;
        }
        return false;
    }
}
