package org.linuxmotion.livewallpaper.photoswitcher;

import java.io.File;

import org.linuxmotion.livewallpaper.utils.lists.ImageLoader;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import android.support.v4.util.LruCache;

public class BasicFileBrowser extends ListActivity {
	
	ListView mSelectionList;
	private LruCache<String,Bitmap> mMemoryCache;
	ImageLoader mImageLoader = new ImageLoader();

	@Override
	  public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        File photoPath = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera"); // Dont hard code this
        // First paramenter - Context
        // Forth - the Array of data
        File[] f = photoPath.listFiles();
        ArrayAdapter adapter = new SimpleFileAdapter(this, f);

        setListAdapter(adapter);
        
     // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int memClass = ((ActivityManager) this.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = 1024 * 1024 * memClass / 8;

        mMemoryCache = new LruCache<String,Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number of items.
                return bitmap.getRowBytes() * bitmap.getHeight();// int result permits bitmaps up to 46,340 x 46,340
            }
        };


	}
	

 
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
	    if (getBitmapFromMemCache(key) == null) {
	    	Log.i("BasicBrowser", "Setting cache file for image "+ key);
	        mMemoryCache.put(key, bitmap);
	    }
	}

	public Bitmap getBitmapFromMemCache(String key) {
		Log.i("BasicBrowser", "Retriveing bitmap for "+ key);
		if(key == null)return null;
	    return mMemoryCache.get(key);
	}

	
	class SimpleFileAdapter extends ArrayAdapter<File>{
		private final Context mContext;
		private final File[] mPhotos;
		BasicFileBrowser mAct;
		
		public SimpleFileAdapter(BasicFileBrowser act, File[] photos) {
			
			super(act.getApplicationContext(), R.layout.rowlayout, android.R.layout.simple_list_item_1, photos);
			
			mContext = act.getApplicationContext();
			mPhotos  = photos;
			mAct = act;
			
		}
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View rowView = convertView;
			// Instatiate the objects
			TextView textView;
			ImageView imageView;
			CheckBox  selectedBox;
			if (rowView == null) {
				
			
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);

			}
			// Find the views for modification
			{
				textView = (TextView) rowView.findViewById(R.id.label);
				 imageView = (ImageView) rowView.findViewById(R.id.icon);
				 selectedBox = (CheckBox) rowView.findViewById(R.id.box);

			}
			// Modify the objects
			{
			String fullname = mPhotos[position].getName();
			String name = fullname.substring(0, fullname.indexOf('.'));
			
			textView.setText(name); // remove the file type from the name
	        final Bitmap bitmap = getBitmapFromMemCache(fullname);
		        if (bitmap != null) {
		        	Log.i("BasicBrowser", "Setting cached bitmap");
		            imageView.setImageBitmap(bitmap);
		        }
		        else{
				imageView.setImageResource(R.drawable.image_loading_bg);
				mImageLoader.download(mAct,mPhotos[position].getAbsolutePath(), imageView);
				}
	        }
			// Return the new view
			return rowView;
		}
		
		
	}
	
	

}
