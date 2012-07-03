package org.linuxmotion.livewallpaper.activities.settings;

import org.linuxmotion.livewallpaper.R;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

public class Settings extends PreferenceActivity implements OnPreferenceClickListener {
	
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
		this.addPreferencesFromResource(R.xml.settings);
		PreferenceScreen screen = this.getPreferenceScreen();
		mTimePref = screen.findPreference("time_between_switches");
		mTimePref.setOnPreferenceClickListener(this);
	}
	
	public void showTimePickerDialog() {
		
		Dialog dialog = new Dialog(this);

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
