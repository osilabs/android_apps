package com.osilabs.android.apps.dfwtraffic;

import android.app.Activity;
import android.app.AlertDialog;
//import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Main class
 * @author dezurik
 */
public class DFWTraffic extends Activity {
	//
	// Consts
	//
	// VIEW_INDEX's
	// 0 = traffic
	// 1 = traffic night mode
	// 2 = incidents
	//
	protected static final String MNDOT_MOBILE_URL = "http://www.dot.state.mn.us/tmc/trafficinfo/mobile/freeways.html";
	protected static final String TRAFFIC_MAP_URL = "http://osilabs.com/m/mobilecontent/dfwtraffic/trafficmap.php";
	protected static final String INCIDENT_FEED = "http://www.dot.state.mn.us/tmc/trafficinfo/incidents.xml";

	protected static final String[] VIEW_URLS = {	TRAFFIC_MAP_URL,
											        TRAFFIC_MAP_URL,
											        TRAFFIC_MAP_URL,
											        MNDOT_MOBILE_URL };
	
	private static final String TAG = "** osilabs.com ** ";
	
	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_TRAFFIC_NM            = 1;
	private static final int MENU_INCIDENTS             = 2;
	//private static final int MENU_CAMERAS             = 3;
	private static final int MENU_REFRESH               = 100;
	private static final int MENU_QUIT                  = 101;
	//private static final int MENU_MNDOT_MOBILE_FREEWAYS = 102;
	//private static final int MENU_PREFS                 = 103;
	private static final int MENU_ABOUT                 = 104;

	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_TRAFFIC_NM           = 1;
	private static final int INDEX_INCIDENTS            = 2;
	//private static final int INDEX_CAMERAS              = 3;

	//
	// Defs
	//
	protected static String CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
	protected static int CURRENT_VIEW_INDEX = INDEX_TRAFFIC;
	protected static String version = "0.1"; // The default, later overwritten w actual 
	
	Spinner spViewChoices;
	WebView wvMain;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //
        // Set locals with manifest variables
        //
        PackageInfo pInfo = null;
		try {
			pInfo = getPackageManager().getPackageInfo("com.osilabs.android.apps.dfwtraffic",
					PackageManager.GET_META_DATA);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
      
        //
		// Restore view
        //
		SharedPreferences mySharedPreferences = getSharedPreferences(
                "com.osilabs.android.apps.dfwtraffic", Activity.MODE_PRIVATE);
        CURRENT_VIEW_INDEX = mySharedPreferences.getInt("pref_current_view", 2);
        CURRENT_WEBVIEW_URL = VIEW_URLS[CURRENT_VIEW_INDEX];
        
        //
		// Quick View Spinner
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
		// Web View
		//
		wvMain = (WebView) findViewById(R.id.mainWebView);
        WebSettings webSettings = wvMain.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultFontSize(23);
        webSettings.setJavaScriptEnabled(true);
        wvMain.setWebViewClient(new MyWebViewClient(this));
        wvMain.setWebChromeClient(new WebChromeClient());
    	// It's going to take a second to load
		Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
		// Load the webview
		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX);
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
			    case MENU_TRAFFIC_NM:
			        CURRENT_VIEW_INDEX = INDEX_TRAFFIC_NM;
			    	break;
			    case MENU_INCIDENTS:
			        CURRENT_VIEW_INDEX = INDEX_INCIDENTS;
			    	break;		        
			    //case MENU_CAMERAS:
			    //    CURRENT_VIEW_INDEX = INDEX_CAMERAS;
			}

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
		    	// Just switch div
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
			// The Texas DOT site terms of use prohibit unauthorized
			//  use of the site pages. I am preventing the links from
			//  taking users to the actual site.  This will make the links
			//  do nothing.
			//view.loadUrl(url);
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
    
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
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
		    case MENU_REFRESH:
		    	Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
		    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_VIEW_INDEX);
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
		        alertDialog.setTitle(R.string.app_name);
		        alertDialog.setMessage("You are running version " + version);
		        alertDialog.setButton("More...", new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		Intent mIntent = new Intent(Intent.ACTION_VIEW, 
		        				Uri.parse("http://osilabs.com/m/mobilecontent/dfwtraffic/about.php#" + version)); 
        				startActivity(mIntent); 
		            } 
		        });
		        alertDialog.setIcon(R.drawable.ic_launcher_main);
		        alertDialog.show();
		    	return true;

		    case MENU_QUIT:
		        finish();
		        return true;
	    }
	    return false;
	}
	
	public boolean setCurrentView(int viewIndex) {
        //Toast.makeText(getApplicationContext(), "set cur view: " + viewIndex, Toast.LENGTH_SHORT).show();
    	SharedPreferences prefs 
			= getSharedPreferences("com.osilabs.android.apps.dfwtraffic", Activity.MODE_PRIVATE);
	    SharedPreferences.Editor editor = prefs.edit();
	    editor.putInt("pref_current_view", viewIndex);
	    editor.commit();
	    return true;
	}
}



/*
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DFWTraffic extends Activity {
	protected static final String BASE_URL = "http://www.dot.state.mn.us/tmc/trafficinfo/traffic.html";
	protected static final String TRAFFIC_IMAGE = "http://dfwtraffic.dot.state.tx.us/Mobile/DalTrans/MapBurnerOutput/WhiteMap.gif";
	protected static final String CURRENT_WEBVIEW_URL = TRAFFIC_IMAGE;
	private static final String LOG_TAG = "DFWTr-LOG";
	private static final int MENU_REFRESH = 0;
	private static final int MENU_QUIT = 1;
	
	ImageView iv;
    Bitmap bmImg;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        iv = (ImageView) findViewById(R.id.mainImageView);
        downloadFile(CURRENT_WEBVIEW_URL);
        iv.setImageBitmap(bmImg);
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_REFRESH, 0, "Refresh");
	    menu.add(0, MENU_QUIT, 0, "Quit");
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_REFRESH:
	         downloadFile(CURRENT_WEBVIEW_URL);
	         iv.setImageBitmap(bmImg);
	         return true;
	    case MENU_QUIT:
	    	// Exit Activity 
	        finish();
	        return true;
	    }
	    return false;
	}
	
    void downloadFile(String fileUrl){
        URL myFileUrl =null;          
        try {
            myFileUrl= new URL(fileUrl);
        } catch (MalformedURLException e) {
        	e.printStackTrace();
        }
        try {
            HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();               
            bmImg = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}

*/