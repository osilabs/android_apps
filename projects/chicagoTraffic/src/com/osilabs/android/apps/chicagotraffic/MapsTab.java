package com.osilabs.android.apps.chicagotraffic;

import java.net.URLEncoder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MapsTab {
	// FIXME - these are duplicated in the main class
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF88;
	public    static final int    TAB_ACTIVE_COLOR = 0xFFAACCAA; // FIXME pull this from color.xml
	public    static final int    BG_OFF = 0xFF333333;
	public    static final int    BG_ON  = Color.BLACK;
	
	// CONSTS
//	private   static final int    MAP = 0;
//	private   static final int    WEB = 1;

	// Indexes for URL stings
	private   static final int    TYPE    = 0;
	private   static final int    URI     = 1;
	private   static final int    WIDTH   = 2;
	private   static final int    HEIGHT  = 3;
	private   static final int    SCROLLX = 4;
	private   static final int    SCROLLY = 5;
	
	// If never set, is set to first map.
	public    static int    	  CURRENT_INDEX = 0;
	
	// Init will set this to the number of system views. Will be used
	//  to determine if a view is above this number and is therefore
	//  a favorite of a mapview.
	public    static int    	  NUMBER_OF_SYSTEM_VIEWS = 0;
	
	public static void init() {
		App.ivMapsTab.setVisibility(View.VISIBLE);
		App.ivMapsTab.setPadding(15, 0, 15, 0);
		App.ivMapsTab.setAlpha(ALPHA_OFF);
		App.ivMapsTab.setBackgroundColor(BG_OFF);
		
		// Set the number of system views this build is using. Any views
		//  accessed above this number is out of the defined list
		//  and is a user favorite of a mapview.
		NUMBER_OF_SYSTEM_VIEWS = Config.traffic_viewtypes.length;
	}
//	public static String getActiveMapURL() {
//		// Keep from exceeding the size of the array
//		if (CURRENT_INDEX > (Config.maps.length - 1)) CURRENT_INDEX = 0;
//		return Config.maps[CURRENT_INDEX];
//	}
	public static String getReloadURLParts() {
		if (CURRENT_INDEX > (Config.traffic_urls.length - 1)) CURRENT_INDEX = 0;
		
		String query_string= "";
		
		if (MapsTab.getAndroidViewType() == Config.IMAGE) {
			query_string 
				= "&traffic=" + URLEncoder.encode(Config.traffic_urls[CURRENT_INDEX])
				+ "&mapscrollx="
				+ MapsTab.getScrollX()
				+ "&mapscrolly="
				+ MapsTab.getScrollY();
		} else if (MapsTab.getAndroidViewType() == Config.FEED) {
			query_string 
				= "&traffic=" + URLEncoder.encode(Config.traffic_urls[CURRENT_INDEX]);
		} else {
			query_string = "&traffic=";			
		}

		return	query_string;
	}
	public static void setActive() {
		App.ivMapsTab.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		App.ivMapsTab.setAlpha(ALPHA_ON);
		App.ivMapsTab.setBackgroundColor(BG_ON);
	}
	public static void setInactive() {
		App.ivMapsTab.setColorFilter(null); 
		App.ivMapsTab.setAlpha(ALPHA_OFF);
		App.ivMapsTab.setBackgroundColor(BG_OFF);
	}
	public static void hideConfiguration() {
		App.ivMapsMore.setVisibility(ImageView.GONE);
		App.tvMapsPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration() {
		App.ivMapsMore.setVisibility(ImageView.VISIBLE);
		App.tvMapsPop.setVisibility(TextView.VISIBLE);
	}
	public static String getWebViewType() {
		String[] as = Config.traffic_urls[CURRENT_INDEX].split("~");
		return as[TYPE];
	}
	public static int getAndroidViewType() {
		if (MapsTab.CURRENT_INDEX > (NUMBER_OF_SYSTEM_VIEWS-1)) {
			// Viewing a map favorite
			return Config.FAVORITE;
		}
		else {
			// Look for the system type defined in Config.traffic_viewtypes
			return Config.traffic_viewtypes[ MapsTab.CURRENT_INDEX ];
		}
	}
	public static String getScrollX() {
		// FIXME - cache these so they are calc'd all the time
		String[] as = Config.traffic_urls[CURRENT_INDEX].split("~");
		return as[SCROLLX];
	}
	public static String getScrollY() {
		String[] as = Config.traffic_urls[CURRENT_INDEX].split("~");
		return as[SCROLLY];
	}
}
