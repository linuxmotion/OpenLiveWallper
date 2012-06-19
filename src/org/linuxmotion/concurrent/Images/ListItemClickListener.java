package org.linuxmotion.concurrent.Images;

import org.linuxmotion.livewallpaper.utils.DataBaseHelper;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

public abstract class ListItemClickListener implements OnClickListener{
	
	protected String mImage; 
	public ListItemClickListener(String pathandname){
		mImage = pathandname;
		
	}
	
}
