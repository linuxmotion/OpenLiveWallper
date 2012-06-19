package org.linuxmotion.concurrent.Images;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import org.linuxmotion.livewallpaper.photoswitcher.BasicFileBrowser;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.widget.ImageView;


public class ImageLoader {
	private static final int MAX_TASK_COUNT = 128;
	private static final int MIN_TASK_COUNT = 5;
	private static final String TAG = ImageLoader.class.getSimpleName();
	private ArrayList<WeakReference<ImageView>> mLastLoaded = new ArrayList<WeakReference<ImageView>>();
	private int mPointer = 0;
	


    public void download(BasicFileBrowser act, String url, ImageView imageView) {



    	Log.i(TAG, "The number of current tasks are" + mPointer);	
    	
    	if(mPointer >= MAX_TASK_COUNT){
    		cancelTasks();
    	}
    	
    	  if (cancelPotentialDecoding(url, imageView)) {
    	         ImageLoaderTask task = new ImageLoaderTask(act,imageView);
    	         DecodedDrawable downloadedDrawable = new DecodedDrawable(task);
    	         imageView.setImageDrawable(downloadedDrawable);
    	         mLastLoaded.add(mPointer++, new WeakReference<ImageView>(imageView));
    	         try{
    	        	 task.execute(url);
    	         }catch(RejectedExecutionException e){
    	        	 //
    	        	 cancelTasks();
    	         }
    	     }

        }
    private void cancelTasks() {
    	
    	ArrayList<WeakReference<ImageView>>tmpLoaded = new ArrayList<WeakReference<ImageView>>();
    	tmpLoaded.add( mLastLoaded.get( mLastLoaded.size()-1));
    	tmpLoaded.add( mLastLoaded.get( mLastLoaded.size()-2));
    	tmpLoaded.add( mLastLoaded.get( mLastLoaded.size()-3));
    	
    	for(int i = 0; i < mLastLoaded.size(); i++){
    		ImageView imageView = mLastLoaded.get(i).get();
    		ImageLoaderTask bitmapDownloaderTask = getImageLoaderTask(imageView);
    		if (bitmapDownloaderTask != null) {
	    			// cancel the given task
	    			Status astatus = bitmapDownloaderTask.getStatus();
	    			// Dont even try and cancel it if it has completed
	    			switch(astatus){
		    			case FINISHED:
		    			{
		    				Log.i(TAG, "Tasks was finished, no need to cancel; but we will recycle the bitmap");
		    				Drawable d = imageView.getDrawable();
							if( d instanceof DecodedDrawable){
								//((DecodedDrawable) d).Recycle();
							}
			    				 
		    			}
		    			break;
		    			case PENDING:
		    			case RUNNING:
		    			{

		                	Log.i(TAG, "Canceling task for image:" + bitmapDownloaderTask.getUrl());
		    				bitmapDownloaderTask.cancel(true);
		    			}
	    			
	    			}
	    		
	    		mLastLoaded.remove(i);
	    		
	    	}
    	}
    
    	
    	// Move the last three to the first three
    	mLastLoaded.clear();
    	mLastLoaded.addAll(tmpLoaded);
    	
    	// Now we point to the fourth spot
    	mPointer = 3;
    	System.gc();
    	
		
	}

	private static boolean cancelPotentialDecoding(String url, ImageView imageView) {
        ImageLoaderTask bitmapDownloaderTask = getImageLoaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.getUrl();
            // Cancel the task if the view is being reused
            ImageView ref = bitmapDownloaderTask.getReference();
            if (bitmapUrl != url){
            	Log.i(TAG, "Canceling prevoius task for image:" + bitmapDownloaderTask.getUrl());
                bitmapDownloaderTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }
    
   protected static ImageLoaderTask getImageLoaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DecodedDrawable) {
                DecodedDrawable downloadedDrawable = (DecodedDrawable)drawable;
                return downloadedDrawable.getImageLoaderTask();
            }
        }
        return null;
    }
    
}