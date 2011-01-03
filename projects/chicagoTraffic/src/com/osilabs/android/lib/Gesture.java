package com.osilabs.android.lib;

import android.util.Log;

public class Gesture {

	//static void Gesture() {
		/**
		 * Double tap handler
		 */
		public static class DoubleTapListner {
	    	private final int DEBUG = 0;
	    	
			// Public static consts for how sensitive to make the check.
			// TODO - These are not all refined. Currently only really tested
			//  medium.
			public static final int THRESHOLD_SHORT  = 500;
			public static final int THRESHOLD_MEDIUM = 600;
			public static final int THRESHOLD_LONG   = 700;
			
			// Tracks the last tap
			static long doubleTapTimer = System.currentTimeMillis();
			
//			// If the last tap was within this number of milliseconds, treat it
//			//  as a double tap
//			private final int doubleTapThreshold = THRESHOLD_MEDIUM;
			
			public DoubleTapListner() {
				super();
			}
	
			/**
			 * Call with each tap of the overlay. Will return true if the
			 *  time since the last tap is < the threshold.
			 *  
			 * @param threshold One of the public static THRESHOLD_XXX consts
			 * @return boolean true if a double tap event has taken place
			 */
			public boolean isDoubleTapped(int threshold) {
				// Default to not a double tap event
				boolean isdoubletapped = false;

				// Get where we are now
				long now = System.currentTimeMillis();
				
				// How long since last
				int timeSinceLast = (int) (now - doubleTapTimer);
				
				// FIXME - user a common TAG.  But don't user App. anything, this is a library.
		    	if (DEBUG>0) Log.d("** osilabs ** Gesture ", "Since last: " + timeSinceLast);
				
				// Look for start of new series of taps
//				if (timeSinceLast == 0 || timeSinceLast > threshold) {
//					Log.d("** osilabs", "Reset timer: " + timeSinceLast);
//				} else {
				if (timeSinceLast < threshold) {
					
					if (DEBUG>0) Log.d("** osilabs", "last:now="+ doubleTapTimer + ":" + now);
					if (DEBUG>0) Log.d("** osilabs", "time="+ (now - doubleTapTimer));
					
//					// Check if last tap was quite recently
//					if (timeSinceLast < doubleTapThreshold) {
						isdoubletapped = true;
//					}
				}

				// Update the last time the check was made
				resetTimer();
				
				return isdoubletapped;
			}
			private void resetTimer() {
				doubleTapTimer = System.currentTimeMillis();
			}
		}
	//}
}
