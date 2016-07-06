package com.example.admin.chargelocker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.SparseArray;

/**
 * 只有ChargeService正在运行时数据才具有可取性
 * Created by liluyue on 2016/7/4.
 */
public class ChargeInfo {
    private static final String TAG = "ChargeInfo";
    public static final String SP_CHARGEINFO_NAME="chargeinfo";
    public static final String KEY_BATTERYRATE = "batteryRate";
    public static final String KEY_ONEPERCENTMINUTE = "onePercentMinute";
    public static SparseArray<String> connectiveState = new SparseArray<>();
    private static int batteryRate;
    private static int chargePlug;
    /**
     *true: 当ChargeLockService正在运行且电量有过改变时
     * false:
     */
    private static boolean isCharging=false;
    /**
     * 平均增加百分之一电量所需的时间
     */
    private static float onePercentMinute = 0f;
    /**
     * 上一次电量增加的时间
     */
    private static long lastMillTime = 0;
    /**
     * 监听到电量改变的次数
     */
    private static int changeCount;
    private SharedPreferences sp_chargeinfo;
    private String sp_chargeinfo_name;

//    public static int lastMillTime;

    public static void updateChargeInfo(long millTime, int butteryRate, int chargePlug, boolean isCharging) {
        int addNum = ChargeInfo.batteryRate - butteryRate;
        ChargeInfo.batteryRate = butteryRate;
        /*如果电量增加则更新lastMillTime,档电量增加且lastMillTime>0时更新onePercentMinute*/
        if (addNum > 0) {
            if (lastMillTime > 0) {
                onePercentMinute = (millTime - ChargeInfo.lastMillTime) / addNum / 1000 / 60;
            }
            lastMillTime = millTime;
        }
        ChargeInfo.isCharging = isCharging;
        ChargeInfo.chargePlug = chargePlug;
        changeCount++;
    }

    public static int getBatteryRate() {
        return batteryRate;
    }

    public static int getChargePlug() {
        return chargePlug;
    }

    public static boolean isCharging() {
        return isCharging;
    }

    public static void setIsCharging(boolean isCharging) {
        ChargeInfo.isCharging = isCharging;
    }

    public static float getOnePercentMinute() {
        return onePercentMinute;
    }

    public static long getLastMillTime() {
        return lastMillTime;
    }

    public static int getChangeCount() {
        return changeCount;
    }

    public static void resolveOnePercentMinute(Context context) {
        SharedPreferences sp_chargeinfo = context.getSharedPreferences(SP_CHARGEINFO_NAME, Context.MODE_PRIVATE);
        onePercentMinute = sp_chargeinfo.getFloat(KEY_ONEPERCENTMINUTE, onePercentMinute);
    }

    public static void saveOnePercentMinute(Context context) {
        isCharging=false;
        SharedPreferences sp_chargeinfo = context.getSharedPreferences(SP_CHARGEINFO_NAME, Context.MODE_PRIVATE);
        sp_chargeinfo.edit().putFloat(KEY_ONEPERCENTMINUTE,onePercentMinute).commit();
    }
}
