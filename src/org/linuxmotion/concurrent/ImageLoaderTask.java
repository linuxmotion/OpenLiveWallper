package org.linuxmotion.concurrent;

import java.io.File;
import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.activities.BasicFileBrowser;
import org.linuxmotion.livewallpaper.utils.images.BitmapHelper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{

	private static final String TAG = ImageLoaderTask.class.getSimpleName();
	private String url;
	private final WeakReference<ImageView> imageViewReference;

	private BasicFileBrowser mAct;
	 
	public ImageLoaderTask(BasicFileBrowser act, ImageView imageView) {
		mAct = act;
	    imageViewReference = new WeakReference<ImageView>(imageView);

	}

	    
	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		  url = params[0];
	      String f = new File(params[0]).getName();
	      
	      String hash = String.valueOf((new File(f)).hashCode());
	      
		  if (isCancelled()) return null;
          
		  for(int i = 0; i < 2; i++){
	          try {
	        	  if (isCancelled()) return null;
	        	  Bitmap bitmap = mAct.getBitmapFromDiskCache(hash);
	        	  if(bitmap != null)Log.i(TAG, "Using disk cached bitmap for image = "+ f);
	        	  // No cached bitmap found
	        	  if(bitmap == null)bitmap = BitmapHelper.decodeSampledBitmapFromImage(url, 100, 100);
 				  if(bitmap != null){// Add bitmap to cache if bitmap was decoded
 					  
 					  
 					  mAct.addBitmapToDiskCache(f, bitmap);
 					  mAct.addBitmapToMemoryCache(f, bitmap);
 				  }
	        		  

	        	  return bitmap;// Return the bitmap,
	        	  //can still be null here if it could not decode, ie a video file

	          } catch (OutOfMemoryError e) {
	        	  if (isCancelled()) return null;
	        	  Log.e("ImageLoaderTask", "Failed to decode the bitmap due to Out of Memory Error");
	        	  System.gc(); // Try and start garbage collection
	 
	        	  if (isCancelled()) return null;
	          }
          } 
		  return null;
	}

	 @Override
	    // Once the image is downloaded, associates it to the imageView
	    protected void onPostExecute(Bitmap bitmap) {
	        if (isCancelled()) {
	        	bitmap.recycle();
	            bitmap = null;
	        }

	        if (imageViewReference != null && bitmap != null) {
	            ImageView imageView = imageViewReference.get();
	            ImageLoaderTask bitmapDownloaderTask = ImageLoader.getImageLoaderTask(imageView);
	            // Change bitmap only if this process is still associated with it
	            if (this == bitmapDownloaderTask) {
	                imageView.setImageBitmap(bitmap);
	            }

	        }
	    }

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	public ImageView getReference(){
		if(imageViewReference != null)
			return imageViewReference.get();
		else 
			return null;
	}

}