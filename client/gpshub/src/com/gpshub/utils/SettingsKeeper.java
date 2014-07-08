package com.gpshub.utils;

import android.app.Activity;
import android.content.SharedPreferences;

public class SettingsKeeper {
    public static final String PREFS_NAME = "gpshubprefs";
    private final Activity activity;

    public SettingsKeeper(Activity activity) {
        this.activity = activity;
    }

    public String getSharedPreference(String preference) {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(preference, null);
    }

    public void setSharedPreferences(String company_hash, String driver_id) {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("company_hash", company_hash);
        editor.putString("driver_id", driver_id);
        editor.commit();
    }

    public void removeSharedPreferences() {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
        settings.edit().remove("driver_id").commit();
    }
}
