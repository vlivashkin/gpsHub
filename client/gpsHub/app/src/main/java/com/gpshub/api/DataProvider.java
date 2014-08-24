package com.gpshub.api;

import android.util.Log;

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
    private static final String TAG = DataProvider.class.getSimpleName();

    public static void postLocation(String serverURL, String driverID, double lat, double lng, double acc, boolean busy) throws IOException {
        String url = serverURL + "/actions/drivers.php";
        String latString = Double.toString(lat);
        String lngString = Double.toString(lng);
        String accString = Double.toString(acc);
        String busyString = Boolean.toString(busy);

        Log.d(TAG, "url: " + url + ", id: " + driverID);
        Log.d(TAG, "lat: " + latString + ", lng: " + lngString + ", acc: " + accString + ", busy: " + busyString);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", driverID));
        nameValuePairs.add(new BasicNameValuePair("action", "post_location"));
        nameValuePairs.add(new BasicNameValuePair("lat", latString));
        nameValuePairs.add(new BasicNameValuePair("lng", lngString));
        nameValuePairs.add(new BasicNameValuePair("acc", accString));
        nameValuePairs.add(new BasicNameValuePair("busy", busyString));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httpclient.execute(httppost);
    }
}
