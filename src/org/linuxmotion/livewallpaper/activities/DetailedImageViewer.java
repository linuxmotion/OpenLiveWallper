package org.linuxmotion.livewallpaper.activities;

import java.util.ArrayList;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.utils.LogWrapper;
import org.linuxmotion.livewallpaper.utils.images.BitmapHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DetailedImageViewer extends Activity implements OnClickListener {
	
	private static final String TAG = "DetailedImageViewer";

	private DataBaseHelper mHelper = new DataBaseHelper();

	ImageView mImageView;
	Button mRemove;
	Button mCancel;
	Button mSet;
	String mPath;
	Boolean mIsInDB = false;
	@Override
	protected void onCreate (Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    mHelper.initDatabase(this.getApplicationContext());
	    mHelper.open();
	    // Get intent, action and MIME type
	    Intent intent = getIntent();
	    String action = intent.getAction();
	    String type = intent.getType();

	    if (Intent.ACTION_SEND.equals(action) && type != null) {
	        if (type.startsWith("image/")) {
	            handleSendImage(intent); // Handle single image being sent
	        }
	    } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
	        if (type.startsWith("image/")) {
	            handleSendMultipleImages(intent); // Handle multiple images being sent
	        }
	    } else {
	        // Handle other intents, such as being started from the home screen
	    }
	    
	    
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		mHelper.open();
		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		mHelper.close();
		
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		mHelper.close();
		
	}

	void handleSendText(Intent intent) {
	    String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
	    if (sharedText != null) {
	        // Update UI to reflect text being shared
	    }
	}

	void handleSendImage(Intent intent) {
	    Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
	    
	    if (imageUri != null) {
	        String abspath = parseUriToFilename(imageUri);
	        LogWrapper.Logi(TAG, "The path is "+ abspath);
	        mPath = abspath;

		    // Get the screen size
		    DisplayMetrics displaymetrics = new DisplayMetrics();
	        ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displaymetrics);
		    setContentView(R.layout.detailedimageviewer);
		    
		    Bitmap b = BitmapHelper.decodeSampledBitmapFromImage(abspath, displaymetrics.widthPixels - 50, displaymetrics.heightPixels  - 50);
		    mImageView = (ImageView) findViewById(R.id.DetailedViewer);
		    mImageView.setImageBitmap(b);
  
		    
		   mSet = (Button)this.findViewById(R.id.setImageButton);
		   mSet.setOnClickListener(this);
		   
		   mCancel = (Button)this.findViewById(R.id.CancelButton);
		   mCancel.setOnClickListener(this);
		   
		   mRemove = (Button)this.findViewById(R.id.RemoveButton);
		   mRemove.setOnClickListener(this);
		   
		   // Is the picture in the database
		   if(mHelper.isInDataBase(abspath)){
			    // If it is only show the remove button
		    	mSet.setVisibility(View.GONE);
		    	//mRemove.setVisibility(View.VISIBLE);
		    }else{ 
		    	// If it is not only show the set button
		    	//mSet.setVisibility(View.VISIBLE);
		    	mRemove.setVisibility(View.GONE);
		    	
		    	
		    }
	    }
	}

	void handleSendMultipleImages(Intent intent) {
	    ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
	    if (imageUris != null) {
	        // Update UI to reflect multiple images being shared
	    }
	}
	
	public String parseUriToFilename(Uri uri) {
		  String selectedImagePath = null;
		  String filemanagerPath = uri.getPath();

		  String[] projection = { MediaStore.Images.Media.DATA };
		  Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

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
			case R.id.CancelButton: {
				mHelper.close();
				finish();
		        
			}break;
			case R.id.RemoveButton: {
		        mHelper.RemoveFromList(mPath);
		        mHelper.close();
		        finish();
			}break;
			case R.id.setImageButton: {
				mHelper.AddToList(mPath);
				mHelper.close();
				finish();
			}break;
		
		}
		
	} 


}
