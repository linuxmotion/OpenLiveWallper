package org.linuxmotion.livewallpaper.activities;

import org.linuxmotion.livewallpaper.activities.fragments.DetailedImageViewerFragment;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class DetailedImageViewerActivity  extends BaseFragmentActivity {

    private static final String TAG = DetailedImageViewerActivity.class.getSimpleName();

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     	   LogWrapper.Logi(TAG, "The detailed image viewer has been called");

           Fragment container = new DetailedImageViewerFragment();
           container.setArguments(savedInstanceState);
      	   FragmentManager fragmentManager = getSupportFragmentManager();
      	   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      	   fragmentTransaction.add(android.R.id.content, container);
      	   fragmentTransaction.commit();
	}

	@Override
	protected void upHomeButonPressed() {
		// TODO Auto-generated method stub
		
	}



}
