package com.osilabs.android.apps.dallastraffic;

public final class Config {
	// Tell classes to dump to logcat
	public static final int     DEBUG = 2;
	
	// This can also be accomplished by recompiling. Also by stopping the app
	public static final boolean DEBUG_FORCE_NEW_VERSION_CHECK = false; 

	// Used by web content versin checker to know which app is checking in
	public static final    String APP_CODE = "dt"; // i.e. tct, st, ld ...
	
	public static final    String DEFAULT_CAMERA_INDEX = "0";
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
	public static final int[]          RADIOS_DEFAULT_NODE = {23034,           21093};
	public static       int[]          RADIOS_CURRENT_NODE = {23034,           21093};
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

	public static final String [] traffic = {
		"Traffic",
		"Traffic at Night",
		"Dallas Metro",
		"Fort Worth Metro"
	};
	public static final String [] traffic_urls = {
		"image",
		"image",
		"{\"longitude\":\"-96821123\",\"zoom\":\"11\",\"label\":\"Dallas Metro\",\"latitude\":\"32773415\"}",
		"{\"longitude\":\"-97250963\",\"zoom\":\"11\",\"label\":\"Chicago Metro\",\"latitude\":\"32737613\"}"
	};
	public static final int [] traffic_viewtypes = {
		IMAGE,
		IMAGE,
		MAP,
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
	public static final int RSS      = 0;
	public static final int ATOM     = 1;
	public static final String [] calendar_viewtypes = {
		"rss",
		"rss",
		"rss",
		"rss",
	};
	public static final String[] calendar = {
		"Weather Report",
		"Food",
		"Music",
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
	 
	public static final String[][] crossroadsValues = {{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44"}
	, {"45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55"}
	, {"56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131"}
	, {"132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163"}
	, {"164", "165", "166", "167", "168", "169", "170", "171"}
	, {"172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195"}
	, {"196", "197", "198", "199", "200", "201", "202", "203", "204"}
	, {"205"}
	, {"206"}
	, {"207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219", "220", "221", "222", "223", "224", "225", "226", "227", "228", "229", "230", "231", "232", "233", "234", "235", "236", "237", "238", "239", "240", "241"}
	, {"242", "243", "244", "245", "246", "247", "248", "249", "250", "251", "252", "253", "254"}
	, {"255", "256", "257", "258", "259", "260", "261"}
	, {"262", "263", "264"}
	, {"265"}
	, {"266"}
	, {"267"}
	, {"268"}
	, {"269"}
	, {"270", "271", "272", "273", "274", "275", "276", "277", "278", "279", "280", "281"}
	, {"282"}
	, {"283", "284", "285", "286", "287", "288", "289"}
	};

}	