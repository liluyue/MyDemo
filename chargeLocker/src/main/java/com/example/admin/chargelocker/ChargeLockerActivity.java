package com.example.admin.chargelocker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.admin.chargelocker.util.DrawUtils;
import com.example.admin.chargelocker.util.SystemInfo;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by liluyue on 2016/7/1.
 */
public class ChargeLockerActivity extends BaseActivity {
    SlideFinishView slideFinishView;
    private IntentFilter ifilter;
    private WaveLoadingView battery_pg;
    private TextView hour_minute;
    private TextView month_year;

    public static final String CHARGE = "updatecharge";
    public static final String STOP = "stopChargeLockerActivity";
    private TextView percent;
    private TextView left_minites;
    private BroadcastReceiver changeReceiver = new ChangeReceiver();
    private ShimmerTextView st_unlodck;

    @Override
    protected void onStart() {
        super.onStart();
        isUnFinished = true;
        timeHandler.sendEmptyMessage(UPDATETIME);
        if (st_unlodck != null) {
            st_unlodck.startAnim();
        }
        registerReceiver(changeReceiver, ifilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        isUnFinished = false;
        if (st_unlodck != null) {
            st_unlodck.stopAnim();
        }
        unregisterReceiver(changeReceiver);
    }

    @Override
    protected void setUpData() {
        ifilter = new IntentFilter();
        ifilter.addAction(CHARGE);
        ifilter.addAction(STOP);
    }

    @Override
    protected void init() {
        this.getWindow().addFlags(524288);
        this.getWindow().addFlags(4194304);
        View mainView = LayoutInflater.from(this).inflate(idOfLayout("charge_main"), null, false);
        slideFinishView = new SlideFinishView(this, SlideFinishView.SlideDirection.RIGHT_FINISH, mainView);
        slideFinishView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        slideFinishView.setCanScroll(true);
        setContentView(slideFinishView);
        battery_pg = $(idOfId("battery_pg"), mainView);
        hour_minute = $(idOfId("hour_minute"), mainView);
        month_year = $(idOfId("month_year"), mainView);
        percent = $(idOfId("percent"), mainView);
        left_minites = $(idOfId("left_minites"), mainView);
        st_unlodck = $(idOfId("st_unlodck"), mainView);
        Intent intent = new Intent(this, ChargeLockService.class);
        startService(intent);
        changeBattery();
    }

    @Override
    protected void setUpView() {

    }

    public class ChangeReceiver extends BroadcastReceiver {
        public ChangeReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (CHARGE.equals(intent.getAction())) {
                changeBattery();
//                Log.e("168",ChargeInfo.batteryRate+" "+ChargeInfo.chargePlug+" "+ ChargeInfo.isCharging);
            } else if (STOP.equals(intent.getAction())) {
                finish();
            }
        }
    }

    private void changeBattery() {
        if (!ChargeInfo.isCharging()) {
            return;
        }

        String charge = "";
        if (ChargeInfo.isCharging()) {
            if (ChargeInfo.getChargePlug() == BatteryManager.BATTERY_PLUGGED_USB) {
                charge = "usb";
            } else if (ChargeInfo.getChargePlug() == BatteryManager.BATTERY_PLUGGED_AC) {
                charge = "Alternating Current";
            }
        } else {
            charge = "";
        }
        battery_pg.setProgressValue(ChargeInfo.getBatteryRate());
        String nowlevel = String.valueOf(ChargeInfo.getBatteryRate());
        String str = "%";
        int levelSize = nowlevel.length();
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(nowlevel + str);
        spanBuilder.setSpan(new TextAppearanceSpan((String) null, 0, DrawUtils.dip2px(80), (ColorStateList) null, (ColorStateList) null), 0, levelSize, 34);
        percent.setText(spanBuilder);
        if (ChargeInfo.getOnePercentMinute() != 0f) {
            float v = ChargeInfo.getOnePercentMinute() * (100 - ChargeInfo.getBatteryRate());
            int left_hour = (int) (v / 60);
            int left_min = (int) (v % 60);
            String s_left = left_hour > 1 ? left_hour + "小时" + left_min + "分" : left_min + "分";
            s_left = String.format("<font color=\"#ffce54\">%s</font>", s_left);
            left_minites.setText(Html.fromHtml(getResources().getString(idOfString("power_saving_charging_text"), s_left)));
        } else {
            left_minites.setText(null);
        }
        battery_pg.setBottomTitle("电量更改次数:" + ChargeInfo.getChangeCount() + "\n当前充电方式:" + charge);
        StringBuilder s_network = new StringBuilder();
        int count = ChargeInfo.connectiveState.size();
        for (int i = 0; i < count; i++) {
            s_network.append(ChargeInfo.connectiveState.valueAt(i));
        }
        if (s_network.length() == 0) {
            s_network.append("无网络");
        }
        battery_pg.setCenterTitle("当前网络状态:" + s_network);
    }

    private static final int UPDATETIME = 0;

    public Handler timeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATETIME:
                    getTime();
                    if (isUnFinished) {
                        this.sendEmptyMessageDelayed(UPDATETIME, 1000 * 30);
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };
    boolean isUnFinished = false;

    //获得当前年月日时分秒星期
    public void getTime() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getDefault());
      /*  String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        if (mMinute.length() < 2) {
            mMinute = "0" + mMinute;
        }
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒
      */
        if (hour_minute != null) {
            hour_minute.setText(String.format("%tR",c));
        }
        if (month_year != null) {
            if (SystemInfo.language(getApplicationContext()).equals("zh")) {
                month_year.setText(String.format("%tA ,%tm月 %td日",c, c, c));
            } else {
            month_year.setText(String.format("%tA ,%tB %td",c,c,c));
            }
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}

