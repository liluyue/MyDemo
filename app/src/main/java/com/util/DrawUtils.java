package com.util;

import android.os.Build;
import android.view.View;

/**
 * Created by admin on 2016/7/1.
 */
public class DrawUtils {
    public static void setAlpha(View view, float alpha) {
        if(Build.VERSION.SDK_INT >= 11) {
            view.setAlpha(alpha);
        } else {
            view.getBackground().setAlpha((int)(alpha * 255.0F));
        }

    }
}
