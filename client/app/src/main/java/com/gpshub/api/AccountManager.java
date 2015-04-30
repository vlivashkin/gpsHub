package com.gpshub.api;

import android.util.Log;

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
    private static final String TAG = AccountManager.class.getSimpleName();
    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_WRONG_NUMBER = 2;
    public static final int RESULT_ERROR = 3;

    public static int login(String url, String driver_id) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("company_hash", "qwerty"));
        nameValuePairs.add(new BasicNameValuePair("id", driver_id));
        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url + "/actions/drivers.php?" + paramString);

        try {
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String responseText = EntityUtils.toString(entity);

            Log.i(TAG, "login result: " + responseText);

            if ("OK".equals(responseText)) {
                Preferences.setServerUrl(url);
                Preferences.setDriverID(driver_id);
                return RESULT_SUCCESS;
            }
        } catch (IOException e) {
            return RESULT_ERROR;
        }

        return RESULT_WRONG_NUMBER;
    }

    public static void logout() {
        Preferences.wipeAccountSettings();
        Log.i(TAG, "logout");
    }

    public static boolean isLoggedIn() {
        String server_url = Preferences.getServerUrl();
        String driver_id = Preferences.getDriverID();

        Log.i(TAG, "logged in: server_url: " + server_url + ", driver_id: " + driver_id);
        return server_url != null && driver_id != null || Preferences.tryMigration();
    }




}
