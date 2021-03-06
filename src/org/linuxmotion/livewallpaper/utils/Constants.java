package org.linuxmotion.livewallpaper.utils;

import org.linuxmotion.livewallpaper.R;

public class Constants {
	
	/**
	 * Debug the license portion without a license.<br/>
	 * This Value should be set to false for a production
	 * build 
	 */
	public static final boolean DEBUG_LICENSE = true;
	
	/**
	 * Turn on/off LogWrappers debug log statements
	 */
	public static final boolean DEBUG = false;
	/**
	 * Turn on/off LogWrappers debug log statements
	 */
	public static final boolean DEBUG_DATABASE = false;
	/**
	 * Turn on/off LogWrappers Error log statements
	 */
	public static final boolean ERRORS = true;
	/**
	 * Turn on/off LogWrappers verbose log statements
	 */
	public static final boolean VERBOSE = false;
	
	/**
	 * Turn on/off LogWrappers warning log statements
	 */
	public static final boolean WARNINGS = true;
	/**
	 * Turn on/off LogWrappers informational log statements
	 */
	public static final boolean INFORMATIONAL = true;

	public static final String DEFAULT_IMAGE_SELECTION = "DEFAULT_IMAGE_SELECTION";


	public static final int WALLPAPER_VEIWER = 0;
	public static final int CUSTOM_PAPERS =  WALLPAPER_VEIWER + 1;
	public static final int SETTINGS = CUSTOM_PAPERS + 1;

	
/*Draw constants, Also maped to the switch types
 * 
 */
	/**
	 * Picture is switched with no affects
	 */
	public static final int PLAIN_DRAW = 0;

	/**
	 * Picture is drawn using the zoom in/out mode
	 */
	public static final int ZOOM_DRAW = 1;

	/**
	 * Picture is drawn using a bounce affect
	 */
	public static final int BOUNCE_DRAW = 2;
	
	/**
	 * Picture is drawn while doing flips
	 */
	public static final int FLIP_DRAW = 3;

	/**
	 * Picture is drawn using a sliding motion
	 */
	public static final int SLIDE_DRAW = 4;

	/**
	 * 
	 * The Draw type constant used for storing the shared preference
	 */
	public static final String DRAW_TYPE = "DRAW_TYPE";

	/**
	 * How the Picture should switch. See 
	 * {@link #PLAIN_SWITCH}, 
	 * {@link #BOUNCE_SWITCH}, 
	 * {@link #FLIP_SWITCH}, 
	 * {@link #SLIDE_SWITCH}.  
	 * 
	 */
	public static final String SWITCH_TYPE = "SWITCH_TYPE";
	
	/**
	 * Picture is switched with no affects
	 */
	public static final int PLAIN_SWITCH = PLAIN_DRAW;
	
	/**
	 * Picture is switched with a slide affect
	 */
	public static final int BOUNCE_SWITCH = BOUNCE_DRAW;

	/**
	 * Picture is switched with a zoom affect
	 */
	public static final int ZOOM_SWITCH = ZOOM_DRAW;

	/**
	 * Picture is switched with a slide affect
	 */
	public static final int SLIDE_SWITCH = SLIDE_DRAW;
	
	/**
	 * Picture is switched with a slide affect
	 */
	public static final int FLIP_SWITCH = FLIP_DRAW;

	public static final String DETAILED_IMAGE_VIEWER = "detailViwer";


	
	
	/**
	 * 
	 * 
	 */
	public static String SHARED_PREFS = "LIVE_WALLPAPER_PREFS";
	
	public static String SINGLE_FILE_PATH = "";
	
	public static int[] DefaultPictures = {R.drawable.agave, R.drawable.clouds1, R.drawable.clouds2,
		R.drawable.coals, R.drawable.hills, R.drawable.snow, R.drawable.stream};
}
