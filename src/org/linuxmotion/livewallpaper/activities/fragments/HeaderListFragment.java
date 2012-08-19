package org.linuxmotion.livewallpaper.activities.fragments;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.activities.BaseFragmentActivity;
import org.linuxmotion.livewallpaper.activities.SingleFragmentActivity;
import org.linuxmotion.livewallpaper.models.HeaderListAdapter;
import org.linuxmotion.livewallpaper.models.preferences.PreferenceListFragment;
import org.linuxmotion.livewallpaper.utils.Constants;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

public class HeaderListFragment extends SherlockListFragment implements OnItemClickListener {
	
	private static final String TAG = HeaderListFragment.class.getSimpleName();
	private boolean mUseCustomHeaders = true;
	boolean mDualPane;
	int mCurCheckPosition = 0;
	DetailsFragmentManager mDetailsFragmentManager = new DetailsFragmentManager();
	    

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		LogWrapper.Logi(this, "onCreate called");
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		//mUseCustomHeaders = false;

		LogWrapper.Logi(this, "Creating layout");
		if(mUseCustomHeaders){
			
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.header_preference_plash, null);
			ListView lv = (ListView)layout.findViewById(android.R.id.list);
			lv.setEnabled(true);
			lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			TextView text = (TextView) layout.findViewById(R.id.HeaderText);
			Drawable d = getResources().getDrawable( R.drawable.image_loading_bg );
			
			d.setBounds(0, 0, layout.getWidth(), layout.getWidth());
			//text.setCompoundDrawables(null, d, null, null);
			text.setCompoundDrawablesWithIntrinsicBounds(null, d, null, null);
	        //image.setBackgroundResource(R.drawable.image_loading_bg);
			return layout;
			
		}else{
			
			View layout = super.onCreateView(inflater, container, savedInstanceState);
			return layout;
			
		}
		
		
		
		
		
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);

		LogWrapper.Logi(this, "Activity created");
		
		this.getListView().setAdapter(new HeaderListAdapter(getActivity().getBaseContext()));
		this.getListView().setClickable(true);
		this.getListView().setEnabled(true);
		this.getListView().setOnItemClickListener(this);
		
		// Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
		
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        
        //getView().findViewById(android.R.id.list);
        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
        	((ListView)getView().findViewById(android.R.id.list)).setChoiceMode(ListView.CHOICE_MODE_SINGLE);
           ((BaseFragmentActivity)this.getActivity()).actionBarUpDisabled();
        	// Make sure our UI is in the correct state.
           showDetails(mCurCheckPosition);
        }else{
        	((ListView)getView().findViewById(android.R.id.list)).setChoiceMode(ListView.CHOICE_MODE_NONE);
        }
		
	}

	/**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;
        
        LogWrapper.Logd(TAG, "Resolving details pane");

		
        if (mDualPane) {
        	Fragment details = getFragmentManager().findFragmentById(R.id.details);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
        	LogWrapper.Logd(TAG, "Using dual pane UI");
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            switch(index){
    		
	    		case Constants.WALLPAPER_VEIWER:{
	            	
	    	          if (!(details instanceof WallpaperViewerFragment)) {
	    	        	  LogWrapper.Logd(TAG, "Setting standard fragment for UI");
	    	                // Make new fragment to show this selection.
	    	                details =  DetailsFragmentManager.newInstance(index);
	    	                // Execute a transaction, replacing any existing fragment
	    	                // with this one inside the frame.
	    	                ft.replace(R.id.details, details);
	    	                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    	                ft.commit();
	    	            }
	    		}break;
	    		
	    		case Constants.CUSTOM_PAPERS:{
	            	
    	            if (!(details instanceof BasicFileBrowserFragment)) {
    	            	LogWrapper.Logd(TAG, "Setting pro user fragment for UI");
    	                // Make new fragment to show this selection.
    	                details =   DetailsFragmentManager.newInstance(index);
    	                // Execute a transaction, replacing any existing fragment
    	                // with this one inside the frame.
    	                ft.replace(R.id.details, details);
    	                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    	                ft.commit();
    	            }
	    			
	    		}break;
	    		
	    		case Constants.SETTINGS:{
	            	
    	            if (!(details instanceof PreferenceListFragment)) {
    	                LogWrapper.Logd(TAG, "Setting settings fragment for UI");
    	                // Make new fragment to show this selection.
    	                details =  DetailsFragmentManager.newInstance(index);
    	                // Execute a transaction, replacing any existing fragment
    	                // with this one inside the frame.
    	                ft.replace(R.id.details, details);
    	                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    	                ft.commit();
    	            }
	    		}break;
    		
            }
          
           

        } else {
        	LogWrapper.Logd(TAG, "Using single pane UI");
        	
        	Intent intent = new Intent();
        	intent.putExtra("index", index);
	        intent.setClass(getActivity(), SingleFragmentActivity.class);                
	        startActivity(intent);

	                
 		
         }
            
        
    }
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		LogWrapper.Logi(TAG, "Position " + arg2  + " clicked");
		showDetails(arg2);
		
	}


}
