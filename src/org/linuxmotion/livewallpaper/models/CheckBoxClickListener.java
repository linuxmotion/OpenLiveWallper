package org.linuxmotion.livewallpaper.models;

import org.linuxmotion.concurrent.CheckBoxLoader;
import org.linuxmotion.livewallpaper.database.DataBaseHelper;

import android.view.View;
import android.widget.CheckBox;

public class CheckBoxClickListener extends ListItemClickListener {
	private DataBaseHelper mDBHelper;
	//private CheckBoxLoader mAsyncCheckHelper;
	
	public CheckBoxClickListener(CheckBoxLoader chelper, DataBaseHelper db , String pathandname) {
		super(pathandname);
		mDBHelper =  db;
		//mAsyncCheckHelper = chelper;
	}

	@Override
	public void onClick(View v) {
		
		Boolean checked = ((CheckBox)v).isChecked();
		if(checked){
			//mAsyncCheckHelper.addBooleanToMemCache(mImage, true);
			mDBHelper.AddToList(mImage);
		}else{
			//mAsyncCheckHelper.addBooleanToMemCache(mImage, false);
			mDBHelper.RemoveFromList(mImage);
		}
		
	}

}
