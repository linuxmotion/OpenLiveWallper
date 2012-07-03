package org.linuxmotion.livewallpaper.activities;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.models.preferences.ProPreference;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.ImageView;

public class HeaderPreference extends PreferenceActivity {//implements OnPreferenceClickListener{
	
	private ImageView mImage;
	private PreferenceScreen mStandardUsers;
	private ProPreference mProUsers;
	private PreferenceScreen mAllUsers;
	private static final String PACKAGES_ACTIVITIES = "org.linuxmotion.livewallpaper.activities";

	private static final String SETTINGS = ".settings.Settings";
	private static final String IMAGEBROWSER = ".BasicFileBrowser";
	private static final String PAPERVIEWER = ".WallpaperViewer";
	
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.preference_list_content);
	        
	         mImage = (ImageView) this.findViewById(R.id.HeaderView);
	         mImage.setBackgroundResource(R.drawable.image_loading_bg);
	         
	         this.addPreferencesFromResource(R.xml.settings_preferences);
	         
	         PreferenceScreen screen = this.getPreferenceScreen();
	         mStandardUsers = (PreferenceScreen) screen.findPreference("standard_users");
	         mProUsers = (ProPreference) screen.findPreference("pro_users");
	         mAllUsers = (PreferenceScreen) screen.findPreference("all_users");

	         Intent intent;
	         {
	        	 intent = new Intent();
	        	 intent.setClassName(getApplicationContext(),PACKAGES_ACTIVITIES + PAPERVIEWER);
	        	 mStandardUsers.setIntent(intent);
	         }
	         {
	        	 if(mProUsers.isLicenseFound()){
	        		 mProUsers.setEnabled(true);
	        		 intent = new Intent();
	        		 intent.setClassName(getApplicationContext(), PACKAGES_ACTIVITIES + IMAGEBROWSER);
	        		 mProUsers.setIntent(intent);
	        	 }else{

	        		 mProUsers.setEnabled(false);
	        	 }
	        	 
	         }
	         {
	        	 intent = new Intent();
	        	 intent.setClassName(getApplicationContext(), PACKAGES_ACTIVITIES + SETTINGS);
	        	 mAllUsers.setIntent(intent);
	         }
	         
	     
	       
	    }

}
