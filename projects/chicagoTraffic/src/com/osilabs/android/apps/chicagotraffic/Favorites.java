package com.osilabs.android.apps.chicagotraffic;

import org.json.JSONArray;
import org.json.JSONException;

public class Favorites {
	/**
	 * Gets the json string that has all the metadata for the passed
	 *  index of the list of favorites. index will only be the
	 *  index of the items in the favorites json array. No need
	 *  to worry about MapsTab.SYS_SIZE here.
	 */
//	public static String getFavoriteCoords(int index) {
//		JSONArray ja = null;
//		try {
//			ja = new JSONArray(Config.MAPVIEW_FAVORITES);
//			// FIXME - return an object. hopefully it can be used more natively
//			//  than returning a string and turning back into json.
//			return ja.getJSONObject( index ).getString("fixme - return the index?").toString();
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return "";
//		}
//	}
}
