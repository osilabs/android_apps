package com.osilabs.android.apps.seattletraffic;

import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

public class MapsTab {
	// FIXME - these are duplicated in the main class
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFFFF;
	public    static final int    TAB_ACTIVE_COLOR = 0xFF00FF00;

	// If never set, is set to first map.
	public    static int    	  CURRENT_INDEX = 0;
	
	public static String getActiveMapURL() {
		return Config.maps[CURRENT_INDEX];
	}
	public static String getReloadURLParts() {
		return	"&map=" + Config.maps_urls[CURRENT_INDEX] +
				"&scrolldown=" +
				"&scrollover=";
	}
	public static void setActive(ImageView ivTraffic) {
		ivTraffic.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		ivTraffic.setAlpha(ALPHA_ON);	
	}
	public static void setInactive(ImageView ivMaps) {
		ivMaps.setColorFilter(null); 
		ivMaps.setAlpha(ALPHA_OFF);
	}
	public static void hideConfiguration(TextView tvMapsPop) {
		tvMapsPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration(TextView tvMapsPop) {
		tvMapsPop.setVisibility(TextView.VISIBLE);
	}
	

}
