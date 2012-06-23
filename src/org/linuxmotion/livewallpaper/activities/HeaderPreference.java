package org.linuxmotion.livewallpaper.activities;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.models.ProPreference;

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
	        	 intent.setClassName(getApplicationContext(), "org.linuxmotion.livewallpaper.activities.WallpaperViewer");
	        	 mStandardUsers.setIntent(intent);
	         }
	         {
	        	 if(mProUsers.isLicenseFound()){
	        		 mProUsers.setEnabled(true);
	        		 intent = new Intent();
	        		 intent.setClassName(getApplicationContext(), "org.linuxmotion.livewallpaper.activities.BasicFileBrowser");
	        		 mProUsers.setIntent(intent);
	        	 }else{

	        		 mProUsers.setEnabled(false);
	        	 }
	        	 
	         }
	         {
	        	 intent = new Intent();
	        	 intent.setClassName(getApplicationContext(), "org.linuxmotion.livewallpaper.activities.settings.Settings");
	        	 mAllUsers.setIntent(intent);
	         }
	         
	     
	       
	    }

}
