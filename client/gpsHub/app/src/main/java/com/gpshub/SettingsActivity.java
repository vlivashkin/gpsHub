package com.gpshub;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.gpshub.utils.ContextHack;
import com.gpshub.utils.ThemeUtils;

public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
    @SuppressWarnings("deprecation")
    public void onCreate(Bundle savedInstanceState) {
        ContextHack.setAppContext(getApplicationContext());
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        if (Build.VERSION.SDK_INT >= 11) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null)
                actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference connectionPref = findPreference(key);
        connectionPref.setSummary(sharedPreferences.getString(key, ""));
        if (key.equals("ui_theme")) {
            ThemeUtils.changeTheme(this);
        }
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}