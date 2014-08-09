package com.gpshub.api;

import com.gpshub.utils.Preferences;
import com.gpshub.utils.TempSettings;
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

    public void postLocation(double lat, double lng) throws IOException {
        String url = Preferences.getPreference("server_url");
        String driver_id = Preferences.getPreference("driver_id");

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url + "/actions/drivers.php");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", driver_id));
        nameValuePairs.add(new BasicNameValuePair("action", "post_location"));
        nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
        nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lng)));
        nameValuePairs.add(new BasicNameValuePair("busy", Boolean.toString(TempSettings.getInstance().isBusy())));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httpclient.execute(httppost);
    }
}
