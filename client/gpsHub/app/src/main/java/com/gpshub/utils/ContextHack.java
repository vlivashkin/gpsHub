package com.gpshub.utils;


import android.app.Application;
import android.content.Context;

public class ContextHack extends Application {

    private static volatile Context context;

    @Override
    public void onCreate(){
        super.onCreate();
        ContextHack.context = getApplicationContext();
    }

    public static void setAppContext(Context c) {
        context = c;
    }

    public static Context getAppContext() {
        return ContextHack.context;
    }
}