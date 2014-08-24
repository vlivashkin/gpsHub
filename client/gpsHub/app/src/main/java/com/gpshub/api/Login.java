package com.gpshub.api;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gpshub.MainActivity;
import com.gpshub.R;
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

public class Login extends AsyncTask<String, Void, Integer> {
    private static final String TAG = Login.class.getSimpleName();
    private static final int RESULT_SUCCESS = 1;
    private static final int RESULT_WRONG_NUMBER = 2;
    private static final int RESULT_ERROR = 3;


    Activity context;

    public Login(Activity con) {
        context = con;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        String driver_id = strings[1];

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

    @Override
    protected void onPostExecute(Integer result) {
        switch (result) {
            case RESULT_SUCCESS:
                showMainActivity();
                break;
            case RESULT_WRONG_NUMBER:
                hideProgressBar();
                Toast.makeText(context, context.getString(R.string.login_fail_message), Toast.LENGTH_SHORT).show();
                break;
            case RESULT_ERROR:
                hideProgressBar();
                Toast.makeText(context, context.getString(R.string.connection_error_message), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showMainActivity() {
        Intent main = new Intent(context, MainActivity.class);
        context.startActivity(main);
        context.finish();
    }

    private void hideProgressBar() {
        context.findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
        context.findViewById(R.id.login_progress).setVisibility(View.GONE);
    }
}
