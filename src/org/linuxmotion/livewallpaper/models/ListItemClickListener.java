package org.linuxmotion.livewallpaper.models;

import android.view.View.OnClickListener;

public abstract class ListItemClickListener implements OnClickListener{
	
	protected String mImage; 
	public ListItemClickListener(String pathandname){
		mImage = pathandname;
		
	}
	
}
