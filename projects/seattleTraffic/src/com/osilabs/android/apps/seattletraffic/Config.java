package com.osilabs.android.apps.seattletraffic;

public final class Config {
	// Tell classes to dump to logcat
	public static final int     DEBUG = 0;
	
	// This can also be accomplished by recompiling. Also by stopping the app
	public static final boolean DEBUG_FORCE_NEW_VERSION_CHECK = false; 

	// True will hide the ads
	public static final boolean NO_ADS = false; 

	// Used by web content versin checker to know which app is checking in
	public static final    String APP_CODE = "st"; // i.e. tct, st, ld ...
	
	public static final    String DEFAULT_CAMERA_INDEX = "0";
	public static final int       DEFAULT_MAP_INDEX = 0; // !!! Can't be above array size-1. Includes favorites, best to set to one of the system traffic maps indexes.
	public static final int       DEFAULT_CALENDAR_INDEX = 0;
	public static final int       DEFAULT_TAB_INDEX = 0;
	protected static       String MOBILECONTENT_URL_PREFIX   = "http://osilabs.com/m/mobilecontent/seattletraffic";
	protected static       String MOBILECONTENT_URL_ABOUT    = "http://osilabs.com/m/mobilecontent/about/shared_about.php";
	protected static final String NAMESPACE = "com.osilabs.android.apps.seattletraffic";

	// Which radios to offer
	// Use './adb logcat |grep node' to see the scanner ids
	public static final int            INDEX_OF_WEATHER = 0;
	public static final int            INDEX_OF_POLICE = 1;
	
	// These are the Defaults and they will be changed as prefs change. Set current node to -1 to disable.
	public static final int[]          RADIOS_DEFAULT_NODE = {20034,           22577};
	public static       int[]          RADIOS_CURRENT_NODE = {20034,           22577};
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
    	"Seattle",
    	"Tacoma",
    	"Puget Sound",
        "Full Traffic Map",
        "North of Seattle",
        "North of Seattle (sm)",
        "Bridges",
        "Bridges (sm)",
        "South of Seattle",
        "South of Seattle (sm)",
        "Weather: Road Temperatures",
        "Weather: Weather Radar",
        "Weather: Sattelite",
        "Ferries: Mukilteo/Clinton",
        "Ferries: Edmonds/Kingston",
        "Ferries: Seattle/Bainbridge",
        "Ferries: Seattle",
        "Ferries: Fauntleroy/Vashon/Southworth",

    };
    public static final String[] traffic_urls = {
        "{\"longitude\":\"-122171027\",\"zoom\":\"11\",\"label\":\"Seattle\",\"latitude\":\"47632586\"}",
        "{\"longitude\":\"-122386624\",\"zoom\":\"11\",\"label\":\"Tacoma\",\"latitude\":\"47313261\"}",
        "{\"longitude\":\"-122197096\",\"zoom\":\"10\",\"label\":\"Tacoma\",\"latitude\":\"47516769\"}",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    	"image",
    };
    public static final int [] traffic_viewtypes = {
    	MAP,
    	MAP,
    	MAP,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE,
    	IMAGE
    };
	// This defaults to one of the system configured mapviews in traffic_urls
    // CAUTION!!! - If you move the maps around, this could refer to the wrong index
	public static final String DEFAULT_MAPVIEW_COORDS = traffic_urls[0];
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
      "Road Incidents",
      "Blewett pass",
      "Satus pass",
      "Snoqualmie pass",
      "Stevens pass",
      "White pass"
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
	
	public static final String[] mainroads = {"I-5", "I-205", "I-405", "I-90", "I-82", "US 2", "US 12", "US 97", "US 97A", "US 101", "US 195", "US 395", "US 395 NSC", "SR 3", "SR 4", "SR 8", "SR 9", "SR 14", "SR 16", "SR 17", "SR 18", "SR 20", "SR 24", "SR 28", "SR 31", "SR 96", "SR 99", "SR 104", "SR 109", "SR 167", "SR 169", "SR 221", "SR 240", "SR 285", "SR 290", "SR 291", "SR 410", "SR 433", "SR 500", "SR 502", "SR 504", "SR 516", "SR 518", "SR 520", "SR 522", "SR 525", "SR 526", "SR 532", "SR 539", "SR 543", "SR 900", "SR 902", "Ferries", "City and County Cams", "Airports"};
	 
	public static final String[] mainroadsValues = {"I_5", "I_205", "I_405", "I_90", "I_82", "US_2", "US_12", "US_97", "US_97A", "US_101", "US_195", "US_395", "US_395_NSC", "SR_3", "SR_4", "SR_8", "SR_9", "SR_14", "SR_16", "SR_17", "SR_18", "SR_20", "SR_24", "SR_28", "SR_31", "SR_96", "SR_99", "SR_104", "SR_109", "SR_167", "SR_169", "SR_221", "SR_240", "SR_285", "SR_290", "SR_291", "SR_410", "SR_433", "SR_500", "SR_502", "SR_504", "SR_516", "SR_518", "SR_520", "SR_522", "SR_525", "SR_526", "SR_532", "SR_539", "SR_543", "SR_900", "SR_902", "Ferries", "City_and_County_Cams", "Airports"};
	 
	public static final String[][] crossroads = {{"I-5 and SR-14", "I-5 Interstate Bridge S", "I-5 Interstate  Bridge N", "I-5 and Mill Plain", "I-5 at Marine Drive", "I-5 and 29th", "I-5 at 35th St", "Main St, Hazel Dell", "I-5 at 63rd", "I-5 and 78th", "I-5 and 99th", "I-5 and 134th", "I-5 at 129th", "SR502 at I-5 SB on ramp", "I-5 at SR502 NB on ramp", "I-5 NB at 199th", "I-5 at Paradise Point", "I-5: Rush Rd", "I-5: MP 73.25", "I-5: Labree Rd", "I-5: 13th Ave", "I-5 at Custer Way (Olympia)", "I-5 Capitol Blvd (Olympia)", "I-5 Pacific Ave (Olympia)", "I-5 at Eastside St (Olympia)", "I-5 at Sleater-Kinney (Olympia)", "I-5 at Martin Way (Olympia)", "Nisqually  on I 5 at MP 114", "I-5: Thorne Lane", "I-5: Gravelly Lk Dr", "I-5: Bridgeport Way", "I-5/SR 512 Interchange", "S 72nd/74th St", "S 38th St", "I-5/SR 16 Interchange", "Pacific Ave", "I-5 and L St", "Puyallup River Bridge", "Port of Tacoma Rd", "I-5: Porter Way", "Emerald St", "I-5 / SR 18 Interchange", "Enchanted Pkwy", "S 320th St", "S 260th St", "I-5/SR 516 Interchange", "S 200th St", "S 188th St", "S 178th St", "Southcenter", "I-5/SR 599 Interchange", "Duwamish River", "Boeing Access Rd", "Rose St (Mid-Boeing Field)", "Albro Pl", "S Spokane St", "S Holgate St", "I-5/I-90 Interchange (4th Ave S-EB)", "Yesler Way", "Pine St", "Mercer St", "Roanoke St", "NE 45th St", "Lake City Way", "NE 85th St", "NE Northgate Way", "NE 175th St, SB", "NE 195th St", "236th St SW", "220th St SW", "44th Ave W", "Alderwood (I-5/405 Interchange)", "164th St SW", "148th St SW", "134th St SW", "128th St SW", "112th St SW, NB", "106th St SW", "SR 526", "I-5/I-405 Interchange (Southcenter)", "36th St SE", "US 2", "1-5 / 228th St NE", "I-5 / SR 532", "I-5 / Anderson Rd (North)", "I-5 / Anderson Rd (South)", "I-5 / North of Kincaid St (North)", "I-5 / North of Kincaid St (South)", "I-5 / College Way Ramps (East)", "I-5 / College Way (North)", "I-5 / SR 20", "Lake Samish Park and Ride ", "South of Old Fairhaven Parkway (north)", "Old Fairhaven Parkway", "Lakeway Dr", "Iowa St", "Sunset Dr", "Meridian St", "Bakerview Rd", "Smith Rd (North)", "Smith Rd (South)", "Loomis Trail Rd", "W 88th St", "Peace Portal Dr", "SR 543", "Mitchell Ave", "D St Offramp", "D St", "Peace Arch"}
	, {"I-205 at 18th St", "I-205 at 78th St", "I-205 at Padden", "I-205 at Government Island", "I-205 and SR-14", "I-205 and Mill Plain", "I-205 and Mill Plain Construction", "I-205 at SR 500 "}
	, {"Andover Park W", "I-405 at Everett", "Longacres Dr", "West Valley Hwy (SR 181)", "I-405/SR 167 Interchange", "Oaksdale Ave", "SR 169", "Sunset Blvd", "NE 44th St", "I-90/I-405 Interchange(Factoria)", "SE 8th St", "SE 20th St, NB", "SE 9th Pl", "SR 520, NB", "NE 85th St", "NE 124th St", "NE 160th St", "SR 522, SB", "204th St SE, NB", "213th St SE", "I-405/SR 524 Interchange (Filbert Rd)"}
	, {"4th Ave S, EB", "Airport Way, WB", "12th Ave S, WB", "12th Ave S, EB", "18th Ave S", "Corwin Pl S", "I 90 Bridge Midspan", "76th Ave SE", "77th Ave SE", "Island Crest Way", "Luther Burbank", "Shorewood", "Bellevue Way SE", "142nd Pl SE", "163rd Ave SE", "150th Ave SE", "192nd Ave SE", "W Lk Samm Pkwy", "SR 900", "Front St EB Ramp", "Sunset Way", "246th Ave SE, WB", "246th Ave SE, EB", "Franklin Falls on I-90 at MP51.3", "Snoqualmie Summit on I-90 at MP52", "Hyak on I-90 at MP55.2", "Price Creek on I-90 at MP61", "Easton Hill on I-90 at MP67.4", "Easton on I-90 at MP70.6", "Bullfrog-facing west on I-90 at MP 79.54", "Bullfrog-facing east on I-90 at MP 79.54", "Cle Elum on I-90 at MP 84.6", "Rocky Canyon on I-90 at MP 96.2", "Ellensburg I-90 at MP 110", "Ryegrass Summit on I-90 at MP 125", "Vantage West ", "Vantage East", "Silica Road on I-90 at MP 144", "Dodson Road on I-90 at MP 164.5", "Warden Interchange on I-90 at MP 189", "I-90/US 395 Interchange at Ritzville", "I-90/US 395 Interchange at Ritzville (2)", "I-90/US 395 Interchange at Ritzville (3)", "I-90/US 395 Interchange at Ritzville (4)", "I-90/US 395 Interchange at Ritzville (5)", "I-90/US 395 Interchange at Ritzville (6)", "I-90/US 395 Interchange at Ritzville (7)", "I-90/US 395 Interchange at Ritzville (8)", "Tyler Interchange on I-90 at MP 257 (2)", "Tyler Interchange on I-90 at MP 257 (3)", "Tyler Interchange on I-90 at MP 257 (4)", "Tyler Interchange on I-90 at MP 257 (5)", "Tyler Interchange on I-90 at MP 257 (6)", "Tyler Interchange on I-90 at MP 257 (7)", "Tyler Interchange on I-90 at MP 257 (8)", "Tyler Interchange on I-90 at MP 257", "I-90/SR-902", "Thomas Mallen Rd.", "Geiger Rd.", "US-2/I-90", "I-90/US 2 Interchange", "I-90 and US 195", "I-90 / Jefferson", "3rd / Maple", "3rd / Washington", "I-90 / Division #2", "I-90 / Division #1", "I-90 / Arthur St", "I-90 / Hamilton St", "Havana St", "Freya St", "Sprague Ave", "Broadway Ave", "Park Rd", "Fancher Rd", "Argonne Rd", "I-90 / University", "I-90 / Pines Rd", "Evergreen and I-90", "Sullivan and I-90", "2nd / Browne", "Liberty Lake on I-90 at MP 296 (2)", "Liberty Lake on I-90 at MP 296 (3)", "Liberty Lake on I-90 at MP 296 (4)", "Liberty Lake on I-90 at MP 296 (5)", "Liberty Lake on I-90 at MP 296 (6)", "Liberty Lake on I-90 at MP 296 (7)", "Liberty Lake on I-90 at MP 296 (8)", "Liberty Lake on I-90 at MP 296", "2nd / Monroe", "Denny Creek on I-90 at MP46.8", "East Cle Elum I-90 mp 86 looking East", "East Cle Elum I-90 mp 86 looking West", "Elk Heights on I-90 at MP 92", "Elk Heights I-90 mp 93 looking west", "Elk Heights I-90 mp 93 looking east"}
	, {"Manastash Ridge Summit on I-82 at MP 7", "Selah Creek Rest Area", "I-82 - Valley Mall Blvd Interchange ", "Union Gap on I-82", "Zillah at I-82", "Outlook at I-82", "Prosser at I-82", "I-82 at I-182", "Horse Heaven on I-82 at MP 121.2", "Plymouth Port of Entry"}
	, {"Homeacres Rd", "US2 at SR9 (north)", "US2 at SR9 (south)", "US 2/SR 522", "Kelsey St", "Lewis St (SR 203)", "Main St", "US 2 at Old Owen Rd", "US 2 at 5th St", "US 2 at Sultan Basin Rd", "Old Faithful at MP 62", "Stevens Pass East Summit at MP 65", "Stevens Pass West Summit at MP 65", "Winton on US 2 at MP 86.6", "Leavenworth at Riverbend Dr.", "US97/US2 Junction (Big Y).", "Wenatchee on US 2 at MP 119", "Waterville", "Fairchild looking East", "Fairchild looking west", "US2/Farwell Rd"}
	, {"White Pass Summit on US12 at MP 150.9", "US12 - SR410", "Delaney on US 12 at MP 381.3", "Alpowa Summit on US-12 at MP 413"}
	, {"Maryhill", "Satus Pass", "YakimaCountyLine  US 97 at MP 34", "Blewett Pass", "US2/US97 Junction(Big Y)", "Odabashian Bridge - East", "Riverside on US-97 at MP 299", "US 97 Border Approach-Oroville - North", "US 97 Border Approach-Oroville - South", "US 97 Border-Oroville - North", "US 97 Border-Oroville - South"}
	, {"Odabashian Bridge - North", "Knapps Tunnel on US 97A at MP 226", "Chelan on US 97 at MP 240.2 "}
	, {"Astoria-Megler Bridge on US 101 ", "Cosmopolis Hill on US-101 at MP 78", "Simpson Ave Bridge South", "Simpson Ave Bridge North", "State Camp Hill on US-101 at MP 100.5", "Queets on  SR 101 at MP 151.7", "Queets on  SR 101 at MP 151.7 21", "Heckelsville Shed on US 101 at MP 214", "Indian Valley on US 101 at MP 237.2", "Seibert on US 101 at MP 256", "Mt Walker, SR-101at 301.5 ", "Mt Walker, SR-101at 301.5 2", "Mt Walker, SR-101at 301.5 3", "Oyster Bay Rd. on SR 101 at MP 359", "US 101 at Crosby Blvd"}
	, {"US-195 at Uniontown", "US-195 at Uniontown 2", "US-195 at Uniontown 3", "US-195 at Uniontown 4", "US-195 at Uniontown 5", "US-195 at Uniontown 6", "US-195 at Uniontown 7", "US-195 at Uniontown 8", "US 195 - Colfax", "US-195 at Spangle", "US-195 at Spangle 2", "US-195 at Spangle 3", "US-195 at Spangle 4", "US-195 at Spangle 5", "US-195 at Spangle 6", "US-195 at Spangle 7", "US-195 at Spangle 8"}
	, {"Blue Bridge on US 395 at MP 19", "Little Spokane River on SR-395 at MP 168", "Little Spokane River on SR-395 at MP 168 (2)", "Little Spokane River on SR-395 at MP 168 (3)", "Little Spokane River on SR-395 at MP 168 (4)", "Little Spokane River on SR-395 at MP 168 (5)", "Little Spokane River on SR-395 at MP 168 (6)", "Little Spokane River on SR-395 at MP 168 (7)", "Little Spokane River on SR-395 at MP 168 (8)", "Loon Lake Summit on US-395 at MP 188 (1)", "Loon Lake Summit on US-395 at MP 188 (2)", "Loon Lake Summit on US-395 at MP 188 (3)", "Loon Lake Summit on US-395 at MP 188 (4)", "Loon Lake Summit on US-395 at MP 188 (5)", "Loon Lake Summit on US-395 at MP 188 (6)", "Loon Lake Summit on US-395 at MP 188", "Loon Lake Summit on US-395 at MP 188 (8)", "US 395 - Colville - Birch", "US 395 - Colville - Canning", "Laurier on US-395 at MP 275 Position 2", "Laurier on US-395 at MP 275 Position 5", "Laurier on US-395 at MP 275 Position 6", "Laurier on US-395 at MP 275 Position 3", "Laurier on US-395 at MP 275 Position 4", "Laurier on US-395 at MP 275 Position 8", "Laurier on US-395 at MP 275 Position 7", "Laurier on US-395 at MP 275"}
	, {"NSC 395 and Lincoln", "NSC 395 and Fairview", "NSC 395 and Gerlach", "NSC 395 and Market", "NSC 395  and Parksmith", "NSC 395 and Farwell"}
	, {"SR 3 at Christopherson", "South of Hood Canal Bridge"}
	, {"KM Mountain on SR-4 at MP 22"}
	, {"Rock Candy Mountain on SR-8 at MP 16.1"}
	, {"SR 522", "SR 524", "Front St", "Abbotsford (Sumas) SR-9"}
	, {"SR14 at  Lieser Rd", "SR-14 East of I-205", "SR-14 West of I-205", "CapeHorn", "Hood River Bridge on SR 14"}
	, {"I-5/SR 16 Interchange", "SR 16: Center St", "SR 16/ SR I63 Interchange(Pearl St", "Tacoma Narrows East", "SR 16 - Sixth Ave", "SR 16: Jackson", "SR 16: 36th Street", "SR 16: Olympic Drive", "Tacoma Narrows West", "SR 16: Wollochet", "SR 16: Toll Plaza", "SR 16: Stone Dr", "SR 16 - Burnham Drive", "SR 16 / SR 302", "SR 16: South 19th St"}
	, {"Mansfield on SR-17 at MP 112"}
	, {"SR 18 at I-5", "Tiger Mountain"}
	, {"SR 20 at Garrett Rd", "SR 20 at Burlington Blvd", "Newhalem", "LoupLoup Pass", "Sherman Pass on SR-20 at MP 320"}
	, {"SR 24 at SR 241", "SR 24 at SR 241 Pos 2", "SR 24 at SR 241 Pos 3", "SR 24 at SR 241 Pos 4", "SR 24 at SR 241 Pos 6", "SR 24 at SR 241 Pos 5", "I-82 at SR 24"}
	, {"Sellar Bridge - East", "GrantRoad_Intersection", "Quincy Rest Area"}
	, {"Metaline Falls on SR 31 at MP 16.69"}
	, {"25th Ave SE", "35th Ave SE"}
	, {"W Marginal Way", "S Michigan St"}
	, {"West of Hood Canal Bridge", "Hood Canal", "SR 104 near 100th Ave W"}
	, {"Pt. Grenville on SR 109 at MP 36.5", "Pt. Grenville on SR 109 at MP 36.51"}
	, {"SR 167/SR 410 Interchange", "15th St NW", "S 277th St", "84th Ave S", "S 222nd St", "S 180th St"}
	, {"140th Way SE"}
	, {"Sellards Road-NB facing on SR 221 at MP13.5"}
	, {"SR 240 at Beloit Rd", "SR 240 at Twin Bridges", "SR 240/ Richland Wye at MP 38"}
	, {"Sellar Bridge - West", "Wenatchee River Bridge on SR 285"}
	, {"SR 290 / Hamilton"}
	, {"Charles Road on SR-291 at MP 9  Pos 7", "Charles Road on SR-291 at MP 9  Pos 6", "Charles Road on SR-291 at MP 9  Pos 5", "Charles Road on SR-291 at MP 9  Pos 1", "Charles Road on SR-291 at MP 9  Pos 3", "Charles Road on SR-291 at MP 9  Pos 2", "Charles Road on SR-291 at MP 9  Pos 4", "Charles Road on SR-291 at MP 9  Pos 8", "SR291 and Swenson Rd"}
	, {"Whistlin Jacks on SR 410"}
	, {"Lewis and Clark Bridge on SR-433"}
	, {"SR 500 at Thurston Way", "SR 500 at 112th and Gher Rd."}
	, {"SR502 at I-5 NB off ramp", "SR502 at 10th Ave."}
	, {"Forest Learning Rest Area andcopy;WSDOT"}
	, {"SR 516"}
	, {"S 154th St", "Airport Dr", "46th Ave S", "51st Ave S", "EB Under I-5"}
	, {"Delmar Dr", "Montlake Ramp", "West Highrise, E", "East Highrise", "92nd Ave NE", "Bellevue Way NE", "148th Ave NE", "NE 51st St", "W Lk Samm Pkwy", "Redmond Way"}
	, {"SR 522 / SR 104 Interchange", "S Campus Way", "I-405/SR 522 Interchange"}
	, {"SR99/SR525 Interchange"}
	, {"Evergreen Way"}
	, {"SR 532 / Sunrise Blvd", "SR 532 / 102nd Ave NW", "SR 532 / 92nd Ave NW", "SR 532 / 88th Ave NW", "SR 532 / Pioneer Hwy", "SR 532 / 72nd Ave NW"}
	, {"Pole Rd", "Wiser Lake (North)", "River Rd", "Badger Rd", "Border"}
	, {"Boblett St", "H St", "Duty Free", "D St", "Truck Spur"}
	, {"I-90/SR 900 Interchange ", "Maple St"}
	, {"T.J. Meenach Bridge"}
	, {"WSF Edmonds W Dayton St", "WSF Mukilteo Clover Ln", "Orcas Ferry", "WSF Anacortes Ferry Terminal", "WSF Anacortes Ferry Holding", "WSF Anacortes Ferry Terminal Rd", "WSF Clinton Terminal", "WSF Port Townsend Terminal", "WSF Kingston Terminal", "WSF Coupeville Terminal", "WSF Coupeville Street", "WSF Kingston Ferry Sign East", "WSF Port Townsend Street", "WSF Mukilteo N 525 at 76th", "Friday Harbor Ferry ", "WSF Edmonds VMS Sign", "WSF Edmonds Holding", "WSF Bainbridge Ferry Holding", "WSF Mukilteo S 525 at 76th", "WSF Kingston Ferry Sign West", "WSF Kingston Barber", "WSF Mukilteo South Holding", "WSF Edmonds Vessel", "WSF Bremerton Holding North", "WSF Edmonds Underpass", "WSF Mukilteo North Holding", "WSF Edmonds Pine", "WSF Bremerton Holding South", "WSF Bainbridge Ferry Holding", "WSF Fauntleroy Ferry Holding", "WSF Southworth Ferry Holding", "WSF Tahlequah Ferry Holding", "WSF Tahlequah at SW Tahlequah Rd", "WSF Vashon Hwy and Bunker (North)", "WSF Vashon Hwy and Bunker (South)", "WSF Vashon Hwy and 112th (North)", "WSF Vashon Hwy and 112th (South)"}
	, {"18th and Ray Spokane", "Downtown Vancouver", "Mt. Rainier", "Port Townsend Airport", "Colfax", "College at Martin Way (Lacey)", "Plum at Union (Olympia)", "Sleater-Kinney at Martin Way (Olympia)", "Leavenworth", "LaPush", "Long Beach", "Mount St. Helens", "Pullman", "Winthrop", "Morton ", "Stevenson Waterfront", "Walla Walla-Whitman College"}
	, {"Arlington Airport East", "Arlington Airport West", "Arlington Airport North", "Arlington Airport South", "Harvey Airfield(Snohomish) ", "Olympia Airport", "Skykomish Airport East", "Skykomish Airport SouthEast", "Skykomish Airport South", "Skykomish Airport West"}
	};
	 
	public static final String[][] crossroadsValues = {{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108"}
	, {"109", "110", "111", "112", "113", "114", "115", "116"}
	, {"117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137"}
	, {"138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199", "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219", "220", "221", "222", "223", "224", "225", "226", "227", "228", "229", "230", "231", "232", "233"}
	, {"234", "235", "236", "237", "238", "239", "240", "241", "242", "243"}
	, {"244", "245", "246", "247", "248", "249", "250", "251", "252", "253", "254", "255", "256", "257", "258", "259", "260", "261", "262", "263", "264"}
	, {"265", "266", "267", "268"}
	, {"269", "270", "271", "272", "273", "274", "275", "276", "277", "278", "279"}
	, {"280", "281", "282"}
	, {"283", "284", "285", "286", "287", "288", "289", "290", "291", "292", "293", "294", "295", "296", "297"}
	, {"298", "299", "300", "301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "311", "312", "313", "314"}
	, {"315", "316", "317", "318", "319", "320", "321", "322", "323", "324", "325", "326", "327", "328", "329", "330", "331", "332", "333", "334", "335", "336", "337", "338", "339", "340", "341"}
	, {"342", "343", "344", "345", "346", "347"}
	, {"348", "349"}
	, {"350"}
	, {"351"}
	, {"352", "353", "354", "355"}
	, {"356", "357", "358", "359", "360"}
	, {"361", "362", "363", "364", "365", "366", "367", "368", "369", "370", "371", "372", "373", "374", "375"}
	, {"376"}
	, {"377", "378"}
	, {"379", "380", "381", "382", "383"}
	, {"384", "385", "386", "387", "388", "389", "390"}
	, {"391", "392", "393"}
	, {"394"}
	, {"395", "396"}
	, {"397", "398"}
	, {"399", "400", "401"}
	, {"402", "403"}
	, {"404", "405", "406", "407", "408", "409"}
	, {"410"}
	, {"411"}
	, {"412", "413", "414"}
	, {"415", "416"}
	, {"417"}
	, {"418", "419", "420", "421", "422", "423", "424", "425", "426"}
	, {"427"}
	, {"428"}
	, {"429", "430"}
	, {"431", "432"}
	, {"433"}
	, {"434"}
	, {"435", "436", "437", "438", "439"}
	, {"440", "441", "442", "443", "444", "445", "446", "447", "448", "449"}
	, {"450", "451", "452"}
	, {"453"}
	, {"454"}
	, {"455", "456", "457", "458", "459", "460"}
	, {"461", "462", "463", "464", "465"}
	, {"466", "467", "468", "469", "470"}
	, {"471", "472"}
	, {"473"}
	, {"474", "475", "476", "477", "478", "479", "480", "481", "482", "483", "484", "485", "486", "487", "488", "489", "490", "491", "492", "493", "494", "495", "496", "497", "498", "499", "500", "501", "502", "503", "504", "505", "506", "507", "508", "509", "510"}
	, {"511", "512", "513", "514", "515", "516", "517", "518", "519", "520", "521", "522", "523", "524", "525", "526", "527"}
	, {"528", "529", "530", "531", "532", "533", "534", "535", "536", "537"}
	};
}	