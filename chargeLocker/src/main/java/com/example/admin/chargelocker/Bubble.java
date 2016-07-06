package com.example.admin.chargelocker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

import com.example.admin.chargelocker.util.DrawUtils;

import java.util.ArrayList;
import java.util.Random;

public class Bubble {
    private final int mAlpha;
    private float x, y, speed;
    private Paint bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint();
    public int radius = 10;
    public static final int MAX_SPEED = 10;
    public static final int MIN_SPEED = 1;
    public static final int MAX_RADIUS = 25;
    public static final int MIN_RADIUS = 15;
    private int bubbleColor=0x2B2B2B;
    private int bubbleType;
    private static ArrayList<String> textList = new ArrayList<String>();
    public String text = "love";
    public static Random random = new Random();
    public int  screenWith;
    public int  screenHeight;
    static {
        textList.add("I");
        textList.add("love");
        textList.add("you");
    }

    private float aFloat;

    public Bubble( int screenWith,int screenHeight) {
        this.screenWith=screenWith;
        this.screenHeight=screenHeight;
        int temp = random.nextInt(MAX_RADIUS);
        this.radius = temp < MIN_RADIUS ? MIN_RADIUS : temp;
     /*   this.speed = speed < MIN_SPEED ? MIN_SPEED : (speed > MAX_SPEED
                ? MAX_SPEED
                : speed);*/
//        bubbleColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        RadialGradient radialGradient=new RadialGradient(x,y,radius,bubbleColor, Color.WHITE, Shader.TileMode.CLAMP);
        this.x = (int)((float)screenWith * 0.45F + (float)random.nextInt((int)((float)this.screenWith * 0.1F)));
        this.y = this.screenHeight;
        this.radius = (int)(DrawUtils.sDensity * 4.0F + (float)this.random.nextInt((int)(DrawUtils.sDensity * 4.0F)));
        this.speed = (int)(DrawUtils.sDensity * 6.0F + (float)this.random.nextInt((int)(DrawUtils.sDensity * 6.0F)));
        this.mAlpha = (int)((float)this.base_Alpa + 50.0F * this.random.nextFloat());
        bubblePaint.setShader(radialGradient);
        bubblePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(radius / 2);
        text = textList.get(random.nextInt(textList.size()));
    }

    int base_Alpa = 105;

    public void draw(Canvas canvas) {
        aFloat = y - radius - top;
        if (aFloat < 0) {
            aFloat = 0;
        } else if (aFloat < 200) {
            bubblePaint.setAlpha((int) (base_Alpa * (aFloat) / 200));
        } else {
            bubblePaint.setAlpha(base_Alpa);
        }
        canvas.drawCircle(x, y, radius, bubblePaint);
//        canvas.drawText(text, x - textPaint.measureText(text) / 2, y - textPaint.getFontMetrics().top / 2, textPaint);
        move();
    }

    public void move() {
        y -= speed;
        if (y< Bubble.top){
            y=screenHeight+1*radius;
        }
       if (random.nextBoolean()){
           x+=random.nextInt(6);
       }else {
           x-=random.nextInt(6);
       }
       if (x-radius<0){
           x=radius;
       }else if (x+radius>screenWith){
           x=screenWith-radius;
       }
    }

    public static int top = 0;

    public boolean isOutOfRange() {
        return (y + radius) < top;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Paint getBubblePaint() {
        return bubblePaint;
    }

    public void setBubblePaint(Paint bubblePaint) {
        this.bubblePaint = bubblePaint;
    }


}
