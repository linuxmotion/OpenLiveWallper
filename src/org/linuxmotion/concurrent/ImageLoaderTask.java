package org.linuxmotion.concurrent;

import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.utils.LogWrapper;
import org.linuxmotion.livewallpaper.utils.images.BitmapHelper;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{

	private static final String TAG = ImageLoaderTask.class.getSimpleName();
	private String mKey;
	private final WeakReference<ImageView> imageViewReference;

	private ImageLoader mLoader;
	 
	public ImageLoaderTask(ImageLoader loader, ImageView imageView) {
		mLoader = loader;
	    imageViewReference = new WeakReference<ImageView>(imageView);

	}

	    
	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		  mKey = params[0];
	      //String f = new File(params[0]).getName();
	      
	      //String hash = String.valueOf((new File(f)).hashCode());
	      
		  if (isCancelled()) return null;
          
		  for(int i = 0; i < 2; i++){
	          try {
	        	  if (isCancelled()) return null;
	        	  Bitmap bitmap = mLoader.getBitmapFromDiskCache(mKey);
	        	  if(bitmap != null) LogWrapper.Logi(TAG, "Using disk cached bitmap for image = "+ mKey);
	        	  // No cached bitmap found
	        	  if(bitmap == null)bitmap = BitmapHelper.decodeSampledBitmapFromImage(mKey, 100, 100);
 				  if(bitmap != null){// Add bitmap to cache if bitmap was decoded
 					  
 					 
 					 mLoader.addBitmapToDiskCache(mKey, bitmap);
 					 mLoader.addBitmapToMemoryCache(mKey, bitmap);
 				  }
	        		  

	        	  return bitmap;// Return the bitmap,
	        	  //can still be null here if it could not decode, ie a video file

	          } catch (OutOfMemoryError e) {
	        	  if (isCancelled()) return null;
	        	  LogWrapper.Logv("ImageLoaderTask", "Failed to decode the bitmap due to Out of Memory Error");
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
	public String getKey() {
		return mKey;
	}

	public ImageView getReference(){
		if(imageViewReference != null)
			return imageViewReference.get();
		else 
			return null;
	}

}
