package com.gpshub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.gpshub.api.AccountManager;
import com.gpshub.utils.ContextHack;
import com.gpshub.utils.Preferences;
import com.gpshub.utils.ThemeUtils;

import java.io.IOException;

public class LoginActivity  extends ActionBarActivity {

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

        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText driverId = (EditText) findViewById(R.id.driver_id);
                try {
                    if (AccountManager.login(sa.getUrl(), driverId.getText().toString())) {
                        showMainActivity();
                    } else {
                        showWrongNumberMessage();
                    }
                } catch (IOException e) {
                    showErrorMessage();
                }
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
}

class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
    private String url;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        url = parent.getItemAtPosition(pos).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        url = null;
    }

    public String getUrl() {
        return url;
    }
}