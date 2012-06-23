package org.linuxmotion.livewallpaper.photoswitcher.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.linuxmotion.livewallpaper.utils.Constants;
import org.linuxmotion.livewallpaper.utils.LicenseChecker;

import android.content.SharedPreferences;
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

	private static final String TAG = OpenLiveWallPaperService.class.getSimpleName();

	private final Handler mHandler = new Handler();
	private SharedPreferences mSharedPrefs;
	private Resources mResources;
	private Bitmap mBackground;
	public float mScalingFactor;

	private int mSwitchType = Constants.PLAIN_SWITCH;
	public int mDrawType = Constants.PLAIN_DRAW;
	
	private static boolean mNewBackground = true;
	private static int mDefaultSelection;
	private static final boolean DBG = false; 
	
	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPrefs = getSharedPreferences(Constants.SHARED_PREFS, 0);
		mResources = getResources();
		mDefaultSelection = mSharedPrefs.getInt(Constants.DEFAULT_IMAGE_SELECTION, 0);
		
		// Set the draw and switch state
		mSwitchType = mSharedPrefs.getInt(Constants.SWITCH_TYPE, Constants.PLAIN_SWITCH);	
		mDrawType = mSharedPrefs.getInt(Constants.DRAW_TYPE, Constants.PLAIN_DRAW);
	
		
		
		
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
		private float mOffset;
		private float mCenterX;
		private float mCenterY;
	    private long  mStartTime;
	    private long  mRunTime = 10000;
	    private boolean mVisible;
	    private static final float mMaxScale = 5;
	    private static final float mMinScale = 0.25f;
	    private static final float mScalingFactor = .10f;
	    private float mScale = 1;
	    private boolean mGrowAnimation = true;
	    private boolean mLicensePresent = LicenseChecker.checkLicense();

	    
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
	    	// Set the draw and switch state
	   		mSwitchType = mSharedPrefs.getInt(Constants.SWITCH_TYPE, Constants.PLAIN_SWITCH);	
	   		mDrawType = mSharedPrefs.getInt(Constants.DRAW_TYPE, Constants.PLAIN_DRAW);
	   	
	   		
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
	        	Log.d(TAG, "Drawing the animation");


	            final SurfaceHolder holder = getSurfaceHolder();
	            
	            switch(mSwitchType){
	            
	            case Constants.PLAIN_SWITCH:{

		        	Log.d(TAG, "Drawing the plain switch");
		        	// Do nothing plain draw will write everything over
	            	//switchPlain(holder);
	            }break;
	            case Constants.ZOOM_SWITCH:{
	            	switchZoom(holder);
	            	
	            }break;
	            case Constants.BOUNCE_SWITCH:{

	            }break;
            }
	            
				
	            switch(mDrawType){
	            
		            case Constants.PLAIN_DRAW:{

			        	Log.d(TAG, "Drawing with no affects");
		            	drawPlain(holder);
		            }break;
		            case Constants.ZOOM_DRAW:{
		            	drawZoom(holder);
		            	
		            }break;
		            case Constants.BOUNCE_DRAW:{

		            }
	            }

	        	Log.d(TAG, "Animation is finished");
	        	if(mDrawType == Constants.PLAIN_DRAW){
		        	if(mStartTime <= SystemClock.elapsedRealtime()){
						
			            mStartTime = SystemClock.elapsedRealtime() + mRunTime;
					}
		            // Reschedule the next redraw
		            mHandler.removeCallbacks(mTimeToSwitch);
		            if (mVisible) {
		               mHandler.postDelayed(mTimeToSwitch, mRunTime);
		            }
	        	}else{
	        		
	        		 mHandler.removeCallbacks(mTimeToSwitch);
			            if (mVisible) {
			               mHandler.postDelayed(mTimeToSwitch, mRunTime);
			            }
	        		
	        	}
	            
	        	mNewBackground = true;


	        }


			private void switchZoom(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}


			private void switchPlain(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}


			private void drawPlain(final SurfaceHolder holder) {
				// TODO Auto-generated method stub
				   Canvas c = null;
				
		            	try {
			                c = holder.lockCanvas();
			                if (c != null) {
			                    // draw something
			                	log("Canvas aquired");
			                	drawPlainPicture(c);
			                }
			            } finally {
			                if (c != null){
			                	log("Canvas posted");
			                	holder.unlockCanvasAndPost(c);
			                	c = null;
			                }
			            }
			            
			}


			private void drawPlainPicture(Canvas c) {
				
				c.drawColor(Color.DKGRAY);
				BitmapFactory.Options o = new BitmapFactory.Options();
			    o.inJustDecodeBounds = false;
			    o.inSampleSize = 10;
			    mBackground = retreiveBitmap(o);
				if(mBackground == null)Log.d(this.getClass().getSimpleName(), "Cannot decoded bitmap");
				else{
					
					Paint paint = mPaint;
					paint.setColor(Color.DKGRAY);

					Matrix transform = new Matrix();

					// Scale the bitmap to the size of the screen
					c.drawBitmap(mBackground, transform,null);
				}
				

				
			}


			private void drawZoom(final SurfaceHolder holder) {

	            Canvas c = null;
	            for(int frame = 0; frame < 300; frame++){

	            	try {
		                c = holder.lockCanvas();
		                if (c != null) {
		                    // draw something
		                	log("Canvas aquired");
		                	drawAnimatedPicture(c);
		                }
		            } finally {
		                if (c != null){
		                	log("Canvas posted");
		                	holder.unlockCanvasAndPost(c);
		                	c = null;
		                }
		            }
		            
		         }

			}


			private void drawAnimatedPicture(Canvas c) {
				//Log.d(this.getClass().getSimpleName(), "drawing animated picure");
				
				String filePath = mSharedPrefs.getString(Constants.SINGLE_FILE_PATH, "");
				//Log.d(this.getClass().getSimpleName(), "the filepath is: " + filePath);
				
			
				
				
				
					c.drawColor(Color.DKGRAY);
									
				
					BitmapFactory.Options o = new BitmapFactory.Options();
				    o.inJustDecodeBounds = false;
				    o.inSampleSize = 10;
				    
				    
				    mBackground = retreiveBitmap(o);
						
				    if(mBackground == null)Log.d(this.getClass().getSimpleName(), "Cannot decoded bitmap");
				
					
					if((mScale <= mMaxScale) && mGrowAnimation){
						
						mScale += mScalingFactor;
					    	
					}
					else{
						
					    mGrowAnimation = false;
					    mScale -= mScalingFactor;
					    if(mScale == mMinScale)mGrowAnimation = true;
					    	
					}
					
					Matrix transform = new Matrix();
					transform.setTranslate(0,0);
					transform.postScale(mScale, mScale);
					
					c.drawBitmap(mBackground, transform, null);

					
		

			}


			private Bitmap retreiveBitmap(BitmapFactory.Options options){
				
				if(mBackground == null || mNewBackground){
					String filePath = mSharedPrefs.getString(Constants.SINGLE_FILE_PATH, "");
					Log.d(this.getClass().getSimpleName(), "Getting a new picture");
					Bitmap b;
					mNewBackground = false;
					
					// If the default selection is at the max length return it to the first picture
					if(mDefaultSelection == (Constants.DefaultPictures.length)){ mDefaultSelection = 0;}
					
					if(mLicensePresent){
						// A license was found
							
							
							try {
								b = BitmapFactory.decodeStream(new FileInputStream(filePath),null,options);
								//Bitmap.createBitmap(b, 0, 0, 400, 800); // Create a croped bitmap for the screen size
								//Bitmap.createScaledBitmap(b, 400, 800, false); // Scale the bit map
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
				else{ // There is already a bitmap ready. Don't waste resources creating it again
					
					
					return mBackground;
				}
				
			}
	
			private void log(String msg){
				
				if(DBG)Log.d(TAG, msg);
				
			}
	}
	
	
	
	    
	    
	    
	    
}