package com.gpshub.api;

import android.util.Log;

import com.gpshub.utils.Preferences;

public class AccountManager {
    private static final String TAG = AccountManager.class.getSimpleName();

    public static boolean isLoggedIn() {
        String server_url = Preferences.getServerUrl();
        String driver_id = Preferences.getDriverID();

        Log.i(TAG, "logged in: server_url: " + server_url + ", driver_id: " + driver_id);
        return server_url != null && driver_id != null || Preferences.tryMigration();
    }

    public static void logout() {
        Preferences.wipeAccountSettings();
        Log.i(TAG, "logout");
    }


}
