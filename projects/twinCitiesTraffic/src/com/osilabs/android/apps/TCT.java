package com.osilabs.android.apps;

import com.osilabs.android.apps.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class TCT extends Activity {
	protected static final String BASE_URL = "http://www.dot.state.mn.us/tmc/trafficinfo/traffic.html";
	protected static final String TRAFFIC_IMAGE = "http://www.dot.state.mn.us/tmc/trafficinfo/map/d_map.png";
	protected static final String ALERTS_IMAGE = "http://www.511mn.org/primary/images/all/TC_Metro.gif";
	protected static String CURRENT_WEBVIEW_URL = TRAFFIC_IMAGE;
	private static final String LOG_TAG = "TCT-LOG";
	private static final int MENU_REFRESH = 0;
	private static final int MENU_TRAFFIC = 1;
	private static final int MENU_ALERTS = 2;
	private static final int MENU_QUIT = 3;
	
	WebView wv;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        wv = (WebView) findViewById(R.id.mainWebView);
        WebSettings webSettings = wv.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultFontSize(23);
        webSettings.setJavaScriptEnabled(true);
        
        wv.loadUrl(CURRENT_WEBVIEW_URL);
    }
    
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_REFRESH, 0, "Refresh");
	    menu.add(0, MENU_TRAFFIC, 0, "Traffic");
	    menu.add(0, MENU_ALERTS, 0, "Alerts");
	    menu.add(0, MENU_QUIT, 0, "Quit");
	    return true;
	}
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case MENU_REFRESH:
	    	wv.loadUrl(CURRENT_WEBVIEW_URL);
	        return true;
	    case MENU_TRAFFIC:
	    	CURRENT_WEBVIEW_URL = TRAFFIC_IMAGE;
	    	wv.loadUrl(CURRENT_WEBVIEW_URL);
	        return true;
	    case MENU_ALERTS:
	    	CURRENT_WEBVIEW_URL = ALERTS_IMAGE;
	    	wv.loadUrl(CURRENT_WEBVIEW_URL);
	        return true;
	    case MENU_QUIT:
	    	// Exit Activity 
	        finish();
	        return true;
	    }
	    return false;
	}
}