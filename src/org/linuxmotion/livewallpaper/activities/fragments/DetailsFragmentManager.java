package org.linuxmotion.livewallpaper.activities.fragments;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.models.preferences.PreferenceListFragment;
import org.linuxmotion.livewallpaper.utils.Constants;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class DetailsFragmentManager {

	
	public DetailsFragmentManager(){};
	
	
	
	
	
	
	 public static Fragment newInstance(int index) { 
		 Fragment mShownFragment = null;
		 switch(index){
			 		
			 		case Constants.WALLPAPER_VEIWER:{
			 			WallpaperViewerFragment f = new WallpaperViewerFragment();
			
				        // Supply index input as an argument.
				        Bundle args = new Bundle();
				        args.putInt("index", index);
				        f.setArguments(args);
				        mShownFragment = f;
			 	            
			 		}break;
			 		
			 		case Constants.CUSTOM_PAPERS:{
			 			
			 			BasicFileBrowserFragment f = new BasicFileBrowserFragment();
			
				        // Supply index input as an argument.
				        Bundle args = new Bundle();
				        args.putInt("index", index);
				        args.putInt("xml", R.layout.layout_list_content);
				        f.setArguments(args);
				        mShownFragment = f;
			 			
			 		}break;
			 		
			 		case Constants.SETTINGS:{
			 			PreferenceListFragment f =  new PreferenceListFragment(R.xml.settings);
			
				        // Supply index input as an argument.
				        Bundle args = new Bundle();
				        args.putInt("index", index);
				        args.putInt("xml", R.xml.settings);
				        f.setArguments(args);
				        mShownFragment = f;
				        
				        
			 		}break;
					
			     }
	    	

	        return mShownFragment;
	    }
	
}
