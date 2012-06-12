package org.linuxmotion.livewallpaper.photoswitcher;

import org.linuxmotion.livewallpaper.utils.Constants;
import org.linuxmotion.livewallpaper.utils.LicenseChecker;
import org.linuxmotion.livewallpaper.photoswitcher.GalleryViewer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpenLiveWallPaperActivity extends Activity {

	private static final int REQ_CODE_PICK_IMAGE = 1;
	private SharedPreferences mSharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		Button phoneGalleryButtonSelect = (Button) this.findViewById(R.id.phoneGalleryButton);
		TextView phoneGalleryTextSelect = (TextView) this.findViewById(R.id.phoneGalleryTextView);
		
		if(LicenseChecker.checkLicense() == false){ // Hide the views if the license is not available
			
			phoneGalleryTextSelect.setVisibility(View.GONE);
			phoneGalleryButtonSelect.setVisibility(View.GONE);
		}
		
	}

	
	public void startSelection(View v){
		Intent i = new Intent(Intent.ACTION_PICK,
	               android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		
		
		startActivityForResult(i, REQ_CODE_PICK_IMAGE); 
	
		
	}
	public void startDefaultSelection(View v){
		
		Intent i = new Intent(this, org.linuxmotion.livewallpaper.photoswitcher.WallpaperViewer.class);
		
		startActivity(i); 
	
		
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

	    switch(requestCode) { 
	    case REQ_CODE_PICK_IMAGE:
	        if(resultCode == RESULT_OK){  
	            Uri selectedImage = imageReturnedIntent.getData();
	            String[] filePathColumn = {MediaStore.Images.Media.DATA};

	            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
	            cursor.moveToFirst();

	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String filePath = cursor.getString(columnIndex);
	            cursor.close();

	            mSharedPreferences = this.getSharedPreferences(Constants.SHARED_PREFS, 0);
	             // Save the filepath to a shared preference

				Log.d(this.getClass().getSimpleName(), "the filepath is: " + filePath);
	            SharedPreferences.Editor editor = mSharedPreferences.edit();
	            editor.putString(Constants.SINGLE_FILE_PATH, filePath);
	            editor.commit();
	           
	        }
	    }
	    
	    
	    
	}

}
