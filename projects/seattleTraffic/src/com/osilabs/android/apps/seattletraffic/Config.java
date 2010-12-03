package com.osilabs.android.apps.seattletraffic;

public final class Config {
	public static final int DEFAULT_CAMERA_ID = 29;
	public static final int DEFAULT_MAP_INDEX = 0;
	public static final int DEFAULT_ALERT_INDEX = 0;
	
	public static final String CAMERA_URL_PREFIX = "http://images.wsdot.wa.gov/nw/small/090vc01581.jpg";

	// Maps
	public static final String[] maps = {
		"Traffic",
		"Road Temperatures"
	};
	public static final String[] maps_urls = {
		"http://images.wsdot.wa.gov/nwflow/flowmaps/video_map_SeattleMetro.gif",
		"http://images.wsdot.wa.gov/rweather/roadtemps/l2psm06.gif"
	};

	// Alerts
	public static final String[] alerts = {
		"Weather Alerts",
		"Blewett pass",
		"Satus pass",
		"Snoqualmie pass",
		"Stevens pass",
		"White pass"
	};
	public static final String[] alerts_src = {
		"http://www.weather.gov/alerts/wa.rss",
		"http://www.wsdot.wa.gov/traffic/passes/blewett/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/satus/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/snoqualmie/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/stevens/rss/",
		"http://www.wsdot.wa.gov/traffic/passes/white/rss/"
	};
	
	public static final String[] mainroads = {"I-5", "I-205", "I-405", "I-90", "I-82", "SR 538", "US 2", "US 12", "US 97", "US 97A", "US 101", "US 195", "US 395", "US 395 NSC", "SR 3", "SR 4", "SR 8", "SR 9", "SR 14", "SR 16", "SR 17", "SR 18", "SR 20", "SR 24", "SR 27", "SR 28", "SR 31", "SR 96", "SR 99", "SR 104", "SR 109", "SR 161", "SR 163", "SR 167", "SR 169", "SR 172", "SR 202", "SR 204", "SR 221", "SR 240", "SR 285", "SR 290", "SR 291", "SR 410", "SR 433", "SR 500", "SR 502", "SR 504", "SR 512", "SR 516", "SR 518", "SR 520", "SR 522", "SR 524", "SR 525", "SR 526", "SR 527", "SR 532", "SR 539", "SR 543", "SR 548", "SR 599", "SR 900", "SR 902", "SR 904", "US - Canadian Border", "Ferries", "City and County Cams", "Airports"};
	 
	public static final String[] mainroadsValues = {"I_5", "I_205", "I_405", "I_90", "I_82", "SR_538", "US_2", "US_12", "US_97", "US_97A", "US_101", "US_195", "US_395", "US_395_NSC", "SR_3", "SR_4", "SR_8", "SR_9", "SR_14", "SR_16", "SR_17", "SR_18", "SR_20", "SR_24", "SR_27", "SR_28", "SR_31", "SR_96", "SR_99", "SR_104", "SR_109", "SR_161", "SR_163", "SR_167", "SR_169", "SR_172", "SR_202", "SR_204", "SR_221", "SR_240", "SR_285", "SR_290", "SR_291", "SR_410", "SR_433", "SR_500", "SR_502", "SR_504", "SR_512", "SR_516", "SR_518", "SR_520", "SR_522", "SR_524", "SR_525", "SR_526", "SR_527", "SR_532", "SR_539", "SR_543", "SR_548", "SR_599", "SR_900", "SR_902", "SR_904", "US___Canadian_Border", "Ferries", "City_and_County_Cams", "Airports"};
	 
	public static final String[][] crossroads = {{"I-5 and SR-14", "I-5 Interstate Bridge S", "I-5 Interstate  Bridge N", "I-5 and Mill Plain", "I-5 at Marine Drive", "I-5 and 29th", "I-5 at 35th St", "Main St, Hazel Dell", "I-5 at 63rd", "I-5 and 78th", "I-5 and 99th", "I-5 and 134th", "I-5 at 129th", "SR502 at I-5 SB on ramp", "I-5 at SR502 NB on ramp", "I-5 NB at 199th", "I-5 at Paradise Point", "I-5: Rush Rd", "I-5: MP 73.25", "I-5: Labree Rd", "I-5: 13th Ave", "I-5 at Custer Way (Olympia)", "I-5 Capitol Blvd (Olympia)", "I-5 Pacific Ave (Olympia)", "I-5 at Eastside St (Olympia)", "I-5 at Sleater-Kinney (Olympia)", "I-5 at Martin Way (Olympia)", "Nisqually  on I 5 at MP 114", "I-5: Thorne Lane", "I-5: Gravelly Lk Dr", "I-5: Bridgeport Way", "I-5/SR 512 Interchange", "S 72nd/74th St", "S 38th St", "I-5/SR 16 Interchange", "Pacific Ave", "I-5 and L St", "Puyallup River Bridge", "Port of Tacoma Rd", "I-5: Porter Way", "Emerald St", "Pierce Co Line", "S 375th St", "I-5 / SR 18 Interchange", "Enchanted Pkwy", "S 333rd St", "S 320th St", "S 317th St", "S 308th St", "S 296th St", "S 288th St", "S 272nd St", "S 260th St", "S 248th St", "I-5/SR 516 Interchange", "S 216th St", "S 200th St", "S 188th St", "S 178th St", "Klickitat Rd", "Southcenter", "S 144th St", "I-5/SR 599 Interchange", "Duwamish River", "Boeing Access Rd", "Rose St (Mid-Boeing Field)", "S Webster St", "Albro Pl", "S Spokane St", "S Holgate St", "I-5/I-90 Interchange (4th Ave S-EB)", "Yesler Way", "Pine St", "Mercer St", "Roanoke St", "NE 45th St", "NE 42nd St Ramp", "NE Ravenna Blvd", "Lake City Way", "NE 85th St", "NE 107th St", "NE Northgate Way", "NE 130th St", "NE 145th St", "NE 175th St, SB", "NE 195th St", "236th St SW", "220th St SW", "44th Ave W", "Alderwood (I-5/405 Interchange)", "164th St, Ramp", "164th St SW", "148th St SW", "134th St SW", "128th St SW", "112th St SW, SB", "112th St SW, NB", "118th St SW", "106th St SW", "Everett Mall Way", "Broadway SB Ramp", "SR 526", "77th St SE", "73rd St SE, SB", "73rd St SE, NB", "I-5/I-405 Interchange (Southcenter)", "61st St SE, NB", "60th St SE, SB", "52nd St SE", "47th St SE", "43rd St SE", "41st St SE", "40th St SE", "36th St SE", "Pacific Ave", "Everett Ave", "US 2", "Marine View Dr", "I-5 at Ebey Slough", "I-5 / SR 531", "1-5 / 228th St NE", "I-5 / SR 532", "I-5 / Anderson Rd (North)", "I-5 / Anderson Rd (South)", "I-5 / North of Kincaid St (North)", "I-5 / North of Kincaid St (South)", "I-5 / College Way Ramps (East)", "I-5 / College Way (North)", "I-5 / SR 20", "Lake Samish Park and Ride ", "South of Old Fairhaven Parkway (north)", "Old Fairhaven Parkway", "Lakeway Dr", "Iowa St", "Sunset Dr", "Meridian St", "Bakerview Rd", "Smith Rd (North)", "Smith Rd (South)", "Loomis Trail Rd", "W 88th St", "Peace Portal Dr", "SR 543", "Mitchell Ave", "D St Offramp", "D St", "Peace Arch"}
	, {"I-205 at 18th St", "I-205 at 78th St", "I-205 at Padden", "I-205 at Government Island", "I-205 and SR-14", "I-205 and Mill Plain", "I-205 and Mill Plain Construction", "I-205 at SR 500 "}
	, {"Southcenter", "Andover Park W", "I-405 at Everett", "Longacres Dr", "West Valley Hwy (SR 181)", "I-405/SR 167 Interchange", "Oaksdale Ave", "Talbot Rd S", "SR 169", "Sunset Blvd", "NE Park Dr", "NE 30th St", "NE 24th St", "NE 44th St", "SE 64th St", "SE 72nd St", "SE 59th St", "112th Ave SE", "Coal Creek Pkwy", "SE 45th St", "SE 40th St", "I-90 I/C SE Quad", "I-90/I-405 Interchange(Factoria)", "SE 20th St, SB", "SE 8th St", "SE 20th St, NB", "SE 9th Pl", "NE 8th St", "Main St", "NE 4th St", "NE 6th St", "NE 14th St", "SR 520, NB", "SR 520, SB", "NE 40th St", "NE 53rd St", "NE 72nd Pl", "NE 85th St", "NE 116th St", "NE 100th St", "NE 124th St", "NE 132nd St", "NE 160th St", "NE 145th St", "SR 522, SB", "NE 195th St", "SR 522, NB", "236th St SE, SB", "236th St SE, NB", "232nd St SE", "I-405/SR 527 Interchange", "SR 527, Westside", "SR 527, Median", "216th St SE", "204th St SE, NB", "204th St SE, SB", "213th St SE", "I-405/SR 524 Interchange (Filbert Rd)", "Damson Rd", "Alderwood"}
	, {"8th Ave S, EB", "4th Ave S, EB", "Airport Way, WB", "12th Ave S, WB", "12th Ave S, EB", "23rd Ave S", "18th Ave S", "Corwin Pl S", "LVM WHR SW Q", "HMH WHR NW Q", "HMH WHR SE Q", "I 90 Bridge Midspan", "76th Ave SE", "77th Ave SE", "ICW Tunnel", "Island Crest Way", "Luther Burbank", "Shorewood", "E Mercer Way", "Bellevue Way SE", "142nd Pl SE", "163rd Ave SE", "150th Ave SE", "192nd Ave SE", "W Lk Samm Pkwy", "SR 900", "Front St", "Front St EB Ramp", "Sunset Way", "246th Ave SE, WB", "246th Ave SE, EB", "256th Ave SE", "Franklin Falls on I-90 at MP51.3", "Snoqualmie Summit on I-90 at MP52", "Hyak on I-90 at MP55.2", "Price Creek on I-90 at MP61", "Easton Hill on I-90 at MP67.4", "Easton on I-90 at MP70.6", "Bullfrog-facing west on I-90 at MP 79.54", "Bullfrog-facing east on I-90 at MP 79.54", "Cle Elum on I-90 at MP 84.6", "Rocky Canyon on I-90 at MP 96.2", "Ellensburg I-90 at MP 110", "Ryegrass Summit on I-90 at MP 125", "Vantage West ", "Vantage East", "Silica Road on I-90 at MP 144", "Dodson Road on I-90 at MP 164.5", "Warden Interchange on I-90 at MP 189", "I-90/US 395 Interchange at Ritzville", "I-90/US 395 Interchange at Ritzville (2)", "I-90/US 395 Interchange at Ritzville (3)", "I-90/US 395 Interchange at Ritzville (4)", "I-90/US 395 Interchange at Ritzville (5)", "I-90/US 395 Interchange at Ritzville (6)", "I-90/US 395 Interchange at Ritzville (7)", "I-90/US 395 Interchange at Ritzville (8)", "Tyler Interchange on I-90 at MP 257 (2)", "Tyler Interchange on I-90 at MP 257 (3)", "Tyler Interchange on I-90 at MP 257 (4)", "Tyler Interchange on I-90 at MP 257 (5)", "Tyler Interchange on I-90 at MP 257 (6)", "Tyler Interchange on I-90 at MP 257 (7)", "Tyler Interchange on I-90 at MP 257 (8)", "Tyler Interchange on I-90 at MP 257", "I-90/SR-902", "Thomas Mallen Rd.", "I-90/SR-902", "Geiger Rd.", "US-2/I-90", "I-90/US 2 Interchange", "I-90 and US 195", "I-90 / Jefferson", "3rd / Maple", "3rd / Washington", "I-90 / Division #2", "I-90 / Division #1", "I-90 / Arthur St", "I-90 / Hamilton St", "Havana St", "Freya St", "Sprague Ave", "Broadway Ave", "Park Rd", "Fancher Rd", "Argonne Rd", "I-90 / University", "I-90 / Pines Rd", "Evergreen and I-90", "Sullivan and I-90", "2nd / Browne", "Liberty Lake on I-90 at MP 296 (2)", "Liberty Lake on I-90 at MP 296 (3)", "Liberty Lake on I-90 at MP 296 (4)", "Liberty Lake on I-90 at MP 296 (5)", "Liberty Lake on I-90 at MP 296 (6)", "Liberty Lake on I-90 at MP 296 (7)", "Liberty Lake on I-90 at MP 296 (8)", "Liberty Lake on I-90 at MP 296", "2nd / Monroe", "Denny Creek on I-90 at MP46.8", "East Cle Elum I-90 mp 86 looking East", "East Cle Elum I-90 mp 86 looking West", "Elk Heights on I-90 at MP 92", "Elk Heights I-90 mp 93 looking west", "Elk Heights I-90 mp 93 looking east"}
	, {"Manastash Ridge Summit on I-82 at MP 7", "Selah Creek Rest Area", "I-82 - Valley Mall Blvd Interchange ", "Union Gap on I-82", "Zillah at I-82", "Outlook at I-82", "Prosser at I-82", "I-82 at I-182", "Horse Heaven on I-82 at MP 121.2", "Horse Heaven Pos 2 on I-82 at MP 121.2", "Plymouth Port of Entry"}
	, {"I-5 / College Way Ramps (East)"}
	, {"I-5 Interchange", "Homeacres Rd", "US 2/ SR 204 Interchange", "US2 at SR9 (north)", "US2 at SR9 (south)", "US 2/SR 522", "Kelsey St", "Lewis St (SR 203)", "Main St", "US 2 at Old Owen Rd", "US 2 at 5th St", "US 2 at Sultan Basin Rd", "Old Faithful at MP 62", "Stevens Pass East Summit at MP 65", "Stevens Pass West Summit at MP 65", "Winton on US 2 at MP 86.6", "Leavenworth at Riverbend Dr.", "US97/US2 Junction (Big Y).", "Wenatchee on US 2 at MP 119", "Waterville", "Fairchild looking East", "Fairchild looking west", "I-90/US 2 Interchange", "US2/Farwell Rd"}
	, {"White Pass Summit on US12 at MP 150.9", "US12 - SR410", "Delaney on US 12 at MP 381.3", "Alpowa Summit on US-12 at MP 413"}
	, {"Maryhill", "Satus Pass", "YakimaCountyLine  US 97 at MP 34", "Blewett Pass", "US2/US97 Junction(Big Y)", "Odabashian Bridge - East", "Riverside on US-97 at MP 299", "US 97 Border Approach-Oroville - North", "US 97 Border Approach-Oroville - South", "US 97 Border-Oroville - North", "US 97 Border-Oroville - South"}
	, {"Odabashian Bridge - North", "Knapps Tunnel on US 97A at MP 226", "Chelan on US 97 at MP 240.2 "}
	, {"Astoria-Megler Bridge on US 101 ", "Cosmopolis Hill on US-101 at MP 78", "Simpson Ave Bridge South", "Simpson Ave Bridge North", "State Camp Hill on US-101 at MP 100.5", "Queets on  SR 101 at MP 151.7", "Queets on  SR 101 at MP 151.7 21", "Heckelsville Shed on US 101 at MP 214", "Indian Valley on US 101 at MP 237.2", "Seibert on US 101 at MP 256", "Mt Walker, SR-101at 301.5 ", "Mt Walker, SR-101at 301.5 2", "Mt Walker, SR-101at 301.5 3", "Oyster Bay Rd. on SR 101 at MP 359", "US 101 at Crosby Blvd"}
	, {"US-195 at Uniontown", "US-195 at Uniontown 2", "US-195 at Uniontown 3", "US-195 at Uniontown 4", "US-195 at Uniontown 5", "US-195 at Uniontown 6", "US-195 at Uniontown 7", "US-195 at Uniontown 8", "US 195 - Colfax", "US-195 at Spangle", "US-195 at Spangle 2", "US-195 at Spangle 3", "US-195 at Spangle 4", "US-195 at Spangle 5", "US-195 at Spangle 6", "US-195 at Spangle 7", "US-195 at Spangle 8"}
	, {"Blue Bridge on US 395 at MP 19", "I-90/US 395 Interchange at Ritzville", "Little Spokane River on SR-395 at MP 168", "Little Spokane River on SR-395 at MP 168 (2)", "Little Spokane River on SR-395 at MP 168 (3)", "Little Spokane River on SR-395 at MP 168 (4)", "Little Spokane River on SR-395 at MP 168 (5)", "Little Spokane River on SR-395 at MP 168 (6)", "Little Spokane River on SR-395 at MP 168 (7)", "Little Spokane River on SR-395 at MP 168 (8)", "Loon Lake Summit on US-395 at MP 188 (1)", "Loon Lake Summit on US-395 at MP 188 (2)", "Loon Lake Summit on US-395 at MP 188 (3)", "Loon Lake Summit on US-395 at MP 188 (4)", "Loon Lake Summit on US-395 at MP 188 (5)", "Loon Lake Summit on US-395 at MP 188 (6)", "Loon Lake Summit on US-395 at MP 188", "Loon Lake Summit on US-395 at MP 188 (8)", "US 395 - Colville - Birch", "US 395 - Colville - Canning", "Laurier on US-395 at MP 275 Position 2", "Laurier on US-395 at MP 275 Position 5", "Laurier on US-395 at MP 275 Position 6", "Laurier on US-395 at MP 275 Position 3", "Laurier on US-395 at MP 275 Position 4", "Laurier on US-395 at MP 275 Position 8", "Laurier on US-395 at MP 275 Position 7", "Laurier on US-395 at MP 275"}
	, {"NSC 395 and Lincoln", "NSC 395 and Fairview", "NSC 395 and Gerlach", "NSC 395 and Market", "NSC 395  and Parksmith", "NSC 395 and Farwell"}
	, {"SR 3 at Christopherson", "South of Hood Canal Bridge"}
	, {"KM Mountain on SR-4 at MP 22"}
	, {"Rock Candy Mountain on SR-8 at MP 16.1"}
	, {"SR 522", "228th", "SR 524", "Front St", "Abbotsford (Sumas) SR-9"}
	, {"SR14 at  Lieser Rd", "SR-14 East of I-205", "SR-14 West of I-205", "CapeHorn", "Hood River Bridge on SR 14"}
	, {"I-5/SR 16 Interchange", "SR 16: Center St", "SR 16/ SR I63 Interchange(Pearl St", "Tacoma Narrows East", "SR 16 - Sixth Ave", "SR 16: Jackson", "SR 16: 36th Street", "SR 16: Olympic Drive", "Tacoma Narrows West", "SR 16: Wollochet", "SR 16: Toll Plaza", "SR 16: Stone Dr", "SR 16 - Burnham Drive", "SR 16 / SR 302", "SR 16: South 19th St"}
	, {"Mansfield on SR-17 at MP 112"}
	, {"SR 18 at I-5", "I-5 Interchange", "Weyerhaeuser Way", "42nd Ave S", "Peasley Canyon R", "West Valley Hwy", "SR-167", "C St", "Auburn Way", "SR 18/SR 516 Interchange", "Tiger Mountain"}
	, {"SR 20 at Garrett Rd", "SR 20 at Burlington Blvd", "Newhalem", "LoupLoup Pass", "Sherman Pass on SR-20 at MP 320"}
	, {"SR 24 at SR 241", "SR 24 at SR 241 Pos 2", "SR 24 at SR 241 Pos 3", "SR 24 at SR 241 Pos 4", "SR 24 at SR 241 Pos 6", "SR 24 at SR 241 Pos 5", "I-82 at SR 24"}
	, {"Pines and I-90 (North Bound)", "Pines and I-90 (South Bound)", "Pines and I-90 (East Bound)", "Pines and I-90 (West Bound)"}
	, {"Sellar Bridge - East", "GrantRoad_Intersection", "Quincy Rest Area"}
	, {"Metaline Falls on SR 31 at MP 16.69"}
	, {"25th Ave SE", "35th Ave SE"}
	, {"SR 99/SR 518 Interchange", "SR 599", "W Marginal Way", "S Michigan St", "SR 525"}
	, {"West of Hood Canal Bridge", "Hood Canal", "SR 104 near 100th Ave W", "SR 104"}
	, {"Pt. Grenville on SR 109 at MP 36.5", "Pt. Grenville on SR 109 at MP 36.51"}
	, {"Military Rd", "19th Way S"}
	, {"SR 16/ SR I63 Interchange(Pearl St)"}
	, {"SR 167/SR 410 Interchange", "15th St SW", "W Main St", "15th St NW", "37th St NW", "S 277th St", "Green River", "SR 516/ SR 167 interchange(Willis St)", "84th Ave S", "4th Ave N", "S 212th St", "S 222nd St", "S 180th St", "S 28th Pl", "I-405/SR 167 Interchange"}
	, {"140th Way SE", "I-405/SR 169 Interchange"}
	, {"Mansfield on SR-17 at MP 112"}
	, {"SR 520 I/C", "E Lk Samm Pkwy", "185th Ave NE", "188th Ave NE", "204th Ave NE", "Sahalee Way"}
	, {"US 2/ SR 204 Interchange"}
	, {"Sellards Road-SB facing on SR 221 at MP13.5", "Sellards Road-NB facing on SR 221 at MP13.5"}
	, {"SR 240 at Beloit Rd", "SR 240 at Twin Bridges", "SR 240/ Richland Wye at MP 38"}
	, {"Sellar Bridge - West", "Wenatchee River Bridge on SR 285"}
	, {"SR 290 / Hamilton"}
	, {"Charles Road on SR-291 at MP 9  Pos 7", "Charles Road on SR-291 at MP 9  Pos 6", "Charles Road on SR-291 at MP 9  Pos 5", "Charles Road on SR-291 at MP 9  Pos 1", "Charles Road on SR-291 at MP 9  Pos 3", "Charles Road on SR-291 at MP 9  Pos 2", "Charles Road on SR-291 at MP 9  Pos 4", "Charles Road on SR-291 at MP 9  Pos 8", "SR291 and Swenson Rd"}
	, {"SR 167/SR 410 Interchange", "Whistlin Jacks on SR 410"}
	, {"Lewis and Clark Bridge on SR-433"}
	, {"SR 500 at Thurston Way", "SR 500 at 112th and Gher Rd."}
	, {"SR502 at I-5 NB off ramp", "SR502 at 10th Ave."}
	, {"Forest Learning Rest Area andcopy;WSDOT"}
	, {"I-5/SR 512 Interchange"}
	, {"SR 516", "SR 516/ SR 167 Interchange(Willis St)", "SR 18/SR 516 Interchange"}
	, {"S 154th St", "Airport Dr", "SR 99/SR 518 Interchange", "46th Ave S", "51st Ave S", "EB Under I-5"}
	, {"Delmar Dr", "Montlake Blvd", "Lk Wa Blvd Ramp", "Lk Wash Blvd", "Montlake Ramp", "West Highrise, E", "West Highrise, W", "Midspan, West", "Midspan, East", "East Highrise", "76th Ave NE", "86th Ave NE", "92nd Ave NE", "Bellevue Way NE", "I-405/SR 520 Interchange NB", "124th Ave NE", "130th Ave NE", "140th Ave NE", "148th Ave NE", "NE 51st St", "NE 40th St", "W Lk Samm Pkwy", "Redmond Way", "W LkSam, EB Ramp", "Marymoor Park"}
	, {"NE 165th St", "SR 522 / SR 104 Interchange", "SR 522 and 68th Ave Interchange", "61st Ave NE", "80th Ave NE", "S Campus Way", "I-405/SR 522 Interchange", "SR 9", "Paradise Lake Rd (North)", "Paradise Lake Rd (South)", "Echo Lk Rd (west)", "Fales Rd (east)"}
	, {"I-405/SR 524 Interchange(Filbert Rd)"}
	, {"Alderwood Mall Pkwy", "164th St SW", "148th St SW", "SR99/SR525 Interchange", "Harbour Pt Bvd S", "Beverly Park Rd", "Chennault Bch Rd", "Harbour Pt Bvd N", "Paine Field Blvd", "76th St SW (north)", "Clover Ln", "76th St SW (south)", "5th St"}
	, {"Evergreen Way", "I-5/SR 526 Interchange"}
	, {"I-405/SR 527 Interchange", "164th St SE", "153rd St SE", "Trillium Blvd", "132nd St (SR 96)"}
	, {"SR 532 / Sunrise Blvd", "SR 532 / 102nd Ave NW", "SR 532 / 92nd Ave NW", "SR 532 / 88th Ave NW", "SR 532 / Pioneer Hwy", "SR 532 / 72nd Ave NW"}
	, {"Pole Rd", "Wiser Lake (North)", "River Rd", "Badger Rd", "Border"}
	, {"Boblett St", "H St", "Duty Free", "D St", "Truck Spur"}
	, {"D  St Offramp"}
	, {"SR 599", "S 133rd St", "METRO Bus Barn", "E Marginal Way"}
	, {"I-90/SR 900 Interchange ", "Maple St"}
	, {"T.J. Meenach Bridge"}
	, {"Tyler Interchange on I-90 at MP 257", "Tyler Interchange on I-90 at MP 257 (2)", "Tyler Interchange on I-90 at MP 257 (3)", "Tyler Interchange on I-90 at MP 257 (4)", "Tyler Interchange on I-90 at MP 257 (5)", "Tyler Interchange on I-90 at MP 257 (6)", "Tyler Interchange on I-90 at MP 257 (7)", "Tyler Interchange on I-90 at MP 257 (8)"}
	, {"Loomis Trail Rd", "I-5 at W 88th St", "I-5 at Peace Portal Dr", "I-5 at SR 543", "I-5 at Mitchell Ave", "I-5 at D St Offramp", "I-5 at D St", "I-5 at Peace Arch", "SR 539 at Pole Rd", "SR 539 at River Rd", "SR 539 at Badger Rd", "SR 539 at Border", "SR 543 at Boblett St", "SR 543 at H St", "SR 543 at D St", "SR 543 at Duty Free", "SR 543 at Truck Spur", "SR 9 at Front St", "US 97 Border Approach-Oroville - North", "US 97 Border Approach-Oroville - South", "US 97 Border-Oroville - North", "US 97 Border-Oroville - South"}
	, {"WSF Edmonds W Dayton St", "WSF Mukilteo Clover Ln", "Orcas Ferry", "WSF Anacortes Ferry Terminal", "WSF Anacortes Ferry Holding", "WSF Anacortes Ferry Terminal Rd", "WSF Clinton Terminal", "WSF Port Townsend Terminal", "WSF Kingston Terminal", "WSF Coupeville Terminal", "WSF Coupeville Street", "WSF Kingston Ferry Sign East", "WSF Port Townsend Street", "WSF Mukilteo N 525 at 76th", "Friday Harbor Ferry ", "WSF Edmonds VMS Sign", "WSF Edmonds Holding", "WSF Bainbridge Ferry Holding", "WSF Mukilteo S 525 at 76th", "WSF Kingston Ferry Sign West", "WSF Kingston Barber", "WSF Mukilteo South Holding", "WSF Edmonds Vessel", "WSF Bremerton Holding North", "WSF Edmonds Underpass", "WSF Mukilteo North Holding", "WSF Edmonds Pine", "WSF Bremerton Holding South", "WSF Bainbridge Ferry Holding", "WSF Fauntleroy Ferry Holding", "WSF Southworth Ferry Holding", "WSF Tahlequah Ferry Holding", "WSF Tahlequah at SW Tahlequah Rd", "WSF Vashon Hwy and Bunker (North)", "WSF Vashon Hwy and Bunker (South)", "WSF Vashon Hwy and 112th (North)", "WSF Vashon Hwy and 112th (South)"}
	, {"18th and Ray Spokane", "Downtown Vancouver", "Mt. Rainier", "Port Townsend Airport", "Colfax", "College at Martin Way (Lacey)", "Plum at Union (Olympia)", "Sleater-Kinney at Martin Way (Olympia)", "Leavenworth", "LaPush", "Long Beach", "Mount St. Helens", "Pullman", "Winthrop", "Morton ", "Stevenson Waterfront", "Walla Walla-Whitman College"}
	, {"Arlington Airport East", "Arlington Airport West", "Arlington Airport North", "Arlington Airport South", "Harvey Airfield(Snohomish) ", "Olympia Airport", "Skykomish Airport East", "Skykomish Airport SouthEast", "Skykomish Airport South", "Skykomish Airport West"}
	};
	 
	public static final String[][] crossroadsValues = {{"1267", "1001", "1002", "1266", "1285", "1265", "1346", "1003", "4021", "1273", "1274", "1275", "4020", "1356", "1357", "1360", "1351", "1543", "1542", "1541", "1540", "3077", "1287", "1286", "3078", "3079", "3080", "4036", "1219", "1220", "1221", "1004", "1005", "1006", "1007", "1008", "1009", "1010", "1011", "9150", "1392", "1393", "1310", "1309", "1313", "1394", "1012", "1395", "1396", "1397", "1013", "1398", "1014", "1399", "1015", "1400", "1016", "1017", "1018", "1401", "9054", "1402", "1020", "1021", "1022", "1023", "1403", "1024", "1025", "1026", "1027", "1028", "1029", "1030", "1031", "1032", "1406", "1407", "1033", "1034", "1409", "1035", "1411", "1036", "1037", "1038", "1039", "1040", "1338", "1042", "1412", "1043", "1044", "9069", "1045", "1046", "1047", "1413", "9070", "1416", "1417", "1048", "1418", "1419", "1420", "1019", "1421", "1422", "1423", "1424", "1425", "1426", "1427", "1428", "1429", "1430", "9071", "1431", "1432", "1433", "5021", "5020", "8104", "8105", "5018", "5019", "8059", "1316", "8060", "1054", "1322", "1526", "1323", "1324", "1325", "1326", "1329", "1327", "1328", "1055", "1330", "1056", "1210", "9072", "1058", "1281", "1057"}
	, {"1349", "1348", "1347", "1128", "1264", "1263", "1352", "3062"}
	, {"1361", "9081", "3026", "1362", "1060", "1061", "9082", "1363", "1062", "1063", "1064", "1065", "1366", "1066", "1067", "1367", "1368", "1068", "1069", "1369", "1370", "1371", "1070", "1071", "1072", "9083", "9084", "1073", "1374", "1375", "1376", "1377", "1074", "1378", "1379", "1075", "1380", "1076", "1077", "1381", "1078", "1079", "1080", "1382", "1081", "1082", "1383", "1384", "1385", "1386", "1083", "1387", "1388", "1389", "1084", "1085", "9085", "1086", "1390", "1087"}
	, {"1088", "1434", "1435", "1436", "1437", "1438", "1089", "9073", "1440", "1441", "1442", "1090", "1091", "1444", "1445", "1446", "1447", "1448", "1449", "1092", "1094", "1450", "9053", "1451", "1095", "1096", "1452", "1453", "1296", "1454", "1455", "1456", "1099", "1100", "1102", "9018", "9019", "1103", "8093", "8094", "9028", "1105", "1355", "1106", "1107", "1108", "1109", "8076", "1110", "8310", "8311", "8312", "8313", "8314", "8315", "8316", "8317", "8261", "8262", "8263", "8264", "8265", "8266", "8267", "8260", "4015", "4014", "4025", "4016", "4017", "1113", "3076", "4005", "4008", "4009", "4006", "4004", "1115", "1116", "1117", "1289", "1291", "1119", "1292", "1290", "1293", "3071", "3072", "3073", "3074", "4011", "8251", "8252", "8253", "8254", "8255", "8256", "8257", "8250", "4010", "9029", "8210", "8211", "1104", "8208", "8209"}
	, {"1127", "4022", "9094", "8061", "4029", "4027", "4028", "8096", "8077", "8084", "4031"}
	, {"1317"}
	, {"1524", "1525", "1131", "1522", "1523", "5000", "5001", "5002", "5003", "9050", "9051", "9052", "9145", "8062", "8063", "8083", "9032", "5017", "1343", "9021", "1279", "1280", "1133", "8071"}
	, {"1134", "8100", "8078", "1135"}
	, {"1136", "1137", "8095", "1138", "8068", "9013", "1139", "9024", "9025", "9022", "9023"}
	, {"9014", "1140", "8091"}
	, {"8069", "1141", "9095", "8103", "1142", "8051", "8054", "1143", "8075", "1144", "8048", "8049", "8050", "8052", "1353"}
	, {"8032", "8033", "8034", "8035", "8036", "8037", "8038", "8039", "4033", "8040", "8041", "8042", "8043", "8044", "8045", "8046", "8047"}
	, {"8031", "1146", "8240", "8241", "8242", "8243", "8244", "8245", "8246", "8247", "8280", "8281", "8282", "8283", "8284", "8285", "8286", "8287", "4034", "4035", "8231", "8234", "8235", "8232", "8233", "8237", "8236", "8230"}
	, {"4047", "4045", "4046", "4044", "4043", "4042"}
	, {"3084", "1150"}
	, {"1251"}
	, {"1151"}
	, {"9078", "4024", "4023", "1152", "1153"}
	, {"1354", "1268", "1269", "1154", "8070"}
	, {"1155", "1156", "1157", "1158", "9146", "9149", "1333", "1334", "1159", "1335", "9147", "9148", "1336", "1341", "1339"}
	, {"1160"}
	, {"1314", "1468", "1469", "1470", "1471", "1472", "1473", "1474", "1475", "1190", "5010"}
	, {"9096", "9097", "5011", "4030", "1161"}
	, {"8081", "8085", "8086", "8087", "9000", "9001", "1345"}
	, {"1162", "1163", "1164", "1165"}
	, {"1167", "1168", "1295"}
	, {"8098"}
	, {"9075", "9074"}
	, {"1302", "1521", "1170", "1171", "1172"}
	, {"1174", "1175", "1457", "1205"}
	, {"8053", "8066"}
	, {"1458", "1459"}
	, {"1212"}
	, {"1176", "1460", "1461", "1177", "1462", "1178", "1463", "1179", "1180", "1464", "1465", "1181", "1182", "1466", "1183"}
	, {"1467", "1185"}
	, {"1222"}
	, {"1476", "1477", "1478", "1479", "1480", "1481"}
	, {"1297"}
	, {"8099", "8097"}
	, {"9034", "9033", "8082"}
	, {"1166", "5016"}
	, {"4012"}
	, {"9011", "9010", "9009", "9008", "9007", "9006", "9012", "9005", "9027"}
	, {"1184", "1284"}
	, {"1252"}
	, {"1254", "1255"}
	, {"1358", "1359"}
	, {"4037"}
	, {"1186"}
	, {"1187", "1188", "1189"}
	, {"9086", "9087", "1303", "9088", "9089", "9090"}
	, {"9080", "1484", "1485", "1486", "1191", "1192", "1487", "1488", "1489", "1193", "1490", "1491", "1194", "1195", "1196", "1197", "1492", "1493", "1198", "1199", "1494", "1200", "1201", "1495", "1496"}
	, {"1497", "1202", "1203", "1498", "1499", "9077", "1204", "9079", "1304", "1305", "1502", "1503"}
	, {"1206"}
	, {"1504", "1505", "1506", "1173", "1507", "1332", "1508", "1509", "1331", "1510", "1511", "1306", "1512"}
	, {"9091", "1207"}
	, {"1208", "1308", "1514", "1515", "1516"}
	, {"5004", "5005", "5006", "5007", "5008", "5013"}
	, {"1527", "9092", "1528", "1529", "1209"}
	, {"1211", "1213", "1214", "1283", "1530"}
	, {"1215"}
	, {"1216", "1517", "1518", "1519"}
	, {"1217", "9093"}
	, {"1277"}
	, {"8270", "8271", "8272", "8273", "8274", "8275", "8276", "8277"}
	, {"1223", "1224", "1227", "1228", "1229", "1230", "1231", "1232", "1282", "1531", "1532", "1533", "1534", "1535", "1536", "1537", "1538", "1539", "1546", "9031", "1544", "1545"}
	, {"9155", "9161", "9171", "9047", "9048", "9049", "9166", "9167", "9151", "9169", "9170", "9152", "9168", "9162", "1233", "9156", "9157", "9040", "9163", "9153", "9154", "9164", "9158", "9037", "9159", "9165", "9160", "9036", "9035", "9038", "9039", "9045", "9046", "9041", "9042", "9043", "9044"}
	, {"1278", "1253", "1350", "1294", "4032", "3083", "3081", "3082", "1239", "1240", "1241", "1242", "1245", "1246", "4038", "1247", "1248"}
	, {"8216", "8217", "8218", "8219", "1237", "1271", "8220", "8221", "8222", "8223"}
	};
}