package org.linuxmotion.livewallpaper.photoswitcher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;

public class OpenLiveWallPaperActivity extends WallpaperService {


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

	    private float mTouchX = -1;
	    private float mTouchY = -1;
	    private long  mStartTime;
	    private long  mRunTime;
	    private float mCenterX;
	    private float mCenterY;

		
	
	}
	    
	    
	    
	    
}