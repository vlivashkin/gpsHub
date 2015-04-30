package com.gpshub.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.gpshub.utils.Preferences;

public class ServiceManager {
    private static final String TAG = ServiceManager.class.getSimpleName();
    private static ServiceManager instance;

    private Messenger messenger;
    private ServiceConnection sc;

    private ServiceManager() {
    }

    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public void startService(Context context) {
        String serverUrl = Preferences.getServerUrl();
        String driverId = Preferences.getDriverID();

        if (serverUrl != null && driverId != null) {
            Intent intent = new Intent(context, LocationService.class);
            intent.putExtra("server_url", serverUrl);
            intent.putExtra("driver_id", driverId);
            intent.putExtra("busy", Preferences.isBusy().toString());
            intent.putExtra("send_period", Preferences.getSendPeriod().toString());
            intent.putExtra("update_time", Preferences.getUpdateTime().toString());
            intent.putExtra("update_distance", Preferences.getUpdateDistance().toString());

            context.getApplicationContext().startService(intent);
        } else {
            Log.e(TAG, "Service not started! Something is null");
            Log.e(TAG, "ServerURL = " + serverUrl);
            Log.e(TAG, "DriverID = " + driverId);
            Toast.makeText(context, "Cannot start service!", Toast.LENGTH_SHORT).show();
        }
    }

    public void stopService(Context context) {
        unbindService(context);
        Intent intent = new Intent(context, LocationService.class);
        context.getApplicationContext().stopService(intent);
    }

    public void restartService(Context context) {
        if (isServiceRunning(context)) {
            stopService(context);
            startService(context);
            bindService(context);
        }
    }

    public void bindService(Context context) {
        Intent intent = new Intent(context, LocationService.class);
        intent.putExtra("server_url", Preferences.getServerUrl());
        intent.putExtra("driver_id", Preferences.getDriverID());
        intent.putExtra("busy", Preferences.isBusy());

        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                messenger = new Messenger(iBinder);
                Log.i(TAG, "Service connected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.i(TAG, "Service disconnected");
            }
        };

        context.bindService(intent, sc, 0);
    }

    public void unbindService(Context context) {
        if (sc != null) {
            context.unbindService(sc);
            sc = null;
        } else {
            Log.w(TAG, "Unbind service launched but connection is null");
        }
    }

    public boolean isServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void sendMessage(Context context, String key, String value) {
        if (sc == null) {
            bindService(context);
        }
        try {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString(key, value);
            message.setData(bundle);
            messenger.send(message);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote exception while update service data", e);
        }
    }
}
