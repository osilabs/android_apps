package com.osilabs.android.apps.seattletraffic;

public final class Config {
	public static String[] maps = {
		"Traffic",
		"Road Temperatures"
	};
	public static String[] maps_urls = {
		"http://images.wsdot.wa.gov/nwflow/flowmaps/video_map_SeattleMetro.gif",
		"http://images.wsdot.wa.gov/rweather/roadtemps/l2psm06.gif"
	};
	
	public static final int DEFAULT_CAMERA_ID = 29;
	public static final int DEFAULT_MAP_INDEX = 0;
	public static final int DEFAULT_ALERT_INDEX = 0;
}