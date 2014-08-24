package com.gpshub.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import com.gpshub.MainActivity;
import com.gpshub.R;
import com.gpshub.api.DataProvider;
import com.gpshub.service.gps.LocationTracker;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {
    private static final String TAG = LocationService.class.getSimpleName();

    private Messenger msg = new Messenger(new RequestHandler());

    private Timer timer;
    private LocationTracker locationTracker;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceTempPrefs stp = ServiceTempPrefs.getInstance();
        stp.setServerURL(intent.getStringExtra("server_url"));
        stp.setDriverID(intent.getStringExtra("driver_id"));
        stp.setBusy(intent.getBooleanExtra("busy", false));

        showNotification();
        startTimer();
        Log.i(TAG, "Service started");

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        hideNotification();
        stopTimer();
        Log.i(TAG, "Service stopped");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return msg.getBinder();
    }

    public void startTimer() {
        locationTracker = new LocationTracker(this);
        locationTracker.start();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Location location = locationTracker.getCurrentLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    double accuracy = location.getAccuracy();

                    try {
                        ServiceTempPrefs stp = ServiceTempPrefs.getInstance();
                        DataProvider.postLocation(stp.getServerURL(), stp.getDriverID(), latitude, longitude, accuracy, stp.isBusy());
                    } catch (IOException e) {
                        Log.e(TAG, "Data transfer error");
                    }
                } else {
                    Log.w(TAG, "Location is null!");
                }
            }
        };

        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 2 * 1000);
        Log.d(TAG, "timer started.");
    }

    public void stopTimer() {
        timer.cancel();
        locationTracker.stop();
        Log.d(TAG, "timer stopped.");
    }

    @SuppressWarnings("deprecation")
    private void showNotification() {
        CharSequence text = "Нажмите, чтобы открыть приложение.";
        Notification note = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
        note.flags = Notification.FLAG_ONGOING_EVENT;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        note.setLatestEventInfo(this, "GPS-сервис работает", text, contentIntent);
        startForeground(13, note);
    }

    private void hideNotification() {
        stopForeground(true);
    }
}
