package org.linuxmotion.livewallpaper.database;

public class ImagePath {
	
	private String mMyImagePath;

	ImagePath(){
		
	}

	ImagePath(String path){
		
		mMyImagePath = path;
	}


	/**
	 * @return the mMyImagePath
	 */
	public String getImagePath() {
		return mMyImagePath;
	}
	
	/**
	 * 
	 * @param path The path to the image
	 * @return True if the path was set, false otherwise
	 */
	public boolean setImagePath(String path) {
		if(path != null){
			mMyImagePath = path;
			return true;
		}
		return false;
	}
}
