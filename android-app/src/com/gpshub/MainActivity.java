package com.gpshub;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
    public static final String PREFS_NAME = "gpshubprefs";

    public static boolean enabled = false;
    Timer timer;
    private android.content.Context mContext = this;
    private GPSMonitor gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final Button tracker = (Button) findViewById(R.id.onoff);
        final Button logout = (Button) findViewById(R.id.logout);
        gps = new GPSMonitor(MainActivity.this);

        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!enabled) {
                    if (gps.isGPSEnabled()) {
                        startTimer();
                        tracker.setText("Отключить");
                    } else {
                        showSettingsAlert();
                        return;
                    }
                } else {
                    stopTimer();
                    tracker.setText("Включить");
                }

                enabled = !enabled;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                settings.edit().remove("driver_id").commit();
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
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
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            String company_hash = settings.getString("company_hash", null);
            String driver_id = settings.getString("driver_id", null);

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("company_hash", company_hash));
            nameValuePairs.add(new BasicNameValuePair("id", driver_id));
            nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
            nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lng)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
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
