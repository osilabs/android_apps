package com.osilabs.android.apps.dallastraffic;

public final class Config {
	// Tell classes to dump to logcat
	public static final int     DEBUG = 2;
	
	// This can also be accomplished by recompiling. Also by stopping the app
	public static final boolean DEBUG_FORCE_NEW_VERSION_CHECK = false; 

	// Used by web content versin checker to know which app is checking in
	public static final    String APP_CODE = "dt"; // i.e. tct, st, ld ...
	
	public static final    String DEFAULT_CAMERA_URL = "";
	public static final int       DEFAULT_MAP_INDEX = 0; // !!! Can't be above array size-1. Includes favorites, best to set to one of the system traffic maps indexes.
	public static final int       DEFAULT_CALENDAR_INDEX = 0;
	public static final int       DEFAULT_TAB_INDEX = 0;
	public static final boolean   NO_ADS = false; // True will hide the ads
	protected static       String MOBILECONTENT_URL_PREFIX   = "http://osilabs.com/m/mobilecontent/dallastraffic";
	protected static       String MOBILECONTENT_URL_ABOUT    = "http://osilabs.com/m/mobilecontent/about/dt_about.php";
	protected static final String NAMESPACE = "com.osilabs.android.apps.dallastraffic";

	// Which radios to offer
	// Use './adb logcat |grep node' to see the scanner ids
	public static final int            INDEX_OF_WEATHER = 0;
	public static final int            INDEX_OF_POLICE = 1;
	
	// These are the Defaults and they will be changed as prefs change. Set current node to -1 to disable.
	public static final int[]          RADIOS_DEFAULT_NODE = {18464,           18589};
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
		"{\"longitude\":\"-87810976\",\"zoom\":\"10\",\"label\":\"Chicago Metro\",\"latitude\":\"41858849\"}"
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
	public static final String DEFAULT_MAPVIEW_COORDS = traffic_urls[2];
	public static String CURRENT_MAPVIEW_COORDS = DEFAULT_MAPVIEW_COORDS;
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
		"rss~http://www.chicagoreader.com/chicago/Rss.xml?section=846971~ ~link",
		"rss~http://chicago.metromix.com/rss/popup/music_daily_picks~ ~link",
		"{TODAY}",
	};
	// "rss~http://chicago.metromix.com/rss/popup/restaurants_new_restaurants~ ~link",

	// Pref based Multiple value arrays
	public static final String[] weather_values = {
		"rss~http://feeds.weatherbug.com/rss.aspx?zipcode=60601&feed=curr,fcst&zcode=z4641~ ~ ",
		"rss~http://rss.accuweather.com/rss/liveweather_rss.asp?metric=0&locCode=60602~ ~ ",
		"rss~http://rss.weather.com/weather/rss/local/USIL0225?cm_ven=LWO&cm_cat=rss&par=LWO_rss~ ~ ",
		"rss~http://feeds.weatherbug.com/rss.aspx?zipcode=60601&feed=currtxt,fcsttxt&zcode=z4641~ ~ "
	};
	public static final String[] today_values = {
		"rss~http://chicago.metromix.com/rss/feed/daily_picks~ ~link",
		"rss~http://chicago.metromix.com/rss/popup/bars-and-clubs_daily_picks~ ~link",
		"rss~http://www.chicagoreader.com/chicago/Rss.xml~ ~link",
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
	
	public static final String[] mainroads = {"US75", "SH183", "IH35E", "IH635", "US67", "IH20", "Spur 408", "High Five S.E. 1", "High Five S.E. 2", "IH30", "Loop 12", "Spur 366", "IH345", "High Five N.E. 1", "High Five N.E. 2", "High Five S.W. 1", "High Five S.W. 2", "IH30/IH35E Mixmaster", "SH114", "IH35", "IH35W"};
	 
	public static final String[] mainroadsValues = {"US75", "SH183", "IH35E", "IH635", "US67", "IH20", "Spur_408", "High_Five_S_E__1", "High_Five_S_E__2", "IH30", "Loop_12", "Spur_366", "IH345", "High_Five_N_E__1", "High_Five_N_E__2", "High_Five_S_W__1", "High_Five_S_W__2", "IH30_IH35E_Mixmaster", "SH114", "IH35", "IH35W"};
	 
	public static final String[][] crossroads = {{"Coit Flyover", "Forest", "Royal", "Meadow North", "Meadow South", "Royal North", "SMU Blvd.", "University", "Walnut Hill", "Northpark", "Caruth Haven", "Lovers Lane", "Mockingbird", "McCommas", "Monticello", "Knox North", "Knox South", "Haskell", "Lemmon", "Hall", "Fitzhugh", "Park Lane", "IH635 North", "Midpark", "Spring Valley", "Belt Line", "Arapaho", "Campbell", "Galatyn Pkwy.", "Dallas/Collin County Line", "SH190 S.W. #1", "SH190 S.W. #2", "SH190 N.E. #1", "SH190 N.E. #2", "15th St.", "Park Blvd.", "Parker South", "Parker North", "Spring Creek Pkwy.", "Legacy", "Ridgemont", "Bethany", "McDermott", "Allen Dr.", "Exchange Pkwy."}
	, {"Esters", "Valley View", "Belt Line", "Story", "MacArthur", "Carl Rd.", "SH114", "Grauwyler", "Profit", "Loop 12", "Wingren"}
	, {"Royal", "Walnut Hill", "Loop 12", "SH183", "Riverfront Blvd.", "Colorado", "East 8th St.", "Louisiana", "Beckley", "Clarendon", "Saner", "US67", "Marsalis", "Danieldale", "Wintergreen", "Commerce", "Spur 366", "Hi Line", "Tollway", "Market Center", "Wycliff", "Medical District Dr.", "Inwood", "Commonwealth", "Mockingbird", "Empire Central", "Regal Row", "Northwest Hwy", "Raceway / Harry Hines", "Spur 482", "Pleasant Run", "Belt Line South", "Reunion", "IH635 South", "Chambers Creek", "SH34", "IH635 North", "Valley View", "Valwood Pkwy.", "Crosby", "Belt Line North", "Luna", "Sandy Lake", "SH190 South", "Parkerville", "US77 North", "FM387", "US287", "Brookside", "US77 South", "Grainery Rd.", "Kiest", "Ann Arbor", "Loop 12", "Laureland", "Camp Wisdom", "Wheatland", "Dallas/Ellis County Line", "SH342", "FM664 (Ovilla Rd.)", "Red Oak Rd.", "SH190 North", "SH121 Bypass", "Business 121", "FM1171 (Main St.)", "Garden Ridge", "Denton Rd.", "Corinth Pkwy.", "Mayhill", "Dallas Dr. (US77)", "US377", "IH35W Split", "Johnston Rd.", "Ellis County Rest Area", "FM329", "Bill Lewis Rd."}
	, {"Rosser", "Ratcliff Pedestrian Bridge", "Josey", "Preston", "Montfort", "Welch", "Harry Hines", "IH30 South", "Towne Centre", "Park Central", "Coit East", "Coit", "TI Blvd West", "TI Blvd East", "Greenville", "Abrams", "Forest", "Skillman", "Miller", "Plano Rd.", "Kingsley", "Jupiter", "Garland Rd.", "Northwest Hwy.", "Centerville", "La Prada", "Oates", "Galloway", "IH30 N.W. 1", "IH30 N.W. 2", "IH35E West", "Town East"}
	, {"Red Bird", "Kiest", "Polk", "Loop 12", "Swansee", "Hampton", "Camp Wisdom", "IH20 North"}
	, {"Kirnwood", "Wheatland", "Hampton", "South Polk", "Willoughby", "Spur 408 East", "Dallas/Tarrant County Line", "Robinson Rd.", "Carrier Pkwy.", "Fish Creek", "Belt Line", "Mountain Creek EB", "Mountain Creek Pkwy.", "Cedar Ridge", "Duncanville Rd.", "Oriole", "Camp Wisdom", "Westmoreland", "Spur 408 EB", "Spur 408 WB", "Spur 408", "IH35E S.W. #1", "IH35E S.W. #2", "IH35E S.W. #3"}
	, {"Loop 12", "Illinois", "Kiest North", "Kiest", "Kiest South", "Grady Niblo", "Artesian Creek NB", "Artesian Creek SB", "IH20 North"}
	, {"High Five S.E. 1"}
	, {"High Five S.E. 2"}
	, {"Hotel St.", "Lamar", "Cadiz", "Akard", "St. Paul", "Good Latimer EB", "Haskell", "Carroll", "East Grand", "St. Francis", "Jim Miller", "Samuell", "Dolphin", "Hunnicut", "IH45", "Good Latimer WB", "2nd Ave.", "US80", "Big Town", "Motley", "Gus Thomasson", "Galloway", "Houston St.", "NW 19th St.", "NW 7th St.", "Belt Line", "MacArthur West", "MacArthur East", "Gifford Hill RR Spur", "Loop 12", "Westmoreland", "Hampton", "Sylvan", "Cesar Chavez Blvd.", "Duncan Perry"}
	, {"Trinity River (Elm Fork)", "SH183 East", "SH183 South", "Grauwyler", "Union Bower", "Irving Blvd.", "Shady Grove", "Trinity River (West Fork)", "Singleton", "IH30 North", "Anderson", "Jefferson", "Keeneland Pkwy."}
	, {"US75", "IH35E North", "IH35E South", "IH35E East", "US75 North", "US75 South", "US75 West"}
	, {"Ross", "Pacific", "Taylor"}
	, {"High Five N.E. 1"}
	, {"High Five N.E. 2"}
	, {"High Five S.W. 1"}
	, {"High Five S.W. 2"}
	, {"IH30/IH35E Mixmaster"}
	, {"Dallas/Tarrant County Line", "Freeport Pkwy.", "Esters", "Belt Line West", "Belt Line East", "Longhorn Dr.", "SH161", "MacArthur", "Hidden Ridge", "OConnor", "Rochelle West", "Rochelle East"}
	, {"Bear Creek"}
	, {"FM2449", "Crawford Rd.", "FM407", "FM1171", "Earnhardt Way", "SH114", "Eagle Pkwy."}
	};
	 
	public static final String[][] crossroadsValues = {{"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/29.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/31.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/43.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/52.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/53.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/93.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/112.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/113.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/283.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/284.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/285.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/286.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/287.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/288.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/289.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/290.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/291.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/292.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/293.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/294.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/314.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/315.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/521.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/522.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/523.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/524.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/527.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/530.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/532.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/575.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/544.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/545.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/542.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/543.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/525.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/537.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/538.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/539.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/546.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/534.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/541.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/529.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/535.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/526.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/531.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/105.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/106.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/107.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/139.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/141.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/143.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/145.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/146.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/147.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/153.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/162.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/123.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/124.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/125.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/154.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/168.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/169.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/170.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/171.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/173.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/174.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/176.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/177.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/232.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/244.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/245.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/273.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/274.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/275.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/276.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/277.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/278.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/279.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/280.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/281.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/324.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/325.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/326.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/327.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/328.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/329.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/246.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/247.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/167.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/122.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/463.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/464.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/488.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/487.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/486.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/485.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/484.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/483.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/482.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/481.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/248.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/581.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/582.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/583.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/584.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/585.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/586.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/548.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/549.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/550.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/551.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/552.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/553.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/577.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/580.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/578.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/579.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/567.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/566.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/565.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/564.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/563.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/598.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/561.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/560.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/559.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/558.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/557.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/602.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/603.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/604.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/605.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/127.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/128.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/129.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/134.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/135.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/136.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/137.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/330.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/331.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/412.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/185.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/413.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/444.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/445.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/446.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/447.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/448.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/449.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/450.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/451.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/452.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/453.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/454.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/455.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/456.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/457.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/458.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/459.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/460.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/461.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/229.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/600.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/175.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/178.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/179.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/180.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/181.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/182.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/183.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/184.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/249.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/250.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/251.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/252.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/253.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/263.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/414.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/415.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/416.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/417.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/418.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/420.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/419.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/424.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/425.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/426.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/428.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/427.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/422.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/423.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/429.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/554.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/555.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/556.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/254.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/255.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/256.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/257.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/258.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/259.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/260.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/261.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/262.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/316.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/317.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/359.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/360.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/361.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/362.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/363.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/366.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/369.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/370.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/371.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/372.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/373.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/377.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/378.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/374.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/367.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/365.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/368.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/383.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/437.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/438.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/439.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/462.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/357.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/588.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/589.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/590.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/591.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/592.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/593.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/594.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/595.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/596.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/597.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/606.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/587.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/156.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/501.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/502.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/509.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/520.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/511.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/517.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/519.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/518.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/510.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/508.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/514.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/516.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/431.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/478.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/479.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/468.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/469.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/470.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/471.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/432.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/433.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/434.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/442.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/443.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/440.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/441.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/358.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/489.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/490.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/491.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/492.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/493.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/494.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/495.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/496.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/497.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/498.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/499.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/500.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/576.jpg"}
	, {"http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/568.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/569.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/570.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/571.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/572.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/573.jpg", "http://dfwtraffic.dot.state.tx.us/DalTrans/CameraSnapshots/574.jpg"}
	};
	 
	
	
	
	
	


}	