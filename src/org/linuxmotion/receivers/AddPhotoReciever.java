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
	PhotoReceiver mReciever = null;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mReciever.onPhotoRecived();
		
	}
	
	public void setReceiver(PhotoReceiver receiver){
		if(receiver != null)
			mReciever = receiver;
	}
	
	public interface PhotoReceiver{
		
		public void onPhotoRecived();
		
	}

}
