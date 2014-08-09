package com.gpshub.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gpshub.GPSService;

public class GPSServiceManager {
    public static void startService(Context context) {
        ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                Preferences.setGpsEnabled(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                Preferences.setGpsEnabled(false);
            }
        };
        Intent intent = new Intent(context, GPSService.class);
        context.getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Preferences.setConnection(mConnection);
    }

    public static void stopService(Context context) {
        if (Preferences.isGpsEnabled()) {
            context.getApplicationContext().unbindService(Preferences.getConnection());
            Preferences.setGpsEnabled(false);
        }
    }
}
