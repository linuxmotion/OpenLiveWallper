package org.linuxmotion.livewallpaper.activities.fragments;

import org.linuxmotion.livewallpaper.R;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

public class SettingsFragment extends ListFragment implements OnPreferenceClickListener  {
	
	Preference mTimePref;
	private int hour;
	private int minute;
	private TimePicker timePicker1;
	
	private TimePickerDialog.OnTimeSetListener timePickerListener = 
        new TimePickerDialog.OnTimeSetListener() {
	public void onTimeSet(TimePicker view, int selectedHour,
			int selectedMinute) {
		hour = selectedHour;
		minute = selectedMinute;

		// set current time into textview
	
		// set current time into timepicker
		timePicker1.setCurrentHour(hour);
		timePicker1.setCurrentMinute(minute);


	}
};

	



	@Override 
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//this.addPreferencesFromResource(R.xml.settings);
		//PreferenceScreen screen = this.getPreferenceScreen();
		//mTimePref = screen.findPreference("time_between_switches");
		//mTimePref.setOnPreferenceClickListener(this);
	}
	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
		  View rawListView = inflater.inflate(R.layout.layout_list_content, container, false);
		  ListView list = (ListView)rawListView;
		 // list.setAdapter(settingsListAdapter());
		//this.addPreferencesFromResource(R.xml.settings);
		//PreferenceScreen screen = this.getPreferenceScreen();
		//mTimePref = screen.findPreference("time_between_switches");
		mTimePref.setOnPreferenceClickListener(this);
		
		return container;
	}
	
	class settingsListAdapter implements ListAdapter {
		
		settingsListAdapter(){
			
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 0;
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
			return false;
		}	
	}
	

	public void showTimePickerDialog() {
		
		Dialog dialog = new Dialog(this.getActivity());

		dialog.setContentView(R.xml.time_picker_dialog);
		dialog.setTitle("Custom Dialog");

		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText("Hello, this is a custom dialog!");
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.ic_launcher);
		dialog.show();
		
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		if(pref.equals(mTimePref)){
			Log.d("Settings", "Time picker clicked");
			showTimePickerDialog();
			
		}
		return false;
	}

}
