package org.linuxmotion.concurrent;

import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.database.DataBaseHelper;

import android.os.AsyncTask;
import android.widget.CheckBox;

public class CheckBoxLoaderTask extends AsyncTask<String, Void, Boolean>{

	DataBaseHelper mHelper;
	WeakReference<CheckBox> mCheckBox;
	private int KEY_ID;
	
	CheckBoxLoaderTask(DataBaseHelper helper, CheckBox box){
		mHelper = helper;
		mCheckBox = new WeakReference<CheckBox>(box);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if(isCancelled())return null;
		return mHelper.isInDataBase(params[0]);

	}
	
	@Override
	protected void onPostExecute(Boolean result){
		if(result == null){
			// Shouldnt recieve a null here, but just in case
			mCheckBox.get().setChecked(false);
		}
		else{
			mCheckBox.get().setChecked(result);
		}
	}

	/**
	 * @param k the KEY_ID to set
	 */
	public void setKey(int k) {
		KEY_ID = k;
	}

	/**
	 * @return the kEY_ID
	 */
	public int getKey() {
		return KEY_ID;
	}
}
