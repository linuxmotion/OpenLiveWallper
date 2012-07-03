package org.linuxmotion.livewallpaper.models.preferences;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TimePickerPreference extends Preference {
	
	private int mHour;
	private int mMinute;
	private int mSeconds;
	
	TextView mTime;

	public TimePickerPreference(Context context) {
		this(context, null);
	}
	public TimePickerPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public TimePickerPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setWidgetLayoutResource(R.layout.time_picked_text);

	}



	 /**
    * Binds the created View to the data for this Preference.
    * <p>
    * This is a good place to grab references to custom Views in the layout and
    * set properties on them.
    * <p>
    * Make sure to call through to the superclass's implementation.
    * 
    * @param view The View that shows this Preference.
    * @see #onCreateView(ViewGroup)
    */
	@Override
   protected void onBindView(View view){
		super.onBindView(view);
		
		View View = view.findViewById(R.id.timePickedText);
		
		if(View != null && View instanceof TextView){
		
			LogWrapper.Logv("ProPreference", "TextView found");
			
			mTime = (TextView) View;
			int hour = this.getSharedPreferences().getInt("HOUR", 0);
			int min = this.getSharedPreferences().getInt("MINUTE", 0);	
			int secs= this.getSharedPreferences().getInt("SECONDS", 10);	
			setTime(hour, min, secs);
		
		}
		
	}
	
	public int[] getTime(){
		return new int[]{mHour, mMinute, mSeconds};
		
	}
	public void setTime(final int hour, final int min, final int secs){
		
		mHour = hour;
		mMinute = min;
		mSeconds = secs;
		mTime.setText(mHour + ":"+ mMinute + ":"+ mSeconds);
		
	}
	
}
