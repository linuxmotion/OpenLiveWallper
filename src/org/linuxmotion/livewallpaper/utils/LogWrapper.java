package org.linuxmotion.livewallpaper.utils;

import android.util.Log;

public class LogWrapper {

	
	public static void Logd(String tag, String msg){
		
		if(Constants.DEBUG)Log.d(tag, msg);
	}
	
	public static void Logv(String tag, String msg){
		
		if(Constants.VERBOSE)Log.v(tag, msg);

	}

	public static void Logi(String tag, String msg) {
		
		if(Constants.INFORMATIONAL)Log.v(tag, msg);
		
	}
}
