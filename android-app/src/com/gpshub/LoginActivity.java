package com.gpshub;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity  extends Activity {
    public static final String PREFS_NAME = "gpshubprefs";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        if (isLoggedIn()) {
            Intent main = new Intent(this, MainActivity.class);
            startActivity(main);
            finish();
        }

        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText companyHash = (EditText) findViewById(R.id.company_hash);
                EditText driverId = (EditText) findViewById(R.id.driver_id);
                login(companyHash.getText().toString(), driverId.getText().toString());
            }
        });
    }

    public void login(String company_hash, String driver_id) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("company_hash", company_hash));
        nameValuePairs.add(new BasicNameValuePair("id", driver_id));
        String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");

        String url = "http://javafiddle.org/gpsHub/actions/drivers.php?" + paramString;
        System.out.println(url);

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);

        try {
            HttpResponse response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
            String responseText = EntityUtils.toString(entity);
            System.out.println(responseText);

            if ("OK".equals(responseText)) {
                saveSettings(company_hash, driver_id);
                Intent main = new Intent(this, MainActivity.class);
                startActivity(main);
            } else
                Toast.makeText(this, "Wrong data.", Toast.LENGTH_SHORT).show();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isLoggedIn() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String company_hash = settings.getString("company_hash", null);
        String driver_id = settings.getString("driver_id", null);

        System.out.println("company_hash = " + company_hash + " driver_id = " + driver_id);
        return company_hash != null && driver_id != null;
    }

    public void saveSettings(String company_hash, String driver_id) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("company_hash", company_hash);
        editor.putString("driver_id", driver_id);
        editor.commit();
    }
}
