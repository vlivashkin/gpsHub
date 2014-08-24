package com.gpshub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gpshub.ui.Theme;

public class Preferences {
    public static String getDriverID() {
        return getPreference("driver_id");
    }

    public static void setDriverID(String driver_id) {
        setPreference("driver_id", driver_id);
    }

    public static String getServerUrl() {
        return getPreference("server_url");
    }

    public static void setServerUrl(String server_url) {
        setPreference("server_url", server_url);
    }

    public static Boolean isBusy() {
        String result = getPreference("is_busy");
        if (result != null) {
            return Boolean.valueOf(result);
        } else {
            return false;
        }
    }

    public static void setBusy(Boolean is_busy) {
        setPreference("is_busy", Boolean.toString(is_busy));
    }

    public static Theme getUiTheme() {
        String result = getPreference("ui_theme");
        if (result != null) {
            Theme theme = Theme.valueOf(result);
            if (theme != null) {
                return theme;
            } else {
                return Theme.THEME_LIGHT;
            }
        } else {
            return Theme.THEME_LIGHT;
        }
    }

    public static void setUiTheme(Theme theme) {
        setPreference("ui_theme", theme.toString());
    }

    public static void wipeAccountSettings() {
        removePreference("driver_id");
    }

    public static void wipeTempSettings() {
        removePreference("is_busy");
    }

    private static String getPreference(String key) {
        return getPreference(key, null);
    }

    private static String getPreference(String key, String defaultValue) {
        Context context = ContextHack.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, defaultValue);
    }

    private static void setPreference(String key, String value) {
        Context context = ContextHack.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, value).apply();
    }

    private static void removePreference(String key) {
        Context context = ContextHack.getAppContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().remove(key).apply();
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
