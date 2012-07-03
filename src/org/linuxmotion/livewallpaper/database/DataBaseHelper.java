package org.linuxmotion.livewallpaper.database;

import java.io.File;
import java.util.List;

import android.content.Context;

public class DataBaseHelper  {
	
	private static MyImageDatabase mDatabase; 
	
	public void initDatabase(Context context){
		mDatabase = new MyImageDatabase(context);
		
	}
	

	public void updatePhotoList(File[] photos){}

	public void updatePhotoList(String[] photos){}

	/**
	 * Add to the database, ran on a seperate thread
	 * @param image
	 */
	public  void AddToList(final String image) {
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDatabase.addImage(image);
			}}).run();	
	}
	
	/**
	 * 
	 * Delete from the database, Ran from another thread
	 * @param image
	 */
	public  void RemoveFromList(final String image) {

		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mDatabase.deleteImage(image);
			}}).run();	
	}

	public Boolean isInDataBase(String path) {
		if(path == null)return false;
		return mDatabase.isImagePathPresent(path);
	}

	public String[] getAllEntries() {
		
		List<ImagePath> im = mDatabase.getAllImages();
		int count = im.size();
		String[] paths = new String[count]; 
		for(int i = 0; i < count; i++){
			
			paths[i] = im.get(i).getImagePath();
			
		}	
		return paths;	
	}

	public void open(){
		if(!mDatabase.isOpen())mDatabase.open();	
	}	
	
	public void close(){
		
		if(mDatabase.isOpen())mDatabase.close();		
	}
	
	public boolean isOpen(){
		return mDatabase.isOpen();
	}
	
	

}
