package com.osilabs.android.apps.chicagotraffic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;
import com.osilabs.android.lib.gestures.Session;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Favorites {
    protected static        GeoPoint gpFavorite;
	protected static String   prefname;
	
	// Flag used to determine if the star is yellow basically. That 
	//  is used to determine if clicking the star removes or adds it.
	public static boolean FAVORITE_IS_ACTIVE = false;
	
	public static final int MODE_ON   = 0;
	public static final int MODE_OFF  = 1;
	public static final int MODE_GONE = 2;
	
	public static void setStarIcon(int mode) {
		switch (mode) {
		case MODE_ON:
			App.ivFavorite.setImageResource(R.drawable.favorite_star_on);
			App.ivFavorite.setVisibility(View.VISIBLE);
			FAVORITE_IS_ACTIVE = true;
			break;
		case MODE_OFF:
			App.ivFavorite.setImageResource(R.drawable.favorite_star_off);
			App.ivFavorite.setVisibility(View.VISIBLE);
			FAVORITE_IS_ACTIVE = false;
			break;
		case MODE_GONE:
			App.ivFavorite.setVisibility(View.INVISIBLE);
			FAVORITE_IS_ACTIVE = false;
			break;
		}
	}
	
	public static void removeFavorite(int index) {
		if (Config.MAPVIEW_FAVORITES != "") {
			String removedLabel = "";
			JSONArray ja = null;
	  		JSONObject jo = null;
  			ArrayList<JSONObject> aj = new ArrayList<JSONObject>();
	  		try {
				ja = new JSONArray(Config.MAPVIEW_FAVORITES);
				for(int i=0; i<ja.length(); i++) {
					// Add all the favs except the one we just deleted
					if (i != index) {
						jo = new JSONObject();
						jo.put("label", ja.getJSONObject(i).getString("label"));
						jo.put("zoom", ja.getJSONObject(i).getString("zoom"));
	    		  		jo.put("latitude", ja.getJSONObject(i).getString("latitude"));
	    		  		jo.put("longitude", ja.getJSONObject(i).getString("longitude"));
						aj.add(jo);
					} else {
						// This is the one we removed so save it's name for the toast message
						removedLabel = ja.getJSONObject(i).getString("label");
					}
				}
			} catch (JSONException e1) {
				// e1.printStackTrace();
			}
			
			// Look at current index and make sure it hasn't exceeded the
			//  array sizes
			if (MapsTab.CURRENT_INDEX >= MapsTab.MenuIndexes.totalSize()) {
				// The deletion caused the current index to exceed the total size
				//  of menu options so set it back to the end of the list
				
				MapsTab.CURRENT_INDEX = (MapsTab.MenuIndexes.totalSize() - 1);
			}

			// Set the favorites string.
			saveNewFav(aj.toString());
			
			// Remove the current mapview from
			//  the current mapview global variable and session
			updateCurrentMapviewCoords();

			// Unstar it
		    setStarIcon(MODE_OFF);
			
			Toast.makeText(App.me, 
					"Removed Favorite " + removedLabel
					, Toast.LENGTH_LONG).show();
		}
	}
	
	public static void updateCurrentMapviewCoords() {
		Config.CURRENT_MAPVIEW_COORDS = "";
		
		// Save to shared prefs
		Session.saveString(App.mySharedPreferences, "pref_current_mapview_coords", "");
//    	App.mySharedPreferences = App.me.getSharedPreferences(Config.NAMESPACE + "_preferences", 0);
//	    SharedPreferences.Editor editor = App.mySharedPreferences.edit();
//	    editor.putString("pref_current_mapview_coords", "");
//	    editor.commit();
	    
		// Tell the main app that prefs have changed so things can reread them
		App.PREFS_UPDATED = true;
	}
	
	public static void saveNewFav(String favString) {
		if(Config.DEBUG>1) Log.d(App.TAG, "Favorites::saveNewFav(): " + favString);
		
		// FIXME - Move Config.MAPVIEW_FAVORITES to this class
		Config.MAPVIEW_FAVORITES = favString;
		
		// Save to shared prefs
		Session.saveString(App.mySharedPreferences, "pref_mapview_favorites", favString);
//    	App.mySharedPreferences = App.me.getSharedPreferences(Config.NAMESPACE + "_preferences", 0);
//	    SharedPreferences.Editor editor = App.mySharedPreferences.edit();
//	    editor.putString("pref_mapview_favorites", favString);
//	    editor.commit();
	    	    
	    // New fav is added to top of list so set the current index 
	    //  to 0 so the new one is the current one
	    MapsTab.CURRENT_INDEX = 0;
	    
	    // Update MenuIndex FAV_SIZE
		MapsTab.MenuIndexes.setFavArraySize();
		MapsTab.MenuIndexes.setFavArrayIndex(MapsTab.CURRENT_INDEX);
	    
		// Tell the main app that prefs have changed so things can reread them
		App.PREFS_UPDATED = true;
	}
	
	public static void handleClick() {

		if (FAVORITE_IS_ACTIVE) {	
			// If a fav has been clicked and the star is still yellow, we
			//  are removing a favorite.
			
			if (MapsTab.MenuIndexes.FAV_INDEX != -1) {
				Log.d(App.TAG, "REMOVE INDEX: " + MapsTab.MenuIndexes.FAV_INDEX);
				
				removeFavorite(MapsTab.MenuIndexes.FAV_INDEX);
			}
		} else {
			
			// 1. Collect the label to call this one.
			// 2. Get the existing list of favorites and 
			//     prepend this one to it
			
	        gpFavorite = App.mvTraffic.getMapCenter();
			prefname = "-1";
			
			// 1. Collect the label
			AlertDialog.Builder alert = new AlertDialog.Builder(App.me);  
			alert.setTitle("New Favorite");  
			alert.setMessage("Short Name");
			alert.setIcon(R.drawable.favorite_star_on);
			
			// Set an EditText view to get user input   
			final EditText input = new EditText(App.me);
			
			// Limit the number of characters for the label
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(15);
			input.setFilters(FilterArray);
			
			// Padding the text edit to make her look nice
			input.setPadding(10, 0, 10, 0);

			alert.setView(input);
			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  
					// User hit cancel, flag so we can exit
					prefname = "-1";
				}  
			});  
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
				public void onClick(DialogInterface dialog, int whichButton) {  
			  		prefname = input.getText().toString();
			  		
			  		if (! prefname.equals("")) {
			  			JSONArray ja = null;
			  			ArrayList<JSONObject> aj = new ArrayList<JSONObject>();
			  			
	    		  		// 2a. Prepend the new favorite
	    		  		JSONObject jo = new JSONObject();
						try {
							jo.put("label", prefname);
							jo.put("zoom", Integer.toString(App.mvTraffic.getZoomLevel()));
	        		  		jo.put("latitude", Integer.toString(gpFavorite.getLatitudeE6()));
	        		  		jo.put("longitude", Integer.toString(gpFavorite.getLongitudeE6()));
						} catch (JSONException e) {
							e.printStackTrace();
						}
						aj.add(jo);
						
	            		Log.d(App.TAG, "New JSON bit: " + aj.toString());
	
			  			// 2b. Get existing list of favorites
						if (Config.MAPVIEW_FAVORITES != "") {
	        		  		try {
								ja = new JSONArray(Config.MAPVIEW_FAVORITES);
								
								// 2c. Append on the rest of existing favorites. Turn each into
								//  a json object and append.
								for(int i=0; i<ja.length(); i++) {
									jo = new JSONObject();
									try {
										jo.put("label", ja.getJSONObject(i).getString("label"));
										jo.put("zoom", ja.getJSONObject(i).getString("zoom"));
		                		  		jo.put("latitude", ja.getJSONObject(i).getString("latitude"));
		                		  		jo.put("longitude", ja.getJSONObject(i).getString("longitude"));
									} catch (JSONException e) {
										e.printStackTrace();
									}
									aj.add(jo);
								}
								
								Log.d(App.TAG, "Full Favs JSON: " + aj.toString());
							} catch (JSONException e1) {
								e1.printStackTrace();
							}

							// Save updates in session and globals
		        			saveNewFav(aj.toString());

						    // Set the favorites star to on
						    setStarIcon(MODE_ON);
						    
							Toast.makeText(App.me,
									"New Favorite Added: " + prefname
									, Toast.LENGTH_LONG).show();
						}
	        		} else {
						Toast.makeText(App.me,
								"Please name your favorite"
								, Toast.LENGTH_LONG).show();
					}
				}  
			});
			alert.show();
		}
	}
}
