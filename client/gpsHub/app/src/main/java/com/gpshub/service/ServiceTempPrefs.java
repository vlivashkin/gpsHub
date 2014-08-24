package com.gpshub.service;

public class ServiceTempPrefs {
    private static ServiceTempPrefs instance;

    private String serverURL;
    private String driverID;
    private Boolean busy;

    private ServiceTempPrefs() {
    }

    public static ServiceTempPrefs getInstance() {
        if (instance == null) {
            instance = new ServiceTempPrefs();
        }
        return instance;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String driverID) {
        this.driverID = driverID;
    }

    public Boolean isBusy() {
        return busy;
    }

    public void setBusy(Boolean busy) {
        this.busy = busy;
    }
}
