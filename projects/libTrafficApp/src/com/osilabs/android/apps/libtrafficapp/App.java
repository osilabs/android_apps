package com.osilabs.android.apps.libtrafficapp;

//import com.Leadbolt.AdController;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import com.osilabs.android.lib.Session;
import com.osilabs.android.lib.Version;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class App extends MapActivity {

	//
	// Global context - used by static classes to get at context
	//
	public static Activity me = null;
	
	//
	// Consts
	//
	public static final String TAG = "** osilabs.com **";

	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_CAMERAS               = 1;
	private static final int MENU_CALENDAR              = 2;
	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_CAMERAS              = 1;
	private static final int INDEX_CALENDAR             = 2;
	private static final int WEBVIEW	                = 0;
	private static final int MAPVIEW                    = 1;
	private static final int INTENT_RESULT_CODE_CAMERA_PICKER= 22;
	private static final int INTENT_RESULT_CODE_PREFS   = 33;

	// URI's
	protected static final String MOBILECONTENT_URL_HELP     = "http://osilabs.com/m/mobilecontent/help/shared.php";
	protected static final String MOBILECONTENT_URL_FEEDBACK = "http://osilabs.com/m/mobilecontent/feedback/shared.php";
	protected static final String MOBILECONTENT_URL_VERSION  = "http://osilabs.com/m/mobilecontent/version/shared_checkForUpdate.php";

	//
	// Globals
	//

	// Prefs
	protected static String WEBVIEW_URL = "";
	protected static String AD_BANNER_URL = "";

	// Will need to up this number if more indexes are needed.
	protected static int CURRENT_TAB_INDEX = 0;
	
	// Flag indicating prefs have changed and need to be reread.
	//  Default to true so it initializes onStart()
	protected static boolean PREFS_UPDATED = true;
	
	// This defaults to true so it can be set once, and only once, from oncreate()
	protected static boolean CHECK_FOR_NEW_VERSION = true;

	// Tracks when the MapView is showing
	protected static boolean MAP_VIEW_IS_VISIBLE = false;
	protected static boolean FEEDBACK_IS_VISIBLE = false;
	protected static boolean HELP_IS_VISIBLE     = false;
	protected static boolean ABOUT_IS_VISIBLE    = false;
	
	protected static Spinner spViewChoices;
	//protected static WebView wvAd;
	protected static WebView wvLeadbolt;
	protected static WebView wvMain;
	protected static SharedPreferences mySharedPreferences;
	
	// LeadBolt ads
//	private AdController leadboltController;

	//
	// Navbar components
	//
	
	// Tabs
	protected static ImageView ivMapsTab;
	protected static ImageView ivCalendarTab;
	protected static ImageView ivCamerasTab;
	
	// Configs
	protected static ImageView ivMapsMore;
	protected static TextView  tvMapsPop;
	
	protected static ImageView ivCalendarMore;
	protected static TextView  tvCalendarPop;
	
	protected static ImageView ivCamerasMore;
	protected static TextView  tvCamerasPop;

	// Misc icons
	protected ImageView ivRefresh;
	protected ImageView ivRadios;
	protected static ImageView ivFavorite;

	// Mapview stuff
	protected static MapView mvTraffic;
    protected static MapController mcMain;
    protected static GeoPoint gpMain;

    // For posting runnables
    private Handler mHandler = new Handler();
    
    // My libs
    protected Version v;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(Config.DEBUG>0)Log.d(TAG, "onCreate");

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set global context;
        me = this;
        
        //
        // Set globals
        //
        
        //
        // Get a version handler
        //
        v = new Version(this, "com.osilabs.android.apps.libtrafficapp", Config.APP_CODE);

		// Set URLs with versioncode
		WEBVIEW_URL = Config.MOBILECONTENT_URL_PREFIX + v.versionCode() + "/trafficmap.php";
		AD_BANNER_URL = Config.MOBILECONTENT_URL_PREFIX + v.versionCode() + "/adbanner.php";
		
        //
		// Restore preferences
        //
		// todo - Can this go away if it is done in setCurrentPrefs()?
		mySharedPreferences = getSharedPreferences("com.osilabs.android.apps.libtrafficapp", Activity.MODE_PRIVATE);
		setCurrentPrefs();
        
	    // -------------------------
	    // Top Nav bar
	    //

	    ivMapsTab = (ImageView) findViewById(R.id.launcher_traffic);
		ivMapsTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewForCurrentTab(INDEX_TRAFFIC);
			}
		});

		ivCalendarTab = (ImageView) findViewById(R.id.launcher_calendar);
		// FIXME - remove App. from these
		App.ivCalendarTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewForCurrentTab(INDEX_CALENDAR);
			}
		});
		
	    // Set up camera tab
		// FIXME - Rename Config.mainroads to Config.camera_mainroads
		if (Config.mainroads.length > 0) {
		    ivCamerasTab = (ImageView) findViewById(R.id.launcher_cameras);
		    ivCamerasTab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setViewForCurrentTab(INDEX_CAMERAS);
				}
			});
		}

	    ivRefresh = (ImageView) findViewById(R.id.navbar_refresh);
	    // Give it a nice blue color. SRC_ATOP means color the icon, not
	    //  the background.
	    ivRefresh.setColorFilter(getResources().getColor(R.color.darkPowderBlue), PorterDuff.Mode.SRC_ATOP); // same as tint
	    ivRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshViews();
				refreshTrafficMap();
			}
		});
	    
	    // IMPORTANT - I would get crashes when I initialized webviews in onStart. Moving
	    //              this into onstart solved the problem.
		
	    //
		// Main Web View
		//
		wvMain = (WebView) findViewById(R.id.mainWebView);
        WebSettings webSettings = wvMain.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultFontSize(23);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvMain.setWebViewClient(new MyWebViewClient(this));
        wvMain.setWebChromeClient(new WebChromeClient());
        // Enable jsi
        wvMain.addJavascriptInterface(new JsiJavaScriptInterface(this), "jsi");
    	wvMain.setVisibility(View.INVISIBLE);
		wvMain.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
    	wvMain.setVerticalScrollBarEnabled(true);
    	wvMain.setVerticalFadingEdgeEnabled(true);
    	wvMain.setBackgroundColor(R.color.backgroundGray);

    	/*
		//
		// Ad banner Web View
		//
		wvAd = (WebView) findViewById(R.id.adWebView);
        WebSettings awebSettings = wvAd.getSettings();
        awebSettings.setJavaScriptEnabled(true);
        awebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    	wvAd.setBackgroundColor(Color.BLACK);
        if (Config.NO_ADS) {
        	wvAd.setVisibility(View.GONE);
        }
        */
    	
    	//
    	// Main Map View
    	//
    	mvTraffic = (MapView) findViewById(R.id.mainMapView);
    	mvTraffic.setBuiltInZoomControls(true);
    	mvTraffic.setTraffic(true);    	
		mcMain = mvTraffic.getController();
		
        // Add a location marker---
        MapOverlay mapOverlay = new MapOverlay();
        List<Overlay> listOfOverlays = mvTraffic.getOverlays();
        listOfOverlays.clear();
        listOfOverlays.add(mapOverlay);    
		
		// -------------------------
	    // Bottom Navigation Bar
	    //
        
        // Maps config...
		ivMapsMore = (ImageView) findViewById(R.id.maps_config_pop_icon);
		ivMapsMore.setColorFilter(getResources().getColor(R.color.lighterDarkPowderBlue), PorterDuff.Mode.SRC_ATOP);
		ivMapsMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchMapPicker();
			}
		});
    	tvMapsPop = (TextView) findViewById(R.id.maps_config_pop);
    	tvMapsPop.setTextColor(getResources().getColor(R.color.lighterDarkPowderBlue));
    	tvMapsPop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchMapPicker();
			}
		});
        
        // Calendar config...
		ivCalendarMore = (ImageView) findViewById(R.id.calendar_config_pop_icon);
		ivCalendarMore.setColorFilter(getResources().getColor(R.color.lighterDarkPowderBlue), PorterDuff.Mode.SRC_ATOP);
		ivCalendarMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCalendarPicker();
			}
		});
    	tvCalendarPop = (TextView) findViewById(R.id.calendar_config_pop);
    	tvCalendarPop.setTextColor(getResources().getColor(R.color.lighterDarkPowderBlue));
    	tvCalendarPop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCalendarPicker();
			}
		});

        // Cameras config...
		ivCamerasMore = (ImageView) findViewById(R.id.cameras_config_pop_icon);
		ivCamerasMore.setColorFilter(getResources().getColor(R.color.lighterDarkPowderBlue), PorterDuff.Mode.SRC_ATOP);
		ivCamerasMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCameraPicker();
			}
		});
		tvCamerasPop = (TextView) findViewById(R.id.cameras_config_pop);
		tvCamerasPop.setTextColor(getResources().getColor(R.color.lighterDarkPowderBlue));
	    tvCamerasPop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCameraPicker();
			}
		});

	    // Radios Icon Click
	    ivRadios = (ImageView) findViewById(R.id.navbar_radios);
	    ivRadios.setColorFilter(getResources().getColor(R.color.lightRed), PorterDuff.Mode.SRC_ATOP); // same as tint
	    ivRadios.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
            	ScannerRadio.launchScanner( 1, me.getApplicationContext());
            	/*
            	AlertDialog alert = new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.radios_dialog_title)
                .setItems(Config.RADIOS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
    		    		// Check prefs for changed radios.
    					//setCurrentRadios();

                    	ScannerRadio.launchScanner( Config.RADIOS_CURRENT_NODE[which], me.getApplicationContext());
                    }
                }).create();
				
				alert.show();
				*/
			}
		});

	    // Favorite Icon Click
	    ivFavorite = (ImageView) findViewById(R.id.favorites_star);
	    ivFavorite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Favorites.handleClick();
        		MapsTab.MenuIndexes.setFavArrayIndex(MapsTab.CURRENT_INDEX);
        		if(Config.DEBUG>0)Log.d(TAG, "App::" + MapsTab.MenuIndexes.debug());
			}
		});

		//if(Config.DEBUG>0)Log.d(App.TAG, "onCreate() complete: " + MapsTab.MenuIndexes.debug());
	    
	    // LeadBolt
	    wvLeadbolt = (WebView) findViewById(R.id.leadboltView);
	    wvLeadbolt.getSettings().setJavaScriptEnabled(true);
	    wvLeadbolt.loadUrl("http://osilabs.com/m/mobilecontent/ads/leadbolt/1.html");
    }
    @Override
    public void onStart() {
    	super.onStart();

    	if(Config.DEBUG>0)Log.d(TAG, "onStart");
    	
    	setCurrentPrefs(); // This must happen before other things.
		MapsTab.init();
		CalendarTab.init();
		if (Config.mainroads.length > 0) {
			CamerasTab.init(); 
		}

    	//MenuIndexes.init();
    	
        if (MAP_VIEW_IS_VISIBLE) { 
        	// Set some long term "pulse" timers to redraw the traffic lines after enough time
        	//  that they may have changed.
            mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 60000);
            mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 180000);
        
            drawTrafficMap();
        }
        
    	//
    	// Set the current tab and load it
    	//
	    
        // It's going to take a second to load
		Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
		
		if(Config.DEBUG>0){
			Log.d(App.TAG, "onStart() MapsTab.MenuIndexes: " + MapsTab.MenuIndexes.debug());
			Log.d(App.TAG, "onStart() CURRENT_TAB_INDEX: " + CURRENT_TAB_INDEX);
			Log.d(App.TAG, "onStart() MapsTab.CURRENT_INDEX: " + MapsTab.CURRENT_INDEX);
			Log.d(App.TAG, "onStart() CalendarTab.CURRENT_INDEX: " + CalendarTab.CURRENT_INDEX);
		}
		
		setViewForCurrentTab(CURRENT_TAB_INDEX);

		reloadViews();
	}
    
    @Override
    public void onPause() {
    	super.onPause();
    	// Prevents the already posted runnables to redraw traffic 
    	//  from causing the map to redraw after we leave
    	MAP_VIEW_IS_VISIBLE = false;
    }
    @Override
    public void onResume() {
    	super.onResume();
    }
    @Override
    public void onDestroy() {
//    	leadboltController.destroyAd();
    	super.onDestroy();
    }
    
    //
    // Map View Methods
    //
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

    protected void launchCameraPicker() { 
        // if(Config.DEBUG>0)Log.d(TAG, "launchCameraPicker");

		Context c = getApplicationContext();
		Intent intent = new Intent().setClassName(c, "com.osilabs.android.apps.libtrafficapp.CamerasELV");
		startActivityForResult(intent, INTENT_RESULT_CODE_CAMERA_PICKER); 
    }
    protected void launchMapPicker() {
        // if(Config.DEBUG>0)Log.d(TAG, "launchMapPicker");
		
		List<String> sl = new ArrayList<String>();

		// TODO - move these into the javaFavorites class
		JSONArray ja = null;
		try {
			ja = new JSONArray(Config.MAPVIEW_FAVORITES);
			for(int i=0; i<ja.length(); i++) {
				//options[optionsIndex++] = ja.getJSONObject(i).getString("label").toString();
				sl.add("~ " + ja.getJSONObject(i).getString("label").toString() + " ~");
			}
		} catch (JSONException e1) {
			// If we have an exception thrown while trying to render the favorites and the
			//  string is not empty, it would seem the string has been corrupt. May have
			//  been modified outside this application. In any case, clear out the string
			//  so "reset" things and move forward with a clean favorites list.
			if (Config.MAPVIEW_FAVORITES.length() > 0) {
				Log.e(TAG, "App::launchMapPicker() Favorites string may be corupt, resetting:" + Config.MAPVIEW_FAVORITES);
				e1.printStackTrace();
				Config.MAPVIEW_FAVORITES = "";
				Session.saveString(mySharedPreferences, "pref_mapview_favorites", Config.MAPVIEW_FAVORITES);
				MapsTab.MenuIndexes.init();
				PREFS_UPDATED = true;
				//reloadViews();
			}
		}

		// Tack on the system menu options
		for(int i=0; i<Config.traffic.length; i++) { sl.add(Config.traffic[i]); } 
		
		// Turn list into menu options
		CharSequence[] options = (String[]) sl.toArray(new String[sl.size()]);
		
        AlertDialog alert = new AlertDialog.Builder(this)
        .setTitle(R.string.txt_map_popup_title)
        .setIcon(R.drawable.ic_police)
        .setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	// Set the MapsTab.CURRENT_INDEX
            	// FIXME - Move into MapsTab
            	MapsTab.CURRENT_INDEX = which;

            	int androidViewType = MapsTab.getAndroidViewType();
            	if(Config.DEBUG>0) Log.d(TAG, "launchMapPicker()::onClick(): androidViewType " + androidViewType);
            	
            	// Favorites come from Config.MAPVIEW_FAVORITES
            	if ( androidViewType == Config.FAVORITE ) {
					// Set the current mapview coords
            		
            		// FIXME - move to Favorites
            		JSONArray ja = null;
            		try {
	        			ja = new JSONArray(Config.MAPVIEW_FAVORITES);
	        			setCurrentMapView(ja.getJSONObject( which ).toString());
            		} catch (JSONException e) {
            			//e.printStackTrace();
            		}
            	}

            	// System maps come from Config.traffic_urls
            	if ( androidViewType == Config.MAP ) {
            		// Use the adjusted index to get the map coords from 
            		//  Config.traffic_urls
            		setCurrentMapView(Config.traffic_urls[ MapsTab.getAdjustedIndex() ]);
            		if(Config.DEBUG>0) Log.d(TAG, "launchMapPicker()::onClick(): SetCurrentMapView to " + Config.traffic_urls[ MapsTab.getAdjustedIndex() ]);
            	}
            	
    			Toast.makeText(getApplicationContext(), 
    					R.string.txt_loading
    					, Toast.LENGTH_LONG).show();
    			
    			// Save current map
    			Session.saveInt(mySharedPreferences, "session_map", MapsTab.CURRENT_INDEX);
//    	    	//mySharedPreferences
//    			//	= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
//    	    	mySharedPreferences = getSharedPreferences(Config.NAMESPACE + "_preferences", Activity.MODE_PRIVATE);
//			    SharedPreferences.Editor editor = mySharedPreferences.edit();
//			    editor.putInt("session_map", MapsTab.CURRENT_INDEX);
//			    editor.commit();
			    
			    // Update the current favorites index.
			    MapsTab.MenuIndexes.setFavArrayIndex(MapsTab.CURRENT_INDEX);
			    if(Config.DEBUG>0)Log.d(App.TAG, "launchMapPicker()::onClick(): " + MapsTab.MenuIndexes.debug());
    			
			    setViewForCurrentTab(INDEX_TRAFFIC);
			    reloadViews(); 
            }
        }).create();
		
		alert.show();
    }
    
    protected void setCurrentMapView(String current) {
		Config.CURRENT_MAPVIEW_COORDS = current;
	
		// Save to shared prefs
	    Session.saveString(mySharedPreferences, "pref_current_mapview_coords", Config.CURRENT_MAPVIEW_COORDS);
//	    SharedPreferences.Editor editor = mySharedPreferences.edit();
//	    editor.putString("pref_current_mapview_coords", Config.CURRENT_MAPVIEW_COORDS);
//	    editor.commit();
	    
	    PREFS_UPDATED = true;
    }
    protected void launchCalendarPicker() { 
        // if(Config.DEBUG>0)Log.d(TAG, "LaunchCalendarPicker");

		AlertDialog alert = new AlertDialog.Builder(this)
        .setTitle(R.string.txt_calendar_popup_title)
        .setIcon(R.drawable.ic_menu_today)
        .setItems(Config.calendar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	CalendarTab.CURRENT_INDEX = which;
    			Toast.makeText(getApplicationContext(), 
    					R.string.txt_loading
    					, Toast.LENGTH_LONG).show();
    			
    			// Save current CALENDAR
    	    	Session.saveInt(mySharedPreferences, "session_calendar", CalendarTab.CURRENT_INDEX);
//    	    	SharedPreferences prefs 
//    				= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
//			    SharedPreferences.Editor editor = prefs.edit();
//			    editor.putInt("session_calendar", CalendarTab.CURRENT_INDEX);
//			    editor.commit(); 
    			
            	reloadViews();
            }
        }).create();
		
		alert.show();
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // if(Config.DEBUG>0)Log.d(TAG, "onActivityResult: " + Integer.toString(requestCode));

		// See which child activity is calling us back.
	    switch (requestCode) {
            
        case INTENT_RESULT_CODE_CAMERA_PICKER:
            // This is the standard resultCode that is sent back if the
            // activity crashed or didn't doesn't supply an explicit result.
            if (resultCode == RESULT_CANCELED){
    			//Toast.makeText(getApplicationContext(), R.string.txt_camera_picker_noop, Toast.LENGTH_LONG).show();
            }
            else {
            	Bundle extras = data.getExtras();
                CamerasTab.CURRENT_CAMERA_INDEX = extras.getString("selected_camera");
    			
    			// Reload the webview so it just shows the chosen camera
				reloadViews();
				
    			// Set the chosen camera in the persistent settings
				Session.saveString(mySharedPreferences, "session_camera_1", CamerasTab.CURRENT_CAMERA_INDEX);
//    	    	SharedPreferences prefs 
//    				= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
//    			    SharedPreferences.Editor editor = prefs.edit();
//    			    editor.putString("session_camera_1", CamerasTab.CURRENT_CAMERA_URL);
//    			    editor.commit();
            }
            break;
            
        case INTENT_RESULT_CODE_PREFS:
        	PREFS_UPDATED = true;
        	setCurrentPrefs();
        	
			Toast.makeText(getApplicationContext(), 
					R.string.txt_prefs_saved
					, Toast.LENGTH_LONG).show();
			break;
        	
	        default:
	            break;
	    }
	}

    /**
     * This gets called in onStart and when the prefs intent returns. 
     * Should cover making sure the local globals always have the current
     * prefs.
     */
    protected void setCurrentPrefs() {
    	if(Config.DEBUG>0)Log.d(TAG, "++ setCurrentPrefs() ++");
    	// Only waste time rereading prefs if they changed.
    	if (PREFS_UPDATED) {
		    if (Config.DEBUG > 1) {
			    Log.d(TAG, "setCurrentPrefs() Orig CURRENT_TAB_INDEX: " + CURRENT_TAB_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Orig MapsTab.CURRENT_INDEX: " + MapsTab.CURRENT_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Orig CalendarTab.CURRENT_INDEX: " + CalendarTab.CURRENT_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Orig CamerasTab.CURRENT_CAMERA_URL: " + CamerasTab.CURRENT_CAMERA_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Orig Config.CURRENT_MAPVIEW_COORDS: " + Config.CURRENT_MAPVIEW_COORDS);
			    Log.d(TAG, "setCurrentPrefs() Orig Config.MAPVIEW_FAVORITES: " + Config.MAPVIEW_FAVORITES);
			    Log.d(TAG, "setCurrentPrefs() Orig CalendarTab.CURRENT_WEATHER_FEED_INDEX: " + CalendarTab.CURRENT_WEATHER_FEED_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Orig CalendarTab.CURRENT_TODAY_FEED_INDEX: " + CalendarTab.CURRENT_TODAY_FEED_INDEX);
			    //Log.d(TAG, "setCurrentPrefs() Orig Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER]: " + Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER]);
			    //Log.d(TAG, "setCurrentPrefs() Orig Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE]: " + Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE]);
		    }
		    
		    // Unset the flag indicating prefs need to be re-read
		    PREFS_UPDATED = false;
	
			//
			// User Preferences
			//			
		    setUserPrefs();
			
			//
			// Session Preferences
			//			
		    setSharedPrefs();
			
	        if (Config.DEBUG > 1) {
			    Log.d(TAG, "setCurrentPrefs() Pull CURRENT_TAB_INDEX: " + CURRENT_TAB_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Pull MapsTab.CURRENT_INDEX: " + MapsTab.CURRENT_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Pull CalendarTab.CURRENT_INDEX: " + CalendarTab.CURRENT_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Pull CamerasTab.CURRENT_CAMERA_URL: " + CamerasTab.CURRENT_CAMERA_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Pull Config.CURRENT_MAPVIEW_COORDS: " + Config.CURRENT_MAPVIEW_COORDS);
			    Log.d(TAG, "setCurrentPrefs() Pull Config.MAPVIEW_FAVORITES: " + Config.MAPVIEW_FAVORITES);
			    Log.d(TAG, "setCurrentPrefs() Pull CalendarTab.CURRENT_WEATHER_FEED_INDEX: " + CalendarTab.CURRENT_WEATHER_FEED_INDEX);
			    Log.d(TAG, "setCurrentPrefs() Pull CalendarTab.CURRENT_TODAY_FEED_INDEX: " + CalendarTab.CURRENT_TODAY_FEED_INDEX);
			    //Log.d(TAG, "setCurrentPrefs() Pull Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER]: " + Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER]);
			    //Log.d(TAG, "setCurrentPrefs() Pull Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE]: " + Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE]);
    		}
    	}
    }
    
    private void setSharedPrefs() {
	    // Set some locals
	    try {
			//
			// Session Preferences
			//
			mySharedPreferences = getSharedPreferences("com.osilabs.android.apps.libtrafficapp", Activity.MODE_PRIVATE);
			
	        CURRENT_TAB_INDEX = mySharedPreferences.getInt("session_current_view", Config.DEFAULT_TAB_INDEX);
	        MapsTab.CURRENT_INDEX = mySharedPreferences.getInt("session_map", Config.DEFAULT_MAP_INDEX);
	        CalendarTab.CURRENT_INDEX = mySharedPreferences.getInt("session_calendar", Config.DEFAULT_CALENDAR_INDEX);
	        CamerasTab.CURRENT_CAMERA_INDEX = mySharedPreferences.getString("session_camera_1", Config.DEFAULT_CAMERA_INDEX);
		    Config.CURRENT_MAPVIEW_COORDS = mySharedPreferences.getString("pref_current_mapview_coords", Config.CURRENT_MAPVIEW_COORDS);
			// todo - This shouldn't be in Config. Move it to App
			Config.MAPVIEW_FAVORITES = mySharedPreferences.getString("pref_mapview_favorites", "");
		} catch (Exception e) {
			e.printStackTrace();

			// If we get errors setting prefs, set them to defaults
			Log.e(TAG, "Could not set shared prefs into session, setting to defaults");
			
	        CURRENT_TAB_INDEX = Config.DEFAULT_TAB_INDEX;
	        MapsTab.CURRENT_INDEX = Config.DEFAULT_MAP_INDEX;
	        CalendarTab.CURRENT_INDEX = Config.DEFAULT_CALENDAR_INDEX;
	        CamerasTab.CURRENT_CAMERA_INDEX = Config.DEFAULT_CAMERA_INDEX;
		    Config.CURRENT_MAPVIEW_COORDS = Config.DEFAULT_MAPVIEW_COORDS;
			Config.MAPVIEW_FAVORITES = "";
		}
    }
    
    private void setUserPrefs() {
	    try {
	    	// If this namespace path doesn't end in '_preferences' this won't work.
	    	SharedPreferences userPrefs = getSharedPreferences("com.osilabs.android.apps.libtrafficapp_preferences", Activity.MODE_PRIVATE);
	    	
	    	/*
	        // Get weather radio
	    	String wr_saved = this.getResources().getString(R.string.pref_weather_radios_selected);
	    	Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER] = Integer.parseInt(userPrefs
	          .getString(wr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER])));

	        // Get police radio
	        String pr_saved = this.getResources().getString(R.string.pref_police_radios_selected);
	    	Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE] = Integer.parseInt(userPrefs
	          .getString(pr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE])));
	    	 */
	    	
	    	// today feed
	    	String tf_saved = this.getResources().getString(R.string.pref_today_feed_selected);
	    	CalendarTab.CURRENT_TODAY_FEED_INDEX = Integer.parseInt(userPrefs
	    		.getString(tf_saved, "0"));

	    	// weather feed
	    	String wf_saved = this.getResources().getString(R.string.pref_weather_feed_selected);
	    	CalendarTab.CURRENT_WEATHER_FEED_INDEX = Integer.parseInt(userPrefs
	    		.getString(wf_saved, "0"));

		} catch (Exception e) {
			e.printStackTrace();

			// If we get errors setting prefs, set them to defaults
			Log.e(TAG, "Could not set user prefs, using defaults");
			
			Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER] = Config.RADIOS_DEFAULT_NODE[Config.INDEX_OF_WEATHER];
			Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE] = Config.RADIOS_DEFAULT_NODE[Config.INDEX_OF_POLICE];
			CalendarTab.CURRENT_TODAY_FEED_INDEX = 0;
			CalendarTab.CURRENT_WEATHER_FEED_INDEX = 0;
		}
    }
        
	public void colorTheCurrentTab() {
        // if(Config.DEBUG>0)Log.d(TAG, "colorTheCurrentTab");

		MapsTab.setInactive();
		CalendarTab.setInactive();
		CamerasTab.setInactive();
		
		switch (CURRENT_TAB_INDEX) {
			case MENU_TRAFFIC:
				MapsTab.setActive();
				break;
		    case MENU_CALENDAR:
				CalendarTab.setActive();
		    	break;
		    case MENU_CAMERAS:
				CamerasTab.setActive();
		    	break;
		}
	}

	public void setViewForCurrentTab(int tab_index) {
		
		// FIXME - CAn I consolidate setViewForCurrentTab, 
		//  setCurrentMapView, and setCurrentTab. All these 'Set' 
		//  functions are confusing
		if(Config.DEBUG>0)Log.d(TAG, "setViewForCurrentTab() tab_index: " + tab_index);

		// By default, remove the star, it will be shown later if it needs to be.
		Favorites.setStarIcon(Favorites.MODE_GONE);
		
		CURRENT_TAB_INDEX = tab_index;
		setCurrentTab(CURRENT_TAB_INDEX);

		/**
		 * OnTab switch
 		 * 1. Make current view invisible
		 * 2. Make the tab clicked green
		 * 3. Swap in loading message
         * 4. Do all the expensive work, trap errors, if errors, ? pop up config picker to pick another map?
		 */

		// Hide stuff
    	MAP_VIEW_IS_VISIBLE = false;
    	
    	MapsTab.hideConfiguration();
        CalendarTab.hideConfiguration();
        CamerasTab.hideConfiguration();
        
        // Makes the current tab green
        colorTheCurrentTab();
        
		switch (CURRENT_TAB_INDEX) {
			case MENU_TRAFFIC:
				MapsTab.showConfiguration();
				
				switch(MapsTab.getAndroidViewType()) {
					case Config.FAVORITE:
						// Show the star in MODE_ON because this is a favorite.
						Favorites.setStarIcon(Favorites.MODE_ON);
						// Allow to drop into next case as this is a mapview too
					case Config.MAP:
				    	activateViewType(MAPVIEW);
			        	
			        	// Set flag indicating this thing is visible.
			        	MAP_VIEW_IS_VISIBLE = true;
			        	drawTrafficMap();
			        	
						break;
						
					case Config.IMAGE:
					case Config.FEED:
					case Config.WEB:
						// FIXME - rename to wvTraffic
				    	activateViewType(WEBVIEW);
						wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ ")");
						break;
				}
		        
				break;
		    case MENU_CALENDAR:
		    	// FIXME - i think these are already set above
		        CURRENT_TAB_INDEX = INDEX_CALENDAR;
		        CalendarTab.showConfiguration();
		    	activateViewType(WEBVIEW);
				wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ ")");
		    	break;
		    case MENU_CAMERAS:
		        CURRENT_TAB_INDEX = INDEX_CAMERAS;
		        CamerasTab.showConfiguration();
		    	activateViewType(WEBVIEW);
				wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ ")");
		        break;
		}
		
		// Feedback was just showing and need to refresh the views
		//  to make it go away.
		if (FEEDBACK_IS_VISIBLE) {
			FEEDBACK_IS_VISIBLE = false;
			refreshViews();
		}
		
		if (HELP_IS_VISIBLE) {
			HELP_IS_VISIBLE = false;
			refreshViews();
		}
		
		if (ABOUT_IS_VISIBLE) {
			ABOUT_IS_VISIBLE = false;
			refreshViews();
		}
	}
		
	//----------------------------------------------------------
	// Map view map
	//
	public void drawTrafficMap() {
		// Check for updated prefs and reread them
		setCurrentPrefs();
		
		// Get the current mapview coordinates 
		if(Config.DEBUG>0)Log.d(TAG, "drawTrafficMap() MapsTab.CURRENT_INDEX: " + MapsTab.CURRENT_INDEX);
		if(Config.DEBUG>0)Log.d(TAG, "drawTrafficMap() Config.CURRENT_MAPVIEW_COORDS: " + Config.CURRENT_MAPVIEW_COORDS);
		
		JSONObject jo = null;
		try {
			jo = new JSONObject(Config.CURRENT_MAPVIEW_COORDS);

			gpMain = new GeoPoint(
					Integer.parseInt(jo.getString("latitude").toString()),
					Integer.parseInt(jo.getString("longitude").toString()));
	        mcMain.setZoom(Integer.parseInt(jo.getString("zoom").toString()));
	        mcMain.animateTo(gpMain);
		} catch (JSONException e) {
			e.printStackTrace();
		}

        // Redraw traffic
        mvTraffic.invalidate();

        // Put in some catch all redraws to play out after all the map tiles have loaded.
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } },  1000);
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } },  3000);
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } },  5000);
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 10000);
	}
	
	/**
	 * Called by Runnables multiple times to draw 
	 */
	public void refreshTrafficMap() {
		if (MAP_VIEW_IS_VISIBLE) {
			Log.d(TAG, "mvTraffic.invalidate");
	        mvTraffic.invalidate();
		}
	}

	//
	// Map view map
	//----------------------------------------------------------

	
	
	
	
	
	public boolean setCurrentTab(int viewIndex) {
        if(Config.DEBUG>0)Log.d(TAG, "setCurrentTab() viewIndex: " + viewIndex);

		// Save Settings
        // FIXME - Is this really happening? Not committing... Maybe I don't need this.
		Session.saveInt(mySharedPreferences, "session_current_view", viewIndex);

	    return true;
	}
	
	/**
	 * Like reload views but displays a toast about it.
	 */
	public void refreshViews() {
        if(Config.DEBUG>0)Log.d(TAG, "refreshViews");
		Toast.makeText(getApplicationContext(), R.string.txt_loading, Toast.LENGTH_SHORT).show();

		reloadViews();
		refreshTrafficMap();
	}

	/**
	 * No toast message
	 */
	public void reloadViews() {
		
		// Check for if a new version check is needed
		String versionCheckParams = "";
		if (Config.DEBUG_FORCE_NEW_VERSION_CHECK || CHECK_FOR_NEW_VERSION) {
			versionCheckParams = v.getVersionCheckURLParams();
		}

		// Only check once per onCreate();
		CHECK_FOR_NEW_VERSION = false;
		
		String reloadString = 
			WEBVIEW_URL
			+ "?target=" + CURRENT_TAB_INDEX
			+ versionCheckParams
			+ "&city_id=" + Config.APP_CODE
			+ MapsTab.getReloadURLParts()
			+ CalendarTab.getReloadURLParts()
			+ CamerasTab.getReloadURLParts();
			
		// Refresh main content webview
		wvMain.loadUrl(reloadString);
		if(Config.DEBUG>0) Log.d(TAG, "reloadViews() " + reloadString);
		
		// Refresh banner webview
		//wvAd.loadUrl(AD_BANNER_URL);
	}

	public void activateViewType(int v) {
		mvTraffic.setVisibility(View.INVISIBLE);
		wvMain.setVisibility(View.INVISIBLE);
		
		switch(v) {
			case WEBVIEW:
				wvMain.setVisibility(View.VISIBLE);
				if(Config.DEBUG>0) Log.d(TAG,"activateViewType() WEBVIEW");
				break;
			case MAPVIEW:
				mvTraffic.setVisibility(View.VISIBLE);			
				if(Config.DEBUG>0) Log.d(TAG,"activateViewType() MAPVIEW");
				break;
		};
	}

    
	
	
	
    
    
    // -----------------------------------------------
    // Options Menu
    //

    /* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		
        // if(Config.DEBUG>0)Log.d(TAG, "onCreateOptionsMenu");

	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if (item.getItemId() == R.id.menu_radios) {
			ScannerRadio.launchScanner(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE], me.getApplicationContext());
			return true;
		} else if (item.getItemId() == R.id.menu_refresh) {
			refreshViews();
			refreshTrafficMap();
			return true;
		} else if (item.getItemId() == R.id.menu_prefs) {
			Intent intent = new Intent();
			intent.setClassName(this, "com.osilabs.android.apps.libtrafficapp.Prefs");
			this.startActivityForResult(intent, INTENT_RESULT_CODE_PREFS);
			return true;
		} else if (item.getItemId() == R.id.menu_share) {
			final Intent shareintent = new Intent(Intent.ACTION_SEND);
			shareintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			shareintent.setType("text/plain");
			shareintent.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.txt_sharing_subject_a) + " " + this.getResources().getString(R.string.app_name) + " " + this.getResources().getString(R.string.txt_sharing_subject_b));
			shareintent.putExtra(Intent.EXTRA_TEXT, 
						this.getResources().getString(R.string.txt_sharing_body_a) + " "
						+ this.getResources().getString(R.string.app_name) + " "
						+ this.getResources().getString(R.string.txt_sharing_body_b) + "\n\n"
						+ this.getResources().getString(R.string.txt_sharing_body_c) + "\n\n"
						+ this.getResources().getString(R.string.market_link_http) + "\n\n"
						+ this.getResources().getString(R.string.txt_sharing_body_d) + "\n\n"
						+ this.getResources().getString(R.string.txt_sharing_body_e) + "\n\n"
						+ this.getResources().getString(R.string.app_name) + "\n\n"
						+ this.getResources().getString(R.string.txt_sharing_body_f));
			startActivity(Intent.createChooser(shareintent, "Share"));
			return true;
		} else if (item.getItemId() == R.id.menu_help) {
			activateViewType(WEBVIEW);
			Favorites.setStarIcon(Favorites.MODE_GONE);
			HELP_IS_VISIBLE = true;
			Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
			wvMain.loadUrl(MOBILECONTENT_URL_HELP);
			return true;
		} else if (item.getItemId() == R.id.menu_about) {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle(R.string.app_name);
			alertDialog.setMessage(getApplicationContext().getResources().getString(R.string.txt_version) + " " + v.versionName());
			alertDialog.setButton(this.getResources().getString(R.string.txt_btn_more), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
			    	activateViewType(WEBVIEW);
			    	Favorites.setStarIcon(Favorites.MODE_GONE);
			    	ABOUT_IS_VISIBLE = true;
					Toast.makeText(getApplicationContext(), R.string.txt_loading, Toast.LENGTH_LONG).show();
			    	wvMain.loadUrl(Config.MOBILECONTENT_URL_ABOUT);
			    } 
			});
			alertDialog.setIcon(R.drawable.ic_launcher);
			alertDialog.show();
			return true;
		} else if (item.getItemId() == R.id.menu_feedback) {
			String phoneinfo = 
				getApplicationContext().getResources().getString(R.string.app_name ) + 
				" v" + v.versionName() + ", " +
				Build.MANUFACTURER + ", " +
				Build.MODEL + ", " +
				Build.BRAND + ", " +
				Build.DEVICE + ", " +
				Build.DISPLAY + ", " +
				Build.FINGERPRINT + ", " +
				Build.PRODUCT + ", " +
				Build.VERSION.RELEASE;
			activateViewType(WEBVIEW);
			FEEDBACK_IS_VISIBLE = true;
			Favorites.setStarIcon(Favorites.MODE_GONE);
			Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
			wvMain.loadUrl(MOBILECONTENT_URL_FEEDBACK + "?phoneinfo=" + URLEncoder.encode(phoneinfo));
			wvMain.requestFocus(View.FOCUS_DOWN); // Necessary or the input boxes may not take input.
			return true;
		} else if (item.getItemId() == R.id.menu_exit) {
			finish();
			return true;
		}
		
	    return false;
	}
    //
    // Options Menu
    // -----------------------------------------------











	// -----------------------------------------------
    // Web browser
    //
	private class MyWebViewClient extends WebViewClient {
		Activity activity;
		public MyWebViewClient(Activity a) { activity = a; }

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
	        // if(Config.DEBUG>0)Log.d(TAG, "mywebviewclient::shouldoverrideurlloading");
			
			// 0=in webview, 1=in system browser
			int launchmode = 1;
			
			switch ( launchmode ) {
				case 0:
					view.loadUrl(url);
					break;
					
				case 1:
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					startActivity(i);
					break;
			}
			
			return true;
		}
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			Toast.makeText(activity, "Could not connect. Please try again when the internet is available.", Toast.LENGTH_SHORT).show();
		}
	}

	final class MyWebChromeClient extends WebViewClient {
        // @Override
        public boolean onJsCalendar(WebView view, String url, String message, JsResult result) {
            // if(Config.DEBUG>0)Log.d(TAG, "MyWebChromeClient::onjsalert");

            result.confirm();
            return true;
        }
        // Javascript in webview can call colsole.log('the message') to log messages.
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			// if(Config.DEBUG>0)Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
		}
	}
    
    final class JsiJavaScriptInterface {
    	App parent;
    	JsiJavaScriptInterface(App parent) { this.parent = parent; }

    	public void setLoadedTab(int loadedIndex) {
    		// This can't set current tab for the following reason:
    		//  android.view.ViewRoot$CalledFromWrongThreadException: 
    		//  Only the original thread that created a view hierarchy 
    		//  can touch its views.
    		//
    		// setViewForCurrentTab(loadedIndex); 
    	}
    	
    	public void feedbackSaved() {
			Toast.makeText(getApplicationContext(), "Thank you for your feedback", Toast.LENGTH_LONG).show();
			
		    final Runnable r = new Runnable() { 
		        public void run () {
		        	setViewForCurrentTab(CURRENT_TAB_INDEX);
		            reloadViews(); // Here you can modify UI 
		        }
		    }; 
		    parent.mHandler.post(r);  // adding this to queue 
    	}	
    	
    	public void newVersionAlert(String msg) {
	        AlertDialog alertDialog = new AlertDialog.Builder(App.me).create();
	        alertDialog.setTitle(R.string.app_name);
	        // FIXME - Move these to strings xml
	        alertDialog.setMessage("A new version is available. It will bring new features or stability. What would you like to do?");
	        alertDialog.setButton("Remind me later", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		// Do nothing, alert closes.
	            } 
	        });
	        alertDialog.setButton2("Upgrade", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	        		Intent intent = new Intent(Intent.ACTION_VIEW);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        		intent.setData(Uri.parse(me.getResources().getString(R.string.market_link_http)));
	        		startActivity(intent);
	            } 
	        });
	        alertDialog.setIcon(R.drawable.ic_launcher);
	        alertDialog.show();
    	}	

    }
    //
    // Web browser
    // -----------------------------------------------
}
	
    
	



//
// Geocodes
//
////gcMain = new Geocoder(this, Locale.getDefault());    
//Geocoder ass = new Geocoder(this, Locale.getDefault());    
//try {
//	// FIXME - move to config
//  List<Address> addresses = ass.getFromLocationName("Minneapolis, MN", 5);
//  if (addresses.size() > 0) {
//      gpMain = new GeoPoint(
//              (int) (addresses.get(0).getLatitude() * 1E6), 
//              (int) (addresses.get(0).getLongitude() * 1E6));
//      mcMain.animateTo(gpMain);
//      mcMain.setZoom(11);
//  	mvMain.setTraffic(true);
//      mvMain.invalidate();
//  }    
//} catch (IOException e) {
//  e.printStackTrace();
//}



