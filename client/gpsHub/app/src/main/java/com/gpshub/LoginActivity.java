package com.gpshub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.gpshub.api.Login;
import com.gpshub.utils.ContextHack;
import com.gpshub.utils.Preferences;
import com.gpshub.utils.SpinnerActivity;
import com.gpshub.utils.ThemeUtils;

public class LoginActivity  extends ActionBarActivity {
    Activity activity;

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

        String url = Preferences.getPreference("server_url");
        if (url != null) {
            spinner.setSelection(adapter.getPosition(url));
        }

        activity = this;
        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgressBar();
                EditText driverId = (EditText) findViewById(R.id.driver_id);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(driverId.getWindowToken(), 0);
                new Login(activity).execute(sa.getUrl(), driverId.getText().toString());
            }
        });
    }

    private void showMainActivity() {
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);
        finish();
    }

    private void showWrongNumberMessage() {
        Toast.makeText(this, getString(R.string.login_fail_message), Toast.LENGTH_SHORT).show();
    }

    private void showErrorMessage() {
        Toast.makeText(this, getString(R.string.connection_error_message), Toast.LENGTH_LONG).show();
    }

    private void showProgressBar() {
        findViewById(R.id.login_layout).setVisibility(View.GONE);
        findViewById(R.id.login_progress).setVisibility(View.VISIBLE);
    }
}