package com.example.admin.chargelocker;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

public class ChargeLockService extends Service {


    private Intent zdLockIntent = null;
    private String TAG = "ChargeLockService";
    private IntentFilter ifilter;

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        super.onCreate();
        zdLockIntent = new Intent(this, ChargeLockerActivity.class);
        zdLockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        IntentFilter mScreenOnFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        this.registerReceiver(mScreenOnReceiver, mScreenOnFilter);

		/* ע��㲥 */
        IntentFilter mScreenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        this.registerReceiver(mScreenOffReceiver, mScreenOffFilter);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        ifilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(batteryLevelReceiver, ifilter);
        ChargeInfo.resolveOnePercentMinute(this);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.i(TAG, "onDestroy");
        return Service.START_STICKY;

    }

    public void onDestroy() {
        ChargeInfo.saveOnePercentMinute(this);
        unregisterReceiver(batteryLevelReceiver);
        super.onDestroy();
        ChargeLockService.this.unregisterReceiver(mScreenOnReceiver);
        ChargeLockService.this.unregisterReceiver(mScreenOffReceiver);
//        Log.e(TAG, "onDestroy");
        // �ڴ���������
//        startService(new Intent(ChargeLockService.this, ChargeLockService.class));
    }

    private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            }
        }

    };

    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                startActivity(zdLockIntent);
            }
        }
    };
    private BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent batteryStatus) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(batteryStatus.getAction())) {
                int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                        status == BatteryManager.BATTERY_STATUS_FULL;
                int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                float scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int batteryPct = (int) (level / scale * 100);
//                Log.e("95", batteryPct + " " + chargePlug + " " + isCharging+"  "+level+" "+ scale);

                ChargeInfo.updateChargeInfo(System.currentTimeMillis(), batteryPct, chargePlug, isCharging);
                Intent intent = new Intent(ChargeLockerActivity.CHARGE);
                sendBroadcast(intent);
            } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(batteryStatus.getAction())) {
                ChargeInfo.connectiveState.clear();
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] networks = connectivityManager.getAllNetworks();
                    NetworkInfo networkInfo;
                    for (Network mNetwork : networks) {
                        networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                        if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                            ChargeInfo.connectiveState.put(networkInfo.getType(), networkInfo.getTypeName());
                        }
                    }
                } else {
                    if (connectivityManager != null) {
                        //noinspection deprecation
                        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                        if (info != null) {
                            for (NetworkInfo networkInfo : info) {
                                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                                    Log.d("Network", "NETWORKNAME: " + networkInfo.getTypeName());
                                    ChargeInfo.connectiveState.put(networkInfo.getType(), networkInfo.getTypeName());
                                }
                            }
                        }
                    }
                }
                Intent intent = new Intent(ChargeLockerActivity.CHARGE);
                sendBroadcast(intent);
            }
        }
    };
}
