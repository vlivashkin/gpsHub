package com.gpshub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gpshub.gps.GPSServiceManager;
import com.gpshub.api.AccountManager;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final AccountManager am = new AccountManager();
        if (am.isLoggedIn()) {
            GPSServiceManager.startService(context);
        }
    }

}