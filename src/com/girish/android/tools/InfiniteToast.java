package com.girish.android.tools;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

public class InfiniteToast {
	
	private Toast mToast;
	private boolean stop = false;
	private Thread mThread;
	private int timeLimit;
	private CountDownTimer mCountDownTimer;
	private static final int TIMEOUT = 1800;
	
	public InfiniteToast(Context mContext, String message){
		mToast = Toast.makeText(mContext, message,Toast.LENGTH_SHORT);
	}
	
	public void cancel() {
		mToast.cancel();
		stop = true;
		if(mThread != null)
			mThread.interrupt();
		if(mCountDownTimer != null)
			mCountDownTimer.cancel();
	}

	
	public int getDuration() {
		return timeLimit;
	}

	
	public int getGravity() {
		return mToast.getGravity();
	}

	
	public float getHorizontalMargin() {
		return mToast.getHorizontalMargin();
	}

	
	public float getVerticalMargin() {
		return mToast.getVerticalMargin();
	}

	
	public View getView() {
		return mToast.getView();
	}

	
	public int getXOffset() {
		return mToast.getXOffset();
	}

	
	public int getYOffset() {
		return mToast.getYOffset();
	}

	
	public void setDuration(int milliseconds) {
		
	}

	
	public void setGravity(int gravity, int xOffset, int yOffset) {
		mToast.setGravity(gravity, xOffset, yOffset);
	}

	
	public void setMargin(float horizontalMargin, float verticalMargin) {
		mToast.setMargin(horizontalMargin, verticalMargin);
	}

	
	public void setText(CharSequence s) {
		mToast.setText(s);
	}

	
	public void setText(int resId) {
		mToast.setText(resId);
	}

	
	public void setView(View view) {
		mToast.setView(view);
	}

	
	public void show() {
		showInfiniteToast();
	}
	
	
	public Toast getToastObject(){
		return mToast;
	}
	
	
	private void showInfiniteToast() {
		stop = false;
		if(mThread != null)
			mThread.interrupt();
		mThread = new Thread() {
			public void run() {
				try {
					while (true) {
						if (!stop) {
							mToast.show();
						} else {
							InfiniteToast.this.cancel();
							return;
						}
						sleep(TIMEOUT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		mThread.start();
	}
	
}
