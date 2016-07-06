package com.example.admin.chargelocker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * Created by _SOLID
 * Date:2016/3/30
 * Time:9:40
 */
public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
//        setContentView(setLayoutResourceID());
        setUpView();
        setUpData();
    }

    protected abstract void setUpData();

    /***
     * 用于在初始化View之前做一些事
     */
    protected abstract void init();

    protected abstract void setUpView();

    protected int setLayoutResourceID() {
        return 0;
    }

    ;

    protected <T extends View> T $(int id) {
        return (T) super.findViewById(id);
    }

    protected <T extends View> T $(int id, View parent) {
        return (T) parent.findViewById(id);
    }

    protected int idOfLayout(String s) {
       return getResources().getIdentifier(s, "layout", getPackageName());
    }

    protected int idOfId(String s) {
       return getResources().getIdentifier(s, "id", getPackageName());
    }

    protected int idOfString(String s) {
       return getResources().getIdentifier(s, "string", getPackageName());
    }

    protected void startActivityWithoutExtras(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    protected void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);

    }

}
