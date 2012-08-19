package org.linuxmotion.livewallpaper.activities;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.models.preferences.PreferenceListFragment.OnPreferenceAttachedListener;

import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.util.Log;

public class MainActivity extends BaseFragmentActivity implements OnPreferenceAttachedListener, OnPreferenceChangeListener, OnPreferenceClickListener{

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.d(this.getClass().getSimpleName(), "Seting the content view");
        setContentView(R.layout.layout_fragment_header);
  
        if (savedInstanceState == null) {
            // During initial setup, plug in the details fragment.
           // HeaderListFragment details = new HeaderListFragment();
           // details.setArguments(getIntent().getExtras());
           // getSupportFragmentManager().beginTransaction().add(
           //         android.R.id.content, details).commit();
        }
        
        if (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE) {
            // If the screen is now in landscape mode, we can show the
            // dialog in-line so we don't need this activity.
            
            return;
        }

        
    }
    
    @Override
    protected void upHomeButonPressed() {
 
    	
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


}
