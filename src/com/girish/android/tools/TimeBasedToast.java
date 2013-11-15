package com.girish.android.tools;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

public class TimeBasedToast {
	
	private Toast mToast;
	private int timeLimit;
	private CountDownTimer mCountDownTimer;
	private boolean timerStarted = false;
	private static final int TIMEOUT = 1800;
	
	public TimeBasedToast(Context mContext, String message, int timeInSeconds){
		this.timeLimit = timeInSeconds *1000;
		mToast = Toast.makeText(mContext, message,Toast.LENGTH_SHORT);
		mCountDownTimer = new CountDownTimer(timeLimit,TIMEOUT) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				mToast.show();
			}
			
			@Override
			public void onFinish() {
				mToast.cancel();
				timerStarted = false;
			}
		};
	}
	
	public void cancel() {
		mToast.cancel();
		if(mCountDownTimer != null)
			mCountDownTimer.cancel();
		timerStarted = false;
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
		if(!timerStarted)
		mCountDownTimer.start();
	}
	
	
	public Toast getToastObject(){
		return mToast;
	}
	
	
}
