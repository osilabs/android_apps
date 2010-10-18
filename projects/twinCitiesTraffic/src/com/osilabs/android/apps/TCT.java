
package com.osilabs.android.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

//import com.osilabs.android.apps.R;
//import android.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
public class TCT extends Activity {
	//
	// Consts
	//
	// VIEW_INDEX's
	// 0 = traffic
	// 1 = incidents
	// 2 = alerts
	// 3 = camera
	//
	protected static final String MNDOT_MOBILE_URL = "http://www.dot.state.mn.us/tmc/trafficinfo/mobile/freeways.html";
	protected static final String TRAFFIC_MAP_URL = "http://osilabs.com/m/mobilecontent/tctraffic/trafficmap.php";
	protected static final String INCIDENT_FEED = "http://www.dot.state.mn.us/tmc/trafficinfo/incidents.xml";

	protected static final String[] VIEW_URLS = {	TRAFFIC_MAP_URL,
											        TRAFFIC_MAP_URL,
											        TRAFFIC_MAP_URL,
											        MNDOT_MOBILE_URL };
	
	private static final String LOG_TAG = "<*^*^*^*^*^*^*^*^*^*> ";
	
	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_INCIDENTS             = 1;
	private static final int MENU_ALERTS                = 2;
	private static final int MENU_REFRESH               = 100;
	private static final int MENU_QUIT                  = 101;
	private static final int MENU_MNDOT_MOBILE_FREEWAYS = 102;
	private static final int MENU_PREFS                 = 103;

	private static final int INDEX_TRAFFIC              = 0;
	private static final int INDEX_INCIDENTS            = 1;
	private static final int INDEX_ALERTS               = 2;

	//
	// Defs
	//
	protected static String CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
	protected static String CURRENT_TARGET = "traffic";
	protected static int CURRENT_VIEW_INDEX = INDEX_TRAFFIC;

	Spinner spViewChoices;
	WebView wvMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get the default view to display
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        CURRENT_VIEW_INDEX = prefs.getInt("pref_selected", INDEX_TRAFFIC);
        CURRENT_WEBVIEW_URL = VIEW_URLS[CURRENT_VIEW_INDEX];
        Toast.makeText(this, "cur idx " + Integer.toString(CURRENT_VIEW_INDEX), Toast.LENGTH_SHORT).show();
        // debug
        CURRENT_VIEW_INDEX  = 2;
        
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
        wvMain.loadUrl(CURRENT_WEBVIEW_URL);
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
    
    //
	// Quick View Spinner
	// 
	public class QuickViewOnItemSelectedListener implements OnItemSelectedListener {
	
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			// Toast.makeText(	parent.getContext(), 
			//				"Loading: " + parent.getItemAtPosition(pos).toString(), 
			//				Toast.LENGTH_LONG).show();
			switch (pos) {
		    case MENU_TRAFFIC:
		    	// Toast.makeText(parent.getContext(), "Loading traffic", Toast.LENGTH_SHORT).show();
		    	CURRENT_TARGET = "traffic";
		    	if( CURRENT_WEBVIEW_URL == TRAFFIC_MAP_URL) {
		    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_TARGET+"')");
		    	} else {
		    		CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
		    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		    	}
		    	break;
	    		
		    case MENU_INCIDENTS:
		    	// Toast.makeText(parent.getContext(), "Loading incidents", Toast.LENGTH_SHORT).show();
		    	CURRENT_TARGET = "incidents";
		    	if( CURRENT_WEBVIEW_URL == TRAFFIC_MAP_URL) {
		    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_TARGET+"')");
		    	} else {
		    		CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
		    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		    	}
		    	break;
		        
		    case MENU_ALERTS:
		    	// Toast.makeText(parent.getContext(), "Loading alerts", Toast.LENGTH_SHORT).show();
		    	CURRENT_TARGET = "alerts";
		    	if( CURRENT_WEBVIEW_URL == TRAFFIC_MAP_URL) {
		    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_TARGET+"')");
		    	} else {
		    		CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
		    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		    	}
		    	break;
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
			Log.d("iMedNetMobile application", message + " -- From line " + lineNumber + " of " + sourceID);
		}
	}
    
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_REFRESH, 0, "Refresh");
	    menu.add(0, MENU_MNDOT_MOBILE_FREEWAYS, 0, "Cameras");
	    menu.add(0, MENU_PREFS, 0, "Preferences");
	    menu.add(0, MENU_QUIT, 0, "Quit");
	    
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case MENU_REFRESH:
		    	Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_SHORT).show();
		    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		        return true;
		        
		    case MENU_MNDOT_MOBILE_FREEWAYS:
		    	Toast.makeText(getApplicationContext(), "Loading cameras", Toast.LENGTH_SHORT).show();
		    	CURRENT_WEBVIEW_URL = MNDOT_MOBILE_URL;
		    	wvMain.loadUrl(CURRENT_WEBVIEW_URL);
	    		return true;
		        
		    case MENU_PREFS:
		    	//Toast.makeText(getApplicationContext(), "Prefs", Toast.LENGTH_SHORT).show();
		    	Intent intent = new Intent()
		    		.setClass(this, com.osilabs.android.apps.Prefs.class);
		    	this.startActivityForResult(intent, 0);
		    	return true;

		    case MENU_QUIT:
		        finish();
		        return true;
	    }
	    return false;
	}
}