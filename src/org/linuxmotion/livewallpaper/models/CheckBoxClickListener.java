package org.linuxmotion.livewallpaper.models;

import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.database.MyImageDatabase;

import android.view.View;
import android.widget.CheckBox;

public class CheckBoxClickListener extends ListItemClickListener {
	DataBaseHelper mDBHelper;
	
	public CheckBoxClickListener(DataBaseHelper db , String pathandname) {
		super(pathandname);
		mDBHelper =  db;
	}

	@Override
	public void onClick(View v) {
		
		Boolean checked = ((CheckBox)v).isChecked();
		if(checked){
			mDBHelper.AddToList(mImage);
		}else{
			mDBHelper.RemoveFromList(mImage);
		}
		
	}

}
