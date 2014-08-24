package com.gpshub.service;

import android.os.Handler;
import android.os.Message;

import com.gpshub.utils.Utils;

public class RequestHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        String serverURL = msg.getData().getString("server_url");
        if (Utils.isNotEmpty(serverURL)) {
            ServiceTempPrefs.getInstance().setServerURL(serverURL);
        }

        String driverID = msg.getData().getString("driver_id");
        if (Utils.isNotEmpty(driverID)) {
            ServiceTempPrefs.getInstance().setServerURL(driverID);
        }

        String busy = msg.getData().getString("busy");
        if (Utils.isNotEmpty(busy)) {
            ServiceTempPrefs.getInstance().setBusy(Boolean.valueOf(busy));
        }
    }
}
