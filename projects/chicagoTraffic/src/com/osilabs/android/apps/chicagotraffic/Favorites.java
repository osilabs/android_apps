package com.osilabs.android.apps.chicagotraffic;

import org.json.JSONArray;
import org.json.JSONException;

public class Favorites {
	/**
	 * Gets the json string that has all the metadata for the passed
	 *  index of the list of favorites
	 */
	public static String getFavorite(int index) {
		JSONArray ja = null;
		try {
			ja = new JSONArray(Config.MAPVIEW_FAVORITES);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
		
		if (ja == null || ja.isNull(0)) {
			return "";
		} else {
			try {
				return ja.getJSONObject( index ).getString("label").toString();
			} catch (JSONException e) {
				e.printStackTrace();
				return "";
			}
		}
	}
}
