package org.linuxmotion.livewallpaper.activities;

import java.io.File;

import org.linuxmotion.concurrent.AsyncCheckBoxHelper;
import org.linuxmotion.concurrent.ImageLoader;
import org.linuxmotion.io.DiskLruImageCache;
import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.models.AsyncCheckBox;
import org.linuxmotion.livewallpaper.models.CheckBoxClickListener;
import org.linuxmotion.livewallpaper.models.ImageClickListener;

import android.app.ActivityManager;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BasicFileBrowser extends ListActivity {
	
	private static final String TAG = BasicFileBrowser.class.getSimpleName();
	private ListView mSelectionList;
	private LruCache<String,Bitmap> mMemoryCache;
	private DiskLruImageCache mDiskCache;
	private static final int DISK_CACHE_SIZE = 1024 * 1024 * 10; // 10MB
	private static final String DISK_CACHE_SUBDIR = "thumbnails";
	private DataBaseHelper mDBHelper = new DataBaseHelper();
	private AsyncCheckBoxHelper mCheckBoxHelper = new AsyncCheckBoxHelper();

	
	ImageLoader mImageLoader; 
	@Override
	  public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
       
        mDBHelper.openDatabase(this); // Prepare the helper
        
        mImageLoader = new ImageLoader(((BitmapDrawable) this.getResources().getDrawable(R.drawable.image_loading_bg)).getBitmap());
        
     
        File[] List = getPhotoList();
        
        ArrayAdapter adapter = new SimpleFileAdapter(this, List);

        setListAdapter(adapter);

        
        initMemCache();
        initDiskCache();
        

	}
	

 
	/**
	 * Retrieves the list of photos to select from 
	 * 
	 * @return The files to select from
	 */
	private File[] getPhotoList() {
		// TODO Auto-generated method stub
		File photoPath = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera"); // Dont hard code this
        return photoPath.listFiles();
        
	}



	private void initDiskCache() {
		  mDiskCache = new DiskLruImageCache(this, DISK_CACHE_SUBDIR, DISK_CACHE_SIZE, CompressFormat.JPEG, 50);

		
	}



	private void initMemCache() {

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
	    	Log.i("BasicBrowser", "Setting mem cache file for image "+ key);
	        mMemoryCache.put(key, bitmap);
	    }
	}
	public void addBitmapToDiskCache(String key, Bitmap bitmap) {
	    if (getBitmapFromDiskCache(key) == null) {
	    	Log.i("BasicBrowser", "Setting disk cache file for image "+ key);
	    	mDiskCache.put(key, bitmap);
	    }
	}
	public Bitmap getBitmapFromMemCache(String key) {
		//Log.i("BasicBrowser", "Retriveing bitmap for "+ key);
		if(key == null)return null;
	    return mMemoryCache.get(key);
	}
	public Bitmap getBitmapFromDiskCache(String key) {
		//Log.i("BasicBrowser", "Retriveing bitmap for "+ key);
		if(key == null)return null;
	    return mDiskCache.getBitmap(key);
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
			AsyncCheckBox  selectedBox;
			if (rowView == null) {
				
			
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);

			}
			// Find the views for modification
			{
				textView = (TextView) rowView.findViewById(R.id.label);
				 imageView = (ImageView) rowView.findViewById(R.id.icon);
				 selectedBox = (AsyncCheckBox) rowView.findViewById(R.id.box);

			}
			
			// Modify the objects
			{
			String Absolutepath = mPhotos[position].getAbsolutePath();
			String fullname = mPhotos[position].getName();
			String name = fullname.substring(0, fullname.indexOf('.'));
			
				// Set the click listener
				{
					imageView.setOnClickListener(new ImageClickListener(Absolutepath));
					selectedBox.setOnClickListener(new CheckBoxClickListener(mDBHelper,Absolutepath));
					
				}
				// Is the file present in the database
				// Is so inform the user with the checkbox
				// Threaded to not block the UI
				{
					selectedBox.setChecked(false);// So the user wont see checks when scrolling
					mCheckBoxHelper.setChecked(mDBHelper, selectedBox, Absolutepath); // Will set the check for real
	
				}
			
			textView.setText(name); // remove the file type from the name
	        final Bitmap bitmap = getBitmapFromMemCache(fullname);
		        if (bitmap != null) {
		        	Log.i("BasicBrowser", "Setting cached bitmap");
		            imageView.setImageBitmap(bitmap);
		        }
		        else{
				//imageView.setImageResource(R.drawable.image_loading_bg);
				mImageLoader.download(mAct,mPhotos[position].getAbsolutePath(), imageView);
				}
	        }
			// Return the new view
			return rowView;
		}
		
		
	}
	
	

}
