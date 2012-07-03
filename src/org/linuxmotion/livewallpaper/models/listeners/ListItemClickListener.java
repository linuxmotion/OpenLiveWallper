package org.linuxmotion.livewallpaper.models.listeners;

import android.view.View.OnClickListener;

public abstract class ListItemClickListener implements OnClickListener{
	
	protected String mImage; 
	public ListItemClickListener(String absolutepath){
		mImage = absolutepath;
		
	}
	
	public String getListnerAbsoluteImagePath(){
		return mImage;
	}
	
}
