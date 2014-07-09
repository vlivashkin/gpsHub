package com.gpshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.gpshub.settings.AccountManager;

public class LoginActivity  extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        final AccountManager am = new AccountManager(LoginActivity.this);

        if (am.isLoggedIn()) {
            showMainActivity();
        }

        final Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText driverId = (EditText) findViewById(R.id.driver_id);
                if (am.login("qwerty", driverId.getText().toString())) {
                    showMainActivity();
                } else {
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

    private void showErrorMessage() {
        Toast.makeText(this, "Wrong data.", Toast.LENGTH_SHORT).show();
    }
}
