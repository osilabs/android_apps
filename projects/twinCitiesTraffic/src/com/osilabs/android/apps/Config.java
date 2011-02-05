package com.osilabs.android.apps;

public final class Config {
	// Tell classes to dump to logcat
	public static final int     DEBUG = 0;
	
	// This can also be accomplished by recompiling. Also by stopping the app
	public static final boolean DEBUG_FORCE_NEW_VERSION_CHECK = false; 

	// True will hide the ads
	public static final boolean NO_ADS = false; 

	// Used by web content versin checker to know which app is checking in
	public static final    String APP_CODE = "tct"; // i.e. tct, st, ld ...
	
	public static final    String DEFAULT_CAMERA_INDEX = "0";
	public static final int       DEFAULT_MAP_INDEX = 2; // !!! Can't be above array size-1. Includes favorites, best to set to one of the system traffic maps indexes.
	public static final int       DEFAULT_CALENDAR_INDEX = 0;
	public static final int       DEFAULT_TAB_INDEX = 0;
	protected static       String MOBILECONTENT_URL_PREFIX   = "http://osilabs.com/m/mobilecontent/tctraffic";
	protected static       String MOBILECONTENT_URL_ABOUT    = "http://www.facebook.com/pages/Twin-Cities-Traffic-Camera/154240191294364?v=info";
	protected static final String NAMESPACE = "com.osilabs.android.apps";

	// Which radios to offer
	// Use './adb logcat |grep node' to see the scanner ids
	public static final int            INDEX_OF_WEATHER = 0;
	public static final int            INDEX_OF_POLICE = 1;
	
	// These are the Defaults and they will be changed as prefs change. Set current node to -1 to disable.
	public static final int[]          RADIOS_DEFAULT_NODE = {26277,           24429};
	public static       int[]          RADIOS_CURRENT_NODE = {26277,           24429};
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
		"Minneapolis",
		"Saint Paul",
		"Metro",
        "Color Traffic Map",
        "Hi-Contrast Traffic Map",
        "Alternate Map",
    };
    public static final String[] traffic_urls = {
        "{\"longitude\":\"-93253325\",\"zoom\":\"13\",\"label\":\"Mpls\",\"latitude\":\"44988396\"}",
        "{\"longitude\":\"-93095399\",\"zoom\":\"13\",\"label\":\"Sp\",\"latitude\":\"44950990\"}",
        "{\"longitude\":\"-93201498\",\"zoom\":\"11\",\"label\":\"Met\",\"latitude\":\"44950747\"}",
    	"{\"zoom\":\"130%\",\"scrollx\":\"30\",\"scrolly\":\"80\"}",
    	"{\"zoom\":\"130%\",\"scrollx\":\"40\",\"scrolly\":\"80\"}",
    	"{\"zoom\":\"160%\",\"scrollx\":\"130\",\"scrolly\":\"220\"}",
    };
    public static final int [] traffic_viewtypes = {
    	MAP,
    	MAP,
    	MAP,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    };
	// This defaults to one of the system configured mapviews in traffic_urls
    // CAUTION!!! - If you move the maps around, this could refer to the wrong index
	public static final String DEFAULT_MAPVIEW_COORDS = traffic_urls[2]; /// FIXME - this is reduntant to default mapview index
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
		"Things To Do",		
		"Food",
		"Music",
        "Traffic Incidents",
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
	
	public static final String[] mainroads = {"I-35", "I-35E", "I-35W", "I-394", "I-494", "I-694", "I-94", "T.H.10", "T.H.100", "T.H.110", "T.H.212", "T.H.36", "T.H.5", "T.H.52", "T.H.55", "T.H.61", "T.H.610", "T.H.62", "T.H.77", "U.S.12", "U.S.169"};
	 
	public static final String[] mainroadsValues = {"I_35", "I_35E", "I_35W", "I_394", "I_494", "I_694", "I_94", "T_H_10", "T_H_100", "T_H_110", "T_H_212", "T_H_36", "T_H_5", "T_H_52", "T_H_55", "T_H_61", "T_H_610", "T_H_62", "T_H_77", "U_S_12", "U_S_169"};
	 
	public static final String[][] crossroads = {{"Crystal Lake Rd", "Co Rd 46", "Co Rd 50", "Co Rd 60"}
	, {"Goose Lake Rd", "Co Rd E", "Little Canada Rd", "Roselawn Ave", "Larpenteur Ave", "Maryland Ave", "Cayuga St", "University Ave", "Kellogg Blvd", "Grand Ave", "St. Clair Ave", "Victoria St", "Jefferson Ave", "Randolph Ave", "W. 7th St", "Shepard Rd", "T.H.13", "S of T.H.13", "N of T.H.110", "T.H.110", "Wagon Wheel Tr", "I-494", "Lone Oak Rd", "Yankee Doodle Rd", "Pilot Knob Rd", "Deerwood Dr", "Diffley Rd", "Cliff Rd", "T.H.77", "S of T.H.77", "Co Rd 11", "McAndrews Rd", "Co Rd 42"}
	, {"Lexington Ave", "95th Ave", "Lake Dr", "T.H.10", "Co Rd I", "Co Rd 10", "T.H.96", "Co Rd E2", "T.H.88", "Co Rd C", "T.H.280", "Industrial Blvd", "Stinson Blvd", "Johnson St", "Hennepin Ave", "4th St", "Washington Ave", "3rd St", "Hiawatha Ave", "Chicago Ave", "26th St", "Lake St", "35th St", "42nd St", "49th St", "58th St", "T.H.62", "Lyndale Ave", "T.H.121", "66th St", "76th St", "86th St", "94th St", "98th St", "110th St", "Cliff Rd", "T.H.13", "Burnsville Pkwy", "Co Rd 42"}
	, {"I-94", "I-94", "Dunwoody Blvd", "Penn Ave", "Wirth Pkwy", "France Ave", "T.H.100", "T.H.100 CD", "Xenia Ave", "Hampshire Ave", "Winnetka Ave", "General Mills Blvd", "U.S.169", "Co Rd 73", "Plymouth Rd", "I-494"}
	, {"Fish Lake Rd", "Bass Lake Rd", "N of 49th Ave", "49th Ave", "Rockford Rd", "T.H.55", "Co Rd 6", "Carlson Pkwy", "Stone Rd", "Minnetonka Blvd", "T.H.7", "Excelsior Blvd", "Baker Rd", "Valley View Rd", "Valley View Rd", "Flying Cloud Dr", "U.S.169", "W Bush Lake Rd", "E Bush Lake Rd", "T.H.100", "France Ave", "Penn Ave", "I-35W", "Lyndale Ave", "Portland Ave", "12th Ave", "T.H.77", "24th Ave", "T.H.5", "Pilot Knob Rd", "I-35E", "Dodd Rd", "Delaware Ave", "60th St", "T.H.110", "Blaine Ave", "W of Concord St", "T.H.52", "Concord Ave", "Bailey Rd", "Carver Ave", "Valley Creek Rd", "Tamarack Rd"}
	, {"10th St", "15th St N", "Stillwater Blvd", "T.H.5", "40th St", "50th St", "T.H.36", "Century Ave", "McKnight Rd", "White Bear Ave", "T.H.61", "Labore Rd", "Edgerton St", "I-35E", "Rice St", "W of Rice St", "Victoria St", "Lexington Ave", "T.H.10", "I-35W", "5th Ave", "Silver Lake Rd", "T.H.65", "Main St", "T.H.252"}
	, {"T.H.95", "Co Rd 21", "St. Croix RS", "Co Rd 71", "Co Rd 15", "Co Rd 17B", "Co Rd 19", "Co Rd 13", "I-494 E Jct", "T.H.120", "McKnight Rd", "White Bear Ave", "T.H.61", "Johnson Pkwy", "Mounds Blvd", "T.H.52", "7th St", "I-35E", "Jackson St", "Wabasha St", "John Ireland Blvd", "Western Ave", "Dale St", "Lexington Ave", "Hamline Ave", "Snelling Ave", "Prior Ave", "Cretin Ave", "T.H.280", "W of T.H.280", "Franklin Ave", "Huron St", "Riverside Ave", "Cedar Ave", "Portland Ave", "T.H.65", "Nicollet Ave", "Hennepin and Lyndale", "T.H.55", "Broadway Ave", "Lowry Ave", "42nd Ave", "49th Ave", "57th Ave", "Humboldt Ave", "Xerxes Ave", "Brooklyn Blvd", "Zane Ave", "Co Rd 81", "Boone Ave", "U.S.169", "Hemlock Ln", "I-494", "Weaver Lake Rd", "Elm Creek", "95th Ave", "101st Ave", "W of 101st Ave", "Brockton Ln", "Co Rd 81 West Jct", "E of T.H.101", "T.H.101", "W of T.H.101", "Co Rd 37", "Co Rd 19", "Co Rd 75", "T.H.25", "T.H.24", "W of T.H.24"}
	, {"Main St", "Co Rd 11", "T.H.24", "E of Co Rd J", "Airport Rd", "T.H.65", "University Ave", "Foley Blvd", "W of Egret", "Hanson Blvd", "W of Hanson Blvd", "T.H.242", "Round Lake Blvd", "7th Ave", "U.S.169", "Greenhaven Rd"}
	, {"France Ave", "France Ave", "Co Rd 81", "42nd Ave", "36th Ave", "Duluth St", "Duluth St", "T.H.55", "Glenwood Ave", "27th St", "T.H.7", "Excelsior Blvd", "50th St", "Benton Ave", "66th St", "77th St"}
	, {"T.H.55"}
	, {"Shady Oak Rd", "Valley View Rd", "I-494", "Prarie Center Dr", "Mitchell Rd", "Wallace Rd", "Eden Prairie Rd", "Dell Rd"}
	, {"T.H.120", "T.H.61", "Edgerton St", "I-35E", "Rice St", "Dale St", "Lexington Ave", "Snelling Ave", "Cleveland Ave"}
	, {"Airport Rd"}
	, {"Plato Blvd", "Eaton St", "Concord St", "N of Butler", "Butler Ave", "Thompson Ave", "Southview Blvd", "I-494", "Upper 55th St", "70th St", "80th St", "T.H.55"}
	, {"T.H.5"}
	, {"I-694", "Co Rd C", "Co Rd E", "Glen Rd"}
	, {"T.H.47"}
	, {"I-494", "Clearwater Dr", "Shady Oak Rd", "U.S.169", "Gleason Rd", "Tracy Ave", "T.H.100", "France Ave", "Xerxes Ave", "Portland Ave", "Bloomington Ave", "28th Ave", "43rd Ave", "T.H.55"}
	, {"66th St", "84th St", "Old Shakopee Rd", "Minnesota River", "T.H.13", "Diffley Rd", "Cliff Rd", "I-35E", "127th St", "Co Rd 38"}
	, {"Co Rd 15", "Central Ave"}
	, {"77th Ave", "63rd Ave", "Bass Lake Rd", "49th Ave", "Rockford Rd", "36th Ave", "Medicine Lake Rd", "Plymouth Ave", "T.H.55", "Cedar Lake Rd", "Minnetonka Blvd", "T.H.7", "7th St", "Bren Rd", "Valley View Rd", "I-494", "Anderson Lakes S Bridge", "Anderson Lakes Pkwy", "Pioneer Trail", "Old Shakopee Rd"}
	};
	 
	public static final String[][] crossroadsValues = {{"0", "1", "2", "3"}
	, {"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36"}
	, {"37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75"}
	, {"76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91"}
	, {"92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134"}
	, {"135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159"}
	, {"160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199", "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219", "220", "221", "222", "223", "224", "225", "226", "227", "228"}
	, {"229", "230", "231", "232", "233", "234", "235", "236", "237", "238", "239", "240", "241", "242", "243", "244"}
	, {"245", "246", "247", "248", "249", "250", "251", "252", "253", "254", "255", "256", "257", "258", "259", "260"}
	, {"261"}
	, {"262", "263", "264", "265", "266", "267", "268", "269"}
	, {"270", "271", "272", "273", "274", "275", "276", "277", "278"}
	, {"279"}
	, {"280", "281", "282", "283", "284", "285", "286", "287", "288", "289", "290", "291"}
	, {"292"}
	, {"293", "294", "295", "296"}
	, {"297"}
	, {"298", "299", "300", "301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "311"}
	, {"312", "313", "314", "315", "316", "317", "318", "319", "320", "321"}
	, {"322", "323"}
	, {"324", "325", "326", "327", "328", "329", "330", "331", "332", "333", "334", "335", "336", "337", "338", "339", "340", "341", "342", "343"}
	};
}	