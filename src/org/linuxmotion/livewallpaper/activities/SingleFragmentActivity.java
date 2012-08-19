package org.linuxmotion.livewallpaper.activities;

import org.linuxmotion.livewallpaper.activities.fragments.DetailsFragmentManager;
import org.linuxmotion.livewallpaper.activities.fragments.HeaderListFragment;
import org.linuxmotion.livewallpaper.models.preferences.PreferenceListFragment.OnPreferenceAttachedListener;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class SingleFragmentActivity extends BaseFragmentActivity implements OnPreferenceAttachedListener, OnPreferenceChangeListener, OnPreferenceClickListener {

	

    private static final String TAG = SingleFragmentActivity.class.getSimpleName();

	@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Intent t = getIntent();
        Bundle b = t.getExtras();
    
        	 int index = b.getInt("index");
        	 LogWrapper.Logi(TAG, "The index is " + index);

        	 Fragment container = DetailsFragmentManager.newInstance(index); 
        	 FragmentManager fragmentManager = getSupportFragmentManager();
        	 FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        	 fragmentTransaction.add(android.R.id.content, container);
        	 fragmentTransaction.commit();
        	 
        	 if(container instanceof HeaderListFragment){
        		 this.actionBarUpDisabled();
        	 }else{
        		 // We are in a single pain sub menu
        		 // Enable the up action
        		 this.actionBarUpEnabled();
        	 }
        
       
        
    }    

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPreferenceAttached(PreferenceScreen root, int xmlId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void upHomeButonPressed() {
		// TODO Auto-generated method stub
	   	finish();
	}

}