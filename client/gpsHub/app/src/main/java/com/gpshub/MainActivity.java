package com.gpshub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.gpshub.api.AccountManager;
import com.gpshub.service.ServiceManager;
import com.gpshub.utils.ContextHack;
import com.gpshub.utils.Preferences;
import com.gpshub.ui.Theme;
import com.gpshub.ui.ThemeUtils;

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
        restoreLabels();

        Button busyBtn = (Button) findViewById(R.id.busyBtn);
        busyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBusy();
            }
        });
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
                Preferences.setUiTheme(item.isChecked() ? Theme.THEME_LIGHT :Theme.THEME_DARK);
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

    private void toggleBusy() {
        Boolean busy = !Preferences.isBusy();
        Preferences.setBusy(busy);
        ServiceManager.getInstance().sendMessage(this, "busy", busy.toString());
        restoreLabels();
    }

    private void restoreLabels() {
        TextView idStatus = (TextView) findViewById(R.id.driver_id);
        TextView gpsStatus = (TextView) findViewById(R.id.gps_status);
        TextView busyStatus = (TextView) findViewById(R.id.busy_status);
        Button busyBtn = (Button) findViewById(R.id.busyBtn);

        String driverID = Preferences.getDriverID();
        idStatus.setText(driverID);
        gpsStatus.setText(getString(ServiceManager.getInstance().isServiceRunning(this) ? R.string.enabled : R.string.disabled));

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
        input.setTransformationMethod(new PasswordTransformationMethod());
        AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
        ad.setTitle(title);
        ad.setMessage(message);
        ad.setView(input);
        ad.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                if (value.equals("qwerty")) {
                    switch (method) {
                        case R.id.menuitem_prefs:
                            showPreferences();
                            break;
                        case R.id.menuitem_logout:
                            logout();
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
}
