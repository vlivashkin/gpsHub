package com.gpshub;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.gpshub.api.AccountManager;
import com.gpshub.ui.SpinnerActivity;
import com.gpshub.ui.ThemeUtils;
import com.gpshub.utils.AppConstants;
import com.gpshub.utils.ContextHack;
import com.gpshub.utils.Preferences;

public class LoginActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ContextHack.setAppContext(getApplicationContext());
        ThemeUtils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final SpinnerActivity sa = new SpinnerActivity();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.server_urls, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(sa);

        String url = Preferences.getServerUrl();
        if (url != null) {
            spinner.setSelection(adapter.getPosition(url));
        }

        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText password = (EditText) findViewById(R.id.password);
                EditText driverId = (EditText) findViewById(R.id.driver_id);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(driverId.getWindowToken(), 0);

                FlurryAgent.logEvent("TryLoginClick");

                if (password.getText().toString().equals(AppConstants.PASSWORD)) {
                    showProgressBar();
                    asyncLogin(sa.getUrl(), driverId.getText().toString());
                    FlurryAgent.logEvent("LoginClick");
                } else {
                    Toast.makeText(LoginActivity.this, getString(R.string.wrong_password_message), Toast.LENGTH_SHORT).show();
                    if (password.requestFocus()) {
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FlurryAgent.onStartSession(this, AppConstants.FLURRY_API_KEY);
        FlurryAgent.setLogEnabled(true);
        FlurryAgent.setLogEvents(true);
        FlurryAgent.setLogLevel(Log.VERBOSE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }

    private void asyncLogin(String url, final String driver_id) {
        AsyncTask<String, Void, Integer> task = new AsyncTask<String, Void, Integer>() {
            @Override
            protected Integer doInBackground(String... strings) {
                String url = strings[0];
                String driver_id = strings[1];

                return AccountManager.login(url, driver_id);
            }

            @Override
            protected void onPostExecute(Integer result) {
                switch (result) {
                    case AccountManager.RESULT_SUCCESS:
                        showMainActivity();
                        break;
                    case AccountManager.RESULT_WRONG_NUMBER:
                        hideProgressBar();
                        Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.login_fail_message), Toast.LENGTH_SHORT).show();
                        EditText driverId = (EditText) findViewById(R.id.driver_id);
                        if (driverId.requestFocus()) {
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        }
                        break;
                    case AccountManager.RESULT_ERROR:
                        hideProgressBar();
                        Toast.makeText(LoginActivity.this, LoginActivity.this.getString(R.string.connection_error_message), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        task.execute(url, driver_id);
    }

    private void showMainActivity() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    private void showProgressBar() {
        findViewById(R.id.login_layout).setVisibility(View.GONE);
        findViewById(R.id.login_progress).setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        findViewById(R.id.login_progress).setVisibility(View.GONE);
        findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
    }
}