package com.example.admin.mydemo;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.view.SlideFinishView;

import me.itangqi.waveloadingview.WaveLoadingView;

/**
 * Created by admin on 2016/7/1.
 */
public class ChargeLockerActivity extends  BaseActivity2 {
    SlideFinishView slideFinishView;
    private IntentFilter ifilter;
    private WaveLoadingView battery_pg;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void setUpData() {
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        ifilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
//        Intent intent = new Intent(this.getBaseContext(),ChargeLockService.class);
//        startService(intent);
    }

    @Override
    protected void init() {
//        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置无标题
//        getWindow().setFlags(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);  //设置全屏
        this.getWindow().addFlags(524288);
        this.getWindow().addFlags(4194304);
        View mainView = LayoutInflater.from(this).inflate(R.layout.charge_main, null, false);
        ViewGroup.LayoutParams layoutParams = mainView.getLayoutParams();
        slideFinishView=new SlideFinishView(this, SlideFinishView.SlideDirection.RIGHT_FINISH, mainView);
        slideFinishView.setLayoutParams(new ActionBar.LayoutParams(-1, -1));
        slideFinishView.setCanScroll(true);
        setContentView(slideFinishView);
        battery_pg = $(R.id.battery_pg,mainView);
        shimmerFrameLayout = $(R.id.sfl1,mainView);
    }

    @Override
    protected void setUpView() {

    }
    private BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent batteryStatus) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryPct = level / (float) scale * 100;
            changeBattery(batteryPct, chargePlug, isCharging);
        }
    };
    String networkType = "";
    ConnectionChangeReceiver connectionChangeReceiver = new ConnectionChangeReceiver();

    public class ConnectionChangeReceiver extends BroadcastReceiver {
        public ConnectionChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
                boolean isConnect = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Network[] networks = connectivityManager.getAllNetworks();
                    NetworkInfo networkInfo;
                    for (Network mNetwork : networks) {
                        networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                        if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                            networkType = networkInfo.getTypeName();
                            isConnect=true;
                        }
                    }
                } else {
                    if (connectivityManager != null) {
                        //noinspection deprecation
                        NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                        if (info != null) {
                            for (NetworkInfo anInfo : info) {
                                if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                                    Log.d("Network",
                                            "NETWORKNAME: " + anInfo.getTypeName());
                                    networkType = anInfo.getTypeName();
                                    isConnect=true;
                                }
                            }
                        }
                    }
                }
                if (!isConnect) {
                    networkType = "无网络";
                }
            }
            battery_pg.setCenterTitle("网络状态:" + networkType);
        }
    }
    private void changeBattery(float batteryPct, int chargePlug, boolean isCharging) {

        String charge = "";
        if (isCharging) {
            if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
                charge = "usb";
            } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
                charge = "Alternating Current";
            }
        } else {
            charge = "";
        }
        battery_pg.setProgressValue((int) batteryPct);
        battery_pg.setTopTitle("当前电量为:" + batteryPct + "%" + "\n当前充电方式:" + charge);
    }
    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
        registerReceiver(batteryLevelReceiver, ifilter);
        registerReceiver(connectionChangeReceiver, ifilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmerAnimation();
        unregisterReceiver(batteryLevelReceiver);
        unregisterReceiver(connectionChangeReceiver);
    }

}

