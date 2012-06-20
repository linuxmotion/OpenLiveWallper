package org.linuxmotion.concurrent;


import java.io.File;
import java.util.concurrent.RejectedExecutionException;

import org.linuxmotion.livewallpaper.activities.BasicFileBrowser;
import org.linuxmotion.livewallpaper.models.AsyncDrawable;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;


public class ImageLoader {
	private static final String TAG = ImageLoader.class.getSimpleName();

	private Bitmap mLoadingMap;
	
	public ImageLoader(Bitmap loading){
		
		mLoadingMap = loading;
	}

    public void download(BasicFileBrowser act, String abspath, ImageView imageView) {


    	
    	  if (cancelPotentialDecoding(abspath, imageView)) {
    	         ImageLoaderTask task = new ImageLoaderTask(act,imageView);
    	         AsyncDrawable downloadedDrawable = new   AsyncDrawable(act.getResources(),mLoadingMap, task);
    	         imageView.setImageDrawable(downloadedDrawable);
    	         try{
    	        	 task.execute(abspath);
    	         }catch(RejectedExecutionException e){
    	        	 //e.printStackTrace();
    	        	 // Is the cancel function having a fall through
    	         }
    	     }

        }

	private static boolean cancelPotentialDecoding(String abspath, ImageView imageView) {
        ImageLoaderTask bitmapDownloaderTask = getImageLoaderTask(imageView);

       
        if (bitmapDownloaderTask != null) {
        	// String hash = String.valueOf((new File(bitmapDownloaderTask.getUrl())).hashCode());
            String bitmapPath = bitmapDownloaderTask.getUrl();
            // Cancel the task if the view is being reused
            if (bitmapPath != null && bitmapPath != abspath ){
            	Log.i(TAG, "Canceling prevoius task for image:" + (new File(bitmapPath)).getName() );
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