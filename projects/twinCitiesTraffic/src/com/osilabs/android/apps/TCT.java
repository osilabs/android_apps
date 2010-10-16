/**
 * TCT.java
 */
package com.osilabs.android.apps;
import com.osilabs.android.apps.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Main class
 * @author dezurik
 */
public class TCT extends Activity {
	//
	// Consts
	//
	protected static final String MNDOT_MOBILE_URL = "http://www.dot.state.mn.us/tmc/trafficinfo/mobile/freeways.html";
	protected static final String TRAFFIC_MAP_URL = "http://osilabs.com/m/mobilecontent/tctraffic/trafficmap.php";
	protected static final String ALERTS_IMAGE = "http://www.511mn.org/primary/images/all/TC_Metro.gif";
	protected static final String INCIDENT_FEED = "http://www.dot.state.mn.us/tmc/trafficinfo/incidents.xml";
	
	private static final String LOG_TAG = "<*^*^*^*^*^*^*^*^*^*> ";
	
	private static final int MENU_TRAFFIC               = 0;
	private static final int MENU_INCIDENTS             = 1;
	private static final int MENU_ALERTS                = 2;
	private static final int MENU_REFRESH               = 3;
	private static final int MENU_QUIT                  = 4;
	private static final int MENU_MNDOT_MOBILE_FREEWAYS = 5;

	//
	// Defs
	//
	protected static String CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
	protected static String CURRENT_TARGET = "traffic";
	WebView wvMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        wvMain = (WebView) findViewById(R.id.mainWebView);
        WebSettings webSettings = wvMain.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        //webSettings.setSupportZoom(true);
        webSettings.setDefaultFontSize(23);
        webSettings.setJavaScriptEnabled(true);
        
        wvMain.setWebViewClient(new MyWebViewClient(this));
        wvMain.setWebChromeClient(new WebChromeClient());

        wvMain.loadUrl(CURRENT_WEBVIEW_URL);
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
	    menu.add(0, MENU_TRAFFIC, 0, "Traffic");
	    menu.add(0, MENU_INCIDENTS, 0, "Incidents");
	    menu.add(0, MENU_ALERTS, 0, "Alerts");
	    menu.add(0, MENU_REFRESH, 0, "Refresh");
	    menu.add(0, MENU_QUIT, 0, "Quit");
	    menu.add(0, MENU_MNDOT_MOBILE_FREEWAYS, 0, "Cameras");
	    
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		// Get a context for toast
		Context context = getApplicationContext();
		
	    switch (item.getItemId()) {
		    case MENU_REFRESH:
		    	Toast.makeText(context, "Refreshing", Toast.LENGTH_SHORT).show();
		    	wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		        return true;
		        
		    case MENU_TRAFFIC:
		    	Toast.makeText(context, "Loading traffic", Toast.LENGTH_SHORT).show();
		    	CURRENT_TARGET = "traffic";
		    	if( CURRENT_WEBVIEW_URL == TRAFFIC_MAP_URL) {
		    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_TARGET+"')");
		    	} else {
		    		CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
		    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		    	}
	    		return true;
	    		
		    case MENU_INCIDENTS:
		    	Toast.makeText(context, "Loading incidents", Toast.LENGTH_SHORT).show();
		    	CURRENT_TARGET = "incidents";
		    	if( CURRENT_WEBVIEW_URL == TRAFFIC_MAP_URL) {
		    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_TARGET+"')");
		    	} else {
		    		CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
		    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		    	}
		        return true;
		        
		    case MENU_ALERTS:
		    	Toast.makeText(context, "Loading alerts", Toast.LENGTH_SHORT).show();
		    	CURRENT_TARGET = "alerts";
		    	if( CURRENT_WEBVIEW_URL == TRAFFIC_MAP_URL) {
		    		wvMain.loadUrl("javascript: jumpTo('"+CURRENT_TARGET+"')");
		    	} else {
		    		CURRENT_WEBVIEW_URL = TRAFFIC_MAP_URL;
		    		wvMain.loadUrl(CURRENT_WEBVIEW_URL+"?target="+CURRENT_TARGET);
		    	}
		        return true;
		        
		    case MENU_MNDOT_MOBILE_FREEWAYS:
		    	Toast.makeText(context, "Loading cameras", Toast.LENGTH_SHORT).show();
		    	CURRENT_WEBVIEW_URL = MNDOT_MOBILE_URL;
		    	wvMain.loadUrl(CURRENT_WEBVIEW_URL);
	    		return true;
	    		
		    case MENU_QUIT:
		        finish();
		        return true;
	    }
	    return false;
	}
}