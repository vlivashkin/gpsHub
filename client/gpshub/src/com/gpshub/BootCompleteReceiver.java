package com.gpshub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gpshub.gps.GPSService;
import com.gpshub.settings.AccountManager;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final AccountManager am = new AccountManager(context.getApplicationContext());
        if (am.isLoggedIn()) {
            Intent service = new Intent(context.getApplicationContext(), GPSService.class);
            context.startService(service);
        }
    }

}