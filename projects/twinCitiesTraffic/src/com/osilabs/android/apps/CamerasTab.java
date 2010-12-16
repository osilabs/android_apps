package com.osilabs.android.apps;

import java.net.URLEncoder;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

public class CamerasTab {
	// FIXME - these are duplicated in the main class
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF88;
	public    static final int    TAB_ACTIVE_COLOR = 0xFFAACCAA;
	public    static final int    BG_OFF = 0xFF333333;
	public    static final int    BG_ON  = Color.BLACK;

	// If never set, is set to first camera
	public    static       String CURRENT_CAMERA_URL = Config.DEFAULT_CAMERA_URL;
	
	public static void init() {
		App.ivCamerasTab.setPadding(15, 0, 15, 0);
		App.ivCamerasTab.setAlpha(ALPHA_OFF);
		App.ivCamerasTab.setBackgroundColor(BG_OFF);
	}
	public static String getReloadURLParts() {
		return	"&camera=" + URLEncoder.encode(CURRENT_CAMERA_URL);
	}
	public static void setActive() {
		App.ivCamerasTab.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		App.ivCamerasTab.setAlpha(ALPHA_ON);
		App.ivCamerasTab.setBackgroundColor(BG_ON);
	}
	public static void setInactive() {
		App.ivCamerasTab.setColorFilter(null); 
		App.ivCamerasTab.setAlpha(ALPHA_OFF);
		App.ivCamerasTab.setBackgroundColor(BG_OFF);
	}
	public static void hideConfiguration() {
		App.ivCamerasMore.setVisibility(ImageView.GONE);
		App.tvCamerasPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration() {
		App.ivCamerasMore.setVisibility(ImageView.VISIBLE);
		App.tvCamerasPop.setVisibility(TextView.VISIBLE);
	}
}