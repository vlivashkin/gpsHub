package com.gpshub;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
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

    Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Switch s = (Switch) findViewById(R.id.onoff);

        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    timer = new Timer(true);
                    final GPSTracker gps = new GPSTracker(MainActivity.this);

                    if (gps.canGetLocation()) {

                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                double latitude = gps.getLatitude();
                                double longitude = gps.getLongitude();

                                try {
                                    System.out.println("lat: " + latitude + ", lng: " + longitude);
                                    postData(latitude, longitude);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        timer.scheduleAtFixedRate(timerTask, 0, 2 * 1000);

                    } else {
                        gps.showSettingsAlert();
                    }
                } else {
                    timer.cancel();
                }
            }
        });
    }

    public void postData(double lat, double lng) throws IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://javafiddle.org/gpsHub/actions/drivers.php");

        try {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
            nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lng)));
            nameValuePairs.add(new BasicNameValuePair("id", "101"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
