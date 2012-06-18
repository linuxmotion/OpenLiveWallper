package org.linuxmotion.livewallpaper.utils.lists;

import java.lang.ref.WeakReference;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

class DecodedDrawable extends ColorDrawable {
    private final WeakReference<ImageLoaderTask> ImageloaderTaskReference;

    public DecodedDrawable(ImageLoaderTask bitmaploaderTask) {
        super(Color.BLACK);
        ImageloaderTaskReference =
            new WeakReference<ImageLoaderTask>(bitmaploaderTask);
    }

    public ImageLoaderTask getImageLoaderTask() {
        return ImageloaderTaskReference.get();
    }
}
