package com.example.admin.chargelocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by admin on 2016/7/5.
 */
public class PowerReceiver extends BroadcastReceiver {
    private static final String TAG = "PowerReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.e(TAG,intent.getAction());
        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())){
             intent=new Intent(context,ChargeLockService.class);
            context.startService(intent);
        }else if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())){
            intent=new Intent(context,ChargeLockService.class);
             context.stopService(intent);
            intent = new Intent(ChargeLockerActivity.STOP);
            context.sendBroadcast(intent);
        }
    }
}
