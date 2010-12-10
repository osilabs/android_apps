package com.osilabs.android.apps;

import java.net.URLEncoder;

import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class MapsTab {
	// FIXME - these are duplicated in the main class
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF88;
	public    static final int    TAB_ACTIVE_COLOR = 0xFF00FF00; // FIXME pull this from color.xml

	// If never set, is set to first map.
	public    static int    	  CURRENT_INDEX = 0;
	
	public static String getActiveMapURL() {
		// Keep from exceeding the size of the array
		if (CURRENT_INDEX > (Config.maps.length - 1)) CURRENT_INDEX = 0;
		return Config.maps[CURRENT_INDEX];
	}
	public static String getReloadURLParts() {
		if (CURRENT_INDEX > (Config.maps_urls.length - 1)) CURRENT_INDEX = 0;
		return	"&map=" + URLEncoder.encode(Config.maps_urls[CURRENT_INDEX])
						+ "&mapscrollx="
						+ MapsTab.getScrollX()
						+ "&mapscrolly="
						+ MapsTab.getScrollY();
	}
	public static void setActive(ImageView ivTraffic) {
		ivTraffic.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		ivTraffic.setAlpha(ALPHA_ON);	
	}
	public static void setInactive(ImageView ivMaps) {
		ivMaps.setColorFilter(null); 
		ivMaps.setAlpha(ALPHA_OFF);
	}
	public static void hideConfiguration(ImageView ivMapIcon, TextView tvMapsPop) {
		ivMapIcon.setVisibility(ImageView.GONE);
		tvMapsPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration(ImageView ivMapIcon, TextView tvMapsPop) {
		ivMapIcon.setVisibility(ImageView.VISIBLE);
		tvMapsPop.setVisibility(TextView.VISIBLE);
	}
	public static String getScrollX() {
		// FIXME - cache these so they are calc'd all the time
		String[] as = Config.maps_urls[CURRENT_INDEX].split("~");
		return as[3];
	}
	public static String getScrollY() {
		String[] as = Config.maps_urls[CURRENT_INDEX].split("~");
		return as[4];
	}

}
