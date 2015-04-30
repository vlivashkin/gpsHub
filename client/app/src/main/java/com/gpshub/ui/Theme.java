package com.gpshub.ui;

import com.gpshub.R;

public enum Theme {
    THEME_DARK("Theme.AppCompat", R.style.Theme_AppCompat, R.drawable.abc_cab_background_top_holo_dark),
    THEME_LIGHT("Theme.AppCompat.Light", R.style.Theme_AppCompat_Light, R.drawable.abc_cab_background_top_holo_light);

    private String name;
    private int resource;
    private int headerBkgResource;

    Theme(String name, int resource, int headerBkgResource) {
        this.name = name;
        this.resource = resource;
        this.headerBkgResource = headerBkgResource;
    }

    public String getName() {
        return name;
    }

    public int getResource() {
        return resource;
    }

    public int getHeaderBkgResource() {
        return headerBkgResource;
    }

    public static Theme getTheme(String name) {
        if (name != null) {
            if (name.equals(THEME_LIGHT.getName()))
                return THEME_LIGHT;
            else if (name.equals(THEME_DARK.getName()))
                return THEME_DARK;
        }
        return null;
    }
}
