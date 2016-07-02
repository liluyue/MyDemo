package com.example.admin.mydemo;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class ChargeLockService extends Service {


    private Intent zdLockIntent = null;
    private String TAG="ChargeLockService";

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
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onDestroy");
        return Service.START_STICKY;

    }

    public void onDestroy() {
        super.onDestroy();
        ChargeLockService.this.unregisterReceiver(mScreenOnReceiver);
        ChargeLockService.this.unregisterReceiver(mScreenOffReceiver);
        Log.i(TAG, "onDestroy");
        // �ڴ���������
        startService(new Intent(ChargeLockService.this, ChargeLockService.class));
    }

    private KeyguardManager mKeyguardManager = null;
    private KeyguardManager.KeyguardLock mKeyguardLock = null;
    // ��Ļ�����Ĺ㲥,����Ҫ����Ĭ�ϵ���������
    private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
             /*   mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                mKeyguardLock = mKeyguardManager.newKeyguardLock("");
                mKeyguardLock.disableKeyguard();*/
            }
        }

    };

    // ��Ļ�䰵�Ĺ㲥 �� ����Ҫ����KeyguardManager����Ӧ����ȥ�����Ļ����
    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                startActivity(zdLockIntent);
            }
        }
    };

}
