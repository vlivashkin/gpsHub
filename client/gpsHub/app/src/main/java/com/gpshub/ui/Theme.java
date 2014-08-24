package com.gpshub.ui;

import com.gpshub.R;

public enum Theme {
    THEME_DARK("Theme.AppCompat", R.style.Theme_AppCompat),
    THEME_LIGHT("Theme.AppCompat.Light", R.style.Theme_AppCompat_Light);

    private String name;
    private int resource;

    Theme(String name, int resource) {
        this.name = name;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public int getResource() {
        return resource;
    }
}
