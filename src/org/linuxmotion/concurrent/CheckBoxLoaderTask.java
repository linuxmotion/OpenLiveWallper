package org.linuxmotion.concurrent;

import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.database.DataBaseHelper;

import android.os.AsyncTask;
import android.widget.CheckBox;

public class CheckBoxLoaderTask extends AsyncTask<String, Void, Boolean>{

	DataBaseHelper mHelper;
	WeakReference<CheckBox> mCheckBox;
	private int KEY_ID;
	private String mPath;
	
	CheckBoxLoaderTask(DataBaseHelper helper, CheckBox box){
		mHelper = helper;
		mCheckBox = new WeakReference<CheckBox>(box);
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if(isCancelled())return null;
		mPath = params[0];
		return mHelper.isInDataBase(mPath);

	}
	
	@Override
	protected void onPostExecute(Boolean result){
		 if (isCancelled()) {
	        	return;
	        }
		 if(mCheckBox != null){
			 
			if(result == null){
				// Shouldnt recieve a null here, but just in case
				mCheckBox.get().setChecked(false);
			}
			else{
				mCheckBox.get().setChecked(result);
			}
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

	public String getPath() {
		return mPath;
	}
}
