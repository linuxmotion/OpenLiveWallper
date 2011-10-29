package org.linuxmotion.livewallpaper.photoswitcher;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class OpenLiveWallPaperService extends WallpaperService {


	private final Handler mHandler = new Handler();

	

	@Override
	public void onCreate() {
		super.onCreate();
	}


	@Override
	public void onDestroy() {      
		super.onDestroy();
	        
	}


	@Override
	public Engine onCreateEngine(){ 
		return new PhotSwitcherEngine(); 
	}

	    
	
	class PhotSwitcherEngine extends Engine {


        private final Paint mPaint = new Paint();

	    private float mTouchX = -1;
	    private float mTouchY = -1;
	    private long  mStartTime;
	    private long  mRunTime;
	    private float mCenterX;
	    private float mCenterY;
	    private float mOffset;
	    private boolean mVisible;

	    
	    @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            final Paint paint = mPaint;
            paint.setColor(0xffffffff);
            paint.setAntiAlias(true);
            paint.setStrokeWidth(2);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStyle(Paint.Style.STROKE);


            mStartTime = SystemClock.elapsedRealtime();

            // By default we don't get touch events, so enable them.
            // We dont want this just yet
            //setTouchEventsEnabled(true);
        }

        private final Runnable mTimeToSwitch = new Runnable() {
            public void run() {
                displayPicture();
            }
        };

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            // store the center of the surface, so we can draw the cube in the right spot
            mCenterX = width/2.0f;
            mCenterY = height/2.0f;
            displayPicture();
        }


		@Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xStep, float yStep, int xPixels, int yPixels) {
            mOffset = xOffset;
            displayPicture();
        }

	    
	       @Override
	        public void onVisibilityChanged(boolean visible) {
	            mVisible = visible;
	            if (visible) {
	                displayPicture();
	            } else {
	                mHandler.removeCallbacks(mTimeToSwitch);
	            }
	        }


	       @Override
	        public void onSurfaceCreated(SurfaceHolder holder) {
	            super.onSurfaceCreated(holder);
	        }

	        @Override
	        public void onSurfaceDestroyed(SurfaceHolder holder) {
	            super.onSurfaceDestroyed(holder);
	            mVisible = false;
	            mHandler.removeCallbacks(mTimeToSwitch);
	        }


	        protected void displayPicture() {


	            final SurfaceHolder holder = getSurfaceHolder();
				

	            Canvas c = null;
	            try {
	                c = holder.lockCanvas();
	                if (c != null) {
	                    // draw something
	                	drawPicture(c);
	                }
	            } finally {
	                if (c != null) holder.unlockCanvasAndPost(c);
	            }
	            
	            // Reschedule the next redraw
	            mHandler.removeCallbacks(mTimeToSwitch);
	            if (mVisible) {
	                mHandler.postDelayed(mTimeToSwitch, 1000 / 25);
	            }


	        }


			private void drawPicture(Canvas c) {
				// TODO Auto-generated method stub
				
				c.drawCircle(mCenterX, mTouchY, mRunTime, mPaint);
				// get the picture directroy
				
				// Determine picture to grab
				
				// place picture into a bitmap
				
				// draw the bitmap picture into a canvas
				
				//
				
				
			}
	
	}
	    
	    
	    
	    
}