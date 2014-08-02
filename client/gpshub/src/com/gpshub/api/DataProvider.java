package com.gpshub.api;

import android.content.Context;

import com.gpshub.settings.SettingsKeeper;
import com.gpshub.settings.TempSettings;
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
    final String driversURL = "http://javafiddle.org/gpsHub/actions/drivers.php";
    Context context;

    public DataProvider(Context context) {
        this.context = context;
    }

    public void postLocation(double lat, double lng) throws IOException {
        System.out.println("Send: lat: " + lat + ", lng: " + lng + " busy: " + TempSettings.getInstance().isBusy());

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(driversURL);

        SettingsKeeper sk = new SettingsKeeper(context);
        String company_hash = sk.getSharedPreference("company_hash");
        String driver_id = sk.getSharedPreference("driver_id");

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("company_hash", company_hash));
        nameValuePairs.add(new BasicNameValuePair("id", driver_id));
        nameValuePairs.add(new BasicNameValuePair("lat", Double.toString(lat)));
        nameValuePairs.add(new BasicNameValuePair("lng", Double.toString(lng)));
        nameValuePairs.add(new BasicNameValuePair("busy", Boolean.toString(TempSettings.getInstance().isBusy())));
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        httpclient.execute(httppost);
    }
}
