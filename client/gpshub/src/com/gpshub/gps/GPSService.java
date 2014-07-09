package com.gpshub.gps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import com.gpshub.MainActivity;
import com.gpshub.R;
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

public class GPSService extends Service {
    private final IBinder mBinder = new LocalBinder();
    NotificationManager mNM;
    private Timer timer;
    private GPSMonitor gps;

    public class LocalBinder extends Binder {
        public GPSService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GPSService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mNM.cancel(R.string.remote_service_started);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        gps = new GPSMonitor(GPSService.this);
        startTimer();
        System.out.println("service started.");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopTimer();
        System.out.println("service stopped.");
        return super.onUnbind(intent);
    }

    public void startTimer() {
        gps.start();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Location location = gps.getCurrentLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    try {
                        postData(latitude, longitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Location is null!");
                }

            }
        };

        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 2 * 1000);
        System.out.println("timer started.");
    }

    public void stopTimer() {
        timer.cancel();
        gps.stop();
        System.out.println("timer stopped.");
    }

    public void postData(double lat, double lng) throws IOException {
        System.out.println("lat: " + lat + ", lng: " + lng);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://javafiddle.org/gpsHub/actions/drivers.php");

        try {
            SettingsKeeper sk = new SettingsKeeper(GPSService.this);
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

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "GPS-сервис работает";

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_launcher, text,
                System.currentTimeMillis());

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        // Set the info for the views that show in the notification panel.
        notification.setLatestEventInfo(this, "gpsHub: GPS-сервис работает",
                text, contentIntent);

        // Send the notification.
        // We use a string id because it is a unique number.  We use it later to cancel.
        mNM.notify(R.string.remote_service_started, notification);
    }
}
