package com.osilabs.android.apps.dallastraffic;


import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.osilabs.android.lib.Gesture;

class MapOverlay extends com.google.android.maps.Overlay
{
	private final int DEBUG = 0;

	@Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) 
    {
		return false;
    }

    @Override
    public boolean onTap(GeoPoint p, MapView mapView) {
    	if (Config.DEBUG>0) {
    		Log.d(App.TAG, "MapOverlay::onTap() Coords=lat:" 
    				+ p.getLatitudeE6()
    				+ ", lon:"
    				+ p.getLongitudeE6()
    				+ ", zoom:"
    				+ App.mvTraffic.getZoomLevel());
    	}

    	// Look for double tap and zoom
    	Gesture.DoubleTapListner dt = new Gesture.DoubleTapListner();
    	
    	if (dt.isDoubleTapped(Gesture.DoubleTapListner.THRESHOLD_MEDIUM)) {
    		// FIXME - This onTap doesn't seem to fire fast enough for a quick
    		//  double tap.  May need to kick off a thread for processing
    		//  between taps
    		App.mcMain.zoomIn();
    		return true;
    	}

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

