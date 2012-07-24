package org.linuxmotion.livewallpaper.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.linuxmotion.livewallpaper.utils.AeSimpleSHA1;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class MyImageDatabase extends SQLiteOpenHelper {


    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "selectedImages";
 
    // Path table name
    private static final String PATHS_TABLE = "imagepath";
 
 // Path Table Columns names
    private static final String KEY_ID = "id";
    // Path Table Columns names
    private static final String PATH_ID = "path";
    
    //private static final String KEY_COL_ID = "id";
    // Path Table Columns names
    //private static final String PATH_COL_ID = "path";

	private static final String TAG = "MyImageDatabase";
    
    /**
     * Wrapper function , use defaults when called
     * @param context
     */
	public MyImageDatabase(Context context) {
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
        //mDatabase = this.getWritableDatabase();
        //this.close();
		//super(context, name, factory, version);
	}
	
	public MyImageDatabase(Context context, String name, CursorFactory factory,
			int version) {
        super(context, name, null, version);
		//super(context, name, factory, version);
	}
	 
	public void open(){
		mDatabase = this.getWritableDatabase();
		
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "Creating database");
		// TODO Auto-generated method stub
		 String CREATE_IMAGE_PATH_TABLE = "CREATE TABLE " + PATHS_TABLE + "("
         + KEY_ID + " INTERGER PRIMARY KEY," + PATH_ID + " TEXT )";
		 db.execSQL(CREATE_IMAGE_PATH_TABLE);
		 //db.close();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	    // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + PATHS_TABLE);
 
        // Create tables again
        onCreate(db);// Will close the db
		
	}
	
	// Adding new contact
	public synchronized void addImage(String path) {

		 
	    ContentValues values = new ContentValues();
	    
	  
	    
	   if(!isImagePathPresent(path)){ // No need to insert into db if present
		   
		   String hash = AeSimpleSHA1.SHA1(path);
		   LogWrapper.Logi(TAG, "Adding image with the path: " + path + " and hash: " + hash);
	
		    values.put(KEY_ID, hash); //  String hash
		    values.put(PATH_ID, path); // String path
		 
		    // Inserting Row
	    	mDatabase.insert(PATHS_TABLE, null, values);
	   
	    }
	}
	 
	// Getting single contact
	public synchronized ImagePath getImage(int imageNameHash) {
		
		return null;}
	 
	// Getting All Contacts
	public synchronized  List<ImagePath> getAllImages() {
	    List<ImagePath> pathList = new ArrayList<ImagePath>();
	    // Select All Query
	    String selectQuery = "SELECT  * FROM " + PATHS_TABLE;
	 
	    mCursor = mDatabase.rawQuery(selectQuery, null);
	
	    // looping through all rows and adding to list
	    if (mCursor.moveToFirst()) {
	        do {
	        	ImagePath image = new ImagePath();
	            image.setImagePath(mCursor.getString(1));
	            // Adding contact to list
	            pathList.add(image);
	        } while (mCursor.moveToNext());
	    }
	    mCursor.close();

	    // return contact list
	    return pathList;
		}
	 
	// Getting contacts Count
	public synchronized  int getImagesCount() {
		 String selectQuery = "SELECT  * FROM " + PATHS_TABLE;
		 
	    	
		    mCursor = 	mDatabase.rawQuery(selectQuery, null);
		    int count = mCursor.getCount();
		    mCursor.close();

		    return count;
		}
	// Updating single contact
	public synchronized  int updateImage(ImagePath contact) {
		return 0;}
	 
	// Deleting single contact
	public synchronized void deleteImage(String path) {

		String hash = AeSimpleSHA1.SHA1(path);		
		LogWrapper.Logi(TAG, "Deleting image with name " + path + " and hash " + hash);	
    	mDatabase.delete(PATHS_TABLE, KEY_ID + " = ?", new String[]{hash});
  
	}
	

	public synchronized boolean isImagePathPresent(String path) {

	    // Select All Query
		String hash = AeSimpleSHA1.SHA1(path);
	    String selectQuery = "SELECT  * FROM " + PATHS_TABLE + " WHERE " + KEY_ID + " = \"" + hash +"\"";
	 
    	mDatabase = this.getWritableDatabase();
	    mCursor = 	mDatabase.rawQuery(selectQuery, null);
	    if(mCursor.getCount() > 0){
	    	mCursor.moveToFirst();
	    	//Log.i(TAG, "Entry is present for " + mCursor.getInt(0));
	    	mCursor.close();

	    	return true; // A key entry was found
	    	
	    }
		return false; // A key entry was not found
	}
	

	public boolean isOpen(){

		return (mDatabase != null ? mDatabase.isOpen() : false);

	}
    

}
