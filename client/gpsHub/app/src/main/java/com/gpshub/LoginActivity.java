package com.gpshub;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.flurry.android.FlurryAgent;
import com.gpshub.api.Login;
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
                showProgressBar();
                EditText driverId = (EditText) findViewById(R.id.driver_id);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(driverId.getWindowToken(), 0);
                new Login(LoginActivity.this).execute(sa.getUrl(), driverId.getText().toString());
                FlurryAgent.logEvent("LoginClick");
            }
        });
    }

    private void showProgressBar() {
        findViewById(R.id.login_layout).setVisibility(View.GONE);
        findViewById(R.id.login_progress).setVisibility(View.VISIBLE);
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
}