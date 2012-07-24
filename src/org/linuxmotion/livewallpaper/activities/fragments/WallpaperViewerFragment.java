package org.linuxmotion.livewallpaper.activities.fragments;

import java.io.InputStream;
import java.util.ArrayList;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.utils.Constants;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class WallpaperViewerFragment extends Fragment implements AdapterView.OnItemSelectedListener,
        OnClickListener {

	private Gallery mGallery;
    private ImageView mImageView;
    private TextView mInfoView;
    private boolean mIsWallpaperSet;

    private Bitmap mBitmap;

    private ArrayList<Integer> mThumbs;
    private ArrayList<Integer> mImages;
    private WallpaperLoader mLoader;
    SharedPreferences mSharedPreferences;

    public static WallpaperViewerFragment newInstance(int index) {
    	WallpaperViewerFragment f = new WallpaperViewerFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);

        return f;
    }
    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
    
    @Override
    public void onCreate(Bundle icicle) {
    	super.onCreate(icicle);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);

      //  
        
        findWallpapers();

       // setContentView(R.layout.wallpaper_chooser);



    }
    
    @Override
    public void onActivityCreated (Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	mSharedPreferences = this.getActivity().getSharedPreferences(Constants.SHARED_PREFS, 0);
   
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.wallpaper_chooser, container, false);
     	mGallery = (Gallery) v.findViewById(R.id.gallery);
        mGallery.setAdapter(new ImageAdapter(this));
        mGallery.setOnItemSelectedListener(this);
        mGallery.setCallbackDuringFling(false);

        v.findViewById(R.id.set).setOnClickListener(this);

        mImageView = (ImageView) v.findViewById(R.id.wallpaper);
        mInfoView = (TextView) v.findViewById(R.id.info);
        
        return v;
    }

    private void findWallpapers() {
        mThumbs = new ArrayList<Integer>(24);
        mImages = new ArrayList<Integer>(24);

        final Resources resources = getResources();
        final String packageName = this.getActivity().getApplication().getPackageName();

        addWallpapers(resources, packageName, R.array.imageIds);
    }

    private void addWallpapers(Resources resources, String packageName, int list) {
        final String[] extras = resources.getStringArray(list);
        for (String extra : extras) {
            int res = resources.getIdentifier(extra, "drawable", packageName);
            if (res != 0) {
                final int thumbRes = resources.getIdentifier(extra + "_small",
                        "drawable", packageName);

                if (thumbRes != 0) {
                    mThumbs.add(thumbRes);
                    mImages.add(res);
                }
            }
        }
    }

    @Override
	public void onResume() {
        super.onResume();
        mIsWallpaperSet = false;
    }

    @Override
	public void onDestroy() {
        super.onDestroy();
        
        if (mLoader != null && mLoader.getStatus() != WallpaperLoader.Status.FINISHED) {
            mLoader.cancel(true);
            mLoader = null;
        }
    }

    public void onItemSelected(AdapterView parent, View v, int position, long id) {
        if (mLoader != null && mLoader.getStatus() != WallpaperLoader.Status.FINISHED) {
            mLoader.cancel();
        }
        mLoader = (WallpaperLoader) new WallpaperLoader().execute(position);
    }

    /*
     * When using touch if you tap an image it triggers both the onItemClick and
     * the onTouchEvent causing the wallpaper to be set twice. Ensure we only
     * set the wallpaper once.
     */
    private void selectWallpaper(int position) {
        if (mIsWallpaperSet) {
            return;
        }

        mIsWallpaperSet = true;
        InputStream stream = getResources().openRawResource(mImages.get(position));
		Toast.makeText(this.getActivity().getBaseContext(), "" + position, Toast.LENGTH_SHORT).show();
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putInt(Constants.DEFAULT_IMAGE_SELECTION, position);
		editor.commit();
		//setResult(Activity.RESULT_OK);
		//finish();
    }

    public void onNothingSelected(AdapterView parent) {
    }

    private class ImageAdapter extends BaseAdapter {
        private LayoutInflater mLayoutInflater;

        ImageAdapter(WallpaperViewerFragment fragment) {
            mLayoutInflater = fragment.getActivity().getLayoutInflater();
        }

        public int getCount() {
            return mThumbs.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image;

            if (convertView == null) {
                image = (ImageView) mLayoutInflater.inflate(R.layout.wallpaper_item, parent, false);
            } else {
                image = (ImageView) convertView;
            }
    	    
    	    BitmapFactory.Options resample = new BitmapFactory.Options();
            resample.inSampleSize = 2;
            
            int thumbRes = mThumbs.get(position);
            
            image.setImageBitmap(BitmapFactory.decodeResource(getResources(), thumbRes, resample));
            
            //image.setImageResource(thumbRes);
            Drawable thumbDrawable = image.getDrawable();
            if (thumbDrawable != null) {
                thumbDrawable.setDither(true);
            } else {
                Log.e("Paperless System", String.format(
                    "Error decoding thumbnail resId=%d for wallpaper #%d",
                    thumbRes, position));
            }
            return image;
        }
    }

    public void onClick(View v) {
        selectWallpaper(mGallery.getSelectedItemPosition());
    }

    class WallpaperLoader extends AsyncTask<Integer, Void, Bitmap> {
        BitmapFactory.Options mOptions;

        WallpaperLoader() {
            mOptions = new BitmapFactory.Options();
            mOptions.inDither = false;
            mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;            
        }
        
        protected Bitmap doInBackground(Integer... params) {
            if (isCancelled()) return null;
            try {
                return BitmapFactory.decodeResource(getResources(),
                        mImages.get(params[0]), mOptions);
            } catch (OutOfMemoryError e) {
                return null;
            }            
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            if (b == null) return;

            if (!isCancelled() && !mOptions.mCancel) {
                // Help the GC
                if (mBitmap != null) {
                    mBitmap.recycle();
                }

                mInfoView.setText(getResources().getStringArray(R.array.imageIds)[mGallery.getSelectedItemPosition()]);

                final ImageView view = mImageView;
                view.setImageBitmap(b);

                mBitmap = b;

                final Drawable drawable = view.getDrawable();
                drawable.setFilterBitmap(true);
                drawable.setDither(true);

                view.postInvalidate();

                mLoader = null;
            } else {
               b.recycle(); 
            }
        }

        void cancel() {
            mOptions.requestCancelDecode();
            super.cancel(true);
        }
    }
}