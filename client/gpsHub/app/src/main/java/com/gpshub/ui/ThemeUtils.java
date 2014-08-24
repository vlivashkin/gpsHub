package com.gpshub.ui;

import android.app.Activity;
import android.content.Intent;

import com.gpshub.MainActivity;
import com.gpshub.utils.Preferences;

public class ThemeUtils {

    public static void changeTheme(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        Theme theme = Preferences.getUiTheme();
        activity.setTheme(theme.getResource());
    }
}
