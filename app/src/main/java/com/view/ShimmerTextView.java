//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.widget.TextView;

public class ShimmerTextView extends TextView {
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mLinearGradientMatrix;
    private float mGradientX = 0.0F;
    private float mGradientXSpeed = 0.0F;
    private boolean mHasInit = false;
    private int mPrimaryColor = -1;
    private int mReflectionColor = -12303292;
    private float sDensity=1f;

    public ShimmerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShimmerTextView(Context context) {
        super(context);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.init();
    }

    private void init() {
        if(!this.mHasInit) {
            this.mPaint = this.getPaint();
            this.mLinearGradient = new LinearGradient((float)(-this.getMeasuredWidth()), 0.0F, 0.0F, 0.0F, new int[]{this.mPrimaryColor, this.mReflectionColor, this.mPrimaryColor}, new float[]{0.0F, 0.5F, 1.0F}, TileMode.CLAMP);
            this.mPaint.setShader(this.mLinearGradient);
            this.mLinearGradientMatrix = new Matrix();
            this.mGradientXSpeed = sDensity * 2.0F;
            this.mHasInit = true;
        }
    }

    protected void onDraw(Canvas canvas) {
        if(this.mHasInit) {
            if(this.mPaint.getShader() == null) {
                this.mPaint.setShader(this.mLinearGradient);
            }

            this.mGradientX += this.mGradientXSpeed;
            if(this.mGradientX >= (float)(2 * this.getMeasuredWidth())) {
                this.mGradientX = 0.0F;
            }

            this.mLinearGradientMatrix.setTranslate(2.0F * this.mGradientX, 0.0F);
            this.mLinearGradient.setLocalMatrix(this.mLinearGradientMatrix);
        }

        super.onDraw(canvas);
        this.invalidate();
    }
}
