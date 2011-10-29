package org.linuxmotion.livewallpaper.photoswitcher;

import org.linuxmotion.livewallpaper.utils.Constants;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

public class OpenLiveWallPaperService extends WallpaperService {


	private final Handler mHandler = new Handler();
	private SharedPreferences mSharedPrefs;
	

	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPrefs = getSharedPreferences(Constants.SHARED_PREFS, 0);
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


        private boolean mAnimating = false;

		private final Paint mPaint = new Paint();

	    private float mTouchX = -1;
	    private float mTouchY = -1;
	    private long  mStartTime;
	    private long  mRunTime = 10000;
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


            mStartTime = SystemClock.elapsedRealtime() + mRunTime;

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
	            mAnimating = false;
	            if (visible) {
	                displayPicture();
	            } else {
	                mHandler.removeCallbacks(mTimeToSwitch);
	            }
	        }


	       @Override
	        public void onSurfaceCreated(SurfaceHolder holder) {
	            super.onSurfaceCreated(holder);
	            mAnimating = false;
	        }

	        @Override
	        public void onSurfaceDestroyed(SurfaceHolder holder) {
	            super.onSurfaceDestroyed(holder);
	            mVisible = false;
	            mHandler.removeCallbacks(mTimeToSwitch);
	            mAnimating = false;
	        }

			
	        protected void displayPicture() {


	            final SurfaceHolder holder = getSurfaceHolder();
				

	            Canvas c = null;
	            try {
	                c = holder.lockCanvas();
	                if (c != null) {
	                    // draw something
	                	Log.d(this.getClass().getSimpleName(), "The animation is: " + Boolean.toString(mAnimating));
	                	if(!mAnimating)drawPicture(c);
	                	else drawAnimatedPicture(c);
	                }
	            } finally {
	                if (c != null) holder.unlockCanvasAndPost(c);
	            }
	            
	            // Reschedule the next redraw
	            mHandler.removeCallbacks(mTimeToSwitch);
	            if (mVisible) {
	               mHandler.postDelayed(mTimeToSwitch, mRunTime/25);
	            }


	        }


			private void drawAnimatedPicture(Canvas c) {
				Log.d(this.getClass().getSimpleName(), "drawing animated picure");
				
				String filePath = mSharedPrefs.getString(Constants.SINGLE_FILE_PATH, "");
				Log.d(this.getClass().getSimpleName(), "the filepath is: " + filePath);
				
				if(!filePath.equals("")){
					c.drawColor(Color.BLACK);
					Bitmap b = BitmapFactory.decodeFile(filePath);
					Matrix transform = new Matrix();
				    transform.setTranslate(mCenterX, mCenterY);
				    transform.preRotate(20.0f, b.getHeight()/2, b.getWidth()/2);
				    c.drawBitmap(b, transform, null);
				    
				}
				
				if(mStartTime <= SystemClock.elapsedRealtime()){
					Log.d(this.getClass().getSimpleName(), "Stoping animation");
					mAnimating = false;
		            mStartTime = SystemClock.elapsedRealtime() + mRunTime;
				}

			}


			private void drawPicture(Canvas c) {
				// TODO Auto-generated method stub
				mAnimating = false;
				Log.d(this.getClass().getSimpleName(), "drawing the picture");
				String filePath = mSharedPrefs.getString(Constants.SINGLE_FILE_PATH, "");
				Log.d(this.getClass().getSimpleName(), "the filepath is: " + filePath);
				
				if(filePath.equals("")){
					
					// grab the default bitmap
					mPaint.setColor(Color.RED);
					// draw the bitmap picture into a canvas
					c.drawCircle(mCenterX, mTouchY, mRunTime, mPaint);
					//
					mPaint.setColor(Color.BLACK);
					
				}else{
					// get the picture directroy
					Bitmap b = BitmapFactory.decodeFile(filePath);
					// Determine picture to grab
					// place picture into a bitmap
					Matrix m = new Matrix();
					// draw the bitmap picture into a canvas
					c.drawColor(Color.BLACK);
					c.drawBitmap(b, m, mPaint);

		           
					//
					
					
				}
				
				if(mStartTime <= SystemClock.elapsedRealtime()){
					Log.d(this.getClass().getSimpleName(), "Starting animation catch");
					mAnimating = true;
		            mStartTime = SystemClock.elapsedRealtime() + mRunTime;
				}
				
				
			}
	
	}
	    
	    
	    
	    
}