package com.example.admin.bubbleanim;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

public class Bubble {
	private float x, y, speed;
	private Paint bubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint textPaint=new Paint();
	private int radius =10;
	public static final int MAX_SPEED = 8;
	public static final int MIN_SPEED = 1;
	public static final int MAX_RADIUS=50;
	public static final int MIN_RADIUS=10;
	private int bubbleColor;
	private int bubbleType;
	private static ArrayList<String> textList=new ArrayList<String>();
	private String text="love";
	public static Random random=new Random();

	static{
		textList.add("I");
		textList.add("love");
		textList.add("you");
	}
	public Bubble(float x, float y, float speed) {
		this.x = x;
		this.y = y;
		int temp=random.nextInt(MAX_RADIUS);
		this.radius=temp<MIN_RADIUS?MIN_RADIUS:temp;
		this.speed = speed < MIN_SPEED ? MIN_SPEED : (speed > MAX_SPEED
				? MAX_SPEED
				: speed);
		bubbleColor=Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
		bubblePaint.setColor(bubbleColor);
		bubblePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		bubblePaint.setAlpha(10);
		textPaint.setTextSize(radius/2);
		text=textList.get(random.nextInt(textList.size()));
	}
	
	public void draw(Canvas canvas){
		canvas.drawCircle(x, y, radius, bubblePaint);
		canvas.drawText(text, x-textPaint.measureText(text)/2, y-textPaint.getFontMetrics().top/2, textPaint);
	}
	
	public void move(){
		y-=speed;
	}
	
	public boolean isOutOfRange(){
		return (y+radius)<0;
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
