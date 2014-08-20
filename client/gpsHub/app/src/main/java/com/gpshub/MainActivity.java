package com.gpshub;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gpshub.utils.GPSServiceManager;
import com.gpshub.api.AccountManager;
import com.gpshub.utils.Preferences;
import com.gpshub.utils.ContextHack;
import com.gpshub.utils.ThemeUtils;


public class MainActivity extends ActionBarActivity {
    AlertDialog ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ContextHack.setAppContext(getApplicationContext());
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);

        if (!AccountManager.isLoggedIn()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
            return;
        }

        if (!Preferences.isGpsEnabled()) {
            GPSServiceManager.startService(this);
            Preferences.setGpsEnabled(true);
        }

        setContentView(R.layout.main);
        restoreLabels();

        Button busyBtn = (Button) findViewById(R.id.busybtn);
        busyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBusy();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        String theme = Preferences.getPreference("ui_theme", ThemeUtils.THEME_LIGHT);
        menu.findItem(R.id.menuitem_nigthmode).setChecked(theme.equals(ThemeUtils.THEME_DARK));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuitem_nigthmode:
                if (!item.isChecked()) {
                    Preferences.setPreference("ui_theme", ThemeUtils.THEME_DARK);
                } else {
                    Preferences.setPreference("ui_theme", ThemeUtils.THEME_LIGHT);
                }
                ThemeUtils.changeTheme(this);
                return super.onOptionsItemSelected(item);
            case R.id.menuitem_prefs:
                buildDialog("Настройки", "Введите мастер-пароль для изменения настроек:", R.id.menuitem_prefs);
                return true;
            case R.id.menuitem_logout:
                buildDialog("Выход из аккаунта", "Введите мастер-пароль для выхода:", R.id.menuitem_logout);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void restoreLabels() {
        TextView idStatus = (TextView) findViewById(R.id.driver_id);
        TextView gpsStatus = (TextView) findViewById(R.id.gps_status);
        TextView busyStatus = (TextView) findViewById(R.id.busy_status);
        Button busyBtn = (Button) findViewById(R.id.busybtn);

        idStatus.setText(Preferences.getPreference("driver_id"));
        gpsStatus.setText(getString(Preferences.isGpsEnabled() ? R.string.enabled : R.string.disabled));

        if (Preferences.isBusy()) {
            busyStatus.setText(getString(R.string.busy));
            busyBtn.setText(getString(R.string.free));
        } else {
            busyStatus.setText(getString(R.string.free));
            busyBtn.setText(getString(R.string.busy));
        }
    }

    private void toggleBusy() {
        final TextView busyStatus = (TextView) findViewById(R.id.busy_status);
        final Button busyBtn = (Button) findViewById(R.id.busybtn);

        if (!Preferences.isBusy()) {
            busyStatus.setText(getString(R.string.busy));
            busyBtn.setText(getString(R.string.free));
            Preferences.setBusy(true);
        } else {
            busyStatus.setText(getString(R.string.free));
            busyBtn.setText(getString(R.string.busy));
            Preferences.setBusy(false);
        }
    }

    private void buildDialog(String title, String message, final int method) {
        final EditText input = new EditText(MainActivity.this);
        input.setTransformationMethod(new PasswordTransformationMethod());
        ad = new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setView(input)
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        if (value.equals("qwerty"))
                            switch (method) {
                                case R.id.menuitem_prefs:
                                    showPreferences();
                                    break;
                                case R.id.menuitem_logout:
                                    logout();
                                    break;
                            }
                        else
                            Toast.makeText(MainActivity.this, "Неверный мастер-пароль", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //nothing
            }
        }).show();
    }

    public void showPreferences() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    public void logout() {
        GPSServiceManager.stopService(this);
        Preferences.wipeTempSettings();
        AccountManager.logout();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
    }
}
