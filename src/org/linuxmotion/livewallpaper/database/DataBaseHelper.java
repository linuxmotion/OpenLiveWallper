package org.linuxmotion.livewallpaper.database;

import java.io.File;
import java.util.List;

import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.content.Context;

public class DataBaseHelper  {
	
	private static final String TAG = DataBaseHelper.class.getSimpleName(); 
	
	onDatabaseTransactionFinished mDatabaseTransactionListener = null;
	Context mContext = null;
	public interface onDatabaseTransactionFinished{
		public static final int TRANSACTION_INCOMPLETE = 0;
		public static final int TRANSACTION_ADD = 1;
		public static final int TRANSACTION_DELETE = 2;
		public void onTransactionFinished(int action);
		
	}
	
	public void setTransactionFinishedListener(onDatabaseTransactionFinished listener){
		
		mDatabaseTransactionListener = listener;
	}
	
	private static MyImageDatabase mDatabase; 
	
	public void initDatabase(Context context){
		mDatabase = new MyImageDatabase(context);
		mContext = context;
		
	}
	
	public void initDatabase(Context context, onDatabaseTransactionFinished listener){
		setTransactionFinishedListener(listener);
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
			long transcode = mDatabase.addImage(image);
			LogWrapper.Logi(TAG, "The add transaction code is = " + transcode);
			if(transcode == MyImageDatabase.PATH_ERROR   || MyImageDatabase.INSERT_ERROR == transcode ){
					
				if(mDatabaseTransactionListener != null){
					LogWrapper.Logi(TAG, "Invoking transaction finished with code = " +  onDatabaseTransactionFinished.TRANSACTION_INCOMPLETE);
					mDatabaseTransactionListener.onTransactionFinished(onDatabaseTransactionFinished.TRANSACTION_INCOMPLETE);
					}
			
			}else if(transcode >= 0){
				
				if(mDatabaseTransactionListener != null){
					LogWrapper.Logi(TAG, 
							"Invoking transaction finished with code = " +  onDatabaseTransactionFinished.TRANSACTION_ADD);		
					mDatabaseTransactionListener.onTransactionFinished(onDatabaseTransactionFinished.TRANSACTION_ADD);

					//mContext.sendBroadcast();
				}
			}
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
				int transcode = mDatabase.deleteImage(image);
				LogWrapper.Logi(TAG, "The delete transaction code is = " + transcode);
				if(transcode > 0){
					if(mDatabaseTransactionListener != null){
						LogWrapper.Logi(TAG, 
								"Invoking transaction finished with code = " +  onDatabaseTransactionFinished.TRANSACTION_DELETE);	
						mDatabaseTransactionListener.onTransactionFinished(onDatabaseTransactionFinished.TRANSACTION_DELETE);
						}
				}else if(transcode == 0 || transcode == MyImageDatabase.PATH_ERROR ){
					if(mDatabaseTransactionListener != null){
						LogWrapper.Logi(TAG, 
								"Invoking transaction finished with code = " +  onDatabaseTransactionFinished.TRANSACTION_INCOMPLETE);		
						
						mDatabaseTransactionListener.onTransactionFinished(onDatabaseTransactionFinished.TRANSACTION_INCOMPLETE);
					}
					
				}
				
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
		if(mDatabase != null || !mDatabase.isOpen())
			mDatabase.open();	
		
	}	
	
	public void close(){
		
		if(mDatabase != null || mDatabase.isOpen())
				mDatabase.close();		
	}

	
	
	
	public boolean isOpen(){
		return mDatabase.isOpen();
	}
	
	

}
