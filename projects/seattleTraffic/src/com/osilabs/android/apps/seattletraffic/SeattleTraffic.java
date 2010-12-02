package com.osilabs.android.apps.seattletraffic;

import java.net.URLEncoder;
import java.util.List;
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
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
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

public class SeattleTraffic extends Activity {
	
	// --------------------------------------------------
	// Configuration Data
	// 
	protected static String MOBILECONTENT_URL_PREFIX    = "http://osilabs.com/m/mobilecontent/seattletraffic";
	protected static String MOBILECONTENT_URL_ABOUT     = "http://osilabs.com/m/mobilecontent/about/st_about.php";
	protected static String MOBILECONTENT_URL_HELP      = "http://osilabs.com/m/mobilecontent/help/st_help.php";
//	private static final String[] INDEX_STRINGS = {		
//		"xMaps...",
//		"xInfo...", 
//		"xCameras..."
//	};
	protected static final String NAMESPACE = "com.osilabs.android.apps.seattletraffic";
	// Use './adb logcat |grep node' to see the scanner ids
	private static int SCAN_NODE_POLICE  			= 21093;
	private static int SCAN_NODE_WEATHER 			= 23034; 
	//
	// /Configuration Data
	// --------------------------------------------------

	
	//
	// Consts
	//

	private static final String TAG = "** osilabs.com **";
	
	protected static final String SCANNER_RADIO_NAMESPACE = "net.gordonedwards.scannerradio"; // FIXME - move all these to class for scanner radio
	protected static final String SCANNER_RADIO_ACTION = "ACTION_PLAY_SCANNER";
//	public    static final int    ALPHA_ON = 0xFFFF;
//	public    static final int    ALPHA_OFF = 0xFFFF;
//	public    static final int    ALPHA_ON_ALERTS = 0xFFEE;
//	public    static final int    ALPHA_OFF_ALERTS = 0xFF88;
	public    static final int    TAB_ACTIVE_COLOR = 0xFF00FF00; // green

	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_ALERTS                = 1;
	private static final int MENU_CAMERAS               = 2;
	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_ALERTS               = 1;
	private static final int INDEX_CAMERAS              = 2;
	private static final int RADIO_OPTION_WEATHER       = 0;
	private static final int RADIO_OPTION_POLICE        = 1;

	//
	// Globals
	//

	// Prefs
	protected static int PREF_CAMERA_1;
	protected static int PREF_MAP;
	protected static int PREF_ALERT;
	
	protected static String CURRENT_WEBVIEW_URL = "";
	protected static String TRAFFIC_MAP_URL = "";
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
	
	// Tints and paints
	protected int color_tab;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(TAG, "onCreate");

        //
        // Set globals
        //
        
        // FIXME - i think this green is set elsewhere too
        color_tab = Color.GREEN;
        
        // Read in manifest
		try {
			pInfo = getPackageManager().getPackageInfo(NAMESPACE, PackageManager.GET_META_DATA);
			//version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		// Set URLs with versioncode
		TRAFFIC_MAP_URL = MOBILECONTENT_URL_PREFIX + pInfo.versionCode + "/trafficmap.php";
		AD_BANNER_URL = MOBILECONTENT_URL_PREFIX + pInfo.versionCode + "/adbanner.php";
		
		// Default the current view
		// FIXME - this should be an index not a url
        CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
		
        //
		// Restore preferences
        //
		mySharedPreferences = getSharedPreferences(
				NAMESPACE, Activity.MODE_PRIVATE);
        CURRENT_VIEW_INDEX = mySharedPreferences.getInt("session_current_view", 2);
        
        PREF_CAMERA_1 = mySharedPreferences.getInt("session_camera_1", Config.DEFAULT_CAMERA_ID);
        PREF_MAP = mySharedPreferences.getInt("session_map", Config.DEFAULT_MAP_INDEX);
        PREF_ALERT = mySharedPreferences.getInt("session_alert", Config.DEFAULT_CAMERA_ID);

        
	    // -------------------------
	    // Top Nav bar
	    //

	    ivMaps = (ImageView) findViewById(R.id.launcher_traffic);
	    ivMaps.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMainWebView(INDEX_TRAFFIC);
			}
		});

	    ivAlerts = (ImageView) findViewById(R.id.launcher_alerts);
	    // This particular icon is much whiter than the others so i am making it darker
	    //  with the alpha.
	    ivAlerts.setAlpha(AlertsTab.ALPHA_OFF);
	    ivAlerts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMainWebView(INDEX_ALERTS);
			}
		});

	    // Set up camera tab
        PREF_CAMERA_1 = mySharedPreferences.getInt("session_camera_1", Config.DEFAULT_CAMERA_ID); // FIXME - defaultcamid should come form config.defaultcam...
	    ivCameras = (ImageView) findViewById(R.id.launcher_cameras);
	    ivCameras.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMainWebView(INDEX_CAMERAS);
			}
		});

	    ivRefresh = (ImageView) findViewById(R.id.navbar_refresh);
	    // Give it a nice blue color. SRC_ATOP means color the icon, not
	    //  the background.
	    ivRefresh.setColorFilter(getResources().getColor(R.color.darkPowderBlue), PorterDuff.Mode.SRC_ATOP); // same as tint
	    ivRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//setMainWebView(CURRENT_VIEW_INDEX);
				refreshViews();
			}
		});
    }

    @Override
    public void onStart() {
    	super.onStart();
    	
    	Log.d(TAG, "onStart");

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

		//
		// Ad banner Web View
		//
		wvAd = (WebView) findViewById(R.id.adWebView);
        WebSettings awebSettings = wvAd.getSettings();
        awebSettings.setJavaScriptEnabled(true);
        awebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    	// wvAd.loadUrl(AD_BANNER_URL);
    	
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
				final CharSequence[] items = {"Weather Radio", "Police Scanner"};
				AlertDialog alert = new AlertDialog.Builder(SeattleTraffic.this)
                .setTitle("Radios")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
    		    		// Check prefs for changed radios.
    					setCurrentRadios();

    					switch(which) {
	        		        case RADIO_OPTION_WEATHER:
	        			        launchScanner(SCAN_NODE_WEATHER);
	            		        break;
	        		        case RADIO_OPTION_POLICE:
	            		        launchScanner(SCAN_NODE_POLICE);
	            		        break;
        		        }
                    }
                }).create();
				
				alert.show();
			}
		});

	    //
    	// Set the current tab and load it
    	//
	    
        // It's going to take a second to load
		Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
		
    	setMainWebView(CURRENT_VIEW_INDEX);
		reloadViews();
    }

    protected void launchCameraPicker() { 
		Context c = getApplicationContext();
		Intent intent = new Intent().setClassName(c, NAMESPACE + ".CameraELV");
		startActivityForResult(intent, 33); // FIXME - Make this a const
    }
    protected void launchMapPicker() { 
		AlertDialog alert = new AlertDialog.Builder(SeattleTraffic.this)
        .setTitle("Maps")
        .setItems(Config.maps, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	MapsTab.CURRENT_INDEX = which;
            	reloadViews();
            }
        }).create();
		
		alert.show();
    }
    protected void launchAlertPicker() { 
		AlertDialog alert = new AlertDialog.Builder(SeattleTraffic.this)
        .setTitle("Info Source") // FIXME - put in strings file
        .setItems(Config.alerts, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	AlertsTab.CURRENT_INDEX = which;
            	reloadViews();
            }
        }).create();
		
		alert.show();
    }
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// See which child activity is calling us back.
	    switch (requestCode) {
            
        case 33:
            // This is the standard resultCode that is sent back if the
            // activity crashed or didn't doesn't supply an explicit result.
            if (resultCode == RESULT_CANCELED){
    			Toast.makeText(getApplicationContext(), 
    					"Camera may have moved, please try again"
    					, Toast.LENGTH_LONG).show();
            } 
            else {
               Bundle extras = data.getExtras();
               int cam = extras.getInt("selected_camera");	                
            	// FIXME - this happens in a few places, consolidate it. Like the
    			//  next few lines do
    			PREF_CAMERA_1 = cam;
    			
    			// Reload the webview so it just shows the chosen camera
    	    	//wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);
				refreshViews();
				
    			// Set the chosen camera in the persistent settings
    	    	SharedPreferences prefs 
    				= getSharedPreferences(NAMESPACE, Activity.MODE_PRIVATE);
    			    SharedPreferences.Editor editor = prefs.edit();
    			    editor.putInt("session_camera_1", PREF_CAMERA_1);
    			    editor.commit();
            }
            
	        default:
	            break;
	    }
	}


    
    protected void setCurrentRadios() {
    	// Set Global with current prefs
    	// If this namespace path doesn't end in '_preferences' this won't work.
    	mySharedPreferences = getSharedPreferences(NAMESPACE + "_preferences", 0);

		String wr_saved = getApplicationContext().getResources().getString(R.string.pref_weather_radios_selected);
		String wr_def   = getApplicationContext().getResources().getString(R.string.pref_weather_radios_default);
		SCAN_NODE_WEATHER = Integer.parseInt(mySharedPreferences.getString(wr_saved, wr_def));
		String pr_saved = getApplicationContext().getResources().getString(R.string.pref_police_radios_selected);
		String pr_def   = getApplicationContext().getResources().getString(R.string.pref_police_radios_default);
		SCAN_NODE_POLICE = Integer.parseInt(mySharedPreferences.getString(pr_saved, pr_def));
    }
    
    // FIXME - move this into its own class for intent stuff
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
            packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

	
	// FIXME - move these
//	public void hideCameraConfiguration() {
//		ivMore.setVisibility(ImageView.GONE);
//		tvCamerasPop.setVisibility(TextView.GONE);
//	}
//	public void showCameraConfiguration() {
//		ivMore.setVisibility(ImageView.VISIBLE);
//		tvCamerasPop.setVisibility(TextView.VISIBLE);
//	}
	public void setCurrentTabStyle() {
		//ivMaps.setColorFilter(null); ivMaps.setAlpha(ALPHA_OFF);
		//ivAlerts.setColorFilter(null); ivAlerts.setAlpha(ALPHA_OFF_ALERTS);
		//ivCameras.setColorFilter(null); ivCameras.setAlpha(ALPHA_OFF);
		MapsTab.setInactive(ivMaps);
		AlertsTab.setInactive(ivAlerts);
		CamerasTab.setInactive(ivCameras);
		
		switch (CURRENT_VIEW_INDEX) {
			case MENU_TRAFFIC: // FIXME - rename this to MENU_MAPS
//				ivMaps.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
//				ivMaps.setAlpha(ALPHA_ON);
				MapsTab.setActive(ivMaps);
				break;
		    case MENU_ALERTS:
//				ivAlerts.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
//				ivAlerts.setAlpha(ALPHA_ON_ALERTS);
				AlertsTab.setActive(ivAlerts);
		    	break;
		    case MENU_CAMERAS:
				//ivCameras.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
				//ivCameras.setAlpha(ALPHA_ON);
				CamerasTab.setActive(ivCameras);
		    	break;
		}
	}

	public void setMainWebView(int view_index) {
		CURRENT_VIEW_INDEX = view_index;
		setCurrentView(CURRENT_VIEW_INDEX);
        //hideCameraConfiguration();
        
        MapsTab.hideConfiguration(ivMapMore, tvMapsPop);
        AlertsTab.hideConfiguration(ivAlertMore, tvAlertsPop);
        CamerasTab.hideConfiguration(ivCameraMore, tvCamerasPop);
        
        setCurrentTabStyle();
		switch (CURRENT_VIEW_INDEX) {
			case MENU_TRAFFIC:
				CURRENT_VIEW_INDEX = INDEX_TRAFFIC;
		        MapsTab.showConfiguration(ivMapMore, tvMapsPop);
				break;
		    case MENU_ALERTS:
		        CURRENT_VIEW_INDEX = INDEX_ALERTS;
		        AlertsTab.showConfiguration(ivAlertMore, tvAlertsPop);
		    	break;
		    case MENU_CAMERAS:
		        CURRENT_VIEW_INDEX = INDEX_CAMERAS;
		        // Enable camera configuration
		        //showCameraConfiguration();
		        CamerasTab.showConfiguration(ivCameraMore, tvCamerasPop);
		        break;
		}

		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_VIEW_INDEX+"')");
	}
	
	private class MyWebViewClient extends WebViewClient {
		Activity activity;
		public MyWebViewClient(Activity a) { activity = a; }

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		}
	}

	final class MyWebChromeClient extends WebViewClient {
        // @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
        }
        // Javascript in webview can call colsole.log('the message') to log messages.
		public void onConsoleMessage(String message, int lineNumber, String sourceID) {
			Log.d(TAG, message + " -- From line " + lineNumber + " of " + sourceID);
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
    		// setMainWebView(loadedIndex); 
    	}
    	
        public void setChosenCamera(int camnum, int camid) {
        	
			Toast.makeText(getApplicationContext(), "Loading new camera...", Toast.LENGTH_LONG).show();

			// Set the current camera
			CURRENT_VIEW_INDEX = INDEX_CAMERAS;
			PREF_CAMERA_1 = camid;
			
			// Reload the webview so it just shows the chosen camera
	    	//wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);
			refreshViews();
			
			// Set the chosen camera in the persistent settings
	    	SharedPreferences prefs 
				= getSharedPreferences(NAMESPACE, Activity.MODE_PRIVATE);
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putInt("session_camera_1", PREF_CAMERA_1);
			    editor.commit();

			// FIXME - verify this doesn't need to be posted as a runnable.
	    	
        	// Post a runnable
            //mHandler.post(new Runnable() {
            //    public void run() {
            //    	spinner.setSelection(0);
            //    }
            //});
        }
    } 
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_scanner_police:
				setCurrentRadios();
				launchScanner(SCAN_NODE_POLICE);
		    	return true;
	
			case R.id.menu_scanner_weather:
				setCurrentRadios();
				launchScanner(SCAN_NODE_WEATHER);
		    	return true;
				
			case R.id.menu_refresh:
				refreshViews();
				return true;
				
		    case R.id.menu_prefs:
		    	Intent intent = new Intent();
				intent.setClassName(this, NAMESPACE + ".Prefs");
		    	this.startActivityForResult(intent, 22);
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
        		Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MOBILECONTENT_URL_HELP + "?phoneinfo=" + URLEncoder.encode(phoneinfo))); 
				startActivity(mIntent); 
		    	return true;

		    case R.id.menu_about:
		        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		        alertDialog.setTitle(R.string.app_name);
		        alertDialog.setMessage("You are running version " + pInfo.versionName);
		        alertDialog.setButton("More...", new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		Intent mIntent = new Intent(Intent.ACTION_VIEW, 
		        				Uri.parse(MOBILECONTENT_URL_ABOUT)); 
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
	
	public boolean setCurrentView(int viewIndex) {
		// Save Settings
        //Toast.makeText(getApplicationContext(), "set cur view: " + viewIndex, Toast.LENGTH_SHORT).show();
    	SharedPreferences prefs 
			= getSharedPreferences(NAMESPACE, Activity.MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt("session_current_view", viewIndex);
	    editor.commit();

	    // Set text of spinner
		//tvCamerasPop.setText(INDEX_STRINGS[ viewIndex ]);

	    return true;
	}
	
	public void refreshViews() {
		Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
		reloadViews();
	}
	
	public void reloadViews() {
		Log.d(TAG, "reloadViews()");
		
		// Refresh main content webview
		wvMain.loadUrl(CURRENT_WEBVIEW_URL
						+ "?target=" + CURRENT_VIEW_INDEX
						+ "&camera=" + PREF_CAMERA_1 
						+ MapsTab.getReloadURLParts()
						+ AlertsTab.getReloadURLParts()
						+ CamerasTab.getReloadURLParts());
		
		// Refresh banner webview
		wvAd.loadUrl(AD_BANNER_URL);
	}
	
	public void launchScanner(int which_scanner) {
		boolean scannerAvailable = isIntentAvailable(SeattleTraffic.this,
				SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);

		// FIXME - put these strings in file and move this to own scanner radio class
		
		if (scannerAvailable) {
			Intent intent = new Intent(SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("node", which_scanner);
			startActivity(intent);
		} else {
		    AlertDialog scannerAlert = new AlertDialog.Builder(SeattleTraffic.this).create();
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
}
