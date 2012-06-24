package org.linuxmotion.concurrent;

import java.util.concurrent.RejectedExecutionException;

import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.models.AsyncCheckBox;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.support.v4.util.LruCache;


public class CheckBoxLoader {

	private static final String TAG = CheckBoxLoader.class.getSimpleName(); 
	private LruCache<String, Boolean> mCheckBoxCache;
	private DataBaseHelper mDBHelper;
	
	private static int mMemClass;
	private boolean mUseCache = false;
	
	public CheckBoxLoader(DataBaseHelper helper){
		this(helper, false, 0);
	}
	public CheckBoxLoader(DataBaseHelper helper, boolean useCache, int memClass){
		mDBHelper = helper;
		mMemClass = memClass;
		mUseCache = useCache;
		if(mUseCache)initCheckBoxCache(mMemClass);
		
	}
	
	public void setChecked(AsyncCheckBox selectedBox, String absolutepath) {
		
		selectedBox.setChecked(false); // Always set the view to not checked then we will set it later
		if(mUseCache){
			
			Boolean bool = getBooleanFromMemCache(absolutepath);
			if(bool != null){
				selectedBox.setChecked(bool);
				return;
			}
		}
		
		if(!mDBHelper.isOpen())mDBHelper.open();
		
		if(cancelPotentialQuery(selectedBox, absolutepath)){
			CheckBoxLoaderTask task = new CheckBoxLoaderTask(this, selectedBox, mDBHelper);
			if(selectedBox != null){
				
				task.setStringKey(absolutepath);
				selectedBox.setTask(task); // Save a reference to the task
				
			}
			
			try{
				task.execute(absolutepath);
			}catch(RejectedExecutionException e){
				
			}
		}
		
	}
	private boolean cancelPotentialQuery(AsyncCheckBox selectedBox, String absolutepath) {
		
		if(selectedBox != null){
			
			CheckBoxLoaderTask task = selectedBox.getTask();
			if(task != null){
				//int hash = (new File(absolutepath)).hashCode();
				String path = task.getStringKey();
				
				if( path != null && path != absolutepath){
					LogWrapper.Logi(TAG, "Canceling task for image " + path);
					task.cancel(true);
					
				}
				else{
					
					return false; // We are the same task
				}
			
			}
			
			
		}
		return true; // Task was cancelled
		
	}
	

	
	private void initCheckBoxCache(int memClass){

        // Use 1/16th of the available memory for this memory cache.
        final int boxcacheSize = 1024 * 1024 * memClass / 16;

       mCheckBoxCache = new LruCache<String,Boolean>(boxcacheSize); // cache will be measured in number of elements
       
       String[] s = mDBHelper.getAllEntries();

       for(int i = 0; i < s.length; i++){
    	   
    	 
    	  addBooleanToMemCache(s[i],true);
    	  Boolean bool = getBooleanFromMemCache(s[i]);
    	  LogWrapper.Logv(TAG, "Got value "+ (bool == null ? "null" : bool) + " for checkbox key " + s[i]);
    		
       }

       LogWrapper.Logi(TAG, "Added " + mCheckBoxCache.size() + " Checkboxes");
       LogWrapper.Logi(TAG, "The checkbox max cache size is: " + mCheckBoxCache.maxSize() );
	
	}

	/**
	 * 
	 * @param key The key to store the Boolean against
	 * @param bool The Boolean to store
	 * 
	 */
	public synchronized void addBooleanToMemCache(String key, Boolean bool) {
	    if (mUseCache && (getBooleanFromMemCache(key) == null)) {
	    	LogWrapper.Logv("BasicBrowser", "Adding mem cache for checkbox "+ key);
	    	
	    	if(mCheckBoxCache == null)
	    		return; // I guess we are not using the cache
	    	mCheckBoxCache.put(key, bool);

	    	
	    }	
	  
	
	}
	

	
	public void revalidateCheckMemCache(final int memclass){
		if(mUseCache)mCheckBoxCache.evictAll();
		//new Thread(new Runnable(){

			//@Override
			//public void run() {
		initCheckBoxCache(memclass);
			//}
			
		//}).run();
		
		
		
		
	}

	public synchronized Boolean getBooleanFromMemCache(String key) {
		if(mUseCache){
			
			if(key == null)
				return null;
			
			LogWrapper.Logv("BasicBrowser", "Retriveing boolean for "+ key);
			Boolean bool = mCheckBoxCache.get(key);
			LogWrapper.Logv("BasicBrowser", "boolean retived is " + (bool == null ? "null": bool));
			return bool;
		}
		
		return null;
	   
	}

}
