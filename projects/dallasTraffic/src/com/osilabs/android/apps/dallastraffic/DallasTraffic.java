package com.osilabs.android.apps.dallastraffic;

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

/**
 * Main class
 * @author dezurik
 */
public class DallasTraffic extends Activity {
	
	// --------------------------------------------------
	// Configuration Data
	// 
	protected static String MOBILECONTENT_URL_PREFIX = "http://osilabs.com/m/mobilecontent/dallastraffic";
	protected static String MOBILECONTENT_URL_ABOUT = "http://osilabs.com/m/mobilecontent/about/dallastraffic_about.php";
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
	private static final int SCAN_NODE_POLICE  = 16004; // FIXME - put these scanner values in an array. Use ./adb logcat |grep node to see the scanner ids
	private static final int SCAN_NODE_WEATHER = 24058; 
	private static final int SCAN_NODE_WEATHER2 = 19405; // Clearwater Weather Radio (WunderGround.com)
	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_ALERTS               = 1;
	private static final int INDEX_CAMERAS              = 2;
	private static final int INDEX_NOTICE               = 3;
	private static final int INDEX_INCIDENTLIST         = 4;
	private static final int INDEX_CONGESTION           = 5;
	private static final String[] INDEX_STRINGS = {		"Traffic", 
														"Alerts", 
														"Cameras", 
														"Alert Map", 
														"Incident Report", 
														"Congestion"};
	protected static final String NAMESPACE = "com.osilabs.android.apps.dallastraffic";
	//
	// /Configuration Data
	// --------------------------------------------------

	

	
	
	//
	// Consts
	//

	private static final String TAG = "** osilabs.com **";
	
	protected static final String SCANNER_RADIO_NAMESPACE = "net.gordonedwards.scannerradio";
	protected static final String SCANNER_RADIO_ACTION = "ACTION_PLAY_SCANNER";

	//
	// Globals
	//

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

	// Navbar components
	protected ImageView ivTraffic;
	protected ImageView ivRefresh;
	protected ImageView ivAlerts;
	protected ImageView ivCameras;
	protected ImageView ivMore;
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
			// FIXME 0.4 - Add toast message here.
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
		SharedPreferences mySharedPreferences = getSharedPreferences(
				NAMESPACE, Activity.MODE_PRIVATE);
        CURRENT_VIEW_INDEX = mySharedPreferences.getInt("session_current_view", 2);
        CURRENT_WEBVIEW_URL = VIEW_URLS[CURRENT_VIEW_INDEX];
        
        // Restore camera 1
        PREF_CAMERA_1 = mySharedPreferences.getInt("session_camera_1", 1);
        
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
	    ivRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//setMainWebView(CURRENT_VIEW_INDEX);
				refreshViews();
			}
		});

	    // -------------------------
	    // Bottom Navigation Bar
	    //
	    

	    
		// Spinner Choices
		 
        spViewChoices = (Spinner) findViewById(R.id.view_choice_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
			this, R.array.view_choices, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spViewChoices.setOnItemSelectedListener(new QuickViewOnItemSelectedListener());
		spViewChoices.setAdapter(adapter);
		spViewChoices.setHapticFeedbackEnabled(true); // fixme - doesn't seem to work
		spViewChoices.setSelection(CURRENT_VIEW_INDEX);
		
//	    //
//		// View Choice Expand Icon
//	    //
//		ivMore = (ImageView) findViewById(R.id.launcher_more);
//		ivMore.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				spViewChoices.performClick();
//			}
//		});
//
	    //
		// View Choice Spinner
	    //
		tvSpinner = (TextView) findViewById(R.id.view_spinner);
	    tvSpinner.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Context c = v.getContext();
				
		    	//Toast.makeText(getApplicationContext(), "Prefs", Toast.LENGTH_SHORT).show();
		    	Intent intent = new Intent()
		    		.setClass(c, com.osilabs.android.apps.dallastraffic.Prefs.class);
		    	c.startActivity(intent);
				
				
//                //set up dialog
//                final Dialog dialog = new Dialog(v.getContext());
//                dialog.setContentView(R.layout.cameraconfig);
//                dialog.setTitle("Camera Options");
//                dialog.setCancelable(true);
//
//                //there are a lot of settings, for dialog, check them all out!
//                //set up text
//                TextView drop = (TextView) dialog.findViewById(R.id.drop_textview);
//                drop.setBackgroundColor(Color.YELLOW);
//                
////                //set up text
////                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
////                text.setText(R.string.drop_color_chosen_message);
// 
//                //set up image view
////                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
////                img.setImageResource(R.drawable.icon);
// 
//                //set up button
//                Button button = (Button) dialog.findViewById(R.id.Button01);
//                button.setOnClickListener(new OnClickListener() {
//                	@Override
//                    public void onClick(View vc) {
//                		dialog.cancel();
//                    }
//                });
//                //now that the dialog is set up, it's time to show it    
//                dialog.show();
			}
		});
    }

    @Override
    public void onStart() {
    	super.onStart();
    	
        Log.d(TAG, "onStart");

        // Inflate some views.
		ivMore = (ImageView) findViewById(R.id.launcher_more);
		tvSpinner = (TextView) findViewById(R.id.view_spinner);

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
			//setMainWebView(pos);
			Toast.makeText(getApplicationContext(), "Set sub cameras", Toast.LENGTH_LONG).show();	
		}
	
		public void onNothingSelected(AdapterView parent) {
		  // Do nothing.
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
		
		//LEFT OFF HERE TRYING TO SET TINT OF ICONS - clicking the icon causes FC
		ivTraffic.setColorFilter(null);
		ivAlerts.setColorFilter(null);
		ivCameras.setColorFilter(null);
		
		switch (CURRENT_VIEW_INDEX) {
			case MENU_TRAFFIC:
				ivTraffic.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
				break;
		    case MENU_CAMERAS:
				ivCameras.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
		    	break;
		    case MENU_ALERTS:
				ivAlerts.setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);
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
		
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_scanner_police:
				launchScanner(SCAN_NODE_POLICE);
		    	return true;
	
			case R.id.menu_scanner_weather:
				launchScanner(SCAN_NODE_WEATHER2);
		    	return true;
				
			case R.id.menu_refresh:
				refreshViews();
				return true;
			        
	//		    case MENU_MNDOT_MOBILE_FREEWAYS:
	//		    	setCurrentView(INDEX_CAMERAS);
	//		    	// CURRENT_WEBVIEW_URL = MNDOT_MOBILE_URL;
	//		    	// CURRENT_VIEW_INDEX  = INDEX_CAMERAS;
	//		    	wvMain.loadUrl(VIEW_URLS[INDEX_CAMERAS]);
	//	    		return true;
	//		        
//			    case MENU_PREFS:
//			    	//Toast.makeText(getApplicationContext(), "Prefs", Toast.LENGTH_SHORT).show();
//			    	Intent intent = new Intent()
//			    		.setClass(this, com.osilabs.android.apps.dallastraffic.Prefs.class);
//			    	this.startActivityForResult(intent, 0);
//			    	return true;
	
				case R.id.menu_help:
					
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
		boolean scannerAvailable = isIntentAvailable(DallasTraffic.this,
				SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);

		if (scannerAvailable) {
			Intent intent = new Intent(SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);
			intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("node", which_scanner);
			startActivity(intent);
		} else {
		    AlertDialog scannerAlert = new AlertDialog.Builder(DallasTraffic.this).create();
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
