package org.linuxmotion.livewallpaper.models;

import org.linuxmotion.livewallpaper.R;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;


public class HeaderListAdapter implements ListAdapter{

	String[] mHeaderNames = {"Standard users", "Pro users", "Settings"};
	String[] mHeaderSummary = {"Standard users", "Pro users", "General settings"};
	String[] mHeaderIntents = {"org.linuxmotion.livewallpaper.activities.WallpaperViewer"
			,"org.linuxmotion.livewallpaper.activities.BasicFileBrowser",
			"org.linuxmotion.livewallpaper.activities.settings.Settings"};
	private Context mContext;
	
	public HeaderListAdapter(Context context){
		mContext = context;
	}
	@Override
	public int getCount() {
		return mHeaderNames.length;
	}

	@Override
	public Object getItem(int position) {
		return mHeaderIntents[position];
	}

	@Override
	public long getItemId(int position) {
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

			root = inflater.inflate(R.layout.layout_row_list_header, parent, false);

		}
		
		
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