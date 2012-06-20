package org.linuxmotion.livewallpaper.models;

import java.lang.ref.WeakReference;

import org.linuxmotion.concurrent.CheckBoxLoaderTask;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class AsyncCheckBox extends CheckBox {


    private WeakReference<CheckBoxLoaderTask> BoxloaderTaskReference = null;
    private int mKey = 0;
    
	public AsyncCheckBox(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	public AsyncCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public AsyncCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/**
	 * The task must have a key associated with it
	 * see. CheckBoxLoaderTask.setKey(int) first
	 * @param task
	 */
	public void setTask(CheckBoxLoaderTask task){
		BoxloaderTaskReference = new WeakReference<CheckBoxLoaderTask>(task);
		mKey = task.getKey();
	}
	
	/**
	 * 
	 * @return The task associated with this checkbox
	 */
	public CheckBoxLoaderTask getTask(){
		if(BoxloaderTaskReference != null)
			return BoxloaderTaskReference.get();
		else 
			return null;
	}
	
	

}
