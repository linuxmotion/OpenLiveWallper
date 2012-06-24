package org.linuxmotion.concurrent;

import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.os.AsyncTask;
import android.widget.CheckBox;

public class CheckBoxLoaderTask extends AsyncTask<String, Void, Boolean>{

	private static final String TAG = CheckBoxLoaderTask.class.getSimpleName();
	private DataBaseHelper mHelper;
	private WeakReference<CheckBox> mCheckBox;
	private CheckBoxLoader mCheckBoxHelper;
	private int KEY_ID;
	private String STRING_KEY_ID;
	private String mPath;
	
	CheckBoxLoaderTask(CheckBoxLoader parentEnque, CheckBox box, DataBaseHelper helper){
		mHelper = helper;
		mCheckBox = new WeakReference<CheckBox>(box);
		mCheckBoxHelper = parentEnque;

	}
	@Override
	protected Boolean doInBackground(String... params) {
		if(isCancelled())return null;
		mPath = params[0];
		
		Boolean inDB = mHelper.isInDataBase(mPath);
		
		if(isCancelled())return null;
		LogWrapper.Logv(TAG, "Beggining to add a boolean to mem cache for img = " + mPath);
		mCheckBoxHelper.addBooleanToMemCache(mPath, inDB); // Always add the CheckBox to the cache

		return inDB;

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
	
	public void setStringKey(String key) {
		STRING_KEY_ID = key;
	}

	/**
	 * @return the kEY_ID
	 */
	public String getStringKey() {
		return STRING_KEY_ID;
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
