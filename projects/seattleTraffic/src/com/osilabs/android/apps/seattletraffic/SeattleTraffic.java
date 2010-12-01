package com.osilabs.android.apps.seattletraffic;

import java.net.URLEncoder;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
//import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.os.Bundle;

public class SeattleTraffic extends Activity {
	
	// --------------------------------------------------
	// Configuration Data
	// 
	protected static String MOBILECONTENT_URL_PREFIX    = "http://osilabs.com/m/mobilecontent/seattletraffic";
	protected static String MOBILECONTENT_URL_ABOUT     = "http://osilabs.com/m/mobilecontent/about/dt_about.php";
	protected static String MOBILECONTENT_URL_HELP      = "http://osilabs.com/m/mobilecontent/help/dt_help.php";
	private static final int DEFAULT_CAMERA_ID          = 29;
	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_ALERTS                = 1;
	private static final int MENU_CAMERAS               = 2;
	private static final int MENU_NOTICE                = 3;// not used
	private static final int MENU_INCIDENTLIST          = 4;// not used
	private static final int MENU_CONGESTION            = 5;// not used
	private static final int MENU_REFRESH               = 100;
	private static final int MENU_QUIT                  = 101;
	private static final int MENU_SCANNER               = 102;
	private static final int MENU_PREFS                 = 103;
	private static final int MENU_ABOUT                 = 104;
	private static final int MENU_HELP                  = 105;
	private static final int RADIO_OPTION_WEATHER       = 0;
	private static final int RADIO_OPTION_POLICE        = 1;
	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_ALERTS               = 1;
	private static final int INDEX_CAMERAS              = 2;
	private static final int INDEX_NOTICE               = 3;
	private static final int INDEX_INCIDENTLIST         = 4;
	private static final int INDEX_CONGESTION           = 5;
	private static final String[] INDEX_STRINGS = {		
		"Traffic", 
		"Alerts", 
		"Cameras...", 
		"Alert Map", 
		"Incident Report", 
		"Congestion"};
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
	
	protected static final String SCANNER_RADIO_NAMESPACE = "net.gordonedwards.scannerradio";
	protected static final String SCANNER_RADIO_ACTION = "ACTION_PLAY_SCANNER";
	public    static final int    ALPHA_ON = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFFFF;
	public    static final int    ALPHA_ON_ALERTS = 0xFFEE;
	public    static final int    ALPHA_OFF_ALERTS = 0xFF88;
	public    static final int    TAB_ACTIVE_COLOR = 0xFF00FF00; // green
	
	//
	// Globals
	//
	
	// Used to affect behavior during oncreate/onstart
	private boolean spinner_initialized = false;

	// Prefs
	protected static int PREF_CAMERA_1;
	
	protected static String CURRENT_WEBVIEW_URL = "";
	protected static String TRAFFIC_MAP_URL = "";
	protected static String AD_BANNER_URL = "";

	// Will need to up this number if more indexes are needed.
	protected static String[] VIEW_URLS = new String[8]; 
	protected static int CURRENT_VIEW_INDEX = 0;

	protected static PackageInfo pInfo = null;
	protected static Spinner spViewChoices;
	protected static WebView wvAd;
	protected static WebView wvMain;
	protected static SharedPreferences mySharedPreferences;

	// Navbar components
	protected ImageView ivTraffic;
	protected ImageView ivAlerts;
	protected ImageView ivCameras;
	protected ImageView ivMore;
	protected ImageView ivRefresh;
	protected ImageView ivRadios;
	protected TextView tvSpinner;
	
	// Tints and paints
	protected int color_tab;
	
	// For posting runnables
	private Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.d(TAG, "onCreate");

        //
        // Set globals
        //
        
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

		// The order of these needs to match the order of the VIEW_INDEXes. It is used to determine
		//  which webview URI to use for the view.
		//
		// This is intialized with a finite number of indexes, if you exceed them,
		//  up that number or get array out of bounds exception
		VIEW_URLS[0] = TRAFFIC_MAP_URL;
		VIEW_URLS[1] = TRAFFIC_MAP_URL;
		VIEW_URLS[2] = TRAFFIC_MAP_URL;
		VIEW_URLS[3] = TRAFFIC_MAP_URL;
		VIEW_URLS[4] = TRAFFIC_MAP_URL;
		VIEW_URLS[5] = TRAFFIC_MAP_URL;
		VIEW_URLS[6] = TRAFFIC_MAP_URL;
		VIEW_URLS[7] = TRAFFIC_MAP_URL;
		
        //
		// Restore view
        //
		mySharedPreferences = getSharedPreferences(
				NAMESPACE, Activity.MODE_PRIVATE);
        CURRENT_VIEW_INDEX = mySharedPreferences.getInt("session_current_view", 2);
        CURRENT_WEBVIEW_URL = VIEW_URLS[CURRENT_VIEW_INDEX];
        
        // Restore camera 1
        PREF_CAMERA_1 = mySharedPreferences.getInt("session_camera_1", DEFAULT_CAMERA_ID);
        
	    // -------------------------
	    // Top Nav bar
	    //

	    ivTraffic = (ImageView) findViewById(R.id.launcher_traffic);
	    ivTraffic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMainWebView(INDEX_TRAFFIC);
			}
		});

	    ivAlerts = (ImageView) findViewById(R.id.launcher_alerts);
	    // This particular icon is much whiter than the others so i am making it darker
	    //  with the alpha.
	    ivAlerts.setAlpha(ALPHA_OFF_ALERTS);
	    //ivAlerts.setColorFilter(Color.MAGENTA, PorterDuff.Mode.DST_ATOP); // same as tint
	    //ivAlerts.setBackgroundColor(Color.CYAN);
	    ivAlerts.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMainWebView(INDEX_ALERTS);
			}
		});

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
    	// It's going to take a second to load
		Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
		// Load the webview
    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);

		//
		// Ad banner Web View
		//
		wvAd = (WebView) findViewById(R.id.adWebView);
        WebSettings awebSettings = wvAd.getSettings();
        awebSettings.setJavaScriptEnabled(true);
        awebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    	wvAd.loadUrl(AD_BANNER_URL);
    	
	    // -------------------------
	    // Bottom Navigation Bar
	    //

        // Inflate some views.
		ivMore = (ImageView) findViewById(R.id.launcher_more);
		ivMore.setColorFilter(getResources().getColor(R.color.lighterDarkPowderBlue), PorterDuff.Mode.SRC_ATOP);
		ivMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				launchCameraPicker();
			}
		});

		tvSpinner = (TextView) findViewById(R.id.camera_config_spinner);
		tvSpinner.setTextColor(getResources().getColor(R.color.lighterDarkPowderBlue));
	    tvSpinner.setOnClickListener(new View.OnClickListener() {
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
    	// Set the current tab
    	//
    	setMainWebView(CURRENT_VIEW_INDEX);

    }

    protected void launchCameraPicker() { 
		Context c = getApplicationContext();
		Intent intent = new Intent().setClassName(c, NAMESPACE + ".CameraELV");
		startActivityForResult(intent, 33); // FIXME - Make this a const
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
    
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
            packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
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
    	    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);
    	    	
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


	// FIXME - move these
	public void hideCameraConfiguration() {
		ivMore.setVisibility(ImageView.GONE);
		tvSpinner.setVisibility(TextView.GONE);
	}
	public void showCameraConfiguration() {
		ivMore.setVisibility(ImageView.VISIBLE);
		tvSpinner.setVisibility(TextView.VISIBLE);
	}
	public void setCurrentTab() {
		ivTraffic.setColorFilter(null); ivTraffic.setAlpha(ALPHA_OFF);
		ivAlerts.setColorFilter(null); ivAlerts.setAlpha(ALPHA_OFF_ALERTS);
		ivCameras.setColorFilter(null); ivCameras.setAlpha(ALPHA_OFF);
		
		switch (CURRENT_VIEW_INDEX) {
			case MENU_TRAFFIC:
				ivTraffic.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
				ivTraffic.setAlpha(ALPHA_ON);
				break;
		    case MENU_ALERTS:
				ivAlerts.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
				ivAlerts.setAlpha(ALPHA_ON_ALERTS);
		    	break;
		    case MENU_CAMERAS:
				ivCameras.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
				ivCameras.setAlpha(ALPHA_ON);
		    	break;
			case MENU_NOTICE:
				break;
		    case MENU_INCIDENTLIST:
		    	break;
		    case MENU_CONGESTION:
		    	break;
		}
	}

	public void setMainWebView(int view_index) {
		CURRENT_VIEW_INDEX = view_index;
		setCurrentView(CURRENT_VIEW_INDEX);
        hideCameraConfiguration();
        setCurrentTab();
		switch (CURRENT_VIEW_INDEX) {
			case MENU_TRAFFIC:
				CURRENT_VIEW_INDEX = INDEX_TRAFFIC;
				break;
			case MENU_NOTICE:
				CURRENT_VIEW_INDEX = INDEX_NOTICE;
				break;
		    case MENU_CAMERAS:
		        CURRENT_VIEW_INDEX = INDEX_CAMERAS;
		        // Enable camera configuration
		        showCameraConfiguration();
		    	break;
		    case MENU_ALERTS:
		        CURRENT_VIEW_INDEX = INDEX_ALERTS;
		    	break;
		    case MENU_INCIDENTLIST:
		        CURRENT_VIEW_INDEX = INDEX_INCIDENTLIST;
		    	break;
		    case MENU_CONGESTION:
		        CURRENT_VIEW_INDEX = INDEX_CONGESTION;
		    	break;		        
		}

		// CURRENT_WEBVIEW_URL is still what the last view was, 
		//  VIEW_URLS[CURRENT_VIEW_INDEX] has the new view which
		//  has a different URL (This was used when the cameras
		//  view had to reload the webview with a different url).
    	if (CURRENT_WEBVIEW_URL != VIEW_URLS[CURRENT_VIEW_INDEX]) {
    		// Set the new Current URL
	    	CURRENT_WEBVIEW_URL = VIEW_URLS[CURRENT_VIEW_INDEX];

	    	// It's going to take a second to reload a new webview,
	    	//  state the obvious
			Toast.makeText(	getApplicationContext(), 
							"Loading...", Toast.LENGTH_LONG).show();

	    	// Load the other URL into the webview
    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX);
    	} else {
	    	// Just switch div - This stays fast because we don't have to
    		//  reload the webview a lot, only for camera switching.
    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_VIEW_INDEX+"')");
    	}
		
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
	    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);
	    	
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
		tvSpinner.setText(INDEX_STRINGS[ viewIndex ]);

	    return true;
	}
	
	public void refreshViews() {
		Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
		// Refresh main content webview
		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);
		// Refresh banner webview
		wvAd.loadUrl(AD_BANNER_URL);
	}
	
	public void launchScanner(int which_scanner) {
		boolean scannerAvailable = isIntentAvailable(SeattleTraffic.this,
				SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);

		if (scannerAvailable) {
			Intent intent = new Intent(SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);
			intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
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
	    			intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
	    			startActivity(intent);
	            } 
	        });
	        scannerAlert.show();
		}
	}
}
