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
        Boolean result = Boolean.valueOf(getPreference("is_busy", "false"));
        if (result != null) {
            return result;
        } else {
            return false;
        }
    }

    public static void setBusy(Boolean is_busy) {
        setPreference("is_busy", Boolean.toString(is_busy));
    }

    public static Theme getUiTheme() {
        Theme theme = Theme.getTheme(getPreference("ui_theme"));
        if (theme != null) {
            return theme;
        } else {
            return Theme.THEME_LIGHT;
        }
    }

    public static void setUiTheme(Theme theme) {
        setPreference("ui_theme", theme.getName());
    }

    public static Long getSendPeriod() {
        Long period = Long.valueOf(getPreference("send_period", "2000"));
        if (period != null) {
            return period;
        } else {
            return (long) 2000;
        }
    }

    public static Long getUpdateTime() {
        Long time = Long.valueOf(getPreference("update_time", "1000"));
        if (time != null) {
            return time;
        } else {
            return (long) 1000;
        }
    }

    public static Float getUpdateDistance() {
        Float distance = Float.valueOf(getPreference("update_distance", "2"));
        if (distance != null) {
            return distance;
        } else {
            return (float) 2;
        }
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
