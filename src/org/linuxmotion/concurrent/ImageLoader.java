package org.linuxmotion.concurrent;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

import org.linuxmotion.concurrent.images.AsyncDrawable;
import org.linuxmotion.livewallpaper.activities.BasicFileBrowser;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;


public class ImageLoader {
	private static final int MAX_TASK_COUNT = 128;
	private static final int MIN_TASK_COUNT = 5;
	private static final String TAG = ImageLoader.class.getSimpleName();
	private ArrayList<WeakReference<ImageView>> mLastLoaded = new ArrayList<WeakReference<ImageView>>();
	private int mPointer = 0;
	private Bitmap mLoadingMap;
	
	public ImageLoader(Bitmap loading){
		
		mLoadingMap = loading;
	}

    public void download(BasicFileBrowser act, String url, ImageView imageView) {


    	
    	  if (cancelPotentialDecoding(url, imageView)) {
    	         ImageLoaderTask task = new ImageLoaderTask(act,imageView);
    	         AsyncDrawable downloadedDrawable = new   AsyncDrawable(act.getResources(),mLoadingMap, task);
    	         imageView.setImageDrawable(downloadedDrawable);
    	         try{
    	        	 task.execute(url);
    	         }catch(RejectedExecutionException e){
    	        	 //e.printStackTrace();
    	        	 // Is the cancel function having a fall through
    	         }
    	     }

        }

	private static boolean cancelPotentialDecoding(String url, ImageView imageView) {
        ImageLoaderTask bitmapDownloaderTask = getImageLoaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.getUrl();
            // Cancel the task if the view is being reused
            if (bitmapUrl != null && bitmapUrl != url ){
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
            if (drawable instanceof   AsyncDrawable) {
            	  AsyncDrawable downloadedDrawable = (  AsyncDrawable)drawable;
                return downloadedDrawable.getImageLoaderTask();
            }
        }
        return null;
    }
    
}