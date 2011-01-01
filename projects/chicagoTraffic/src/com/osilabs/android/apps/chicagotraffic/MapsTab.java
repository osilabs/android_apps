package com.osilabs.android.apps.chicagotraffic;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MapsTab {
	// FIXME - these are duplicated in the main class
	public    static final int    ALPHA_ON  = 0xFFFF;
	public    static final int    ALPHA_OFF = 0xFF88;
	public    static final int    TAB_ACTIVE_COLOR = 0xFFAACCAA; // FIXME pull this from color.xml
	public    static final int    BG_OFF = 0xFF333333;
	public    static final int    BG_ON  = Color.BLACK;
	
	// CONSTS
//	private   static final int    MAP = 0;
//	private   static final int    WEB = 1;

	// Indexes for URL stings
	private   static final int    TYPE    = 0;
	private   static final int    URI     = 1;
	private   static final int    WIDTH   = 2;
	private   static final int    HEIGHT  = 3;
	private   static final int    SCROLLX = 4;
	private   static final int    SCROLLY = 5;
	
	// If never set, is set to first map.
	public    static int    	  CURRENT_INDEX = 0;
	
//	// Init will set this to the number of system views. Will be used
//	//  to determine if a view is above this number and is therefore
//	//  a favorite of a mapview.
//	public    static int    	  NUMBER_OF_SYSTEM_VIEWS = 0;
	
	public static void init() {
		App.ivMapsTab.setVisibility(View.VISIBLE);
		App.ivMapsTab.setPadding(15, 0, 15, 0);
		App.ivMapsTab.setAlpha(ALPHA_OFF);
		App.ivMapsTab.setBackgroundColor(BG_OFF);
		
		// Set the number of system views this build is using. Any views
		//  accessed above this number is out of the defined list
		//  and is a user favorite of a mapview.
//		NUMBER_OF_SYSTEM_VIEWS = Config.traffic_viewtypes.length;
		
		if (MapsTab.CURRENT_INDEX > (MenuIndexes.totalSize()) - 1) {
			// The saved value that came from the session was larger
			//  than the total indexes available. Set the current index
			//  back to defaults.
			MapsTab.CURRENT_INDEX = Config.DEFAULT_MAP_INDEX;
		}
		
		// Init the sizes of the favorites and system map 
		//  popup menu option arrays.
		MenuIndexes.init();
		
		Log.d(App.TAG, "MapsTab.Init() complete: FAV_SIZE: " + MenuIndexes.FAV_SIZE);
		Log.d(App.TAG, "MapsTab.Init() complete: Config.MAPVIEW_FAVORITES: " + Config.MAPVIEW_FAVORITES);

	}
//	public static String getActiveMapURL() {
//		// Keep from exceeding the size of the array
//		if (CURRENT_INDEX > (Config.maps.length - 1)) CURRENT_INDEX = 0;
//		return Config.maps[CURRENT_INDEX];
//	}
	public static String getReloadURLParts() {
		int adjustedIndex = getAdjustedIndex();
		if (CURRENT_INDEX > (Config.traffic_urls.length - 1)) CURRENT_INDEX = 0;
		
		String query_string= "";
		
		if (MapsTab.getAndroidViewType() == Config.IMAGE) {
			query_string 
				= "&traffic=" + URLEncoder.encode(Config.traffic_urls[adjustedIndex])
				+ "&mapscrollx="
				+ MapsTab.getScrollX()
				+ "&mapscrolly="
				+ MapsTab.getScrollY();
		} else if (MapsTab.getAndroidViewType() == Config.FEED) {
			query_string 
				= "&traffic=" + URLEncoder.encode(Config.traffic_urls[adjustedIndex]);
		} else {
			query_string = "&traffic=";			
		}

		return	query_string;
	}
	
	public static void setActive() {
		App.ivMapsTab.setColorFilter(TAB_ACTIVE_COLOR, PorterDuff.Mode.SRC_ATOP);
		App.ivMapsTab.setAlpha(ALPHA_ON);
		App.ivMapsTab.setBackgroundColor(BG_ON);
	}
	public static void setInactive() {
		App.ivMapsTab.setColorFilter(null); 
		App.ivMapsTab.setAlpha(ALPHA_OFF);
		App.ivMapsTab.setBackgroundColor(BG_OFF);
	}
	public static void hideConfiguration() {
		App.ivMapsMore.setVisibility(ImageView.GONE);
		App.tvMapsPop.setVisibility(TextView.GONE);
	}
	public static void showConfiguration() {
		App.ivMapsMore.setVisibility(ImageView.VISIBLE);
		App.tvMapsPop.setVisibility(TextView.VISIBLE);
	}
	public static String getWebViewType() {
		String[] as = Config.traffic_urls[getAdjustedIndex()].split("~");
		return as[TYPE];
	}
	public static String getScrollX() {
		// FIXME - cache these so they are calc'd all the time
		String[] as = Config.traffic_urls[getAdjustedIndex()].split("~");
		return as[SCROLLX];
	}
	public static String getScrollY() {
		String[] as = Config.traffic_urls[getAdjustedIndex()].split("~");
		return as[SCROLLY];
	}
	/**
	 * The adjusted index takes the CURRENT_INDEX and subtracts
	 *  out the number of favorites so it can be used with
	 *  Config.traffic_viewtypes[ adjustedIndex ]
	 */
	public static int getAdjustedIndex() {
		// The favs section must be ignorred for looking up in traffic_viewtypes
		int noFavs = (MapsTab.CURRENT_INDEX - MenuIndexes.FAV_SIZE);
		
		// Floor at 0
		int index = noFavs<0 ? 0 : noFavs;
		
		// Ceil at Config.traffic.length
		int trafficSize = Config.traffic.length;
		int adjustedIndex = index > (trafficSize-1) ? trafficSize : index;
		
		if(Config.DEBUG>0) Log.d(App.TAG, "MapsTab::getAdjustedIndex() adjustedIndex: " + Integer.toString(adjustedIndex));
		return adjustedIndex;
	}
	
	public static int getAndroidViewType() {
		if(Config.DEBUG>0) Log.d(App.TAG, "MapsTab::getAndroidViewType() MapsTab.CURRENT_INDEX: " + Integer.toString(MapsTab.CURRENT_INDEX));
		if(Config.DEBUG>0) Log.d(App.TAG, "MapsTab::getAndroidViewType() MenuIndexes.FAV_SIZE: " + Integer.toString(MenuIndexes.FAV_SIZE));
		
		if (MenuIndexes.FAV_SIZE > 0) {
			// Need to use adjusted index for the favorites
			if ( MapsTab.CURRENT_INDEX < (MenuIndexes.FAV_SIZE)) {
				return Config.FAVORITE;
			}
		}

		return Config.traffic_viewtypes[ getAdjustedIndex() ];
	}

	/**
	 * A data type to hold and track indexes for a
	 *  menu with different types
	 */
	public static class MenuIndexes {
		// FIXME - This should be detached from the MapsTab code
		//  and in its own class so favorites can be used on any tab.
		public static int FAV_INDEX = 0;
		public static int FAV_SIZE  = 0;
		public static int SYS_INDEX = 0;
		public static int SYS_SIZE  = 0;
		
		public static void init() {
			setSysArraySize();
			setFavArraySize();
			setFavArrayIndex(MapsTab.CURRENT_INDEX);
			
			//Log.d(App.TAG, "MenuIndexes.Init() complete: FAV_SIZE : " + FAV_SIZE);
			//Log.d(App.TAG, "MenuIndexes.Init() complete: Config.MAPVIEW_FAVORITES: " + Config.MAPVIEW_FAVORITES);
		}
		public static void setFavArraySize() {
			JSONArray ja = null;
			try {
				ja = new JSONArray(Config.MAPVIEW_FAVORITES);
				FAV_SIZE = ja.length();
			} catch (JSONException e) {
				Log.d(App.TAG, "Current map view coords empty");
			}
			//Log.d(App.TAG, "MenuIndexes.setFavArraySize(): FAV_INDEX: " + FAV_INDEX);
		}
		public static void setFavArrayIndex(int index) {
			if (FAV_SIZE > 0 && index < FAV_SIZE) {
				FAV_INDEX = index;
			} else {
				// The current index is not in the range of favs so there
				//  is no current favorite selected.
				FAV_INDEX = -1;
			}
		}
		public static void setSysArraySize() {
			SYS_SIZE = Config.traffic.length;
		}
		public static int totalSize() {
			return FAV_SIZE + SYS_SIZE;
		}
		public static String debug() {
			return "MenuIndexes:: SYS_SIZE:" + SYS_SIZE +
			" SYS_INDEX:" + SYS_INDEX +
			" FAV_SIZE:" + FAV_SIZE +
			" FAV_INDEX:" + FAV_INDEX;
		}
	};
}
