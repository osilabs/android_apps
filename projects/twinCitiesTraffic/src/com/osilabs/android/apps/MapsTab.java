package com.osilabs.android.apps;

import java.net.URLEncoder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
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
	private   static final int    MAP = 0;
	private   static final int    WEB = 1;

	// Indexes for URL stings
	private   static final int    TYPE    = 0;
	private   static final int    URI     = 1;
	private   static final int    WIDTH   = 2;
	private   static final int    HEIGHT  = 3;
	private   static final int    SCROLLX = 4;
	private   static final int    SCROLLY = 5;
	
	// If never set, is set to first map.
	public    static int    	  CURRENT_INDEX = 0;
	
	public static void init() {
		App.ivMapsTab.setPadding(15, 0, 15, 0);
		App.ivMapsTab.setAlpha(ALPHA_OFF);
		App.ivMapsTab.setBackgroundColor(BG_OFF);

	}
//	public static String getActiveMapURL() {
//		// Keep from exceeding the size of the array
//		if (CURRENT_INDEX > (Config.maps.length - 1)) CURRENT_INDEX = 0;
//		return Config.maps[CURRENT_INDEX];
//	}
	public static String getReloadURLParts() {
		if (CURRENT_INDEX > (Config.maps_urls.length - 1)) CURRENT_INDEX = 0;
		
		String query_string= "";
		String viewtype = MapsTab.getViewType();
		
		if (viewtype.equals("map")) {
			// If using a mapview, no map is used in the webview
			query_string = "&map=0&mapscrollx=0&mapscrolly=0";
		}
		else if (viewtype.equals("web")) {
			// Webveiew needs map passed in
			query_string 
				= "&map=" + URLEncoder.encode(Config.maps_urls[CURRENT_INDEX])
				+ "&mapscrollx="
				+ MapsTab.getScrollX()
				+ "&mapscrolly="
				+ MapsTab.getScrollY();
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
	public static String getViewType() {
		String[] as = Config.maps_urls[CURRENT_INDEX].split("~");
		return as[TYPE];
	}
	public static String getScrollX() {
		// FIXME - cache these so they are calc'd all the time
		String[] as = Config.maps_urls[CURRENT_INDEX].split("~");
		return as[SCROLLX];
	}
	public static String getScrollY() {
		String[] as = Config.maps_urls[CURRENT_INDEX].split("~");
		return as[SCROLLY];
	}
}
