package org.linuxmotion.livewallpaper.utils.lists;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap>{

	private String url;
	private final WeakReference<ImageView> imageViewReference;
	BitmapFactory.Options mOptions;
	 
	public ImageLoaderTask(ImageView imageView) {
	    imageViewReference = new WeakReference<ImageView>(imageView);
	    mOptions = new BitmapFactory.Options();
        mOptions.inSampleSize = 8;
        mOptions.inDither = false;
        mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
	}

	    
	@Override
	// Actual download method, run in the task thread
	protected Bitmap doInBackground(String... params) {
		// params comes from the execute() call: params[0] is the url.
		  if (isCancelled()) return null;
          
          try {
              return BitmapFactory.decodeFile(params[0], mOptions);
          } catch (OutOfMemoryError e) {
              return null;
          } 
	}

	 @Override
	    // Once the image is downloaded, associates it to the imageView
	    protected void onPostExecute(Bitmap bitmap) {
	        if (isCancelled()) {
	            bitmap = null;
	        }

	        if (imageViewReference != null) {
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

	

}
