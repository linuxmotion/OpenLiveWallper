package org.linuxmotion.livewallpaper.photoswitcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.linuxmotion.livewallpaper.utils.Constants;
import org.linuxmotion.livewallpaper.utils.LicenseChecker;


import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.SurfaceHolder;

public class OpenLiveWallPaperService extends WallpaperService {


	private final Handler mHandler = new Handler();
	private SharedPreferences mSharedPrefs;
	private Resources mResources;
	private Bitmap mBackground;
	private static boolean mNewBackground = true;
	private static int mDefaultSelection;

	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPrefs = getSharedPreferences(Constants.SHARED_PREFS, 0);
		mResources = getResources();
		mDefaultSelection = mSharedPrefs.getInt(Constants.DEFAULT_IMAGE_SELECTION, 0);
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
	    private long  mRunTime = 1000;
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
	                	drawAnimatedPicture(c);
	                }
	            } finally {
	                if (c != null) holder.unlockCanvasAndPost(c);
	            }
	            
	            // Reschedule the next redraw
	            mHandler.removeCallbacks(mTimeToSwitch);
	            if (mVisible) {
	               mHandler.postDelayed(mTimeToSwitch, mRunTime);
	            }


	        }


			private void drawAnimatedPicture(Canvas c) {
				Log.d(this.getClass().getSimpleName(), "drawing animated picure");
				
				String filePath = mSharedPrefs.getString(Constants.SINGLE_FILE_PATH, "");
				Log.d(this.getClass().getSimpleName(), "the filepath is: " + filePath);
				
			
					c.drawColor(Color.BLACK);
					
					
				
					BitmapFactory.Options o = new BitmapFactory.Options();
				    o.inJustDecodeBounds = false;
				    o.inSampleSize = 2;
				    
				    mBackground = retreiveBitmap(o);
						
				    if(mBackground == null)Log.d(this.getClass().getSimpleName(), "Decoded cannot be decoded bitmap");
					Matrix transform = new Matrix();
					
					
					transform.setTranslate(0,0);
					//transform.setTranslate(mCenterX, mCenterY);
					
					int width = mBackground.getWidth();
					int height = mBackground.getHeight();
					
					c.drawBitmap(mBackground, transform, null);
					
					/*
				    transform.preRotate(20.0f, height/2, width/2);
				    c.drawBitmap(mBackground, transform, null);
				    
				    transform.setTranslate((width/2), (width/2));
				    transform.preRotate(20.0f, height/2, width/2);
				    
				    c.drawBitmap(mBackground, transform, null);
					 */
				
				if(mStartTime <= SystemClock.elapsedRealtime()){
					mNewBackground = true;
					Log.d(this.getClass().getSimpleName(), "Stoping animation");
					mAnimating = false;
		            mStartTime = SystemClock.elapsedRealtime() + mRunTime;
				}

			}


			private Bitmap retreiveBitmap(BitmapFactory.Options options){
				
				if(mBackground == null || mNewBackground){
					String filePath = mSharedPrefs.getString(Constants.SINGLE_FILE_PATH, "");
					Log.d(this.getClass().getSimpleName(), "Setting a new picture");
					Bitmap b;
					mNewBackground = false;
					
					// If the default selection is at the max length return it to the first picture
					if(mDefaultSelection == (Constants.DefaultPictures.length)){ mDefaultSelection = 0;}
					
					if(LicenseChecker.checkLicense()){
						// A license was found
							
							
							try {
								b = BitmapFactory.decodeStream(new FileInputStream(filePath),null,options);
							} catch (FileNotFoundException e) {
								
								// If the file cannot be found resort to a default bitmap and increment the number
								return BitmapFactory.decodeResource(mResources, Constants.DefaultPictures[mDefaultSelection++], options);
							}
							if(b == null){
								// If the bitmap cannot be decoded resort to a default bitmap and increment the number
								return BitmapFactory.decodeResource(mResources, Constants.DefaultPictures[mDefaultSelection++], options);
							}
				        	return b; // Return the decoded bitmap
				        	
				        }
				        else{// If the license cannot be found resort to a default bitmap and increment the number
				        	
				        	b = BitmapFactory.decodeResource(mResources, Constants.DefaultPictures[mDefaultSelection++], options);
				        	return b;
				        }
					}
				else{ // There is already a bitmap ready. Dont waste resources creating it again
					
					
					return mBackground;
				}
				
			}
	
	}
	    
	    
	    
	    
}