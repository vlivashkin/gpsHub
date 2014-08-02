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

import com.gpshub.api.DataProvider;
import com.gpshub.gps.GPSServiceManager;
import com.gpshub.settings.AccountManager;
import com.gpshub.settings.TempSettings;


public class MainActivity extends ActionBarActivity {
    TempSettings ts;
    DataProvider dp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final AccountManager am = new AccountManager(MainActivity.this);
        if (!am.isLoggedIn()) {
            Intent login = new Intent(this, LoginActivity.class);
            startActivity(login);
            finish();
            return;
        }

        setContentView(R.layout.main);

        ts = TempSettings.getInstance();
        dp = new DataProvider(MainActivity.this);

        final TextView gpsStatus = (TextView) findViewById(R.id.gpsstatus);
        final TextView busyStatus = (TextView) findViewById(R.id.busystatus);
        final Button busyBtn = (Button) findViewById(R.id.busybtn);

        gpsStatus.setText(getString(ts.isGpsEnabled() ? R.string.enabled : R.string.disabled));

        if (ts.isBusy()) {
            busyStatus.setText(getString(R.string.busy));
            busyBtn.setText(getString(R.string.free));
        } else {
            busyStatus.setText(getString(R.string.free));
            busyBtn.setText(getString(R.string.busy));
        }

        if (!ts.isGpsEnabled()) {
            GPSServiceManager.startService(this);
            gpsStatus.setText(getString(R.string.enabled));
            ts.setGpsEnabled(true);
        }

        busyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ts.isBusy()) {
                    busyStatus.setText(getString(R.string.busy));
                    busyBtn.setText(getString(R.string.free));
                    ts.setBusy(true);
                } else {
                    busyStatus.setText(getString(R.string.free));
                    busyBtn.setText(getString(R.string.busy));
                    ts.setBusy(false);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logoutbtn:
                final EditText input = new EditText(MainActivity.this);
                input.setTransformationMethod(new PasswordTransformationMethod());
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Выход из аккаунта")
                        .setMessage("Введите мастер-пароль для выхода:")
                        .setView(input)
                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String value = input.getText().toString();
                                if (value.equals("qwerty"))
                                    logout();
                                else
                                    Toast.makeText(MainActivity.this, "Неверный мастер-пароль", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //nothing
                    }
                }).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        GPSServiceManager.stopService(this);
        TempSettings.getInstance().wipeData();
        new AccountManager(MainActivity.this).logout();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
    }
}
