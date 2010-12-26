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
            		AlertDialog.Builder alert = new AlertDialog.Builder(App.me);  
            		alert.setTitle("Title");  
            		alert.setMessage("Message");  
            		// Set an EditText view to get user input   
            		final EditText input = new EditText(App.me);  
            		alert.setView(input);  
            		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
            			public void onClick(DialogInterface dialog, int whichButton) {  
            		  		prefname = input.getText().toString();
            		  		
            		  		if (prefname != "") {
            		  			
                		  		JSONObject jo = new JSONObject();
                		  		JSONArray ja = new JSONArray();
                		  		
                 		  		try {
									jo.put("label", prefname);
									jo.put("zoom", Integer.toString(App.mvTraffic.getZoomLevel()));
	                		  		jo.put("latitude", Integer.toString(gpFavorite.getLatitudeE6()));
	                		  		jo.put("longitude", Integer.toString(gpFavorite.getLongitudeE6()));
								} catch (JSONException e) {
									e.printStackTrace();
								}
								
//								HashMap<Integer, JSONObject> hm = new HashMap<Integer, JSONObject>();
//								ArrayList<Map<String, String>> al = new ArrayList<Map<String, String>>();
								ArrayList<JSONObject> aj = new ArrayList<JSONObject>();
								
								aj.add(jo);
								aj.add(jo);
								aj.add(jo);
								
						//		ja.put(aj);
								//al.add();
								
								
								//hm.put(0, jo);
								//ja.put(hm);
								
								//ja.put(jo);
								
								
        	            		Toast.makeText(App.me,
        	                            "Created JSON: " + jo.toString(), 
        	                            Toast.LENGTH_SHORT).show();

//	                		  	// Create a bundle of geopoint and zoom level to save as favorite
//        	            		String mapframe =
//        	            			Integer.toString(App.mvTraffic.getZoomLevel())
//        	            			+ ":" 
//        	            			+ Integer.toString(gpFavorite.getLatitudeE6())
//        	            			+ ":" 
//        	            			+ Integer.toString(gpFavorite.getLongitudeE6());
        	            		
        	            		// Save to shared prefs
        					    SharedPreferences.Editor editor = App.mySharedPreferences.edit();
        					    editor.putString("pref_favorite_map_frame_1", aj.toString());
        					    editor.commit();
        	            		
        	            		// Tell the main app that prefs have changed so things can reread them
        	            		App.PREFS_UPDATED = true;
        	            		
        	            		// A message that stuff happened
        	            		Toast.makeText(App.me,
        	                            "New view set for the map", 
        	                            Toast.LENGTH_SHORT).show();
                    		}
            			}  
            		});  
            		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
            			public void onClick(DialogInterface dialog, int whichButton) {  
            				// User hit cancel, flag so we can exit
            				prefname = "-1";
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

