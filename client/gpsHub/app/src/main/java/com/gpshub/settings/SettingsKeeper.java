package com.gpshub.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsKeeper {
    public static final String PREFS_NAME = "gpshubprefs";
    private final Context context;

    public SettingsKeeper(Context context) {
        this.context = context;
    }

    public String getSharedPreference(String preference) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(preference, null);
    }

    public void setSharedPreference(String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setSharedPreferences(String company_hash, String driver_id) {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("company_hash", company_hash);
        editor.putString("driver_id", driver_id);
        editor.apply();
    }

    public void removeSharedPreferences() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        settings.edit().remove("driver_id").apply();
    }
}
