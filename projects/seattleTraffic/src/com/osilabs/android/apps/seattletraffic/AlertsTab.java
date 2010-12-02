package com.osilabs.android.apps.seattletraffic;

import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

public class AlertsTab {
	// FIXME - these are duplicated in the main class
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF66;
	public    static final int    TAB_ACTIVE_COLOR = 0xFF00FF00;

	// If never set, is set to first map.
	public    static       int    CURRENT_INDEX = 0;
	
	public static String getActiveMapURL() {
		return Config.alerts[CURRENT_INDEX];
	}
	public static String getReloadURLParts() {
		return	"&alert=" + Config.alerts_src[CURRENT_INDEX];
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
