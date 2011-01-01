package com.osilabs.android.apps.chicagotraffic;

public final class Config {
	// Tell classes to dump to logcat
	public static final int DEBUG = 2; 

	public static final    String DEFAULT_CAMERA_URL = "";
	public static final int       DEFAULT_MAP_INDEX = 2; // !!! Can't be above array size-1. Includes favorites, best to set to one of the system traffic maps indexes.
	public static final int       DEFAULT_CALENDAR_INDEX = 0;
	public static final int       DEFAULT_TAB_INDEX = 0;
	public static final boolean   NO_ADS = true; // True will hide the ads
	protected static       String MOBILECONTENT_URL_PREFIX   = "http://osilabs.com/m/mobilecontent/chicagotraffic";
	protected static       String MOBILECONTENT_URL_ABOUT    = "http://osilabs.com/m/mobilecontent/about/ct_about.php";
	protected static final String NAMESPACE = "com.osilabs.android.apps.chicagotraffic";

	// Which radios to offer
	// Use './adb logcat |grep node' to see the scanner ids
	public static final int            INDEX_OF_WEATHER = 0;
	public static final int            INDEX_OF_POLICE = 1;
	// These are the Defaults and they will be changed as prefs change. Set current node to -1 to disable.
	public static       int[]          RADIOS_CURRENT_NODE = {18464,           18589};
	public static final CharSequence[] RADIOS              = {"Weather Radio", "Police Scanner"};

	// ----------------------------------------------------------
	// Formats:
	//
	// >> rss << 
	// '(rss,atom)~uri~(titleonly)~(link)'
	// "rss~http://www.startribune.com/entertainment/dining/index.rss2~ ~link"
	//
	// >> Images/Maps << 
	// 'map~uri~width~height~scrollx~scrolly'
	// "map~http://www.dot.state.mn.us/tmc/trafficinfo/map/d_map.png~128%~ ~20~66"
	//
	// >> Tags for preference based options
	// "{WEATHER}"
	// "{TODAY}"
	// ----------------------------------------------------------

	
	
	
	// ---------------------------------------------------------
	// Traffic Tab
	//
	public static final String [] traffic = {
		"Winter Weather",
		"Winter Weather State",
		"Chicago Metro"
	};
	public static final String [] traffic_urls = {
		"map~http://wrc.gettingaroundillinois.com/images/wrccook.jpg~120%~ ~40~66",
		"map~http://wrc.gettingaroundillinois.com/images/wrcimage.jpg~150%~ ~80~6",
		"{\"longitude\":\"-87810976\",\"zoom\":\"11\",\"label\":\"Chicago Metro\",\"latitude\":\"41858849\"}"
	};
	
	
	// Android viewtypes
	public static final int WEB      = 0;
	public static final int MAP      = 1;
	public static final int IMAGE    = 2;
	public static final int FEED     = 3;
	public static final int FAVORITE = 4;
	public static final int [] traffic_viewtypes = {
		IMAGE,
		IMAGE,
		MAP,
	};
	// This defaults to one of the system configured mapviews in traffic_urls
	public static String CURRENT_MAPVIEW_COORDS = traffic_urls[2];
	public static String MAPVIEW_FAVORITES = "";
	
	//
	// Traffic Tab
	// ---------------------------------------------------------

	
	
	// ---------------------------------------------------------
	// Calendar tab
	//
	public static final String[] calendar = {
		"Weather Report",
		"Food",
		"Music",
		"Things To Do",
	};
	public static final String[] calendar_src = {
		"{WEATHER}",
		"rss~http://chicago.metromix.com/rss/popup/restaurants_new_restaurants~ ~link",
		"rss~http://chicago.metromix.com/rss/popup/music_daily_picks~ ~link",
		"{TODAY}",
	};
	
	// Pref based Multiple value arrays
	public static final String[] weather_values = {
		"rss~http://feeds.weatherbug.com/rss.aspx?zipcode=60601&feed=curr,fcst,fcsttxt,cpht&zcode=z4641~ ~ ",
		"rss~http://rss.accuweather.com/rss/liveweather_rss.asp?metric=0&locCode=60602~ ~ ",
		"rss~http://rss.weather.com/weather/rss/local/USIL0225?cm_ven=LWO&cm_cat=rss&par=LWO_rss~ ~ ",
	};
	public static final String[] today_values = {
		"rss~http://chicago.metromix.com/rss/feed/daily_picks~ ~link",
		"rss~http://chicago.metromix.com/rss/popup/bars-and-clubs_daily_picks~ ~link",
	};
	//
	// Calendar tab
	// ---------------------------------------------------------
	
	
	
	

	//
	//
	// BELOW HERE PASTE IN CAMERA ARRAYS
	//
	//
	//
	
	public static final String[] mainroads = {};
	 
	public static final String[] mainroadsValues = {};
	 
	public static final String[][] crossroads = {{}};
	 
	public static final String[][] crossroadsValues = {{}};
	
	
	
	
	


}	