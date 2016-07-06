package com.example.admin.mydemo;

import android.content.ComponentName;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.shimmer.example.ShimmerActivity;

import me.itangqi.waveloadingview.WaveLoadingActivity;

public class MainActivity extends BaseActivity {
    Snackbar snackbar;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;


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
    }


    @Override
    protected void setUpView() {
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
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
    }

    @Override
    protected void onPause() {
        super.onPause();
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
                        intent = new Intent();
                        intent.setComponent(new ComponentName(getApplication(), WaveLoadingActivity.class));
                        startActivity(intent);
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
