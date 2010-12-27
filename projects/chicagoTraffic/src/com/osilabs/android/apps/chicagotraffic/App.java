package com.osilabs.android.apps.chicagotraffic;

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
	private static final String TAG = "** osilabs.com **";

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

	//
	// Globals
	//

	// Prefs
	protected static String WEBVIEW_URL = "";
	protected static String AD_BANNER_URL = "";

	// Will need to up this number if more indexes are needed.
	protected static int CURRENT_TAB_INDEX = 0;
	
	// Flag indicating prefs have changed and need to be reread
	protected static boolean PREFS_UPDATED = false;
	

	// Tracks when the MapView is showing
	protected static boolean MAP_VIEW_IS_VISIBLE = false;
	protected static boolean FEEDBACK_IS_VISIBLE = false;
	protected static boolean HELP_IS_VISIBLE     = false;
	protected static boolean ABOUT_IS_VISIBLE    = false;
	
	protected static PackageInfo pInfo = null;
	protected static Spinner spViewChoices;
	protected static WebView wvAd;
	protected static WebView wvMain;
	protected static SharedPreferences mySharedPreferences;

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

	// Mapview stuff
	protected static MapView mvTraffic;
    protected static MapController mcMain;
    protected static GeoPoint gpMain;

    // For posting runnables
    private Handler mHandler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Log.d(TAG, "onCreate");

    	super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set global context;
        me = this;
        
        //
        // Set globals
        //
        
        // Read in manifest
		try {
			pInfo = getPackageManager().getPackageInfo(Config.NAMESPACE, PackageManager.GET_META_DATA);
			//version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// Set URLs with versioncode
		WEBVIEW_URL = Config.MOBILECONTENT_URL_PREFIX + pInfo.versionCode + "/trafficmap.php";
		AD_BANNER_URL = Config.MOBILECONTENT_URL_PREFIX + pInfo.versionCode + "/adbanner.php";
		
        //
		// Restore preferences
        //
		mySharedPreferences = getSharedPreferences(
				Config.NAMESPACE, Activity.MODE_PRIVATE);
        CURRENT_TAB_INDEX = mySharedPreferences.getInt("session_current_view", Config.DEFAULT_TAB_INDEX);
        
        MapsTab.CURRENT_INDEX = mySharedPreferences.getInt("session_map", Config.DEFAULT_MAP_INDEX);
        CalendarTab.CURRENT_INDEX = mySharedPreferences.getInt("session_calendar", Config.DEFAULT_CALENDAR_INDEX);
        CamerasTab.CURRENT_CAMERA_URL = mySharedPreferences.getString("session_camera_1", Config.DEFAULT_CAMERA_URL);
        
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
		MapsTab.init();

		ivCalendarTab = (ImageView) findViewById(R.id.launcher_calendar);
		App.ivCalendarTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewForCurrentTab(INDEX_CALENDAR);
			}
		});
		CalendarTab.init();
		
	    // Set up camera tab
		if (Config.mainroads.length > 0) {
		    ivCamerasTab = (ImageView) findViewById(R.id.launcher_cameras);
		    ivCamerasTab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setViewForCurrentTab(INDEX_CAMERAS);
				}
			});
			CamerasTab.init();
		}

	    ivRefresh = (ImageView) findViewById(R.id.navbar_refresh);
	    // Give it a nice blue color. SRC_ATOP means color the icon, not
	    //  the background.
	    ivRefresh.setColorFilter(getResources().getColor(R.color.darkPowderBlue), PorterDuff.Mode.SRC_ATOP); // same as tint
	    ivRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshViews();
				refreshTrafficMap(); // FIXME - make sure the menu option does the same.
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
				AlertDialog alert = new AlertDialog.Builder(v.getContext())
                .setTitle(R.string.radios_dialog_title)
                .setItems(Config.RADIOS, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
    		    		// Check prefs for changed radios.
    					//setCurrentRadios();

                    	ScannerRadio.launchScanner( Config.RADIOS_CURRENT_NODE[which]);
                    }
                }).create();
				
				alert.show();
			}
		});
    }

    @Override
    public void onStart() {
    	// Log.d(TAG, "onStart");
    	super.onStart();
    	
    	// Set some long term "pulse" timers to redraw the traffic lines after enough time
    	//  that they may have changed.
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 60000);
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 180000);

        if (MAP_VIEW_IS_VISIBLE) { drawTrafficMap(); }
        
    	//
    	// Set the current tab and load it
    	//
	    
        // It's going to take a second to load
		Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
		
		setCurrentFavorites();
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
    
    
    
    
    
    
    
    

    //
    // Map View Methods
    //
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

    protected void launchCameraPicker() { 
        // Log.d(TAG, "launchCameraPicker");

		Context c = getApplicationContext();
		Intent intent = new Intent().setClassName(c, Config.NAMESPACE + ".CameraELV");
		startActivityForResult(intent, INTENT_RESULT_CODE_CAMERA_PICKER); 
    }
    protected void launchMapPicker() {
        // Log.d(TAG, "launchMapPicker");

    	// Add selected favorites to the list of Traffic maps to view.
		//CharSequence[] options = new CharSequence[Config.traffic.length]; 
		//optionsConfig.traffic;
		//for(int i=0; i<Config.traffic.length; i++) { options[i] = Config.traffic[i]; }
//		System.arraycopy(Config.traffic, 0, options, 0, Config.traffic.length);
//		System.arraycopy(Config.traffic, 0, options, 0, Config.traffic.length);
		
//		int optionsIndex = options.length;

    	// Fixme, this size should be figured out first.
		List<String> sl = new ArrayList<String>(6);

		JSONArray ja;
		try {
			ja = new JSONArray(Config.FAVORITE_GEO_POINTS[0]);
			Log.d(TAG, "favs: " + ja.toString());
			Log.d(TAG, "labeled: " + ja.getJSONObject(0).getString("label").toString());
			
//			sl.add(ja.getJSONObject(0).getString("label").toString());			
//			sl.add(ja.getJSONObject(1).getString("zoom").toString());			
//			sl.add(ja.getJSONObject(2).getString("longitude").toString());			
//			options[3] = ja.getJSONObject(0).getString("label").toString();
//			options[4] = ja.getJSONObject(1).getString("label").toString();
//			options[5] = ja.getJSONObject(2).getString("label").toString();
			for(int i=0; i<ja.length(); i++) {
				//options[optionsIndex++] = ja.getJSONObject(i).getString("label").toString();
				sl.add("Favorite: " + ja.getJSONObject(i).getString("label").toString());			
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		// Tack on the system menu options
		for(int i=0; i<Config.traffic.length; i++) { sl.add(Config.traffic[i]); } 
		
		CharSequence[] options = (String[]) sl.toArray(new String[sl.size()]);
		
        AlertDialog alert = new AlertDialog.Builder(this)
        .setTitle(R.string.txt_map_popup_title)
        .setIcon(R.drawable.ic_police)
        .setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	MapsTab.CURRENT_INDEX = which;
    			Toast.makeText(getApplicationContext(), 
    					R.string.txt_loading
    					, Toast.LENGTH_LONG).show();
    			
    			// Save current map
    	    	SharedPreferences prefs 
    				= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putInt("session_map", MapsTab.CURRENT_INDEX);
			    editor.commit();
    			
			    setViewForCurrentTab(INDEX_TRAFFIC);
			    reloadViews();
            }
        }).create();
		
		alert.show();
    }
    protected void launchCalendarPicker() { 
        // Log.d(TAG, "LaunchCalendarPicker");

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
    	    	SharedPreferences prefs 
    				= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putInt("session_calendar", CalendarTab.CURRENT_INDEX);
			    editor.commit();
    			
            	reloadViews();
            }
        }).create();
		
		alert.show();
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // Log.d(TAG, "onActivityResult: " + Integer.toString(requestCode));

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
                CamerasTab.CURRENT_CAMERA_URL = extras.getString("selected_camera");
    			
    			// Reload the webview so it just shows the chosen camera
				reloadViews();
				
    			// Set the chosen camera in the persistent settings
    	    	SharedPreferences prefs 
    				= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
    			    SharedPreferences.Editor editor = prefs.edit();
    			    editor.putString("session_camera_1", CamerasTab.CURRENT_CAMERA_URL);
    			    editor.commit();
            }
            break;
            
        case INTENT_RESULT_CODE_PREFS:
        	setCurrentFavorites();
        	
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
    protected void setCurrentFavorites() {
    	// Only waste time rereading prefs if they changed.
    	if (PREFS_UPDATED) {
    		PREFS_UPDATED = false;
	        // Log.d(TAG, "setCurrentRadios");
	
	    	// Set Global with current prefs
	    	// If this namespace path doesn't end in '_preferences' this won't work.
	    	mySharedPreferences = getSharedPreferences(Config.NAMESPACE + "_preferences", 0);
	
	    	Context c = getApplicationContext();
	    	
			String wr_saved = c.getResources().getString(R.string.pref_weather_radios_selected);
			Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER] = Integer.parseInt(mySharedPreferences
		      .getString(wr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER])));
	
			String pr_saved = c.getResources().getString(R.string.pref_police_radios_selected);
			Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE] = Integer.parseInt(mySharedPreferences
		      .getString(pr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE])));
	
			// today feed
			String tf_saved = c.getResources().getString(R.string.pref_today_feed_selected);
			CalendarTab.CURRENT_TODAY_FEED_INDEX = Integer.parseInt(mySharedPreferences
				.getString(tf_saved, "0"));
	
			// weather feed
			String wf_saved = c.getResources().getString(R.string.pref_weather_feed_selected);
			CalendarTab.CURRENT_WEATHER_FEED_INDEX = Integer.parseInt(mySharedPreferences
				.getString(wf_saved, "0"));
	
			// Favorite Map View
			// FIXME - Make sure I call set current favorites after I update this value
			mySharedPreferences = getSharedPreferences(Config.NAMESPACE, 0);
		    Config.FAVORITE_GEO_POINTS[0]
		        = mySharedPreferences.getString("pref_favorite_map_frame_1", Config.GEO_POINTS[0]);
	
//		    Toast.makeText(getApplicationContext(), "prefs were gotten: " + Config.FAVORITE_GEO_POINTS[0], Toast.LENGTH_LONG).show();
    	}
    }
    
	public void colorTheCurrentTab() {
        // Log.d(TAG, "colorTheCurrentTab");

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
		//Log.d(TAG, "setViewForCurrentTab index" + Integer.toString(tab_index));

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
				
				switch(Config.traffic_viewtypes[ MapsTab.CURRENT_INDEX ]) {
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
		setCurrentFavorites();
		
//	    String[] aFavPoint = Config.FAVORITE_GEO_POINTS[0].split(":");
//		// Animate to a view.
//        gpMain = new GeoPoint(
//        		Integer.parseInt(aFavPoint[1]), 
//        		Integer.parseInt(aFavPoint[2]));
//        mcMain.setZoom(Integer.parseInt(aFavPoint[0]));
//        mcMain.animateTo(gpMain);
	    
		//Log.d(TAG, "Favorite: " + Config.FAVORITE_GEO_POINTS[0]);
		
		try {
			//JSONObject jo = ja.getJSONObject(0);
			//JSONObject jo = new JSONObject(Config.FAVORITE_GEO_POINTS[0]);			

			JSONArray ja = new JSONArray(Config.FAVORITE_GEO_POINTS[0]);			
			Log.d(TAG, "First label: " + ja.getJSONObject(0).getString("label").toString());
			
			gpMain = new GeoPoint(
					Integer.parseInt(ja.getJSONObject(0).getString("latitude").toString()),
					Integer.parseInt(ja.getJSONObject(0).getString("longitude").toString()));
	        mcMain.setZoom(Integer.parseInt(ja.getJSONObject(0).getString("zoom").toString()));
	        mcMain.animateTo(gpMain);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	    



        
//        gpMain = new GeoPoint(
//        		Config.GEO_POINTS[0][0], 
//        		Config.GEO_POINTS[0][1]);
//        mcMain.animateTo(gpMain);
//        mcMain.setZoom(11);

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
	        mvTraffic.invalidate();
		}
	}

	//
	// Map view map
	//----------------------------------------------------------

	
	
	
	
	
	public boolean setCurrentTab(int viewIndex) {
        // Log.d(TAG, "setCurrentTab");

		// Save Settings
    	SharedPreferences prefs 
			= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt("session_current_view", viewIndex);
	    editor.commit();

	    return true;
	}
	
	/**
	 * Like reload views but displays a toast about it.
	 */
	public void refreshViews() {
        // Log.d(TAG, "refreshViews");
		Toast.makeText(getApplicationContext(), R.string.txt_loading, Toast.LENGTH_SHORT).show();

		reloadViews();
		refreshTrafficMap();
	}

	/**
	 * No toast message
	 */
	public void reloadViews() {
		// Refresh main content webview
		wvMain.loadUrl(WEBVIEW_URL
						+ "?target=" + CURRENT_TAB_INDEX
						+ MapsTab.getReloadURLParts()
						+ CalendarTab.getReloadURLParts()
						+ CamerasTab.getReloadURLParts());
		
		// Refresh banner webview
		wvAd.loadUrl(AD_BANNER_URL);
		
		//Log.d(TAG, AD_BANNER_URL);
	}

	public void activateViewType(int v) {
		mvTraffic.setVisibility(View.INVISIBLE);
		wvMain.setVisibility(View.INVISIBLE);
		
		switch(v) {
			case WEBVIEW:
				wvMain.setVisibility(View.VISIBLE);			
				break;
			case MAPVIEW:
				mvTraffic.setVisibility(View.VISIBLE);			
				break;
		};
	}

    
	
	
	
	
	
	

    
    
    // -----------------------------------------------
    // Options Menu
    //

    /* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		
        // Log.d(TAG, "onCreateOptionsMenu");

	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		
        // Log.d(TAG, "onOptionsItemSelected:" + Integer.toString(item.getItemId()));
		
		switch (item.getItemId()) {
			case R.id.menu_scanner_police:
				//setCurrentRadios();
				ScannerRadio.launchScanner(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE]);
		    	return true;
	
			case R.id.menu_scanner_weather:
				//setCurrentRadios();
				ScannerRadio.launchScanner(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER]);
		    	return true;
				
			case R.id.menu_refresh:
				refreshViews();
				return true;
				
		    case R.id.menu_prefs:
		    	Intent intent = new Intent();
				intent.setClassName(this, Config.NAMESPACE + ".Prefs");
		    	this.startActivityForResult(intent, INTENT_RESULT_CODE_PREFS);
		    	return true;

		    case R.id.menu_help:
		    	activateViewType(WEBVIEW);
		    	HELP_IS_VISIBLE = true;
				Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
		    	wvMain.loadUrl(MOBILECONTENT_URL_HELP);
		    	return true;

		    case R.id.menu_about:
		        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		        alertDialog.setTitle(R.string.app_name);
		        alertDialog.setMessage(getApplicationContext().getResources().getString(R.string.txt_version) + " " + pInfo.versionName);
		        alertDialog.setButton(this.getResources().getString(R.string.txt_btn_more), new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
				    	activateViewType(WEBVIEW);
				    	ABOUT_IS_VISIBLE = true;
						Toast.makeText(getApplicationContext(), R.string.txt_loading, Toast.LENGTH_LONG).show();
				    	wvMain.loadUrl(Config.MOBILECONTENT_URL_ABOUT);
		            } 
		        });
		        alertDialog.setIcon(R.drawable.ic_launcher);
		        alertDialog.show();
		    	return true;

		    case R.id.menu_feedback:
		    	String phoneinfo = 
		    		getApplicationContext().getResources().getString(R.string.app_name ) + 
		    		" v" + pInfo.versionName + ", " +
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
				Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
		    	wvMain.loadUrl(MOBILECONTENT_URL_FEEDBACK + "?phoneinfo=" + URLEncoder.encode(phoneinfo));
		    	wvMain.requestFocus(View.FOCUS_DOWN); // Necessary or the input boxes may not take input.
		        return true;

		    case R.id.menu_exit:
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
	        // Log.d(TAG, "mywebviewclient::shouldoverrideurlloading");
			
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
            // Log.d(TAG, "MyWebChromeClient::onjsalert");

            result.confirm();
            return true;
        }
        // Javascript in webview can call colsole.log('the message') to log messages.
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			// Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
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
		            reloadViews(); // Here you can modify UI 
		        }
		    }; 
		    parent.mHandler.post(r);  // adding this to queue 
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



