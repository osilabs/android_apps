package com.osilabs.android.apps.dfwtraffic;
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