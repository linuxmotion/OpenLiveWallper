package org.linuxmotion.livewallpaper.utils.lists;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

class DecodedDrawable extends ColorDrawable {
    private final WeakReference<ImageLoaderTask> ImageloaderTaskReference;

    public DecodedDrawable(ImageLoaderTask bitmaploaderTask) {
        super(Color.BLACK);
        ImageloaderTaskReference =
            new WeakReference<ImageLoaderTask>(bitmaploaderTask);

    }
    
    public void Recycle(){
    	try {
			ImageloaderTaskReference.get().get().recycle();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }

    public ImageLoaderTask getImageLoaderTask() {
        return ImageloaderTaskReference.get();
    }
}
