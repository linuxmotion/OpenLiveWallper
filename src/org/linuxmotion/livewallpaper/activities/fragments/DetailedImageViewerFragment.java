package org.linuxmotion.livewallpaper.activities.fragments;

import java.util.ArrayList;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.database.DataBaseHelper.onDatabaseTransactionFinished;
import org.linuxmotion.livewallpaper.utils.LogWrapper;
import org.linuxmotion.livewallpaper.utils.images.BitmapHelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

public class DetailedImageViewerFragment extends Fragment implements OnClickListener,  onDatabaseTransactionFinished {
	
	private static final String TAG = DetailedImageViewerFragment.class.getSimpleName();

	private DataBaseHelper mHelper = new DataBaseHelper();

	private ImageView mImageView;
	private Button mSetRemove;
	private Button mQuit;
	private String mPath;
	
	
	@Override 
	public void onCreate(Bundle savedInstanceState){
	    super.onCreate(savedInstanceState);
	    
	    mHelper.initDatabase(this.getActivity().getApplicationContext(), this);
	    mHelper.open();
		
	}
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		  LogWrapper.Logi(TAG, "creating detailed view fragment");
		  
		// Get intent, action and MIME type
		    Intent intent = getActivity().getIntent();
		    String action = intent.getAction();
		    String type = intent.getType();
		    View layout = null;

		    if (Intent.ACTION_SEND.equals(action) && type != null) {
		        if (type.startsWith("image/")) {
		            layout = handleSendImage(inflater, intent, container); // Handle single image being sent
		        }
		    } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
		        if (type.startsWith("image/")) {
		        	layout =  handleSendMultipleImages(inflater, intent, container); // Handle multiple images being sent
		        }
		    } else {
		        // Handle other intents, such as being started from the home screen
		    }
		  
		  return layout;
		  
	}
	


	private View handleSendImage(LayoutInflater inflater, Intent intent, ViewGroup container) {
	    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	    View v = null ;
	    if (imageUri != null) {
	        String abspath = parseUriToFilename(imageUri);
	        LogWrapper.Logi(TAG, "The path is "+ abspath);
	        mPath = abspath;

		    // Get the screen size
		    DisplayMetrics displaymetrics = new DisplayMetrics();
	        ((WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
	        

			LogWrapper.Logi(TAG, "Inflating view");
	        v = inflater.inflate(R.layout.layout_detailed_image_viewer, container, false);
		    
		    Bitmap b = BitmapHelper.decodeSampledBitmapFromImage(abspath, displaymetrics.widthPixels - 50, displaymetrics.heightPixels  - 50);
		    mImageView = (ImageView) v.findViewById(R.id.DetailedViewer);
		    mImageView.setImageBitmap(b);
  
		    
		   
		   mQuit = (Button)v.findViewById(R.id.QuitButton);
		   mQuit.setOnClickListener(this);
		   
		   mSetRemove = (Button)v.findViewById(R.id.SetRemoveButton);
		   mSetRemove.setOnClickListener(this);
		   
		   // Is the picture in the database
		   if(mHelper.isInDataBase(abspath)){
			    // If it is only show the remove button
		    	mSetRemove.setText(R.string.Remove);
		    	//mRemove.setVisibility(View.VISIBLE);
		    }else{ 
		    	// If it is not only show the set button
		    	mSetRemove.setText(R.string.Set);
		    	
		    	
		    }
	    }
	    
	    return v;
	}

	private View handleSendMultipleImages(LayoutInflater inflater, Intent intent, ViewGroup container) {
	    ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
	    View v = null ;
	    
	    if (imageUris != null) {

			  LogWrapper.Logi(TAG, "creating detailed view fragment for multiple pics");
	        // Update UI to reflect multiple images being shared
	    	String abspath = parseUriToFilename(imageUris.get(0));
	        LogWrapper.Logi(TAG, "The path is "+ abspath);
	        mPath = abspath;

		    // Get the screen size
		    DisplayMetrics displaymetrics = new DisplayMetrics();
	        ((WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
	        
	        v = inflater.inflate(R.layout.layout_detailed_image_viewer, container, false);
		    
		    Bitmap b = BitmapHelper.decodeSampledBitmapFromImage(abspath, displaymetrics.widthPixels - 50, displaymetrics.heightPixels  - 50);
		    mImageView = (ImageView) v.findViewById(R.id.DetailedViewer);
		    mImageView.setImageBitmap(b);
  
		    
		
		   
		   mQuit = (Button)v.findViewById(R.id.QuitButton);
		   mQuit.setOnClickListener(this);
		   
		   mSetRemove = (Button)v.findViewById(R.id.SetRemoveButton);
		   mSetRemove.setOnClickListener(this);
		   
		 
	    }
	    return v;
	}
	
	public String parseUriToFilename(Uri uri) {
		  String selectedImagePath = null;
		  String filemanagerPath = uri.getPath();

		  String[] projection = { MediaStore.Images.Media.DATA };
		  Cursor cursor = this.getActivity().getContentResolver().query(uri, projection, null, null, null);

		  if (cursor != null) {
		    // Here you will get a null pointer if cursor is null
		    // This can be if you used OI file manager for picking the media
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    selectedImagePath = cursor.getString(column_index);
		  }

		  if (selectedImagePath != null) {
		    return selectedImagePath;
		  }
		  else if (filemanagerPath != null) {
		    return filemanagerPath;
		  }
		   return null;
		}

	
	@Override
	public void onClick(View arg0) {
		
		int id = arg0.getId();
		switch(id){
			case R.id.QuitButton: {
				mHelper.close();
				this.getActivity().finish();
		        
			}break;
			case R.id.SetRemoveButton: {
				
				if(mHelper.isInDataBase(mPath)){
					LogWrapper.Logi(TAG, "Removing path from database");
					mHelper.RemoveFromList(mPath);
				}else{

					LogWrapper.Logi(TAG, "Adding path from database");
					mHelper.AddToList(mPath);
				}
				
			}break;
		
		}
		
	} 

	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
	   
	    
	    
	    
	}
	
	@Override
	public void onResume(){
		super.onResume();
		mHelper.open();
		
	}
	
	@Override
	public void onPause(){
		super.onPause();
		mHelper.close();
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		mHelper.close();
		
	}

	@Override
	public void onTransactionFinished(int action) {
		
		LogWrapper.Logi(TAG, "Transaction finished. Transaction code = " + action);
		if(action == onDatabaseTransactionFinished.TRANSACTION_ADD){
			LogWrapper.Logi(TAG, "Setting text" + this.getActivity().getApplicationContext().getString(R.string.Remove));
			mSetRemove.setText(R.string.Remove);
		}else if (action == onDatabaseTransactionFinished.TRANSACTION_DELETE){
			LogWrapper.Logi(TAG, "Setting text" + this.getActivity().getApplicationContext().getString(R.string.Set));
			mSetRemove.setText(R.string.Set);
		}
		
	}


}
