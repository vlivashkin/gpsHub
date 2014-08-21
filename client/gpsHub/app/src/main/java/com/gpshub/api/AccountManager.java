package com.gpshub.api;

import android.util.Log;

import com.gpshub.utils.Preferences;

public class AccountManager {
    private static final String TAG = AccountManager.class.getSimpleName();

    public static boolean isLoggedIn() {
        String server_url = Preferences.getPreference("server_url");
        String driver_id = Preferences.getPreference("driver_id");

        Log.i(TAG, "logged in: server_url: " + server_url + ", driver_id: " + driver_id);
        return server_url != null && driver_id != null || Preferences.tryMigration();
    }

    public static void logout() {
        Preferences.removePreference("driver_id");
        Log.i(TAG, "logout");
    }


}
