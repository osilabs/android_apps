package com.osilabs.android.apps.chicagotraffic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

class MapOverlay extends com.google.android.maps.Overlay
{
	
	@Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
    {
		return false;
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
        final CharSequence[] items = {"Save as Favorite", "Zoom In", "Zoom Out"};
        AlertDialog.Builder builder = new AlertDialog.Builder(App.me);
        builder.setTitle("Map Views");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
            	switch (item) {
            	case 0:
            		// Here they have chosen add a new favorite android mapview.
            		Favorites.handleClick();
            		MapsTab.MenuIndexes.setFavArrayIndex(MapsTab.CURRENT_INDEX);
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
		// Once they scroll the mapview, unset the star so they can set it again.
		Favorites.setStarIcon(Favorites.MODE_OFF);
        return false;
    }        
}

