package org.linuxmotion.concurrent;

import java.io.File;
import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.activities.BasicFileBrowser;
import org.linuxmotion.livewallpaper.database.DataBaseHelper;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;

public class CheckBoxLoaderTask extends AsyncTask<String, Void, Boolean>{

	private static final String TAG = CheckBoxLoaderTask.class.getName();
	DataBaseHelper mHelper;
	WeakReference<CheckBox> mCheckBox;
	private int KEY_ID;
	private String mPath;
	BasicFileBrowser mAct;
	
	CheckBoxLoaderTask(BasicFileBrowser act, DataBaseHelper helper, CheckBox box){
		mHelper = helper;
		mCheckBox = new WeakReference<CheckBox>(box);
		mAct = act;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		if(isCancelled())return null;
		mPath = params[0];
		Boolean inDB = mHelper.isInDataBase(mPath);
		
		if(isCancelled())return null;
		//Log.i(TAG, "Adding boolean to mem cache for img = " + (new File(mPath)).getName());
		mAct.addBooleanToMemCache(mPath, inDB); // Always add the CheckBox to the cache
		if(isCancelled())return null;
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
