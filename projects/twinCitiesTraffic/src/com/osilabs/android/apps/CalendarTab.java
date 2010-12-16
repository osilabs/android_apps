package com.osilabs.android.apps;

import java.net.URLEncoder;

import android.R.color;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarTab {
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF55;
	public    static final int    TAB_ACTIVE_COLOR = 0xFFAACCAA;
	public    static final int    BG_OFF = 0xFF333333;
	public    static final int    BG_ON  = Color.BLACK;

	// If never set, is set to first map.
	public    static       int    CURRENT_INDEX = 0;
	public    static       int    CURRENT_TODAY_FEED_INDEX = 0;
	public    static       int    CURRENT_WEATHER_FEED_INDEX = 0;
	
	public static void init() {
		App.ivCalendarTab.setPadding(15, 0, 15, 0);
		App.ivCalendarTab.setAlpha(ALPHA_OFF);
		App.ivCalendarTab.setBackgroundColor(BG_OFF);
	}
	public static String getReloadURLParts() {
		if (CURRENT_INDEX > (Config.calendar_src.length - 1)) CURRENT_INDEX = 0;
		
		String uri = "";
		
		// Trap special configurable items
		if (Config.calendar_src[CURRENT_INDEX].equals("{TODAY}")) {
			// Set configured TODAY feed
			uri = Config.today_values[ CURRENT_TODAY_FEED_INDEX ];
		} else if (Config.calendar_src[CURRENT_INDEX].equals("{WEATHER}")) {
			// Set configured WEATHER feed
			uri = Config.weather_values[ CURRENT_WEATHER_FEED_INDEX ];
		} else {
			// Set the feed from alerts_src
			uri = Config.calendar_src[CURRENT_INDEX];
		}

		return	"&calendar=" + URLEncoder.encode(uri);
	}
	public static void setActive() {
		App.ivCalendarTab.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		App.ivCalendarTab.setAlpha(ALPHA_ON);
		App.ivCalendarTab.setBackgroundColor(BG_ON);
	}
	public static void setInactive() {
		App.ivCalendarTab.setColorFilter(null); 
		App.ivCalendarTab.setAlpha(ALPHA_OFF);
		App.ivCalendarTab.setBackgroundColor(BG_OFF);
	}
	public static void hideConfiguration() {
		App.ivCalendarMore.setVisibility(ImageView.GONE);
		App.tvCalendarPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration() {
		App.ivCalendarMore.setVisibility(ImageView.VISIBLE);
		App.tvCalendarPop.setVisibility(TextView.VISIBLE);
	}
}
