package com.gpshub.service;

import java.util.HashMap;

public class ServiceTempPrefs extends HashMap<String, Object> {
    private static ServiceTempPrefs instance;

    private ServiceTempPrefs() {
    }

    public static ServiceTempPrefs getInstance() {
        if (instance == null) {
            instance = new ServiceTempPrefs();
        }
        return instance;
    }

    public String getServerURL() {
        return get("server_url").toString();
    }

    public String getDriverID() {
        return get("driver_id").toString();
    }

    public Boolean isBusy() {
        return Boolean.valueOf(get("busy").toString());
    }

    public Long getSendPeriod() {
        return Long.valueOf(get("send_period").toString());
    }

    public Long getUpdateTime() {
        return Long.valueOf(get("update_time").toString());
    }

    public Float getUpdateDistance() {
        return Float.valueOf(get("update_distance").toString());
    }
}
