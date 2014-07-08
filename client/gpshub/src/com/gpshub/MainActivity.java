package com.gpshub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import com.gpshub.utils.GPSMonitor;
import com.gpshub.utils.SettingsKeeper;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private boolean gpsEnabled = false;
    private boolean isBusy = false;
    private Timer timer;
    private GPSMonitor gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final TextView gpsStatus = (TextView) findViewById(R.id.gpsstatus);
        final TextView busyStatus = (TextView) findViewById(R.id.busystatus);

        final Button gpsBtn = (Button) findViewById(R.id.gpsbtn);
        final Button busyBtn = (Button) findViewById(R.id.busybtn);

        gps = new GPSMonitor(MainActivity.this);

        gpsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!gpsEnabled) {
                    if (gps.isGPSEnabled()) {
                        startTimer();
                        gpsStatus.setText("Включен");
                        gpsBtn.setText("Отключить GPS");

                    } else {
                        showSettingsAlert();
                        return;
                    }
                } else {
                    stopTimer();
                    gpsStatus.setText("Отключен");
                    gpsBtn.setText("Включить");
                }

                gpsEnabled = !gpsEnabled;
            }
        });

        busyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isBusy) {
                    busyStatus.setText("Занят");
                    busyBtn.setText("Свободен");
                } else {
                    busyStatus.setText("Свободен");
                    busyBtn.setText("Занят");
                }

                isBusy = !isBusy;
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
                new AccountManager(MainActivity.this).logout();
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startTimer() {
        gps.start();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Location location = gps.getCurrentLocation();
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                try {
                    postData(latitude, longitude);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 2 * 1000);
    }

    public void stopTimer() {
        timer.cancel();
        gps.stop();
    }

    public void postData(double lat, double lng) throws IOException {
        System.out.println("lat: " + lat + ", lng: " + lng);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://javafiddle.org/gpsHub/actions/drivers.php");

        try {
            SettingsKeeper sk = new SettingsKeeper(MainActivity.this);
            String company_hash = sk.getSharedPreference("company_hash");
            String driver_id = sk.getSharedPreference("driver_id");

            List<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("company_hash", company_hash));
            nameValuePairs.add(new BasicNameValuePair("id", driver_id));
            nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
            nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lng)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            httpclient.execute(httppost);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not gpsEnabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
