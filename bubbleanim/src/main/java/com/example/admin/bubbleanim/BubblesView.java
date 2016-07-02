package com.example.admin.bubbleanim;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.LinkedList;

public class BubblesView extends SurfaceView implements SurfaceHolder.Callback {

	private LinkedList<Bubble> mBubbles=new LinkedList<Bubble>();
	private SurfaceHolder mSurfaceHolder;
	private float mAddBubbleFrequency=0.3f;
	private GameLoop mGameLoop;
	private Paint bgPaint=new Paint();
	public BubblesView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
		bgPaint.setColor(Color.WHITE);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		mSurfaceHolder=holder;
		startAnimation();
	}

	private void startAnimation() {
		synchronized (this) {
			mGameLoop=new GameLoop();
			mGameLoop.start();
		}
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopAnimation();
	}

	private void stopAnimation() {
		synchronized (this) {
			if (mGameLoop!=null) {
				mGameLoop.running=false;
				boolean retry=true;
				while(retry){
					try {
						mGameLoop.join();
						retry=false;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			mGameLoop=null;
		}
	}

	private void drawBubbles(Canvas canvas){
		canvas.drawRect(0, 0, canvas.getWidth(),canvas.getHeight(), bgPaint);
		for(Bubble bubble:mBubbles){
			bubble.draw(canvas);
		}
	}
	
	private void operateBubbles(Canvas canvas){
		randomlyAddBuddles(canvas.getWidth(),canvas.getHeight());
		LinkedList<Bubble> bubblesToRemove=new LinkedList<Bubble>();
		for(Bubble bubble:mBubbles){
			bubble.move();
			if (bubble.isOutOfRange()) {
				bubblesToRemove.add(bubble);
			}
		}
		for(Bubble bubble:bubblesToRemove){
			mBubbles.remove(bubble);
		}
	}

	private void randomlyAddBuddles(int width, int height) {
		if (Math.random()>mAddBubbleFrequency||mBubbles.size()>10) {
			return;
		}
		mBubbles.add(new Bubble((float)(width*Math.random()), (float)(height*Math.random()*2), (float)(Bubble.MAX_SPEED*Math.random())));
	}
	
	private class GameLoop extends Thread{
		private long mPerFrame = 1000/25;
		public boolean running = true;
		long mFrameTime = 0;
		@Override
		public void run() {
			Canvas canvas=null;
			mFrameTime=System.currentTimeMillis();
			while(running){
				try{
					canvas=mSurfaceHolder.lockCanvas();
					if (canvas!=null) {
						synchronized (canvas) {
							operateBubbles(canvas);
							drawBubbles(canvas);
						}
					}
				}finally{
					if (canvas!=null) {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
				waitNextFrame();
			}
		}
		private void waitNextFrame() {
			long nextSleep=0;
			mFrameTime+=mPerFrame;
			nextSleep=mFrameTime-System.currentTimeMillis();
			if (nextSleep>0) {
				try {
					Thread.sleep(nextSleep);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
