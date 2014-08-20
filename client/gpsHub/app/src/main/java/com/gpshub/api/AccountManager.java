package com.gpshub.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;

import com.gpshub.utils.ContextHack;
import com.gpshub.utils.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountManager {
    public static boolean isLoggedIn() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String server_url = Preferences.getPreference("server_url");
        String driver_id = Preferences.getPreference("driver_id");

        Log.d("isLoggedIn", "server_url: " + server_url + ", driver_id: " + driver_id);
        return server_url != null && driver_id != null || tryMigration();
    }

    public static void logout() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Preferences.removePreference("driver_id");

        Log.d("logout", "");
    }

    public static boolean tryMigration() {
        Context context = ContextHack.getAppContext();
        String PREFS_NAME = "gpshubprefs";
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        String driverID = settings.getString("driver_id", null);
        if (driverID != null) {
            settings.edit().remove("driver_id").apply();
            Preferences.setPreference("server_url", "http://javafiddle.org/gpsHub");
            Preferences.setPreference("driver_id", driverID);
            return true;
        }
        return false;
    }
}
