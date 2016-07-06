import android.app.Application;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

/**
 * Created by admin on 2016/7/5.
 */
public class MyApplication extends Application {
    private static final String TAG = "Application";
    private SharedPreferences sp_chargeinfo;

    @Override
    public void onCreate() {
        // 程序创建的时候执行
        Log.d(TAG, "onCreate");
        sp_chargeinfo=getSharedPreferences("chargeinfo",MODE_PRIVATE);
//        ChargeInfo.connectiveState=sp_chargeinfo.getInt(ChargeInfo.KEY_BATTERYRATE,0);
        super.onCreate();
    }
    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d(TAG, "onTerminate");
        if (sp_chargeinfo!=null){
//            sp_chargeinfo.edit().putInt(ChargeInfo.KEY_BATTERYRATE,ChargeInfo.batteryRate).commit();
        }
        super.onTerminate();
    }
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        Log.d(TAG, "onLowMemory");
        super.onLowMemory();
    }
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        Log.d(TAG, "onTrimMemory");
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
