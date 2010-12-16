package com.osilabs.android.apps;

import java.net.URLEncoder;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertsTab {
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF55;
	public    static final int    TAB_ACTIVE_COLOR = 0xFF00FF00;

	// If never set, is set to first map.
	public    static       int    CURRENT_INDEX = 0;
	public    static       int    CURRENT_TODAY_FEED_INDEX = 0;
	public    static       int    CURRENT_WEATHER_FEED_INDEX = 0;
	
	public static String getReloadURLParts() {
//		if (CURRENT_INDEX > (Config.alerts_src.length - 1)) CURRENT_INDEX = 0;
		
		// Get Saved feed sources
    	//mySharedPreferences.getInt
    	//= getSharedPreferences(Config.NAMESPACE + "_preferences", 0);
		//String wr_saved = getContext().getResources().getString(R.string.pref_weather_radios_selected);
		
		String uri = "";
//		
//		// Trap special configurable items
//		if (Config.alerts_src[CURRENT_INDEX].equals("{TODAY}")) {
//			// Set configured TODAY feed
//			uri = Config.today_values[ CURRENT_TODAY_FEED_INDEX ];
//		} else if (Config.alerts_src[CURRENT_INDEX].equals("{WEATHER}")) {
//			// Set configured WEATHER feed
//			uri = Config.weather_values[ CURRENT_WEATHER_FEED_INDEX ];
//		} else {
//			// Set the feed from alerts_src
//			uri = Config.alerts_src[CURRENT_INDEX];
//		}

		return	"&calendar=" + URLEncoder.encode(uri);
	}
	public static void setActive(ImageView ivAlerts) {
		ivAlerts.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		ivAlerts.setAlpha(ALPHA_ON);	
	}
	public static void setInactive(ImageView ivAlerts) {
		ivAlerts.setColorFilter(null); 
		ivAlerts.setAlpha(ALPHA_OFF);
	}
	public static void hideConfiguration(ImageView ivAlertMore, TextView tvAlertsPop) {
		ivAlertMore.setVisibility(ImageView.GONE);
		tvAlertsPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration(ImageView ivAlertMore, TextView tvAlertsPop) {
		ivAlertMore.setVisibility(ImageView.VISIBLE);
		tvAlertsPop.setVisibility(TextView.VISIBLE);
	}
}
