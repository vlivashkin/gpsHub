package com.gpshub;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gpshub.service.ServiceManager;
import com.gpshub.api.AccountManager;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (AccountManager.isLoggedIn() && !ServiceManager.getInstance().isServiceRunning(context)) {
            ServiceManager.getInstance().startService(context);
        }
    }

}