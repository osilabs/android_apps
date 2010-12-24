package com.osilabs.android.apps.chicagotraffic;

//import com.google.android.maps.GeoPoint;

public final class Config {
	public static final    String DEFAULT_CAMERA_URL = "http://osilabs.com/m/mobilecontent/tctraffic5/trafficmap.php?target=4&camera=832";
	public static final int       DEFAULT_MAP_INDEX = 0; // !!! Can't be above array size-1
	public static final int       DEFAULT_CALENDAR_INDEX = 0;
	public static final int       DEFAULT_TAB_INDEX = 0;
	public static final boolean   NO_ADS = false; // True will hide the ads
	protected static       String MOBILECONTENT_URL_PREFIX   = "http://osilabs.com/m/mobilecontent/tctraffic";
	protected static       String MOBILECONTENT_URL_ABOUT    = "http://osilabs.com/m/mobilecontent/about/tct_about.php";
	protected static final String NAMESPACE = "com.osilabs.android.apps";

	// Which radios to offer
	// Use './adb logcat |grep node' to see the scanner ids
	public static final int            INDEX_OF_WEATHER = 0;
	public static final int            INDEX_OF_POLICE = 1;
	// These are the Defaults and they will be changed as prefs change. Set current node to -1 to disable.
	public static       int[]          RADIOS_CURRENT_NODE = {24761,           24429};
	public static final CharSequence[] RADIOS              = {"Weather Radio", "Police Scanner"};
	
	// Geo points
	//lat=44.787144
	//lon=-92.834472
	public static		int [][] GEO_POINTS = {
		{ 44980000, -93200000 } // Minneapolis
	};

	//		new GeoPoint((int) (44.787144 * 1E6), (int) (-92.834472 * 1E6)),
	
	//
	// Non config studd
	//
	public static final int WEB   = 0;
	public static final int MAP   = 1;
	public static final int IMAGE = 2;
	public static final int FEED  = 3;

		
	// Traffic Tab
	public static final String [] traffic = {
		"Color Traffic Map",
		"Hi-Contrast Traffic Map",
		"Alternate Map",
		"Traffic Alerts",
		"Interactive Map"
	};
	public static final String [] traffic_urls = {
		"map~http://www.dot.state.mn.us/tmc/trafficinfo/map/d_map.png~128%~ ~20~66",
		"map~http://www.dot.state.mn.us/tmc/trafficinfo/map/d_map_alt.png~138%~ ~22~0",
		"map~http://www.511mn.org/primary/images/all/TC_Metro.gif~138%~ ~92~60",
		"rss~http://www.dot.state.mn.us/tmc/trafficinfo/incidents.rss~ ~ ",
		"MAP"
	};
	// View Types: WEB, IMAGE, MAP
	public static final int [] traffic_viewtypes = {
		IMAGE,
		IMAGE,
		IMAGE,
		FEED,
		MAP
	};
	
	//
	// calendar/today
	//
	public static final String[] calendar = {
		"Weather Report",
		"Food",
		"Music",
		"Things To Do", // FIXME - For different themes this can use a different wording
	};
	// Second param can be 'uri|(titleonly)|(link)'
	public static final String[] calendar_src = {
		"{WEATHER}",
		"rss~http://twincities.metromix.com/rss/popup/restaurants_daily_picks~ ~ ",
		"rss~http://twincities.metromix.com/rss/popup/music_headlines~ ~ ",
		"{TODAY}",
	};
	// '(rss,atom)|uri|(titleonly)|(link)'
	public static final String[] today_values = {
		"rss~http://www.citypages.com/syndication/events/~ ~link",
		"rss~http://www.startribune.com/entertainment/dining/index.rss2~ ~link"
	};
	// '(rss,atom)|uri|(titleonly)|(link)'
	public static final String[] weather_values = {
		"rss~http://feeds.weatherbug.com/rss.aspx?zipcode=55401&feed=currtxt,fcsttxt&zcode=z4641~ ~ ",
		"rss~http://rss.accuweather.com/rss/liveweather_rss.asp?metric=0&locCode=55401~ ~ ",
		"rss~http://rss.weather.com/weather/rss/local/USMN0503?cm_ven=LWO&cm_cat=rss&par=LWO_rss~ ~ "
	};
	
	
	
	

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
	 
	public static final String[][] crossroadsValues = {{"http://video.dot.state.mn.us/video/image?id=605", "http://video.dot.state.mn.us/video/image?id=604", "http://video.dot.state.mn.us/video/image?id=603", "http://video.dot.state.mn.us/video/image?id=602"}
	, {"http://video.dot.state.mn.us/video/image?id=42", "http://video.dot.state.mn.us/video/image?id=41", "http://video.dot.state.mn.us/video/image?id=40", "http://video.dot.state.mn.us/video/image?id=39", "http://video.dot.state.mn.us/video/image?id=38", "http://video.dot.state.mn.us/video/image?id=37", "http://video.dot.state.mn.us/video/image?id=36", "http://video.dot.state.mn.us/video/image?id=35", "http://video.dot.state.mn.us/video/image?id=34", "http://video.dot.state.mn.us/video/image?id=33", "http://video.dot.state.mn.us/video/image?id=32", "http://video.dot.state.mn.us/video/image?id=31", "http://video.dot.state.mn.us/video/image?id=30", "http://video.dot.state.mn.us/video/image?id=29", "http://video.dot.state.mn.us/video/image?id=28", "http://video.dot.state.mn.us/video/image?id=27", "http://video.dot.state.mn.us/video/image?id=26", "http://video.dot.state.mn.us/video/image?id=25", "http://video.dot.state.mn.us/video/image?id=24", "http://video.dot.state.mn.us/video/image?id=23", "http://video.dot.state.mn.us/video/image?id=22", "http://video.dot.state.mn.us/video/image?id=21", "http://video.dot.state.mn.us/video/image?id=20", "http://video.dot.state.mn.us/video/image?id=19", "http://video.dot.state.mn.us/video/image?id=18", "http://video.dot.state.mn.us/video/image?id=17", "http://video.dot.state.mn.us/video/image?id=16", "http://video.dot.state.mn.us/video/image?id=15", "http://video.dot.state.mn.us/video/image?id=14", "http://video.dot.state.mn.us/video/image?id=13", "http://video.dot.state.mn.us/video/image?id=12", "http://video.dot.state.mn.us/video/image?id=11", "http://video.dot.state.mn.us/video/image?id=10"}
	, {"http://video.dot.state.mn.us/video/image?id=644", "http://video.dot.state.mn.us/video/image?id=643", "http://video.dot.state.mn.us/video/image?id=642", "http://video.dot.state.mn.us/video/image?id=641", "http://video.dot.state.mn.us/video/image?id=640", "http://video.dot.state.mn.us/video/image?id=639", "http://video.dot.state.mn.us/video/image?id=638", "http://video.dot.state.mn.us/video/image?id=637", "http://video.dot.state.mn.us/video/image?id=636", "http://video.dot.state.mn.us/video/image?id=635", "http://video.dot.state.mn.us/video/image?id=634", "http://video.dot.state.mn.us/video/image?id=633", "http://video.dot.state.mn.us/video/image?id=632", "http://video.dot.state.mn.us/video/image?id=631", "http://video.dot.state.mn.us/video/image?id=630", "http://video.dot.state.mn.us/video/image?id=629", "http://video.dot.state.mn.us/video/image?id=628", "http://video.dot.state.mn.us/video/image?id=627", "http://video.dot.state.mn.us/video/image?id=626", "http://video.dot.state.mn.us/video/image?id=625", "http://video.dot.state.mn.us/video/image?id=624", "http://video.dot.state.mn.us/video/image?id=623", "http://video.dot.state.mn.us/video/image?id=622", "http://video.dot.state.mn.us/video/image?id=621", "http://video.dot.state.mn.us/video/image?id=620", "http://video.dot.state.mn.us/video/image?id=619", "http://video.dot.state.mn.us/video/image?id=618", "http://video.dot.state.mn.us/video/image?id=617", "http://video.dot.state.mn.us/video/image?id=616", "http://video.dot.state.mn.us/video/image?id=615", "http://video.dot.state.mn.us/video/image?id=614", "http://video.dot.state.mn.us/video/image?id=613", "http://video.dot.state.mn.us/video/image?id=612", "http://video.dot.state.mn.us/video/image?id=611", "http://video.dot.state.mn.us/video/image?id=610", "http://video.dot.state.mn.us/video/image?id=609", "http://video.dot.state.mn.us/video/image?id=608", "http://video.dot.state.mn.us/video/image?id=607", "http://video.dot.state.mn.us/video/image?id=606"}
	, {"http://video.dot.state.mn.us/video/image?id=918", "http://video.dot.state.mn.us/video/image?id=917", "http://video.dot.state.mn.us/video/image?id=916", "http://video.dot.state.mn.us/video/image?id=915", "http://video.dot.state.mn.us/video/image?id=914", "http://video.dot.state.mn.us/video/image?id=913", "http://video.dot.state.mn.us/video/image?id=912", "http://video.dot.state.mn.us/video/image?id=911", "http://video.dot.state.mn.us/video/image?id=910", "http://video.dot.state.mn.us/video/image?id=909", "http://video.dot.state.mn.us/video/image?id=908", "http://video.dot.state.mn.us/video/image?id=907", "http://video.dot.state.mn.us/video/image?id=906", "http://video.dot.state.mn.us/video/image?id=905", "http://video.dot.state.mn.us/video/image?id=904", "http://video.dot.state.mn.us/video/image?id=903"}
	, {"http://video.dot.state.mn.us/video/image?id=443", "http://video.dot.state.mn.us/video/image?id=442", "http://video.dot.state.mn.us/video/image?id=441", "http://video.dot.state.mn.us/video/image?id=440", "http://video.dot.state.mn.us/video/image?id=439", "http://video.dot.state.mn.us/video/image?id=438", "http://video.dot.state.mn.us/video/image?id=437", "http://video.dot.state.mn.us/video/image?id=436", "http://video.dot.state.mn.us/video/image?id=435", "http://video.dot.state.mn.us/video/image?id=434", "http://video.dot.state.mn.us/video/image?id=433", "http://video.dot.state.mn.us/video/image?id=432", "http://video.dot.state.mn.us/video/image?id=431", "http://video.dot.state.mn.us/video/image?id=430", "http://video.dot.state.mn.us/video/image?id=429", "http://video.dot.state.mn.us/video/image?id=428", "http://video.dot.state.mn.us/video/image?id=427", "http://video.dot.state.mn.us/video/image?id=426", "http://video.dot.state.mn.us/video/image?id=425", "http://video.dot.state.mn.us/video/image?id=424", "http://video.dot.state.mn.us/video/image?id=423", "http://video.dot.state.mn.us/video/image?id=422", "http://video.dot.state.mn.us/video/image?id=421", "http://video.dot.state.mn.us/video/image?id=420", "http://video.dot.state.mn.us/video/image?id=419", "http://video.dot.state.mn.us/video/image?id=418", "http://video.dot.state.mn.us/video/image?id=417", "http://video.dot.state.mn.us/video/image?id=416", "http://video.dot.state.mn.us/video/image?id=415", "http://video.dot.state.mn.us/video/image?id=414", "http://video.dot.state.mn.us/video/image?id=413", "http://video.dot.state.mn.us/video/image?id=412", "http://video.dot.state.mn.us/video/image?id=411", "http://video.dot.state.mn.us/video/image?id=410", "http://video.dot.state.mn.us/video/image?id=409", "http://video.dot.state.mn.us/video/image?id=408", "http://video.dot.state.mn.us/video/image?id=407", "http://video.dot.state.mn.us/video/image?id=405", "http://video.dot.state.mn.us/video/image?id=404", "http://video.dot.state.mn.us/video/image?id=402", "http://video.dot.state.mn.us/video/image?id=401", "http://video.dot.state.mn.us/video/image?id=400", "http://video.dot.state.mn.us/video/image?id=399"}
	, {"http://video.dot.state.mn.us/video/image?id=725", "http://video.dot.state.mn.us/video/image?id=724", "http://video.dot.state.mn.us/video/image?id=723", "http://video.dot.state.mn.us/video/image?id=722", "http://video.dot.state.mn.us/video/image?id=721", "http://video.dot.state.mn.us/video/image?id=720", "http://video.dot.state.mn.us/video/image?id=719", "http://video.dot.state.mn.us/video/image?id=718", "http://video.dot.state.mn.us/video/image?id=717", "http://video.dot.state.mn.us/video/image?id=716", "http://video.dot.state.mn.us/video/image?id=715", "http://video.dot.state.mn.us/video/image?id=714", "http://video.dot.state.mn.us/video/image?id=713", "http://video.dot.state.mn.us/video/image?id=712", "http://video.dot.state.mn.us/video/image?id=711", "http://video.dot.state.mn.us/video/image?id=710", "http://video.dot.state.mn.us/video/image?id=709", "http://video.dot.state.mn.us/video/image?id=708", "http://video.dot.state.mn.us/video/image?id=707", "http://video.dot.state.mn.us/video/image?id=706", "http://video.dot.state.mn.us/video/image?id=705", "http://video.dot.state.mn.us/video/image?id=704", "http://video.dot.state.mn.us/video/image?id=703", "http://video.dot.state.mn.us/video/image?id=702", "http://video.dot.state.mn.us/video/image?id=701"}
	, {"http://video.dot.state.mn.us/video/image?id=869", "http://video.dot.state.mn.us/video/image?id=868", "http://video.dot.state.mn.us/video/image?id=867", "http://video.dot.state.mn.us/video/image?id=866", "http://video.dot.state.mn.us/video/image?id=865", "http://video.dot.state.mn.us/video/image?id=864", "http://video.dot.state.mn.us/video/image?id=863", "http://video.dot.state.mn.us/video/image?id=862", "http://video.dot.state.mn.us/video/image?id=861", "http://video.dot.state.mn.us/video/image?id=860", "http://video.dot.state.mn.us/video/image?id=859", "http://video.dot.state.mn.us/video/image?id=858", "http://video.dot.state.mn.us/video/image?id=857", "http://video.dot.state.mn.us/video/image?id=856", "http://video.dot.state.mn.us/video/image?id=855", "http://video.dot.state.mn.us/video/image?id=854", "http://video.dot.state.mn.us/video/image?id=853", "http://video.dot.state.mn.us/video/image?id=852", "http://video.dot.state.mn.us/video/image?id=851", "http://video.dot.state.mn.us/video/image?id=850", "http://video.dot.state.mn.us/video/image?id=849", "http://video.dot.state.mn.us/video/image?id=848", "http://video.dot.state.mn.us/video/image?id=847", "http://video.dot.state.mn.us/video/image?id=846", "http://video.dot.state.mn.us/video/image?id=845", "http://video.dot.state.mn.us/video/image?id=844", "http://video.dot.state.mn.us/video/image?id=843", "http://video.dot.state.mn.us/video/image?id=842", "http://video.dot.state.mn.us/video/image?id=841", "http://video.dot.state.mn.us/video/image?id=840", "http://video.dot.state.mn.us/video/image?id=839", "http://video.dot.state.mn.us/video/image?id=838", "http://video.dot.state.mn.us/video/image?id=837", "http://video.dot.state.mn.us/video/image?id=836", "http://video.dot.state.mn.us/video/image?id=835", "http://video.dot.state.mn.us/video/image?id=834", "http://video.dot.state.mn.us/video/image?id=833", "http://video.dot.state.mn.us/video/image?id=832", "http://video.dot.state.mn.us/video/image?id=825", "http://video.dot.state.mn.us/video/image?id=824", "http://video.dot.state.mn.us/video/image?id=823", "http://video.dot.state.mn.us/video/image?id=822", "http://video.dot.state.mn.us/video/image?id=821", "http://video.dot.state.mn.us/video/image?id=820", "http://video.dot.state.mn.us/video/image?id=819", "http://video.dot.state.mn.us/video/image?id=818", "http://video.dot.state.mn.us/video/image?id=817", "http://video.dot.state.mn.us/video/image?id=816", "http://video.dot.state.mn.us/video/image?id=815", "http://video.dot.state.mn.us/video/image?id=814", "http://video.dot.state.mn.us/video/image?id=813", "http://video.dot.state.mn.us/video/image?id=812", "http://video.dot.state.mn.us/video/image?id=811", "http://video.dot.state.mn.us/video/image?id=810", "http://video.dot.state.mn.us/video/image?id=809", "http://video.dot.state.mn.us/video/image?id=808", "http://video.dot.state.mn.us/video/image?id=807", "http://video.dot.state.mn.us/video/image?id=806", "http://video.dot.state.mn.us/video/image?id=805", "http://video.dot.state.mn.us/video/image?id=804", "http://video.dot.state.mn.us/video/image?id=803", "http://video.dot.state.mn.us/video/image?id=802", "http://video.dot.state.mn.us/video/image?id=801", "http://video.dot.state.mn.us/video/image?id=797", "http://video.dot.state.mn.us/video/image?id=796", "http://video.dot.state.mn.us/video/image?id=793", "http://video.dot.state.mn.us/video/image?id=792", "http://video.dot.state.mn.us/video/image?id=788", "http://video.dot.state.mn.us/video/image?id=787"}
	, {"http://video.dot.state.mn.us/video/image?id=1651", "http://video.dot.state.mn.us/video/image?id=1645", "http://video.dot.state.mn.us/video/image?id=1642", "http://video.dot.state.mn.us/video/image?id=673", "http://video.dot.state.mn.us/video/image?id=672", "http://video.dot.state.mn.us/video/image?id=671", "http://video.dot.state.mn.us/video/image?id=670", "http://video.dot.state.mn.us/video/image?id=668", "http://video.dot.state.mn.us/video/image?id=667", "http://video.dot.state.mn.us/video/image?id=666", "http://video.dot.state.mn.us/video/image?id=665", "http://video.dot.state.mn.us/video/image?id=664", "http://video.dot.state.mn.us/video/image?id=663", "http://video.dot.state.mn.us/video/image?id=662", "http://video.dot.state.mn.us/video/image?id=661", "http://video.dot.state.mn.us/video/image?id=660"}
	, {"http://video.dot.state.mn.us/video/image?id=225", "http://video.dot.state.mn.us/video/image?id=224", "http://video.dot.state.mn.us/video/image?id=223", "http://video.dot.state.mn.us/video/image?id=222", "http://video.dot.state.mn.us/video/image?id=221", "http://video.dot.state.mn.us/video/image?id=220", "http://video.dot.state.mn.us/video/image?id=219", "http://video.dot.state.mn.us/video/image?id=218", "http://video.dot.state.mn.us/video/image?id=217", "http://video.dot.state.mn.us/video/image?id=216", "http://video.dot.state.mn.us/video/image?id=215", "http://video.dot.state.mn.us/video/image?id=214", "http://video.dot.state.mn.us/video/image?id=213", "http://video.dot.state.mn.us/video/image?id=212", "http://video.dot.state.mn.us/video/image?id=210", "http://video.dot.state.mn.us/video/image?id=209"}
	, {"http://video.dot.state.mn.us/video/image?id=108"}
	, {"http://video.dot.state.mn.us/video/image?id=396", "http://video.dot.state.mn.us/video/image?id=395", "http://video.dot.state.mn.us/video/image?id=394", "http://video.dot.state.mn.us/video/image?id=393", "http://video.dot.state.mn.us/video/image?id=392", "http://video.dot.state.mn.us/video/image?id=391", "http://video.dot.state.mn.us/video/image?id=390", "http://video.dot.state.mn.us/video/image?id=389"}
	, {"http://video.dot.state.mn.us/video/image?id=1717", "http://video.dot.state.mn.us/video/image?id=8", "http://video.dot.state.mn.us/video/image?id=7", "http://video.dot.state.mn.us/video/image?id=6", "http://video.dot.state.mn.us/video/image?id=5", "http://video.dot.state.mn.us/video/image?id=4", "http://video.dot.state.mn.us/video/image?id=3", "http://video.dot.state.mn.us/video/image?id=2", "http://video.dot.state.mn.us/video/image?id=1"}
	, {"http://video.dot.state.mn.us/video/image?id=100"}
	, {"http://video.dot.state.mn.us/video/image?id=561", "http://video.dot.state.mn.us/video/image?id=560", "http://video.dot.state.mn.us/video/image?id=559", "http://video.dot.state.mn.us/video/image?id=558", "http://video.dot.state.mn.us/video/image?id=557", "http://video.dot.state.mn.us/video/image?id=556", "http://video.dot.state.mn.us/video/image?id=555", "http://video.dot.state.mn.us/video/image?id=554", "http://video.dot.state.mn.us/video/image?id=553", "http://video.dot.state.mn.us/video/image?id=552", "http://video.dot.state.mn.us/video/image?id=551", "http://video.dot.state.mn.us/video/image?id=550"}
	, {"http://video.dot.state.mn.us/video/image?id=109"}
	, {"http://video.dot.state.mn.us/video/image?id=1715", "http://video.dot.state.mn.us/video/image?id=1714", "http://video.dot.state.mn.us/video/image?id=1713", "http://video.dot.state.mn.us/video/image?id=403"}
	, {"http://video.dot.state.mn.us/video/image?id=669"}
	, {"http://video.dot.state.mn.us/video/image?id=123", "http://video.dot.state.mn.us/video/image?id=122", "http://video.dot.state.mn.us/video/image?id=121", "http://video.dot.state.mn.us/video/image?id=120", "http://video.dot.state.mn.us/video/image?id=119", "http://video.dot.state.mn.us/video/image?id=118", "http://video.dot.state.mn.us/video/image?id=117", "http://video.dot.state.mn.us/video/image?id=116", "http://video.dot.state.mn.us/video/image?id=115", "http://video.dot.state.mn.us/video/image?id=114", "http://video.dot.state.mn.us/video/image?id=113", "http://video.dot.state.mn.us/video/image?id=112", "http://video.dot.state.mn.us/video/image?id=111", "http://video.dot.state.mn.us/video/image?id=110"}
	, {"http://video.dot.state.mn.us/video/image?id=512", "http://video.dot.state.mn.us/video/image?id=511", "http://video.dot.state.mn.us/video/image?id=510", "http://video.dot.state.mn.us/video/image?id=509", "http://video.dot.state.mn.us/video/image?id=508", "http://video.dot.state.mn.us/video/image?id=507", "http://video.dot.state.mn.us/video/image?id=506", "http://video.dot.state.mn.us/video/image?id=505", "http://video.dot.state.mn.us/video/image?id=504", "http://video.dot.state.mn.us/video/image?id=503"}
	, {"http://video.dot.state.mn.us/video/image?id=902", "http://video.dot.state.mn.us/video/image?id=901"}
	, {"http://video.dot.state.mn.us/video/image?id=337", "http://video.dot.state.mn.us/video/image?id=336", "http://video.dot.state.mn.us/video/image?id=335", "http://video.dot.state.mn.us/video/image?id=334", "http://video.dot.state.mn.us/video/image?id=333", "http://video.dot.state.mn.us/video/image?id=332", "http://video.dot.state.mn.us/video/image?id=331", "http://video.dot.state.mn.us/video/image?id=330", "http://video.dot.state.mn.us/video/image?id=329", "http://video.dot.state.mn.us/video/image?id=328", "http://video.dot.state.mn.us/video/image?id=327", "http://video.dot.state.mn.us/video/image?id=326", "http://video.dot.state.mn.us/video/image?id=325", "http://video.dot.state.mn.us/video/image?id=324", "http://video.dot.state.mn.us/video/image?id=323", "http://video.dot.state.mn.us/video/image?id=322", "http://video.dot.state.mn.us/video/image?id=321", "http://video.dot.state.mn.us/video/image?id=320", "http://video.dot.state.mn.us/video/image?id=319", "http://video.dot.state.mn.us/video/image?id=318"}
	};}