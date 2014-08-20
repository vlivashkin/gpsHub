package com.gpshub.utils;

import android.app.Activity;
import android.content.Intent;

import com.gpshub.MainActivity;
import com.gpshub.R;

public class ThemeUtils {
    public static final String THEME_DARK = "Theme.AppCompat";
    public static final String THEME_LIGHT = "Theme.AppCompat.Light";

    public static void changeTheme(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
    }

    public static void onActivityCreateSetTheme(Activity activity) {
        String themeName = Preferences.getPreference("ui_theme", THEME_LIGHT);

        int theme;
        if (themeName.equals(THEME_DARK)) {
            theme = R.style.Theme_AppCompat;
        } else {
            theme = R.style.Theme_AppCompat_Light;
        }

        activity.setTheme(theme);
    }
}
