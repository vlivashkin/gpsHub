package com.gpshub.utils;

import android.content.Context;

import com.gpshub.R;

public class Utils {
    public static boolean isNotEmpty(String line) {
        return line != null && !"".equals(line);
    }

    public static String getServerNameByUrl(Context context, String serverUrl) {
        String[] urls = context.getResources().getStringArray(R.array.server_urls);
        String[] names = context.getResources().getStringArray(R.array.server_urls_names);
        for (int i = 0; i < urls.length; i++) {
            if (urls[i].equals(serverUrl)) {
                return names[i];
            }
        }
        return null;
    }
}
