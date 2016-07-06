package com.example.admin.chargelocker.util;

import android.os.Build;
import android.view.View;

/**
 * Created by admin on 2016/7/1.
 */
public class DrawUtils {
    public static float sDensity=2f;

    public static void setAlpha(View view, float alpha) {
        if(Build.VERSION.SDK_INT >= 11) {
            view.setAlpha(alpha);
        } else {
            view.getBackground().setAlpha((int)(alpha * 255.0F));
        }
    }
    public static int dip2px(float dipVlue) {
        return (int)(dipVlue * sDensity + 0.5F);
    }

    public static int px2dip(float pxValue) {
        float scale = sDensity;
        return (int)(pxValue / scale + 0.5F);
    }

    public static int sp2px(float spValue) {
        float scale = sDensity;
        return (int)(scale * spValue);
    }

    public static int px2sp(float pxValue) {
        float scale = sDensity;
        return (int)(pxValue / scale);
    }

}
