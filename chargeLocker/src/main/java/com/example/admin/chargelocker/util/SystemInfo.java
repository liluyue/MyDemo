package com.example.admin.chargelocker.util;

import android.content.Context;

import java.util.Locale;

/**
 * Created by liluyue on 2016/7/6.
 */
public class SystemInfo {
    public static String language(Context context) {
        return Locale.getDefault().getLanguage().toLowerCase();
    }
}
