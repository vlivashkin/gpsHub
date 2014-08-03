package com.gpshub.gps;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.StrictMode;
import android.widget.Toast;

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
            return GPSService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        dp = new DataProvider(GPSService.this);
        showNotification();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mNM.cancel(R.string.remote_service_started);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        startTimer();
        Toast.makeText(this, "gpsHub: Сервис запущен", Toast.LENGTH_LONG).show();
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopTimer();
        Toast.makeText(this, "gpsHub: Сервис остановлен", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }

    public void startTimer() {
        gps = new GPSMonitor(GPSService.this);
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
                        System.out.println("Data transfer error");
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
        CharSequence text = "Нажмите, чтобы открыть приложение.";
        Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        notification.setLatestEventInfo(this, "GPS-сервис работает", text, contentIntent);
        mNM.notify(R.string.remote_service_started, notification);
    }
}
