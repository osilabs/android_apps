package com.osilabs.android.apps.chicagotraffic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

class MapOverlay extends com.google.android.maps.Overlay
{
    protected GeoPoint gpFavorite;

	
	@Override
    public boolean draw(Canvas canvas, MapView mapView, 
    boolean shadow, long when) 
    {
		return false;
       //...
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
                
        final CharSequence[] items = {"Save as Favorite", "Zoom In", "Zoom Out"};
        AlertDialog.Builder builder = new AlertDialog.Builder(App.me);
        builder.setTitle("Map Views");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	switch (item) {
            	case 0:
                    // Create a bundle of geopoint and zoom level to save as favorite
            		String mapframe =
            			Integer.toString(App.mvTraffic.getZoomLevel())
            			+ ":" 
            			+ Integer.toString(gpFavorite.getLatitudeE6())
            			+ ":" 
            			+ Integer.toString(gpFavorite.getLongitudeE6());
            		
            		// Save to shared prefs
				    SharedPreferences.Editor editor = App.mySharedPreferences.edit();
				    editor.putString("pref_favorite_map_frame_1", mapframe);
				    editor.commit();
            		
            		// Tell the main app that prefs have changed so things can reread them
            		App.PREFS_UPDATED = true;
            		
            		// A message that stuff happened
            		Toast.makeText(App.me,
                            "New view set for the map", 
                            Toast.LENGTH_SHORT).show();

            		break;
            	case 1:
            		// Zoome in
            		App.mcMain.zoomIn();
            		break;
            	case 2:
            		// Zoome out
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

