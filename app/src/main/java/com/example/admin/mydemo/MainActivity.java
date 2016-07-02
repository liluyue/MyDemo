package com.example.admin.mydemo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.facebook.shimmer.example.ShimmerActivity;

import me.itangqi.waveloadingview.WaveLoadingView;

public class MainActivity extends BaseActivity {
    Snackbar snackbar;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private ShimmerFrameLayout shimmerFrameLayout;
    private WaveLoadingView battery_pg;
    private IntentFilter ifilter;
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

    @Override
    protected void setUpData() {
        Intent intent = new Intent(this.getBaseContext(),ChargeLockService.class);
        startService(intent);
    }

    @Override
    protected void init() {
       /* this.getWindow().addFlags(524288);
        this.getWindow().addFlags(4194304);*/
        View mainView = LayoutInflater.from(this).inflate(R.layout.activity_main, null, false);
        setContentView(mainView);
        toolbar = $(R.id.toolbar,mainView);
        fab = $(R.id.fab,mainView);
        mNavigationView = $(R.id.navigation_view,mainView);
        mDrawerLayout = $(R.id.drawerlayout,mainView);
        shimmerFrameLayout = $(R.id.sfl1,mainView);
        battery_pg = $(R.id.battery_pg,mainView);
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
    protected void setUpView() {
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       changeBattery(battery_pg.getProgressValue() + 10, 0, false);
                                       if (snackbar == null) {
                                           snackbar = Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG);
                                           snackbar.setAction("Action", new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if (snackbar != null) {
                                                       snackbar.dismiss();
                                                   }

                                               }
                                           });
                                       }
                                       snackbar.show();
                                   }
                               }
        );
        setNavigationViewItemClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        ifilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNavigationViewItemClickListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
            /*    if (null != mPreMenuItem) {
                    mPreMenuItem.setChecked(false);
                }*/
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.navigation_item_shimmer:
                         intent = new Intent();
                        intent.setComponent(new ComponentName(getApplication(), ShimmerActivity.class));
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_waveloading:

                        break;
                    case R.id.navigation_item_chargeLocker:
                         intent = new Intent();
                        intent.setComponent(new ComponentName(getApplication(), ChargeLockerActivity.class));
                        startActivity(intent);
                        break;
                    case R.id.navigation_item_bubble:
                        intent = new Intent();
                        intent.setComponent(new ComponentName(getApplication(), BubbleActivity.class));
                        startActivity(intent);
                        break;

                    default:
                        break;
                }
                item.setChecked(true);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
    }
}
