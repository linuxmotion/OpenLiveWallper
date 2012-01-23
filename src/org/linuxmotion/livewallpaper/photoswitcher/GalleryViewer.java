package org.linuxmotion.livewallpaper.photoswitcher;

import org.linuxmotion.livewallpaper.utils.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;

public class GalleryViewer extends Activity {

	SharedPreferences mSharedPreferences;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.galleryveiwer);

	    Gallery gallery = (Gallery) findViewById(R.id.gallery);
	    gallery.setAdapter(new ImageAdapter(this));

	    mSharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFS, 0);
	    
     
	    
	    gallery.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView parent, View v, int position, long id) {
	            Toast.makeText(GalleryViewer.this, "" + position, Toast.LENGTH_SHORT).show();
	               SharedPreferences.Editor editor = mSharedPreferences.edit();
	               editor.putInt(Constants.DEFAULT_IMAGE_SELECTION, position);
	               editor.commit();
	               finish();
	        }
	    });
	}
	
	public class ImageAdapter extends BaseAdapter {
	    int mGalleryItemBackground;
	    private Context mContext;

	    
	    private Integer[] mImageIds = {Constants.DefaultPictures[0],Constants.DefaultPictures[1],Constants.DefaultPictures[2],Constants.DefaultPictures[3],
	     Constants.DefaultPictures[4],Constants.DefaultPictures[5],Constants.DefaultPictures[6]};

	    public ImageAdapter(Context c) {
	        mContext = c;
	        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.HelloGallery);
	        mGalleryItemBackground = attr.getResourceId(
	                R.styleable.HelloGallery_android_galleryItemBackground, 0);
	        attr.recycle();
	    }

	    public int getCount() {
	        return mImageIds.length;
	    }

	    public Object getItem(int position) {
	        return position;
	    }

	    public long getItemId(int position) {
	        return position;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        ImageView imageView = new ImageView(mContext);
	        imageView.setImageResource(mImageIds[position]);
	        imageView.setLayoutParams(new Gallery.LayoutParams(150, 100));
	        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	        imageView.setBackgroundResource(mGalleryItemBackground);

	        return imageView;
	    }
	}
}
