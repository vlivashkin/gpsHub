package com.gpshub;

import android.app.AlertDialog;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import com.gpshub.api.DataProvider;
import com.gpshub.gps.GPSMonitor;
import com.gpshub.gps.GPSService;
import com.gpshub.settings.AccountManager;
import com.gpshub.settings.TempSettings;


public class MainActivity extends ActionBarActivity {
    GPSService mService;
    TempSettings ts;
    DataProvider dp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ts = TempSettings.getInstance();
        dp = new DataProvider(MainActivity.this);

        final TextView gpsStatus = (TextView) findViewById(R.id.gpsstatus);
        final TextView busyStatus = (TextView) findViewById(R.id.busystatus);

        final Button gpsBtn = (Button) findViewById(R.id.gpsbtn);
        final Button busyBtn = (Button) findViewById(R.id.busybtn);

        if (ts.isGpsEnabled()) {
            gpsStatus.setText("Включен");
            gpsBtn.setText("Отключить GPS");
        } else {
            gpsStatus.setText("Отключен");
            gpsBtn.setText("Включить GPS");
        }

        if (ts.isBusy()) {
            busyStatus.setText("Занят");
            busyBtn.setText("Свободен");
        } else {
            busyStatus.setText("Свободен");
            busyBtn.setText("Занят");
        }

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ts.isGpsEnabled()) {
                    if (GPSMonitor.isGPSEnabled(MainActivity.this)) {
                        startService();
                        gpsStatus.setText("Включен");
                        gpsBtn.setText("Отключить GPS");
                        ts.setGpsEnabled(true);
                    } else {
                        showSettingsAlert();
                        ts.setGpsEnabled(false);
                    }
                } else {
                    stopService();
                    gpsStatus.setText("Отключен");
                    gpsBtn.setText("Включить GPS");
                    ts.setGpsEnabled(false);
                }
            }
        });

        busyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ts.isBusy()) {
                    busyStatus.setText("Занят");
                    busyBtn.setText("Свободен");
                    ts.setBusy(true);
                } else {
                    busyStatus.setText("Свободен");
                    busyBtn.setText("Занят");
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
                stopService();
                TempSettings.getInstance().wipeData();
                new AccountManager(MainActivity.this).logout();
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startService() {
        System.out.println("start service....");
        Intent intent = new Intent(this, GPSService.class);
        getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        ts.setConnection(mConnection);

    }

    public void stopService() {
        System.out.println("stop service....");
        if (ts.isGpsEnabled()) {
            getApplicationContext().unbindService(ts.getConnection());
            ts.setGpsEnabled(false);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GPSService.LocalBinder binder = (GPSService.LocalBinder) service;
            mService = binder.getService();
            ts.setGpsEnabled(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            ts.setGpsEnabled(false);
        }
    };

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Настройки GPS");

        // Setting Dialog Message
        alertDialog.setMessage("GPS-трекинг не разрешен. Разрешите доступ к GPS в настройках, чтобы продолжить");

        // On pressing Settings button
        alertDialog.setPositiveButton("Настройки", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}
