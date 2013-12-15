package com.girish.android.tools;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 *  Note: For some GIFs of high quality the images not rendered properly on the emulator
 */
public class GIFView extends ImageView {
	
	private Movie mMovie;
	private InputStream mInputStream;
	private long startTime;
	private int resourceID;
	private boolean runOnce;
	private String LOG_TAG = "GIFView";
	
	public GIFView(Context context, AttributeSet attrs) {
		super(context, attrs);
		resourceID = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", -1);
		mInputStream = context.getResources().openRawResource(resourceID);
		mMovie = Movie.decodeStream(mInputStream);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		long currentTime = android.os.SystemClock.uptimeMillis();
		 if (startTime == 0) {  
             startTime = currentTime;
         }
		 
		 //This is necessary else the XML view crashes in layout
		 if(mMovie != null){
			 if(!runOnce){
				Drawable mDrawable =  this.getDrawable();
				Bitmap.Config mConfig = Bitmap.Config.ARGB_8888; 
				Bitmap mBitmap = Bitmap.createBitmap(mDrawable.getIntrinsicWidth(), mDrawable.getIntrinsicHeight(), mConfig); 
				setImageBitmap(mBitmap);
				runOnce = true;
			 }
			 mMovie.setTime((int)((currentTime - startTime) % mMovie.duration()));
			 mMovie.draw(canvas,0,0);
			 
	         invalidate();
		 } else {
			 Log.e(LOG_TAG, "mMovie is null");
		 }
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(100, 100);
	}
}
