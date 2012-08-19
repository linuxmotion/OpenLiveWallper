package org.linuxmotion.livewallpaper.models.listeners;

import java.io.File;
import java.lang.ref.WeakReference;

import org.linuxmotion.livewallpaper.activities.DetailedImageViewerActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

public class ImageClickListener extends ListItemClickListener {

	WeakReference <Context> mContext;
	
	public ImageClickListener(Context ct, String pathandname) {
		super(pathandname);
		mContext = new WeakReference<Context>(ct);
	}

	@Override
	public void onClick(View v) {
		
		// Launch new activity to show image in a large view
		
		Intent sharingIntent = new Intent(mContext.get(), DetailedImageViewerActivity.class);
		sharingIntent.setAction(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("image/jpeg");
		sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.fromFile(new File(mImage)));
		mContext.get().startActivity(sharingIntent);
		
		
	}

}
