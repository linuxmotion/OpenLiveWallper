package org.linuxmotion.livewallpaper.activities;

import org.linuxmotion.livewallpaper.R;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public abstract class BaseFragmentActivity extends SherlockFragmentActivity {

	FragmentManager mFragmentManager = getSupportFragmentManager();

	
	
	public void actionBarUpEnabled(){
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		
	}

	public void actionBarUpDisabled(){
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);

		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getSupportMenuInflater();
	   
	    //inflater.inflate(R.menu.base_action_bar, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            // app icon in action bar clicked; go home
	            upHomeButonPressed();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	abstract protected void upHomeButonPressed();
}
