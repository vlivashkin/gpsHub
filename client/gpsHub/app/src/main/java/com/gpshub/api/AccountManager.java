package com.gpshub.api;

import android.os.StrictMode;
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
    public static boolean login(String url, String driver_id) throws IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("company_hash", "qwerty"));
        nameValuePairs.add(new BasicNameValuePair("id", driver_id));
        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url + "/actions/drivers.php?" + paramString);

        HttpResponse response = httpclient.execute(httpget);

        HttpEntity entity = response.getEntity();
        String responseText = EntityUtils.toString(entity);
        Log.d("login", responseText);

        if ("OK".equals(responseText)) {
            Preferences.setPreference("server_url", url);
            Preferences.setPreference("driver_id", driver_id);
            return true;
        }

        return false;
    }

    public static boolean isLoggedIn() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String server_url = Preferences.getPreference("server_url");
        String driver_id = Preferences.getPreference("driver_id");

        Log.d("isLoggedIn", "server_url: " + server_url + ", driver_id: " + driver_id);
        return server_url != null && driver_id != null;
    }

    public static void logout() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Preferences.removePreference("driver_id");

        Log.d("logout", "");
    }

    public static void migration() {
        Preferences.
    }
}
