package com.gpshub.api;

import android.util.Log;

import com.gpshub.utils.Preferences;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    public static void postLocation(double lat, double lng, double acc) throws IOException {
        String url = Preferences.getPreference("server_url") + "/actions/drivers.php";
        String driver_id = Preferences.getPreference("driver_id");
        String latString = Double.toString(lat);
        String lngString = Double.toString(lng);
        String accString = Double.toString(acc);
        String busyString = Boolean.toString(Preferences.isBusy());

        Log.d("postLocation", "url: " + url + ", id: " + driver_id);
        Log.d("postLocation", "lat: " + latString + ", lng: " + lngString + ", acc: " + accString + ", busy: " + busyString);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", driver_id));
        nameValuePairs.add(new BasicNameValuePair("action", "post_location"));
        nameValuePairs.add(new BasicNameValuePair("lat", latString));
        nameValuePairs.add(new BasicNameValuePair("lng", lngString));
        nameValuePairs.add(new BasicNameValuePair("acc", accString));
        nameValuePairs.add(new BasicNameValuePair("busy", busyString));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httpclient.execute(httppost);
    }
}
