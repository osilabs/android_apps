package com.osilabs.android.apps;

import java.io.IOException;
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
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ViewFlipper;

public class TCT extends MapActivity {

	//
	// Consts
	//

	private static final String TAG = "** osilabs.com **";

	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_ALERTS                = 1;
	private static final int MENU_CAMERAS               = 2;
	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_ALERTS               = 1;
	private static final int INDEX_CAMERAS              = 2;
	private static final int INTENT_RESULT_CODE_CAMERA_PICKER= 22;
	private static final int INTENT_RESULT_CODE_PREFS   = 33;

	//
	// Globals
	//

	// Prefs
	protected static String WEBVIEW_URL = "";
	protected static String AD_BANNER_URL = "";

	// Will need to up this number if more indexes are needed.
	protected static int CURRENT_VIEW_INDEX = 0;

	protected static PackageInfo pInfo = null;
	protected static Spinner spViewChoices;
	protected static WebView wvAd;
	protected static WebView wvMain;
	protected static SharedPreferences mySharedPreferences;

	// Navbar components

	// Tabs
	protected ImageView ivMaps;
	protected ImageView ivAlerts;
	protected ImageView ivCameras;
	
	// Configs
	protected ImageView ivMapMore;
	protected TextView  tvMapsPop;
	
	protected ImageView ivAlertMore;
	protected TextView  tvAlertsPop;
	
	protected ImageView ivCameraMore;
	protected TextView  tvCamerasPop;

	// Misc icons
	protected ImageView ivRefresh;
	protected ImageView ivRadios;

	// View flipper
	protected ViewFlipper vfMain;

	// Mapview stuff
	protected static MapView mvMain;
    protected static MapController mcMain;
    protected static GeoPoint gpMain;
    //protected static Geocoder gcMain;
    
	// Tints and paints
	protected int color_tab;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Log.d(TAG, "onCreate");

        //
        // Set globals
        //
//        vfMain = (ViewFlipper) findViewById(R.id.mainViewFlipper);
//        vfMain.
        
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
        CURRENT_VIEW_INDEX = mySharedPreferences.getInt("session_current_view", 2);
        
        MapsTab.CURRENT_INDEX = mySharedPreferences.getInt("session_map", Config.DEFAULT_MAP_INDEX);
        AlertsTab.CURRENT_INDEX = mySharedPreferences.getInt("session_alert", Config.DEFAULT_ALERT_INDEX);
        CamerasTab.CURRENT_CAMERA_URL = mySharedPreferences.getString("session_camera_1", Config.DEFAULT_CAMERA_URL);

        
	    // -------------------------
	    // Top Nav bar
	    //

	    ivMaps = (ImageView) findViewById(R.id.launcher_traffic);
	    ivMaps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewForCurrentTab(INDEX_TRAFFIC);
			}
		});

	    ivAlerts = (ImageView) findViewById(R.id.launcher_alerts);
	    // This particular icon is much whiter than the others so i am making it darker
	    //  with the alpha.
	    ivAlerts.setAlpha(AlertsTab.ALPHA_OFF);
	    ivAlerts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewForCurrentTab(INDEX_ALERTS);
			}
		});

	    // Set up camera tab
	    ivCameras = (ImageView) findViewById(R.id.launcher_cameras);
	    ivCameras.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setViewForCurrentTab(INDEX_CAMERAS);
			}
		});

	    ivRefresh = (ImageView) findViewById(R.id.navbar_refresh);
	    // Give it a nice blue color. SRC_ATOP means color the icon, not
	    //  the background.
	    ivRefresh.setColorFilter(getResources().getColor(R.color.darkPowderBlue), PorterDuff.Mode.SRC_ATOP); // same as tint
	    ivRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshViews();
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
    	wvMain.setVisibility(View.VISIBLE);

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
    	mvMain = (MapView) findViewById(R.id.mainMapView);
    	mvMain.setBuiltInZoomControls(true);
		mcMain = mvMain.getController();
        //gcMain = new Geocoder(this, Locale.getDefault());    
//        Geocoder ass = new Geocoder(this, Locale.getDefault());    
//        try {
//        	// FIXME - move to config
//            List<Address> addresses = ass.getFromLocationName("Minneapolis, MN", 5);
//            if (addresses.size() > 0) {
//                gpMain = new GeoPoint(
//                        (int) (addresses.get(0).getLatitude() * 1E6), 
//                        (int) (addresses.get(0).getLongitude() * 1E6));
//                mcMain.animateTo(gpMain);
//                mcMain.setZoom(11);
//            	mvMain.setTraffic(true);
//                mvMain.invalidate();
//            }    
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        drawTrafficMap();
    	mvMain.setVisibility(View.INVISIBLE);

	    // -------------------------
	    // Bottom Navigation Bar
	    //
        
        // Maps config...
		ivMapMore = (ImageView) findViewById(R.id.maps_config_pop_icon);
		ivMapMore.setColorFilter(getResources().getColor(R.color.lighterDarkPowderBlue), PorterDuff.Mode.SRC_ATOP);
		ivMapMore.setOnClickListener(new View.OnClickListener() {
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
        
        // Alerts config...
		ivAlertMore = (ImageView) findViewById(R.id.alerts_config_pop_icon);
		ivAlertMore.setColorFilter(getResources().getColor(R.color.lighterDarkPowderBlue), PorterDuff.Mode.SRC_ATOP);
		ivAlertMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchAlertPicker();
			}
		});
    	tvAlertsPop = (TextView) findViewById(R.id.alerts_config_pop);
    	tvAlertsPop.setTextColor(getResources().getColor(R.color.lighterDarkPowderBlue));
    	tvAlertsPop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchAlertPicker();
			}
		});

        // Cameras config...
		ivCameraMore = (ImageView) findViewById(R.id.cameras_config_pop_icon);
		ivCameraMore.setColorFilter(getResources().getColor(R.color.lighterDarkPowderBlue), PorterDuff.Mode.SRC_ATOP);
		ivCameraMore.setOnClickListener(new View.OnClickListener() {
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
    					setCurrentRadios();

						launchScanner( Config.RADIOS_CURRENT_NODE[which]);
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

	    //
    	// Set the current tab and load it
    	//
	    
        // It's going to take a second to load
		Toast.makeText(this, R.string.txt_loading, Toast.LENGTH_LONG).show();
		
    	setViewForCurrentTab(CURRENT_VIEW_INDEX);
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
        .setItems(Config.maps, new DialogInterface.OnClickListener() {
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
    protected void launchAlertPicker() { 
        // Log.d(TAG, "LaunchAlertPicker");

		AlertDialog alert = new AlertDialog.Builder(this)
        .setTitle(R.string.txt_alert_popup_title)
        .setItems(Config.alerts, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	AlertsTab.CURRENT_INDEX = which;
    			Toast.makeText(getApplicationContext(), 
    					R.string.txt_loading
    					, Toast.LENGTH_LONG).show();
    			
    			// Save current ALERT
    	    	SharedPreferences prefs 
    				= getSharedPreferences(Config.NAMESPACE, Activity.MODE_PRIVATE);
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putInt("session_alert", AlertsTab.CURRENT_INDEX);
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
			Toast.makeText(getApplicationContext(), 
					R.string.txt_prefs_saved
					, Toast.LENGTH_LONG).show();
			break;
        	
	        default:
	            break;
	    }
	}


    
    protected void setCurrentRadios() {
        // Log.d(TAG, "setCurrentRadios");

    	// Set Global with current prefs
    	// If this namespace path doesn't end in '_preferences' this won't work.
    	mySharedPreferences = getSharedPreferences(Config.NAMESPACE + "_preferences", 0);

		String wr_saved = getApplicationContext().getResources().getString(R.string.pref_weather_radios_selected);
		Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER] = Integer.parseInt(mySharedPreferences
	      .getString(wr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER])));

		String pr_saved = getApplicationContext().getResources().getString(R.string.pref_police_radios_selected);
		Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE] = Integer.parseInt(mySharedPreferences
	      .getString(pr_saved, Integer.toString(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE])));
    }
    
	public void colorTheCurrentTab() {
        // Log.d(TAG, "colorTheCurrentTab");

		MapsTab.setInactive(ivMaps);
		AlertsTab.setInactive(ivAlerts);
		CamerasTab.setInactive(ivCameras);
		
		switch (CURRENT_VIEW_INDEX) {
			case MENU_TRAFFIC:
				MapsTab.setActive(ivMaps);
				break;
		    case MENU_ALERTS:
				AlertsTab.setActive(ivAlerts);
		    	break;
		    case MENU_CAMERAS:
				CamerasTab.setActive(ivCameras);
		    	break;
		}
	}

	public void setViewForCurrentTab(int view_index) {
		//Log.d(TAG, "setViewForCurrentTab index" + Integer.toString(view_index));

		String scrollx = "0";
		String scrolly = "0";

		CURRENT_VIEW_INDEX = view_index;
		setCurrentView(CURRENT_VIEW_INDEX);

		/**
		 * OnTab switch
 		 * 1. Make current view invisible
		 * 2. Make the tab clicked green
		 * 3. Swap in loading message
         * 4. Do all the expensive work, trap errors, if errors, ? pop up config picker to pick another map?
		 */

		// Hide stuff
    	mvMain.setVisibility(View.INVISIBLE);
    	wvMain.setVisibility(View.INVISIBLE);
    	
    	mvMain.forceLayout();

    	MapsTab.hideConfiguration(ivMapMore, tvMapsPop);
        AlertsTab.hideConfiguration(ivAlertMore, tvAlertsPop);
        CamerasTab.hideConfiguration(ivCameraMore, tvCamerasPop);
        
        // Makes the current tab green
        colorTheCurrentTab();
        
		switch (CURRENT_VIEW_INDEX) {
			case MENU_TRAFFIC:
				CURRENT_VIEW_INDEX = INDEX_TRAFFIC;
		        MapsTab.showConfiguration(ivMapMore, tvMapsPop);
		        scrollx = MapsTab.getScrollX();
		        scrolly = MapsTab.getScrollY();
		        
		        String viewtype = MapsTab.getViewType();

		        // FIXME - use a global to track the current view type and don't do anything
		        //  if it is already set.
		        if (viewtype.equals("map")) {
		        	// Show map
		    		Toast.makeText(getApplicationContext(), "Loading map", Toast.LENGTH_SHORT).show();
		        	mvMain.invalidate();
		        	mvMain.setVisibility(View.VISIBLE);
		        	refreshTrafficMap();
		        } else if (viewtype.equals("web")) {
		        	// Show webview
		        	wvMain.setVisibility(View.VISIBLE);
					wvMain.loadUrl("javascript: jumpTo("+CURRENT_VIEW_INDEX+ "," +scrollx+ "," +scrolly+ ")");
		        }
		        //reloadViews();
		        
				break;
		    case MENU_ALERTS:
		        CURRENT_VIEW_INDEX = INDEX_ALERTS;
		        AlertsTab.showConfiguration(ivAlertMore, tvAlertsPop);
	        	wvMain.setVisibility(View.VISIBLE);
				wvMain.loadUrl("javascript: jumpTo("+CURRENT_VIEW_INDEX+ "," +scrollx+ "," +scrolly+ ")");
		    	break;
		    case MENU_CAMERAS:
		        CURRENT_VIEW_INDEX = INDEX_CAMERAS;
		        CamerasTab.showConfiguration(ivCameraMore, tvCamerasPop);
	        	wvMain.setVisibility(View.VISIBLE);
				wvMain.loadUrl("javascript: jumpTo("+CURRENT_VIEW_INDEX+ "," +scrollx+ "," +scrolly+ ")");
		        break;
		}

	}
	
	
	
	//----------------------------------------------------------
	// Map view map
	//
	public void drawTrafficMap() {
        try {
            Geocoder ass = new Geocoder(this, Locale.getDefault());    
        	// FIXME - move to config
            // FIXME - Use coordinates for efficitency
            List<Address> addresses = ass.getFromLocationName("Minneapolis, MN", 5);
            if (addresses.size() > 0) {
                gpMain = new GeoPoint(
                        (int) (addresses.get(0).getLatitude() * 1E6), 
                        (int) (addresses.get(0).getLongitude() * 1E6));
                mcMain.animateTo(gpMain);
                mcMain.setZoom(11);
            	mvMain.setTraffic(true);
                mvMain.invalidate();
            }    
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void refreshTrafficMap() {
        mcMain.animateTo(gpMain);
        mcMain.setZoom(11);
    	mvMain.setTraffic(true);
        mvMain.invalidate();
	}
	//
	// Map view map
	//----------------------------------------------------------

	
	
	
	
	
	public boolean setCurrentView(int viewIndex) {
        Log.d(TAG, "setCurrentView");

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
		reloadViews();
	}
	
	public void reloadViews() {
		Log.d(TAG, "reloadViews()");
		
		// Refresh main content webview
		wvMain.loadUrl(WEBVIEW_URL
						+ "?target=" + CURRENT_VIEW_INDEX
						+ MapsTab.getReloadURLParts()
						+ AlertsTab.getReloadURLParts()
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
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
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
				setCurrentRadios();
				launchScanner(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_POLICE]);
		    	return true;
	
			case R.id.menu_scanner_weather:
				setCurrentRadios();
				launchScanner(Config.RADIOS_CURRENT_NODE[Config.INDEX_OF_WEATHER]);
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

	
	
    
	
	
	
	// -----------------------------------------------
	// Scanner Radio
	// 
		
	// I tried moving it to it's own class but need to watch for having pass
	//  context and creating memory leaks. see"
	// http://developer.android.com/resources/articles/avoiding-memory-leaks.html
	
	public static final String SCANNER_RADIO_NAMESPACE = "net.gordonedwards.scannerradio";
	public static final String SCANNER_RADIO_ACTION = "ACTION_PLAY_SCANNER";
	
	public static boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =
	        packageManager.queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	public void launchScanner(int which_scanner) {
		// Log.d(TAG, "Scanner node: " + Integer.toString(which_scanner));
		
		boolean scannerAvailable = isIntentAvailable(this,
				SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);

		// FIXME - put these strings in file and move this to own scanner radio class
		
		if (scannerAvailable) {
			Intent intent = new Intent(SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("node", which_scanner);
			startActivity(intent);
		} else {
		    AlertDialog scannerAlert = new AlertDialog.Builder(this).create();
	        scannerAlert.setTitle(R.string.app_name);
	        scannerAlert.setMessage("To use this feature, install the \"Scanner Radio\" app from the market");
	        scannerAlert.setIcon(R.drawable.ic_launcher); 
	        scannerAlert.setButton("Get the plugin", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	    			Intent intent = new Intent(
	    					Intent.ACTION_VIEW,
	    					Uri.parse("market://details?id=com.scannerradio"));
	    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    			startActivity(intent);
	            } 
	        });
	        scannerAlert.show();
		}
	}

	// 
	// Scanner Radio
    // -----------------------------------------------

}