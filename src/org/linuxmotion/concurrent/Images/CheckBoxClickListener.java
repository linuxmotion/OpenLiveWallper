package org.linuxmotion.concurrent.Images;

import org.linuxmotion.livewallpaper.utils.DataBaseHelper;

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
			DataBaseHelper.AddToList(mImage);
		}else{
			DataBaseHelper.RemoveFromList(mImage);
		}
		
	}

}
