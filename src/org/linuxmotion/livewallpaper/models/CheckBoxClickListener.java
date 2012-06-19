package org.linuxmotion.livewallpaper.models;

import org.linuxmotion.livewallpaper.database.DataBaseHelper;

import android.view.View;
import android.widget.CheckBox;

public class CheckBoxClickListener extends ListItemClickListener {

	
	public CheckBoxClickListener(String pathandname) {
		super(pathandname);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onClick(View v) {
		
		Boolean checked = ((CheckBox)v).isChecked();
		if(checked){
			org.linuxmotion.livewallpaper.database.DataBaseHelper.AddToList(mImage);
		}else{
			DataBaseHelper.RemoveFromList(mImage);
		}
		
	}

}
