
package com.osilabs.android.apps.supertct;

//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URI;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.impl.client.DefaultHttpClient;

//import com.osilabs.android.apps.R;
//import android.R;


import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
//import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
//import android.preference.PreferenceManager;
//import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Main class
 * @author dezurik
 */
public class SuperTCT extends Activity {
	//
	// Consts
	//
	// VIEW_INDEX's
	// 0 = traffic
	// 1 = incidents
	// 2 = alerts
	// 3 = incidentlist
	// 4 = camera
	//
	protected static final String MNDOT_MOBILE_URL = "http://www.dot.state.mn.us/tmc/trafficinfo/mobile/freeways.html";
	//protected static final String TRAFFIC_MAP_URL = "http://osilabs.com/m/mobilecontent/tctraffic/trafficmap.php";
	//protected static final String INCIDENT_FEED = "http://www.dot.state.mn.us/tmc/trafficinfo/incidents.xml";

	
	private static final String TAG = "** osilabs.com **";
	
	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_CONGESTION             = 1;
	private static final int MENU_ALERTS                = 2;
	private static final int MENU_INCIDENTLIST          = 3;
	private static final int MENU_CAMERAS               = 4;
	private static final int MENU_REFRESH               = 100;
	private static final int MENU_QUIT                  = 101;
	private static final int MENU_SCANNER               = 102;
	//private static final int MENU_MNDOT_MOBILE_FREEWAYS = 102;
	//private static final int MENU_PREFS                 = 103;
	private static final int MENU_ABOUT                 = 104;

	private static final int SCAN_NODE_POLICE  = 16004; // FIXME - put these scanner values in an array. Use ./adb logcat |grep node to see the scanner ids
	private static final int SCAN_NODE_WEATHER = 24058; 

	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_CONGESTION           = 1;
	private static final int INDEX_ALERTS               = 2;
	private static final int INDEX_INCIDENTLIST         = 3;
	private static final int INDEX_CAMERAS              = 4;

	private static final String[] 
        INDEX_STRINGS = {"Traffic", "Congestion", "Alert Map", "Incident Report", "Camera"};

	//
	// Globals
	//

	// Prefs
	protected static int PREF_CAMERA_1;
	
	// a versioncode will be appended to this for it works with the current version
	protected static String MOBILECONTENT_URL_PREFIX = "http://osilabs.com/m/mobilecontent/tctraffic";
	protected static String MOBILECONTENT_URL_ABOUT = "http://osilabs.com/m/mobilecontent/about/tct_about.php";
	protected static String CURRENT_WEBVIEW_URL = "";
	protected static String TRAFFIC_MAP_URL = "";
	// Will need to up this number if more indexes are needed.
	protected static String[] VIEW_URLS = new String[8]; 
	protected static int CURRENT_VIEW_INDEX = 0;

	protected static PackageInfo pInfo = null;
	protected static Spinner spViewChoices;
	protected static WebView wvMain;

	// Navbar components
	protected ImageView launcherScannerPolice;
	protected ImageView launcherScannerWeather;
	protected TextView tvSpinner;
	
	// For posting runnables
	private Handler mHandler = new Handler();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //
        // Set globals
        //
        
        // Read in manifest
		try {
			pInfo = getPackageManager().getPackageInfo("com.osilabs.android.apps.supertct", PackageManager.GET_META_DATA);
			//version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// FIXME 0.4 - Add toast message here.
		}

		// Set URLs with versioncode
		TRAFFIC_MAP_URL = MOBILECONTENT_URL_PREFIX + pInfo.versionCode + "/trafficmap.php";

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
		
        //
		// Restore view
        //
		SharedPreferences mySharedPreferences = getSharedPreferences(
                "com.osilabs.android.apps.supertct", Activity.MODE_PRIVATE);
        CURRENT_VIEW_INDEX = mySharedPreferences.getInt("pref_current_view", 2);
        CURRENT_WEBVIEW_URL = VIEW_URLS[CURRENT_VIEW_INDEX];
        
        // Restore camera 1
        PREF_CAMERA_1 = mySharedPreferences.getInt("pref_camera_1", 1);
    }

    @Override
    public void onStart() {
    	super.onStart();
    	
	    //
	    // Set up Navigation Bar
	    //
	    
	    // Police Scanner
	    launcherScannerPolice = (ImageView) findViewById(R.id.launcher_scanner_police);
	    launcherScannerPolice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				boolean scannerAvailable = isIntentAvailable(SuperTCT.this,
		    		"net.gordonedwards.scannerradio.intent.action.ACTION_PLAY_SCANNER");
				
				if (scannerAvailable) {
	    			Intent intent = new Intent("net.gordonedwards.scannerradio.intent.action.ACTION_PLAY_SCANNER");
	    			intent.putExtra("node", SCAN_NODE_POLICE);
	    			startActivity(intent);
				} else {
				    AlertDialog scannerAlert = new AlertDialog.Builder(SuperTCT.this).create();
			        scannerAlert.setTitle("Super Twin Cities Traffic");
			        scannerAlert.setMessage("To use the police scanner, install the \"Scanner Radio\" app from the market.");
			        scannerAlert.setIcon(R.drawable.ic_launcher);
			        scannerAlert.setButton("Get plugin to use the scanner", new DialogInterface.OnClickListener() {
			        	public void onClick(DialogInterface dialog, int which) {
			    			Intent intent = new Intent(
			    					Intent.ACTION_VIEW,
			    					Uri.parse("market://details?id=com.scannerradio"));
			    			startActivity(intent);
			            } 
			        });
			        scannerAlert.show();
				}
			}
		});
	    
		// Weather Scanner
	    launcherScannerWeather = (ImageView) findViewById(R.id.launcher_scanner_weather);
	    launcherScannerWeather.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				boolean scannerAvailable = isIntentAvailable(SuperTCT.this,
		    		"net.gordonedwards.scannerradio.intent.action.ACTION_PLAY_SCANNERx");
				
				if (scannerAvailable) {
	    			Intent intent = new Intent("net.gordonedwards.scannerradio.intent.action.ACTION_PLAY_SCANNER");
	    			intent.putExtra("node", SCAN_NODE_WEATHER);
	    			startActivity(intent);
				} else {
				    AlertDialog scannerAlert = new AlertDialog.Builder(SuperTCT.this).create();
			        scannerAlert.setTitle("Super Twin Cities Traffic");
			        scannerAlert.setMessage("To use the Weather Radio, install the \"Scanner Radio\" app from market://details?id=net.gordonedwards.scannerradio");
			        scannerAlert.setIcon(R.drawable.ic_launcher);
			        scannerAlert.setButton("Get plugin to use the scanner", new DialogInterface.OnClickListener() {
			        	public void onClick(DialogInterface dialog, int which) {
			    			Intent intent = new Intent(
			    					Intent.ACTION_VIEW,
			    					Uri.parse("market://details?id=com.scannerradio"));
			    			startActivity(intent);
			            } 
			        });
			        scannerAlert.show();
				}
			}
		});

	    //
		// Spinner Choices
		// 
        spViewChoices = (Spinner) findViewById(R.id.view_choice_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
			this, R.array.view_choices, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spViewChoices.setOnItemSelectedListener(new QuickViewOnItemSelectedListener());
		spViewChoices.setAdapter(adapter);
		spViewChoices.setHapticFeedbackEnabled(true); // fixme - doesn't seem to work
		spViewChoices.setSelection(CURRENT_VIEW_INDEX);

	    //
		// View Choice Spinner
	    //
		tvSpinner = (TextView) findViewById(R.id.view_spinner);
	    tvSpinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				spViewChoices.performClick();
			}
		});

		//
		// Web View
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
    	
    	
    }

//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        
//        Toast.makeText(this, "FLIPPED", Toast.LENGTH_SHORT).show();
//        
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
//    }
    
    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
            packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }
    
    //
	// Quick View Spinner
	// 
	public class QuickViewOnItemSelectedListener implements OnItemSelectedListener {
	
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			CURRENT_VIEW_INDEX = pos;
			setCurrentView(CURRENT_VIEW_INDEX);
			switch (CURRENT_VIEW_INDEX) {
			    case MENU_TRAFFIC:
			        CURRENT_VIEW_INDEX = INDEX_TRAFFIC;
			    	break;
			    case MENU_CONGESTION:
			        CURRENT_VIEW_INDEX = INDEX_CONGESTION;
			    	break;		        
			    case MENU_ALERTS:
			        CURRENT_VIEW_INDEX = INDEX_ALERTS;
			    	break;
			    case MENU_INCIDENTLIST:
			        CURRENT_VIEW_INDEX = INDEX_INCIDENTLIST;
			    	break;
			    case MENU_CAMERAS:
			        CURRENT_VIEW_INDEX = INDEX_CAMERAS;
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
				Toast.makeText(	parent.getContext(), 
								"Loading...", Toast.LENGTH_LONG).show();

		    	// Load the other URL into the webview
	    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX);
	    	} else {
		    	// Just switch div - This stays fast because we don't have to
	    		//  reload the webview a lot, only for camera switching.
	    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_VIEW_INDEX+"')");
	    	}
		}
	
		public void onNothingSelected(AdapterView parent) {
		  // Do nothing.
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
			Log.d("tct application", message + " -- From line " + lineNumber + " of " + sourceID);
		}
	}
    
    final class JsiJavaScriptInterface {
		Activity activity;
    	JsiJavaScriptInterface(Activity a) { activity = a; }

        /**
         * 
         * @param camnum quick view camera number
         * @param camid id used in URI paths
         */
        public void setChosenCamera(int camnum, int camid) {
        	
			Toast.makeText(getApplicationContext(), "Loading new camera...", Toast.LENGTH_LONG).show();

			// Set the current camera
			CURRENT_VIEW_INDEX = INDEX_CAMERAS;
			PREF_CAMERA_1 = camid;
			
			// Reload the webview so it just shows the chosen camera
	    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);
	    	
			// Set the chosen camera in the persistent settings
	    	SharedPreferences prefs 
				= getSharedPreferences("com.osilabs.android.apps.supertct", Activity.MODE_PRIVATE);
			    SharedPreferences.Editor editor = prefs.edit();
			    editor.putInt("pref_camera_1", PREF_CAMERA_1);
			    editor.commit();

			// FIXME - verify this doesn't need to be posted as a runnable.
	    	
        	// Post a runnable
            //mHandler.post(new Runnable() {
            //    public void run() {
            //    	spinner.setSelection(0);
            //    }
            //});
        }
        
        
        public void chooseCamera(int oldcamid) {
        	
			Toast.makeText(getApplicationContext(), "Loading camera settings...", Toast.LENGTH_LONG).show();

			CURRENT_VIEW_INDEX = INDEX_CAMERAS;
			
			// Reload the webview, currently viewing camera so just call again 
			//  and leave camera emtpy
	    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX);
	    	
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
	    menu.add(0, MENU_SCANNER, 0, "Scanner");
	    menu.add(0, MENU_REFRESH, 0, "Refresh");
	    // menu.add(0, MENU_MNDOT_MOBILE_FREEWAYS, 0, "Cameras");
	    // menu.add(0, MENU_PREFS, 0, "Preferences");
	    menu.add(0, MENU_ABOUT, 0, "About");
	    menu.add(0, MENU_QUIT, 0, "Quit");
	    
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {

		case MENU_SCANNER:
			
	        AlertDialog scannerAlert = new AlertDialog.Builder(this).create();
	        scannerAlert.setTitle("Super Twin Cities Traffic");
	        scannerAlert.setMessage("Choose a channel");
	        scannerAlert.setIcon(R.drawable.ic_launcher);
	        scannerAlert.setButton("Weather", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	    			Intent intent = new Intent("net.gordonedwards.scannerradio.intent.action.ACTION_PLAY_SCANNER");
	    			intent.putExtra("node", SCAN_NODE_WEATHER);
	    			startActivity(intent);
	            } 
	        });
	        scannerAlert.show();
	    	return true;

		case MENU_REFRESH:
		    	Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
		    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX+"&camera="+PREF_CAMERA_1);
		        return true;
		        
//		    case MENU_MNDOT_MOBILE_FREEWAYS:
//		    	setCurrentView(INDEX_CAMERAS);
//		    	// CURRENT_WEBVIEW_URL = MNDOT_MOBILE_URL;
//		    	// CURRENT_VIEW_INDEX  = INDEX_CAMERAS;
//		    	wvMain.loadUrl(VIEW_URLS[INDEX_CAMERAS]);
//	    		return true;
//		        
//		    case MENU_PREFS:
//		    	//Toast.makeText(getApplicationContext(), "Prefs", Toast.LENGTH_SHORT).show();
//		    	Intent intent = new Intent()
//		    		.setClass(this, com.osilabs.android.apps.Prefs.class);
//		    	this.startActivityForResult(intent, 0);
//		    	return true;

			case MENU_ABOUT:
		        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		        alertDialog.setTitle("Twin Cities Traffic");
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

		    case MENU_QUIT:
		        finish();
		        return true;
	    }
	    return false;
	}
	
	public boolean setCurrentView(int viewIndex) {
		// Save Settings
        //Toast.makeText(getApplicationContext(), "set cur view: " + viewIndex, Toast.LENGTH_SHORT).show();
    	SharedPreferences prefs 
			= getSharedPreferences("com.osilabs.android.apps", Activity.MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt("pref_current_view", viewIndex);
	    editor.commit();

	    // Set text of spinner
		tvSpinner = (TextView) findViewById(R.id.view_spinner);
		tvSpinner.setText(INDEX_STRINGS[ viewIndex ] + " ^");

	    
	    return true;
	}
}