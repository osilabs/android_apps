package com.osilabs.android.apps.seattletraffic;

import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.TextView;

public class CamerasTab {
	// FIXME - these are duplicated in the main class
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF88;
	public    static final int    TAB_ACTIVE_COLOR = 0xFF00FF00;

	// If never set, is set to first camera
	public    static       String CURRENT_CAMERA_URL = Config.DEFAULT_CAMERA_URL;
	
	public static String getReloadURLParts() {
		return	"&camera=" + CURRENT_CAMERA_URL;
	}
	public static void setActive(ImageView ivCameras) {
		ivCameras.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		ivCameras.setAlpha(ALPHA_ON);
	}
	public static void setInactive(ImageView ivCameras) {
		ivCameras.setColorFilter(null); 
		ivCameras.setAlpha(ALPHA_OFF);
	}
	public static void hideConfiguration(ImageView ivCameraMore, TextView tvCamerasPop) {
		ivCameraMore.setVisibility(ImageView.GONE);
		tvCamerasPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration(ImageView ivCameraMore, TextView tvCamerasPop) {
		ivCameraMore.setVisibility(ImageView.VISIBLE);
		tvCamerasPop.setVisibility(TextView.VISIBLE);
	}
}