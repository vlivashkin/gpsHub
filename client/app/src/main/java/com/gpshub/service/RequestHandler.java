package com.gpshub.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.gpshub.utils.Utils;

public class RequestHandler extends Handler {
    @Override
    public void handleMessage(Message msg) {
        Bundle bundle = msg.getData();
        for (String key : bundle.keySet()) {
            String value = bundle.getString(key);
            if (Utils.isNotEmpty(value)) {
                ServiceTempPrefs.getInstance().put(key, value);
            }
        }
    }
}
