package org.linuxmotion.livewallpaper.activities;

import java.io.File;

import org.linuxmotion.concurrent.CheckBoxLoader;
import org.linuxmotion.concurrent.ImageLoader;
import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.models.AsyncCheckBox;
import org.linuxmotion.livewallpaper.models.listeners.CheckBoxClickListener;
import org.linuxmotion.livewallpaper.models.listeners.ImageClickListener;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BasicFileBrowser extends PreferenceActivity {//implements OnScrollListener {
	
	private static final String TAG = BasicFileBrowser.class.getSimpleName();

	//private int mFlingState = 0;

	private DataBaseHelper mDBHelper = new DataBaseHelper();
	private CheckBoxLoader mCheckBoxHelper;
	private int mMemClass;
	
	ImageLoader mImageLoader; 
	@Override
	  public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        mMemClass = ((ActivityManager) this.getApplicationContext().getSystemService(
                Context.ACTIVITY_SERVICE)).getMemoryClass();
       
        mDBHelper.initDatabase(this); // Prepare the helper
        mDBHelper.open();
        
        mImageLoader = new ImageLoader(this);
            

        
        //initCheckBoxCache(mMemClass);

        mCheckBoxHelper = new CheckBoxLoader(mDBHelper,false, mMemClass);
        
     
        File[] List = getPhotoList();
        if(List == null)
        	List = new File[0];
        
        ArrayAdapter adapter = new SimpleFileAdapter(this, List);

        setListAdapter(adapter); 
        //this.getListView().setOnScrollListener(this);
        

	}
	
	@Override
	public void onStart(){
		super.onStart();
		if(!mDBHelper.isOpen()){
			mDBHelper.open();
		}
	}
	
	
	@Override
	public void onStop(){
		super.onStop();
		mDBHelper.close();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		// Close the database if it has not been closed yet
		if(mDBHelper.isOpen())mDBHelper.close();
	}

 
	/**
	 * Retrieves the list of photos to select from 
	 * 
	 * @return The files to select from
	 */
	private File[] getPhotoList() {
		File photoPath = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera"); // Dont hard code this
        return photoPath.listFiles();
        
	}

	
	class SimpleFileAdapter extends ArrayAdapter<File>{
		private final Context mContext;
		private final File[] mPhotos;
		
		public SimpleFileAdapter(Context context, File[] photos) {
			super(context, R.layout.rowlayout, android.R.layout.simple_list_item_1, photos);
			
			mContext = context;
			mPhotos  = photos;
			
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
					selectedBox.setOnClickListener(new CheckBoxClickListener(mCheckBoxHelper, mDBHelper,Absolutepath));
					
				}
				// Is the file present in the database
				// Is so inform the user with the checkbox
				// Threaded to not block the UI
				{
					selectedBox.setChecked(false);// So the user wont see checks when scrolling
					mCheckBoxHelper.setChecked(selectedBox, Absolutepath); // Will set the check for real						

					
				}
			
			textView.setText(name); // remove the file type from the name
			
			//String hash = String.valueOf(((new File(Absolutepath)).hashCode()));
			mImageLoader.setImage(Absolutepath, imageView);

	        }
			// Return the new view
			return rowView;
		}
		
		
	}





/*
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

		
		switch(scrollState){
		
		case OnScrollListener.SCROLL_STATE_FLING:{
			mImageLoader.holdTaskLoader();
			}break;
		case OnScrollListener.SCROLL_STATE_IDLE:{
			mImageLoader.releaseTaskLoader();
			}break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:{
			mImageLoader.releaseTaskLoader();
			}break;
		}
		
		
		
		
        
	}
	*/
	

}
