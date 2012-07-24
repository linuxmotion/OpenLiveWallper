package org.linuxmotion.livewallpaper.activities.fragments;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.activities.BasicFileBrowser;
import org.linuxmotion.livewallpaper.activities.WallpaperViewer;
import org.linuxmotion.livewallpaper.activities.settings.Settings;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class HeaderListFragment extends ListFragment implements OnItemClickListener {
	
	private boolean mUseCustomHeaders = true;
	   boolean mDualPane;
	    int mCurCheckPosition = 0;
	    

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		//mUseCustomHeaders = false;
		if(mUseCustomHeaders){
			
			LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.header_preference_plash, null);
			ImageView image = (ImageView) layout.findViewById(R.id.HeaderView);
	        image.setBackgroundResource(R.drawable.image_loading_bg);
			return layout;
			
		}else{
			
			View layout = super.onCreateView(inflater, container, savedInstanceState);
			return layout;
			
		}
		
		
		
		
		
	}
	
	@Override
	public void onActivityCreated (Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		this.getListView().setAdapter(new SimpleListAdapter(getActivity().getBaseContext()));
		this.getListView().setOnItemClickListener(this);
		
		// Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;
        
        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }
		
	}

	private static final int WALLPAPER_VEIWER = 0;
	private static final int CUSTOM_PAPERS =  WALLPAPER_VEIWER + 1;
	private static final int SETTINGS = CUSTOM_PAPERS + 1;
	/**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int index) {
        mCurCheckPosition = index;

        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(index, true);

            switch(index){
    		
	    		case WALLPAPER_VEIWER:{
	    			   // Check what fragment is currently shown, replace if needed.
	    	           WallpaperViewerFragment details = (WallpaperViewerFragment)
	    	                    getFragmentManager().findFragmentById(R.id.details);
	    	            if (details == null || details.getShownIndex() != index) {
	    	                // Make new fragment to show this selection.
	    	                details =  WallpaperViewerFragment.newInstance(index);
	
	    	                // Execute a transaction, replacing any existing fragment
	    	                // with this one inside the frame.
	    	                FragmentTransaction ft = getFragmentManager().beginTransaction();
	    	                ft.replace(R.id.details, details);
	    	                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    	                ft.commit();
	    	            }
	    		}break;
	    		
	    		case CUSTOM_PAPERS:{
	    			
	    		}break;
	    		
	    		case SETTINGS:{
	    			
	    		}break;
    		
            }
         

        } else {
        	
        	 switch(index){
     		
	    		case WALLPAPER_VEIWER:{
	    			   // Check what fragment is currently shown, replace if needed.
	    			// Otherwise we need to launch a new activity to display
	                // the dialog fragment with selected text.
	                Intent intent = new Intent();
	                intent.setClass(getActivity(), WallpaperViewer.class);
	                intent.putExtra("index", index);
	                startActivity(intent);
	    	            
	    		}break;
	    		
	    		case CUSTOM_PAPERS:{
	    			// Otherwise we need to launch a new activity to display
	                // the dialog fragment with selected text.
	                Intent intent = new Intent();
	                intent.setClass(getActivity(), BasicFileBrowser.class);
	                intent.putExtra("index", index);
	                startActivity(intent);
	    		}break;
	    		
	    		case SETTINGS:{
	    			// Otherwise we need to launch a new activity to display
	                // the dialog fragment with selected text.
	                Intent intent = new Intent();
	                intent.setClass(getActivity(), Settings.class);
	                intent.putExtra("index", index);
	                startActivity(intent);
	    		}break;
 		
         }
            
        }
    }
	
	class SimpleListAdapter implements ListAdapter{

		String[] mHeaderNames = {"One", "Two", "Three"};
		String[] mHeaderSummary = {"1", "2", "3"};
		String[] mHeaderIntents = {"org.linuxmotion.livewallpaper.activities.WallpaperViewer"
				,"org.linuxmotion.livewallpaper.activities.BasicFileBrowser",
				"org.linuxmotion.livewallpaper.activities.settings.Settings"};
		private Context mContext;
		
		public SimpleListAdapter(Context context){
			mContext = context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mHeaderNames.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mHeaderIntents[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			View root = convertView;
			if(root == null){
				
				 LayoutInflater inflater = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));

				root = inflater.inflate(R.layout.list_row_layout, parent, false);

			}
			root.setClickable(true);
			
			
			
			TextView title = (TextView) root.findViewById(R.id.title);
			title.setText(this.mHeaderNames[position]);
			
			TextView summary = (TextView) root.findViewById(R.id.summary);
			summary.setText(this.mHeaderSummary[position]);
			// TODO Auto-generated method stub
			return root;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return mHeaderNames.length;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEmpty() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void unregisterDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return true;
		}
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		fragmentTransaction.replace(R.id.details, new WallpaperViewerFragment());
		fragmentTransaction.commit();
		
	}
	
}
