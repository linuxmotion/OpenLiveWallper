package org.linuxmotion.livewallpaper.models;

import org.linuxmotion.livewallpaper.R;
import org.linuxmotion.livewallpaper.utils.LicenseChecker;
import org.linuxmotion.livewallpaper.utils.LogWrapper;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ProPreference extends Preference {
	
	boolean mIsLicensePresent =  LicenseChecker.checkLicense();

	public ProPreference(Context context) {
		this(context, null); 
	}
	
	public ProPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0); 
	}

	public ProPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		this.setWidgetLayoutResource(R.layout.pro_view_widget);
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
		
		View proView = view.findViewById(R.id.pro_view_widget);
		
		if(proView != null && proView instanceof ImageView){
		
			LogWrapper.Logi("ProPreference", "ImageView found");
			
			view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), 0, view.getPaddingBottom());
			proView.setBackgroundResource(R.drawable.ic_unpurchased);
		
		}
		
	}
	
	public boolean isLicenseFound(){
		return mIsLicensePresent;		
	} 
	

}
