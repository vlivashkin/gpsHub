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
import com.gpshub.api.DataProvider;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GPSService extends Service {
    private final IBinder mBinder = new LocalBinder();
    NotificationManager mNM;
    private Timer timer;
    private GPSMonitor gps;
    private DataProvider dp;

    public class LocalBinder extends Binder {
        public GPSService getService() {
            // Return this instance of LocalService so clients can call public methods
            return GPSService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        dp = new DataProvider(GPSService.this);
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
                        dp.postLocation(latitude, longitude);
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

    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "GPS-сервис работает";

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.ic_launcher, text,
                System.currentTimeMillis());

        notification.flags = Notification.FLAG_ONGOING_EVENT;

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
