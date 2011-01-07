package com.osilabs.android.apps.dallastraffic;

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

	// Indexes for URL stings
	private   static final int    TYPE    = 0;
	private   static final int    URI     = 1;
	private   static final int    WIDTH   = 2;
	private   static final int    HEIGHT  = 3;
	private   static final int    SCROLLX = 4;
	private   static final int    SCROLLY = 5;
	
	// If never set, is set to first map.
	public    static int    	  CURRENT_INDEX = 0;
	
	public static void init() {
		App.ivMapsTab.setVisibility(View.VISIBLE);
		App.ivMapsTab.setPadding(15, 0, 15, 0);
		App.ivMapsTab.setAlpha(ALPHA_OFF);
		App.ivMapsTab.setBackgroundColor(BG_OFF);
		
		// Init the sizes of the favorites and system map 
		//  popup menu option arrays.
		MenuIndexes.init();

		// Must happen after MenuIndexes.init() because that sets the values
		//  for MenuIndexes.totalSize()
		if (MapsTab.CURRENT_INDEX > (MenuIndexes.totalSize()) - 1) {
			// The saved value that came from the session was larger
			//  than the total indexes available. Set the current index
			//  back to defaults.
			MapsTab.CURRENT_INDEX = Config.DEFAULT_MAP_INDEX;
			if(Config.DEBUG>0)Log.d(App.TAG, "MapsTab.Init() Current index fell back on default: " + Config.DEFAULT_MAP_INDEX);
		}
		
		if(Config.DEBUG>0)Log.d(App.TAG, "MapsTab.Init() complete: FAV_SIZE: " + MenuIndexes.FAV_SIZE);
		if(Config.DEBUG>0)Log.d(App.TAG, "MapsTab.Init() complete: Config.MAPVIEW_FAVORITES: " + Config.MAPVIEW_FAVORITES);

	}
	public static String getReloadURLParts() {
		if(Config.DEBUG>0) Log.d(App.TAG, "MapsTab::getReloadURLParts()");
		
		int adjustedIndex = getAdjustedIndex();

		if (CURRENT_INDEX > ((Config.traffic_urls.length+MapsTab.MenuIndexes.FAV_SIZE) - 1)) CURRENT_INDEX = 0;
		
		int androidViewType = MapsTab.getAndroidViewType();
		if(Config.DEBUG>0) Log.d(App.TAG, "MapsTab::getReloadURLParts() androidViewType: " + androidViewType);

		String query_string= "";
		
		if (androidViewType == Config.IMAGE) {
			query_string 
				= "&traffic=" + Config.traffic_urls[adjustedIndex] + "|" + adjustedIndex
				+ "&mapscrollx="
				+ MapsTab.getScrollX()
				+ "&mapscrolly="
				+ MapsTab.getScrollY();
		} else if (androidViewType == Config.FEED) {
			query_string 
				= "&traffic=" + Config.traffic_urls[adjustedIndex] + "|" + adjustedIndex;
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
		return Config.traffic_urls[getAdjustedIndex()];
//		// FIXME - Not a good way
//		String[] as = Config.traffic_urls[getAdjustedIndex()].split("~");
//		return as[TYPE];
	}
	public static String getScrollX() {
		return "";
	}
	public static String getScrollY() {
		return "";
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
		int viewType = -1;
		
		if (MenuIndexes.FAV_SIZE > 0) {
			// Need to use adjusted index for the favorites
			if ( MapsTab.CURRENT_INDEX < (MenuIndexes.FAV_SIZE)) {
				viewType = Config.FAVORITE;
			}
		}

		if (viewType == -1) {
			// Use adjusted index
			viewType = Config.traffic_viewtypes[ getAdjustedIndex() ];
		}
		
		if(Config.DEBUG>0) Log.d(App.TAG, "MapsTab::getAndroidViewType() androidViewType: " + viewType);
		
		return viewType;
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
			
			//if(Config.DEBUG>0)Log.d(App.TAG, "MenuIndexes.Init() complete: FAV_SIZE : " + FAV_SIZE);
			//if(Config.DEBUG>0)Log.d(App.TAG, "MenuIndexes.Init() complete: Config.MAPVIEW_FAVORITES: " + Config.MAPVIEW_FAVORITES);
		}
		public static void setFavArraySize() {
			JSONArray ja = null;
			try {
				ja = new JSONArray(Config.MAPVIEW_FAVORITES);
				FAV_SIZE = ja.length();
			} catch (JSONException e) {
				if(Config.DEBUG>0)Log.e(App.TAG, "Current map view coords empty");
			}
			//if(Config.DEBUG>0)Log.d(App.TAG, "MenuIndexes.setFavArraySize(): FAV_INDEX: " + FAV_INDEX);
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
			return "MenuIndexes::debug() SYS_SIZE:" + SYS_SIZE +
			" SYS_INDEX:" + SYS_INDEX +
			" FAV_SIZE:" + FAV_SIZE +
			" FAV_INDEX:" + FAV_INDEX;
		}
	};
}
