package com.gpshub.utils;

import android.app.Activity;
import android.content.Intent;

import com.gpshub.MainActivity;
import com.gpshub.R;

public class ThemeUtils {

    public static void changeTheme(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        String themeName = Preferences.getPreference("ui_theme", "Theme.AppCompat.Light");

        int theme = R.style.Theme_AppCompat_Light;
        if (themeName.equals("Theme.AppCompat")) {
            theme = R.style.Theme_AppCompat;
        } else if (themeName.equals("Theme.AppCompat.Light")) {
            theme = R.style.Theme_AppCompat_Light;
        } else if (themeName.equals("Theme.AppCompat.Light.DarkActionBar")) {
            theme = R.style.Theme_AppCompat_Light_DarkActionBar;
        }

        activity.setTheme(theme);
    }
}
