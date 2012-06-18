package org.linuxmotion.livewallpaper.photoswitcher;

import java.io.File;

import org.linuxmotion.livewallpaper.photoswitcher.R;
import org.linuxmotion.livewallpaper.utils.lists.ImageLoader;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BasicFileBrowser extends ListActivity {
	
	ListView mSelectionList;
	//GenericQueue data = new GenericQueue();

	@Override
	  public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //setContentView(R.layout.basic_file_selection);
        //mSelectionList = (ListView) this.findViewById(android.R.id.list);
       
        
        File photoPath = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera"); // Dont hard code this
        // First paramenter - Context
        // Forth - the Array of data
        File[] f = photoPath.listFiles();
        ArrayAdapter adapter = new SimpleFileAdapter(this, f);

        setListAdapter(adapter);

		//data.mQue = new ImageQue(this);
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
			CheckBox  selectedBox;
			if (rowView == null) {
				
			
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);

		     textView = (TextView) rowView.findViewById(R.id.label);
			 imageView = (ImageView) rowView.findViewById(R.id.icon);
			 selectedBox = (CheckBox) rowView.findViewById(R.id.box);
			
			//data.mQue.AddToQue(imageView.getId(), mPhotos[position].getAbsolutePath());
			//data.mQue.startQueThread();
			}
			else{
				
				textView = (TextView) rowView.findViewById(R.id.label);
				 imageView = (ImageView) rowView.findViewById(R.id.icon);
				 selectedBox = (CheckBox) rowView.findViewById(R.id.box);
				
			}
			
			// Modify the objects
			String fullname = mPhotos[position].getName();
			String name = fullname.substring(0, fullname.indexOf('.'));
			
			textView.setText(name); // remove the file type from the name
			
			ImageLoader setImage = new ImageLoader();
			setImage.download(mPhotos[position].getAbsolutePath(), imageView);
			
			return rowView;
		}
		
		
	}
	
	

}
