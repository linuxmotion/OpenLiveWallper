package org.linuxmotion.livewallpaper.utils;

import android.util.Log;

public class LicenseChecker {

	private static String TAG = LicenseChecker.class.getSimpleName();
	
	public static boolean checkLicense() {
		
		if(Constants.DEBUG_LICENSE)return true;
		else{
			try{
				Class<?> liscence = Class.forName("org.linuxmotion.slidepapper.liscence");
			}
			catch(ClassNotFoundException error){
				// There is no license present
				Log.d(TAG, "License not found");
				return false;
				
			}
			return true;
		}	
		
	}
}
