package org.linuxmotion.livewallpaper.activities;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.activities.fragments.HeaderListFragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity  extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_layout);
        
        //getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        
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


}
