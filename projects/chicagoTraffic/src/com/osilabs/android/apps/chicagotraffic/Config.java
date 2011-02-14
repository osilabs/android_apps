package com.osilabs.android.apps.chicagotraffic;

public final class Config {
	// Tell classes to dump to logcat
	public static final int     DEBUG = 1;
	
	// This can also be accomplished by recompiling. Also by stopping the app
	public static final boolean DEBUG_FORCE_NEW_VERSION_CHECK = false; 

	// True will hide the ads
	public static final boolean NO_ADS = false; 

	// Used by web content versin checker to know which app is checking in
	public static final    String APP_CODE = "ct"; // i.e. tct, st, ld ...
	
	public static final    String DEFAULT_CAMERA_INDEX = "0";
	public static final int       DEFAULT_MAP_INDEX = 1; // !!! Can't be above array size-1. Includes favorites, best to set to one of the system traffic maps indexes.
	public static final int       DEFAULT_CALENDAR_INDEX = 0;
	public static final int       DEFAULT_TAB_INDEX = 0;
	protected static       String MOBILECONTENT_URL_PREFIX   = "http://osilabs.com/m/mobilecontent/chicagotraffic";
	protected static       String MOBILECONTENT_URL_ABOUT    = "http://www.facebook.com/pages/Chicago-Traffic/184361614921297?v=info";
	protected static       String URL_FACEBOOK               = "http://www.facebook.com/pages/Chicago-Traffic/184361614921297?v=wall";
	protected static final String NAMESPACE = "com.osilabs.android.apps.chicagotraffic";

	// Which radios to offer
	// Use './adb logcat |grep node' to see the scanner ids
	public static final int            INDEX_OF_WEATHER = 0;
	public static final int            INDEX_OF_POLICE = 1;
	
	// These are the Defaults and they will be changed as prefs change. Set current node to -1 to disable.
	public static final int[]          RADIOS_DEFAULT_NODE = {26458,           18589};
	public static       int[]          RADIOS_CURRENT_NODE = {26458,           18589};
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
	
	// Android viewtypes
	public static final int WEB      = 0;
	public static final int MAP      = 1;
	public static final int IMAGE    = 2;
	public static final int FEED     = 3;
	public static final int FAVORITE = 4;

    // Maps
    public static final String[] traffic = {
		"North Metro",
		"Chicago Metro",
		"South Metro",
		"Winter Weather",
		"Winter Weather State",
    };
    public static final String[] traffic_urls = {
    	"{\"longitude\":\"-87888284\",\"zoom\":\"11\",\"label\":\"North Metro\",\"latitude\":\"42093786\"}",
		"{\"longitude\":\"-87810976\",\"zoom\":\"10\",\"label\":\"Chicago Metro\",\"latitude\":\"41858849\"}",
    	"{\"longitude\":\"-87886908\",\"zoom\":\"11\",\"label\":\"South Metro\",\"latitude\":\"41692035\"}",
    	"{\"zoom\":\"130%\",\"scrollx\":\"30\",\"scrolly\":\"80\"}",
    	"{\"zoom\":\"130%\",\"scrollx\":\"30\",\"scrolly\":\"80\"}",
    };
    public static final int [] traffic_viewtypes = {
		MAP,
		MAP,
		MAP,
		IMAGE,
		IMAGE,
    };
	// This defaults to one of the system configured mapviews in traffic_urls
    // CAUTION!!! - If you move the maps around, this could refer to the wrong index
	public static final String DEFAULT_MAPVIEW_COORDS = traffic_urls[1]; /// FIXME - this is reduntant to default mapview index
	public static String CURRENT_MAPVIEW_COORDS = DEFAULT_MAPVIEW_COORDS;
	public static String MAPVIEW_FAVORITES = "";
	//
	// Traffic Tab
	// ---------------------------------------------------------

	


	// ---------------------------------------------------------
	// Calendar tab
	//
	public static final String [] calendar_viewtypes = {
		"rss",
		"rss",
		"rss",
		"rss",
		"rss",
	};
	public static final String[] calendar = {
		"Weather Report",
		"Food",
		"Music Pics",
		"Music Calendar",
		"Things To Do",
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