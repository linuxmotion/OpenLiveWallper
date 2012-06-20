package org.linuxmotion.concurrent;

import java.io.File;
import java.util.concurrent.RejectedExecutionException;

import org.linuxmotion.livewallpaper.activities.BasicFileBrowser;
import org.linuxmotion.livewallpaper.database.DataBaseHelper;
import org.linuxmotion.livewallpaper.models.AsyncCheckBox;

import android.util.Log;

public class AsyncCheckBoxHelper {

	
	private static final String TAG = "AcyncCheckBoxHelper";
	public AsyncCheckBoxHelper(){
		// Empty constructor
	}
	public void setChecked(BasicFileBrowser act, DataBaseHelper helper, AsyncCheckBox selectedBox, String absolutepath) {
		
		if(cancelPotentialQuery(selectedBox, absolutepath)){
			CheckBoxLoaderTask task = new CheckBoxLoaderTask(act, helper, selectedBox);
			if(selectedBox != null){
				
				//task.setKey((new File(absolutepath)).hashCode());
				((AsyncCheckBox)selectedBox).setTask(task); // Save a reference to the task
				
			}
			
			try{
				task.execute(absolutepath);
			}catch(RejectedExecutionException e){
				
			}
		}
		
	}
	private boolean cancelPotentialQuery(AsyncCheckBox selectedBox, String absolutepath) {
		
		if(selectedBox != null){
			
			CheckBoxLoaderTask task = selectedBox.getTask();
			if(task != null){
				//int hash = (new File(absolutepath)).hashCode();
				String path = task.getPath();
				
				if(path != absolutepath && path != null){
					Log.i(TAG, "Canceling task for image " + (new File(path)).getName());
					task.cancel(true);
					
				}
				else{
					
					return false; // We are the same task
				}
			
			}
			
			
		}
		return true; // Task was cancelled
		
	}
	
	

}
