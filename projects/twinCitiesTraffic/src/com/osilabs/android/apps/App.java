package com.osilabs.android.apps;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
	private static final int INTENT_RESULT_CODE_CAMERA_PICKER= 22;
	private static final int INTENT_RESULT_CODE_PREFS   = 33;

	//
	// Globals
	//

	// Prefs
	protected static String WEBVIEW_URL = "";
	protected static String AD_BANNER_URL = "";

	// Will need to up this number if more indexes are needed.
	protected static int CURRENT_TAB_INDEX = 0;

	// Tracks when the MapView is showing
	protected static boolean MAP_VIEW_IS_VISIBLE = false;

	protected static PackageInfo pInfo = null;
	protected static Spinner spViewChoices;
	protected static WebView wvAd;
	protected static WebView wvMain;
	protected static SharedPreferences mySharedPreferences;

	// Navbar components

	// Tabs
	protected static ImageView ivMapsTab;
	protected static ImageView ivCalendarTab;
	protected static ImageView ivCamerasTab;
	
//	// Tab Views
//	protected ImageView ivTraffic;
//	protected ScrollView damien;
	
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
    //protected static Geocoder gcMain;
    
	// Tints and paints
	protected int color_tab;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(TAG, "onCreate");

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
        CURRENT_TAB_INDEX = mySharedPreferences.getInt("session_current_view", 2);
        
        MapsTab.CURRENT_INDEX = mySharedPreferences.getInt("session_map", Config.DEFAULT_MAP_INDEX);
        CalendarTab.CURRENT_INDEX = mySharedPreferences.getInt("session_calendar", Config.DEFAULT_CALENDAR_INDEX);
        CamerasTab.CURRENT_CAMERA_URL = mySharedPreferences.getString("session_camera_1", Config.DEFAULT_CAMERA_URL);
        
	    // -------------------------
	    // Top Nav bar
	    //

	    ivMapsTab = (ImageView) findViewById(R.id.launcher_traffic);
//		ivMapsTab.setBackgroundResource(R.drawable.bg_topnav_vgradient);
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
	    ivCamerasTab = (ImageView) findViewById(R.id.launcher_cameras);
	    ivCamerasTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewForCurrentTab(INDEX_CAMERAS);
			}
		});
		CamerasTab.init();


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

		//
		// Ad banner Web View
		//
		wvAd = (WebView) findViewById(R.id.adWebView);
        WebSettings awebSettings = wvAd.getSettings();
        awebSettings.setJavaScriptEnabled(true);
        awebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
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

		//
		// Main Image View
		//
//	    ivTraffic = (ImageView) findViewById(R.id.trafficImageView);
//	    ivTraffic.setVerticalScrollBarEnabled(true);
//	    ivTraffic.setHorizontalScrollBarEnabled(true);
//	    ivTraffic.setScaleType(ImageView.ScaleType.CENTER);
//	    damien = (ScrollView) findViewById(R.id.trafficImageViewScroll);
//	    //sv.removeAllViews();
		
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
    	super.onStart();
    	
    	// Log.d(TAG, "onStart");
    	
    	// Hide the splash
    	//ImageView splash = (ImageView) findViewById(R.id.splash);
    	//splash.setVisibility(View.GONE);

    	//
        // FIXME - This should only happen if mapview is currently active
    	//
    	// Force the mapview to draw it's traffic layer when it's ready. Progressive approach.
    	mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 3000);
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 5000);
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 10000);
        mvTraffic.postDelayed(new Runnable() { public void run() { refreshTrafficMap(); } }, 15000);
    	drawTrafficMap();

    	// Redraw mapview
    	mvTraffic.invalidate();
    	
	    //
    	// Set the current tab and load it
    	//
	    
        // It's going to take a second to load
		Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
		
		setCurrentFavorites();
    	setViewForCurrentTab(CURRENT_TAB_INDEX);
		reloadViews();
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

        AlertDialog alert = new AlertDialog.Builder(this)
        .setTitle(R.string.txt_map_popup_title)
        .setIcon(R.drawable.ic_police)
        .setItems(Config.traffic, new DialogInterface.OnClickListener() {
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
    			Toast.makeText(getApplicationContext(), 
    					R.string.txt_camera_picker_noop
    					, Toast.LENGTH_LONG).show();
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
		//String wf_saved = c.getResources().getString(R.string.pref_weather_feed_selected);
		//CalendarTab.CURRENT_WEATHER_FEED_INDEX = mySharedPreferences.getInt(wf_saved, 0);
		String wf_saved = c.getResources().getString(R.string.pref_weather_feed_selected);
		CalendarTab.CURRENT_WEATHER_FEED_INDEX = Integer.parseInt(mySharedPreferences
			.getString(wf_saved, "0"));

    }
    
//    protected void setCurrentRadios() {
//        // Log.d(TAG, "setCurrentRadios");
//
//    	// Set Global with current prefs
//    	// If this namespace path doesn't end in '_preferences' this won't work.
//    	mySharedPreferences = getSharedPreferences(Config.NAMESPACE + "_preferences", 0);
//
//		String wr_saved = getApplicationContext().getResources().getString(R.string.pref_weather_radios_selected);
//		Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER] = Integer.parseInt(mySharedPreferences
//	      .getString(wr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER])));
//
//		String pr_saved = getApplicationContext().getResources().getString(R.string.pref_police_radios_selected);
//		Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE] = Integer.parseInt(mySharedPreferences
//	      .getString(pr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE])));
//    }
    
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
//
//		String scrollx = "0";
//		String scrolly = "0";

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
		mvTraffic.setVisibility(View.INVISIBLE);
		wvMain.setVisibility(View.INVISIBLE);
    	
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
			        	mvTraffic.invalidate();
			        	drawTrafficMap();
			        	mvTraffic.setVisibility(View.VISIBLE);
			        	
			        	// Set flag indicating this thing is visible.
			        	MAP_VIEW_IS_VISIBLE = true;
			        	pulseMapView();
			        	
						break;
					case Config.IMAGE:
					case Config.FEED:
					case Config.WEB:
						// FIXME - rename to wvTraffic
			        	wvMain.setVisibility(View.VISIBLE);
						//wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ "," +scrollx+ "," +scrolly+ ")");
						wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ ")");
						break;
				}
		        
				break;
		    case MENU_CALENDAR:
		    	// FIXME - i think these are already set above
		        CURRENT_TAB_INDEX = INDEX_CALENDAR;
		        CalendarTab.showConfiguration();
	        	wvMain.setVisibility(View.VISIBLE);
				//wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ "," +scrollx+ "," +scrolly+ ")");
				wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ ")");
		    	break;
		    case MENU_CAMERAS:
		        CURRENT_TAB_INDEX = INDEX_CAMERAS;
		        CamerasTab.showConfiguration();
	        	wvMain.setVisibility(View.VISIBLE);
				//wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ "," +scrollx+ "," +scrolly+ ")");
				wvMain.loadUrl("javascript: jumpTo("+CURRENT_TAB_INDEX+ ")");
		        break;
		}
	}
		
	//----------------------------------------------------------
	// Map view map
	//
	public void drawTrafficMap() {
        gpMain = new GeoPoint(
        		Config.GEO_POINTS[0][0], 
        		Config.GEO_POINTS[0][1]);
        mcMain.animateTo(gpMain);
        mcMain.setZoom(11);
        
        refreshTrafficMap();
	}
	
	public void refreshTrafficMap() {
		Toast.makeText(getApplicationContext(), "++ Reloading Interactive Map", Toast.LENGTH_SHORT).show();

        mvTraffic.invalidate();
	}
	
	public void pulseMapView() {
    	if (MAP_VIEW_IS_VISIBLE) {
    		// If showing, invalidates every 30 seconds
    		refreshTrafficMap();
    		
    		mvTraffic.postDelayed(new Runnable() { public void run() { pulseMapView(); } }, 20000);
    	}
	}

	//
	// Map view map
	//----------------------------------------------------------

	
	
	
	
	
	public boolean setCurrentTab(int viewIndex) {
        Log.d(TAG, "setCurrentTab");

		// Save Settings
    	SharedPreferences prefs 
			= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt("session_current_view", viewIndex);
	    editor.commit();

	    return true;
	}
		
	public void refreshViews() {
        Log.d(TAG, "refreshViews");

		Toast.makeText(getApplicationContext(), R.string.txt_refreshing, Toast.LENGTH_SHORT).show();

		if (MAP_VIEW_IS_VISIBLE) {
        	mvTraffic.invalidate();
        }
        
		reloadViews();
	}
	
	public void reloadViews() {
		// Refresh main content webview
		wvMain.loadUrl(WEBVIEW_URL
						+ "?target=" + CURRENT_TAB_INDEX
						+ MapsTab.getReloadURLParts()
						+ CalendarTab.getReloadURLParts()
						+ CamerasTab.getReloadURLParts());
		
		// Refresh banner webview
		wvAd.loadUrl(AD_BANNER_URL);
	}

    
	
	
	
	
	
	
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
	        // Log.d(TAG, "mywebviewclient::onrecievederror");

			Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
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

		Activity activity;
    	JsiJavaScriptInterface(Activity a) { activity = a; }

    	public void setLoadedTab(int loadedIndex) {
    		// This can't set current tab for the following reason:
    		//  android.view.ViewRoot$CalledFromWrongThreadException: 
    		//  Only the original thread that created a view hierarchy 
    		//  can touch its views.
    		//
    		// setViewForCurrentTab(loadedIndex); 
    	}	
    }
    //
    // Web browser
    // -----------------------------------------------

    
    
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
        		Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.MOBILECONTENT_URL_HELP + "?phoneinfo=" + URLEncoder.encode(phoneinfo))); 
				startActivity(mIntent); 
		    	return true;

		    case R.id.menu_about:
		        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		        alertDialog.setTitle(R.string.app_name);
		        alertDialog.setMessage(getApplicationContext().getResources().getString(R.string.txt_version) + " " + pInfo.versionName);
		        alertDialog.setButton(this.getResources().getString(R.string.txt_btn_more), new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		Intent mIntent = new Intent(Intent.ACTION_VIEW, 
		        				Uri.parse(Config.MOBILECONTENT_URL_ABOUT)); 
        				startActivity(mIntent); 
		            } 
		        });
		        alertDialog.setIcon(R.drawable.ic_launcher);
		        alertDialog.show();
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



