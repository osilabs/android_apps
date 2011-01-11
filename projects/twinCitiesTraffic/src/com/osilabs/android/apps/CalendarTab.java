package com.osilabs.android.apps;

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
		App.ivCalendarTab.setVisibility(View.VISIBLE);
		App.ivCalendarTab.setPadding(15, 0, 15, 0);
		App.ivCalendarTab.setAlpha(ALPHA_OFF);
		App.ivCalendarTab.setBackgroundColor(BG_OFF);
	}
	public static String getReloadURLParts() {
		if (CURRENT_INDEX > (Config.calendar.length - 1)) CURRENT_INDEX = 0;
		
		return "&calendar=" 
		+ Config.calendar_viewtypes[CURRENT_INDEX] 
		+ "|" 
		+ CURRENT_INDEX 
		+ "|" 
		+ CURRENT_TODAY_FEED_INDEX 
		+ "|" 
		+ CURRENT_WEATHER_FEED_INDEX;
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
