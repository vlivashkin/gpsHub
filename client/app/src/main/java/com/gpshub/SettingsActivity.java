package com.gpshub;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.gpshub.service.ServiceManager;
import com.gpshub.ui.ThemeUtils;
import com.gpshub.utils.ContextHack;

import java.util.Map;

@SuppressWarnings("deprecation")
public class SettingsActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    @Override
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

        Map<String, ?> prefs = getPreferenceScreen().getSharedPreferences().getAll();
        for (Map.Entry<String, ?> entry : prefs.entrySet()) {
            Preference connectionPref = findPreference(entry.getKey());
            Object value = entry.getValue();
            if (connectionPref != null && value != null) {
                connectionPref.setSummary(value.toString());
            }
        }
    }

    @Override
    protected void onResume() {
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        ServiceManager.getInstance().bindService(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        ServiceManager.getInstance().unbindService(this);
        super.onPause();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference connectionPref = findPreference(key);
        connectionPref.setSummary(sharedPreferences.getString(key, ""));

        if (key.equals("ui_theme")) {
            ThemeUtils.changeTheme(this, true);
        } else if (key.equals("update_time") || key.equals("update_distance") || key.equals("send_period")) {
            ServiceManager.getInstance().restartService(this);
        } else {
            ServiceManager.getInstance().sendMessage(this, key, sharedPreferences.getString(key, ""));
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