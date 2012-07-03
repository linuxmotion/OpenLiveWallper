package org.linuxmotion.receivers;

import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

public class AddPhotoReciever extends BroadcastReceiver {
	private static final String TAG = AddPhotoReciever.class.getSimpleName();

	DataBaseHelper helper = new DataBaseHelper();
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		helper.initDatabase(context);
		helper.open();
		
		Bundle extras = intent.getExtras();
		Uri uri = (Uri) extras.getParcelable(Intent.EXTRA_STREAM);
		String filename = parseUriToFilename(context, uri);
		LogWrapper.Logi(TAG, "Going to add picture path" + filename + " to the database");
		//helper.AddToList(filename);
		
	}
	
	public String parseUriToFilename(Context context, Uri uri) {
		  String selectedImagePath = null;
		  String filemanagerPath = uri.getPath();

		  String[] projection = { MediaStore.Images.Media.DATA };
		  Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

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

}
