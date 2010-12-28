package com.osilabs.android.apps.chicagotraffic;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.text.Editable;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

class MapOverlay extends com.google.android.maps.Overlay
{
    protected        GeoPoint gpFavorite;
	protected static String   prefname;


	@Override
    public boolean draw(Canvas canvas, MapView mapView, 
    boolean shadow, long when) 
    {
		return false;
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
        //---when user lifts his finger---
//        if (event.getAction() == 1) {                
//            GeoPoint p = mapView.getProjection().fromPixels(
//                (int) event.getX(),
//                (int) event.getY());
//                Toast.makeText(App.me,
//                    p.getLatitudeE6() / 1E6 + "," + 
//                    p.getLongitudeE6() /1E6 , 
//                    Toast.LENGTH_SHORT).show();
//        }

        gpFavorite = p;
		prefname = "-1";
		
        final CharSequence[] items = {"Save as Favorite", "Zoom In", "Zoom Out"};
        AlertDialog.Builder builder = new AlertDialog.Builder(App.me);
        builder.setTitle("Map Views");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	switch (item) {
            	case 0:
            		// Here they have chosen add a new favorite android mapview.
            		// 1. Collect the label to call this one.
            		// 2. Get the existing list of favorites and 
            		//     prepend this one to it
            		
            		// 1. Collect the label
            		AlertDialog.Builder alert = new AlertDialog.Builder(App.me);  
            		alert.setTitle("Favorite Maps");  
            		alert.setMessage("Title");  
            		// Set an EditText view to get user input   
            		final EditText input = new EditText(App.me);
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
            		  		
            		  		if (prefname != "") {
            		  			// FIXME - Move the setting of Favorites into Favorites class.
            		  			// FIXME - Move the setting of Favorites into Favorites class.
            		  			// FIXME - Move the setting of Favorites into Favorites class.
            		  			// FIXME - Move the setting of Favorites into Favorites class.
            		  			// FIXME - Move the setting of Favorites into Favorites class.
            		  			// FIXME - Move the setting of Favorites into Favorites class.
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
									} catch (JSONException e1) {
										e1.printStackTrace();
									}
									
									// If no favs exist, skip appending them
//									if ( ja != null && ! ja.isNull(0) ) {
//		        	            		Toast.makeText(App.me,
//		        	                            "JSON array: " + ja.toString(),
//		        	                            Toast.LENGTH_SHORT).show();
		
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
																		
		//								HashMap<Integer, JSONObject> hm = new HashMap<Integer, JSONObject>();
		//								ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
								//		ja.put(aj);
										//al.add();
										//hm.put(0, jo);
										//ja.put(hm);
										//ja.put(jo);
									}
//								}
								
        	            		// Set the favorites string.
        	            		Config.MAPVIEW_FAVORITES = aj.toString();

        	            		// Save to shared prefs
        					    SharedPreferences.Editor editor = App.mySharedPreferences.edit();
        					    editor.putString("pref_mapview_favorites", Config.MAPVIEW_FAVORITES);
        					    editor.commit();

        					    Toast.makeText(App.me,
        	                            "Created JSON: " + aj.toString(),
        	                            Toast.LENGTH_SHORT).show();

        	            		// Tell the main app that prefs have changed so things can reread them
        	            		App.PREFS_UPDATED = true;
        	            		
        	            		// A message that stuff happened
        	            		Toast.makeText(App.me,
        	                            "New view set for the map", 
        	                            Toast.LENGTH_SHORT).show();
                    		}
            			}  
            		});
            		alert.show();

            		break;
            	case 1:
            		// Zoom in
            		App.mcMain.zoomIn();
            		break;
            		
            	case 2:
            		// Zoom out
            		App.mcMain.zoomOut();
            		break;
            	}
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
                
        // Return true if the tap was handled by this overlay.
        return false;
    }
    
    public boolean onTouchEvent(MotionEvent event, MapView mapView) 
    {   
//        //---when user lifts his finger---
//        if (event.getAction() == 1) {                
//            GeoPoint p = mapView.getProjection().fromPixels(
//                (int) event.getX(),
//                (int) event.getY());
//                Toast.makeText(App.me,
//                    p.getLatitudeE6() / 1E6 + "," + 
//                    p.getLongitudeE6() /1E6 , 
//                    Toast.LENGTH_SHORT).show();
//        }                            
        return false;
    }        
}

