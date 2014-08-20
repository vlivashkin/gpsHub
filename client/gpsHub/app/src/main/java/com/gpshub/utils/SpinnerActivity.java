package com.gpshub.utils;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;

public class SpinnerActivity extends Activity implements AdapterView.OnItemSelectedListener {
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
