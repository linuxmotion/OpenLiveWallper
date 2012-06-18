package org.linuxmotion.livewallpaper.utils.lists;


import android.graphics.drawable.Drawable;
import android.widget.ImageView;


public class ImageLoader {

    public void download(String url, ImageView imageView) {
    	  if (cancelPotentialDecoding(url, imageView)) {
    	         ImageLoaderTask task = new ImageLoaderTask(imageView);
    	         DecodedDrawable downloadedDrawable = new DecodedDrawable(task);
    	         imageView.setImageDrawable(downloadedDrawable);
    	         task.execute(url);
    	     }

        }
    private static boolean cancelPotentialDecoding(String url, ImageView imageView) {
        ImageLoaderTask bitmapDownloaderTask = getImageLoaderTask(imageView);

        if (bitmapDownloaderTask != null) {
            String bitmapUrl = bitmapDownloaderTask.getUrl();
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
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