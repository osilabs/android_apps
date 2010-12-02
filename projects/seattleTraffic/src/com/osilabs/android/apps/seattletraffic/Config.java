package com.osilabs.android.apps.seattletraffic;

public final class Config {
	public static final int DEFAULT_CAMERA_ID = 29;
	public static final int DEFAULT_MAP_INDEX = 0;
	public static final int DEFAULT_ALERT_INDEX = 0;

	// Maps
	public static String[] maps = {
		"Traffic",
		"Road Temperatures"
	};
	public static String[] maps_urls = {
		"http://images.wsdot.wa.gov/nwflow/flowmaps/video_map_SeattleMetro.gif",
		"http://images.wsdot.wa.gov/rweather/roadtemps/l2psm06.gif"
	};

	// Alerts
	public static String[] alerts = {
		"Weather Alerts",
		"Blewett pass",
		"Satus pass",
		"Snoqualmie pass",
		"Stevens pass",
		"White pass"
	};
	public static String[] alerts_src = {
		"http://www.weather.gov/alerts/wa.rss",
		"http://www.wsdot.wa.gov/traffic/passes/blewett/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/satus/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/snoqualmie/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/stevens/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/white/rss/"
	};
	
}