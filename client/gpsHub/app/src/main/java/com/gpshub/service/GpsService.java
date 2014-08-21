package com.gpshub.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.gpshub.MainActivity;
import com.gpshub.R;
import com.gpshub.api.DataProvider;
import com.gpshub.gps.Tracker;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GpsService extends Service {
    private static final String TAG = GpsService.class.getSimpleName();

    private Timer timer;
    private Tracker gpsTracker;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        return null;
    }

    public void startTimer() {
        gpsTracker = new Tracker(this);
        gpsTracker.start();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Location location = gpsTracker.getCurrentLocation();
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    double accuracy = location.getAccuracy();

                    try {
                        DataProvider.postLocation(latitude, longitude, accuracy);
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
        gpsTracker.stop();
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
