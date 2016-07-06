//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.example.admin.chargelocker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.chargelocker.util.DrawUtils;

import java.lang.ref.WeakReference;

public class SlideFinishView extends ViewPager {
    static final String TAG = "SlideFinishView";
    private static final float MIN_ALPHA = 0.3F;
    private WeakReference<Activity> mActivityRef;
    private SlideFinishView.ViewPagerAdapter mViewPagerAdapter = null;
    private View mMainView;
    private View mEmptyView;
    private boolean mCanScroll = true;
    private int mMainIndex;
    private Runnable mFinishTask = new Runnable() {
        public void run() {
            Activity activity = (Activity) SlideFinishView.this.mActivityRef.get();
            if (activity != null) {
                Context context = activity.getApplicationContext();

                activity.finish();
            }

        }
    };
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        public void onPageSelected(int position) {
            if (position == 1 - SlideFinishView.this.mMainIndex) {
                SlideFinishView.this.setCanScroll(false);
                SlideFinishView.this.removeCallbacks(SlideFinishView.this.mFinishTask);
                SlideFinishView.this.postDelayed(SlideFinishView.this.mFinishTask, 400L);
            }

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (SlideFinishView.this.mMainIndex == arg0) {
                DrawUtils.setAlpha(SlideFinishView.this.mMainView, 1.0F);
            } else {
                DrawUtils.setAlpha(SlideFinishView.this.mMainView, Math.min(Math.max(0.3F, arg1), 1.0F));
            }

        }

        public void onPageScrollStateChanged(int arg0) {
        }
    };


    public SlideFinishView(Activity activity, SlideFinishView.SlideDirection direction, View contentView) {
        super(activity.getApplicationContext());
        this.initView(activity, direction, contentView);
    }

    private void initView(Activity activity, SlideFinishView.SlideDirection direction, View contentView) {
        this.setBackgroundColor(Color.parseColor("#434a54"));
        this.processBG();
        this.mActivityRef = new WeakReference(activity);
        this.mMainView = contentView;
        this.mEmptyView = new View(activity.getApplicationContext());
        this.mEmptyView.setBackgroundColor(Color.TRANSPARENT);
        this.setBackgroundColor(Color.TRANSPARENT);
        this.mViewPagerAdapter = new SlideFinishView.ViewPagerAdapter();
        this.setAdapter(this.mViewPagerAdapter);
        this.setOffscreenPageLimit(2);
        byte mainIndex1;
        switch (1 - direction.mValue) {
            case 1:
                mainIndex1 = 0;
                break;
            case 2:
                boolean mainIndex = true;
            default:
                mainIndex1 = 1;
        }

        this.setCurrentItem(mainIndex1);
        this.mMainIndex = mainIndex1;
        this.setOnPageChangeListener(this.mOnPageChangeListener);
    }

    private void processBG() {
        BitmapDrawable bd = null;

    }

    private void setBGDrawable(final BitmapDrawable bd) {

    }

    public void setCanScroll(boolean isCanScroll) {
        this.mCanScroll = isCanScroll;
    }

    public boolean onTouchEvent(MotionEvent arg0) {
        if (this.mCanScroll) {
            try {
                return super.onTouchEvent(arg0);
            } catch (IllegalArgumentException var3) {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return this.mCanScroll ? super.onInterceptTouchEvent(arg0) : false;
    }

    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
//        return v != this && v instanceof AnimationViewTopContainer ? true : super.canScroll(v, checkV, dx, x, y);
        return v != this;
    }

    public class ViewPagerAdapter extends PagerAdapter {
        public ViewPagerAdapter() {
        }

        public int getCount() {
            return 2;
        }

        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == SlideFinishView.this.mMainIndex) {
                SlideFinishView.this.removeView(SlideFinishView.this.mMainView);
            } else if (position == 1 - SlideFinishView.this.mMainIndex) {
                SlideFinishView.this.removeView(SlideFinishView.this.mEmptyView);
            }

        }

        public Object instantiateItem(ViewGroup container, int position) {
            if (position == SlideFinishView.this.mMainIndex) {
                SlideFinishView.this.addView(SlideFinishView.this.mMainView);
                return SlideFinishView.this.mMainView;
            } else if (position == 1 - SlideFinishView.this.mMainIndex) {
                SlideFinishView.this.addView(SlideFinishView.this.mEmptyView);
                return SlideFinishView.this.mEmptyView;
            } else {
                return super.instantiateItem(container, position);
            }
        }
    }

    public static enum SlideDirection {
        LEFT_FINISH(0),
        RIGHT_FINISH(1);

        private int mValue;
        private static final int VALUE_MIN = 0;
        private static final int VALUE_MAX;

        private SlideDirection(int value) {
            this.mValue = value;
        }

        public int getValue() {
            return this.mValue;
        }


        static {
            VALUE_MAX = values().length;
        }
    }
}
