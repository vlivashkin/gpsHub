package com.gpshub.gps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.gpshub.utils.TempSettings;

public class GPSServiceManager {
    public static void startService(Context context) {
        ServiceConnection mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                TempSettings.getInstance().setGpsEnabled(true);
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                TempSettings.getInstance().setGpsEnabled(false);
            }
        };
        Intent intent = new Intent(context, GPSService.class);
        context.getApplicationContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        TempSettings.getInstance().setConnection(mConnection);

    }

    public static void stopService(Context context) {
        TempSettings ts = TempSettings.getInstance();
        if (ts.isGpsEnabled()) {
            context.getApplicationContext().unbindService(ts.getConnection());
            ts.setGpsEnabled(false);
        }
    }


}
