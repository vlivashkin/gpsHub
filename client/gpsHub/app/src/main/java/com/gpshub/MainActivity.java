package com.gpshub;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.gpshub.api.AccountManager;
import com.gpshub.service.ServiceManager;
import com.gpshub.ui.Theme;
import com.gpshub.ui.ThemeUtils;
import com.gpshub.utils.AppConstants;
import com.gpshub.utils.ContextHack;
import com.gpshub.utils.Preferences;
import com.gpshub.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ContextHack.setAppContext(getApplicationContext());
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);

        if (!AccountManager.isLoggedIn()) {
            switchToLoginActivity();
            return;
        }

        if (!ServiceManager.getInstance().isServiceRunning(this)) {
            ServiceManager.getInstance().startService(this);
        }

        setContentView(R.layout.main);
        ThemeUtils.onActivityShowSetTheme(this);
        restoreLabels();
        listenGpsStatus();

        Button busyBtn = (Button) findViewById(R.id.busyBtn);
        busyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBusy();
                FlurryAgent.logEvent("ToggleBusy");
            }
        });

        Button showGpsSettingsBtn = (Button) findViewById(R.id.gpsSettingsBtn);
        showGpsSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                FlurryAgent.logEvent("ShowGPSSettings");
            }
        });

        if (getIntent().getBooleanExtra("show_settings", false)) {
            showPreferences();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, AppConstants.FLURRY_API_KEY);
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);

        Map<String, String> map = new HashMap<String, String>();
        map.put("url", Preferences.getServerUrl());
        map.put("id", Preferences.getDriverID());
        FlurryAgent.logEvent("Account", map);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    @Override
    protected void onResume() {
        ServiceManager.getInstance().bindService(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        ServiceManager.getInstance().unbindService(this);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Theme theme = Preferences.getUiTheme();
        menu.findItem(R.id.menuitem_nigthmode).setChecked(theme == Theme.THEME_DARK);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_nigthmode:
                Preferences.setUiTheme(item.isChecked() ? Theme.THEME_LIGHT : Theme.THEME_DARK);
                ThemeUtils.changeTheme(this);
                FlurryAgent.logEvent("ToggleThemeClick");
                return super.onOptionsItemSelected(item);
            case R.id.menuitem_prefs:
                buildDialog("Настройки", "Введите мастер-пароль для изменения настроек:", R.id.menuitem_prefs);
                FlurryAgent.logEvent("TrySettingsClick");
                return true;
            case R.id.menuitem_logout:
                buildDialog("Выход из аккаунта", "Введите мастер-пароль для выхода:", R.id.menuitem_logout);
                FlurryAgent.logEvent("TryLogoutClick");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleBusy() {
        Boolean busy = !Preferences.isBusy();
        Preferences.setBusy(busy);
        ServiceManager.getInstance().sendMessage(this, "busy", busy.toString());
        restoreLabels();
    }

    private void restoreLabels() {
        TextView gpsStatus = (TextView) findViewById(R.id.gpsStatus);
        TextView busyStatus = (TextView) findViewById(R.id.busyStatus);
        Button busyBtn = (Button) findViewById(R.id.busyBtn);

        String driverId = Preferences.getDriverID();
        String serverName = Utils.getServerNameByUrl(this, Preferences.getServerUrl());
        getSupportActionBar().setTitle(driverId + "@" + serverName + " - gpsHub");

        gpsStatus.setText(getString(Preferences.isGpsEnabled() ? R.string.enabled : R.string.disabled));

        if (Preferences.isBusy()) {
            busyStatus.setText(getString(R.string.busy));
            busyBtn.setText(getString(R.string.free));
        } else {
            busyStatus.setText(getString(R.string.free));
            busyBtn.setText(getString(R.string.busy));
        }
    }

    private void buildDialog(String title, String message, final int method) {
        final EditText input = new EditText(MainActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setView(input);
        ad.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (value.equals(AppConstants.PASSWORD)) {
                    switch (method) {
                        case R.id.menuitem_prefs:
                            showPreferences();
                            FlurryAgent.logEvent("ShowSettings");
                            break;
                        case R.id.menuitem_logout:
                            logout();
                            FlurryAgent.logEvent("Logout");
                            break;
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Неверный мастер-пароль", Toast.LENGTH_LONG).show();
                }
            }
        });
        ad.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //nothing
            }
        }).show();
    }

    public void logout() {
        ServiceManager.getInstance().stopService(this);
        Preferences.wipeTempSettings();
        AccountManager.logout();
        switchToLoginActivity();
    }

    public void switchToLoginActivity() {
        Intent login = new Intent(this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void showPreferences() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void listenGpsStatus() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.addGpsStatusListener(new android.location.GpsStatus.Listener() {
            public void onGpsStatusChanged(int event) {
                TextView gpsStatus = (TextView) findViewById(R.id.gpsStatus);
                switch (event) {
                    case GpsStatus.GPS_EVENT_STARTED:
                        gpsStatus.setText(getString(R.string.enabled));
                        Preferences.setGpsEnabled(true);
                        break;
                    case GpsStatus.GPS_EVENT_STOPPED:
                        gpsStatus.setText(getString(R.string.disabled));
                        Preferences.setGpsEnabled(false);
                        break;
                }
            }
        });

    }
}
