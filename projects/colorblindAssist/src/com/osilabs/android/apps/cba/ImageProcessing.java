package com.osilabs.android.apps.cba;
import java.lang.Math;
import com.osilabs.android.lib.osicolor.ColorDrop;

public class ImageProcessing {
	
	public static int BEHAVIOR_FREE = 0;
	public static int BEHAVIOR_PRO  = 1;

	/**
	 * dotPosition and modifier
	 * 
	 * The "night rider" dot and its movement
	 */
	public static int dotPosition=3;
	public static int dotPositionModifier=1;


	/**
	 * Turn a ColorDrop object into a string of which color it represents
	 * 
	 *	Algorithm
	 *	1. Determine if red, green, or blue. If no other colors are
	 *	     set after this point, the color will end up falling
	 *	     back on one of these three colors.
	 *	2. Figure out if black, gray, or white.
	 *	3. Look for alternates: purple, yellow, pink, brown, orange.
	 *
	 * @param d ColorDrop object
	 * @param behavior free or pro version behavior
	 * @return string of color value
	 */
	public static String getColorNameFromRGB(ColorDrop d, int behavior) {
		// Calculate Max and base color
		int max = d.R;
		String baseC = "Red";
		if (d.G>d.R) {
			max = d.G;
			baseC = "Green";
		}
		if (d.B>max) {
			max = d.B;
			baseC = "Blue";
		}
		
		// WHITE
		if ((d.R+d.G+d.B) > 650) {
			return (behavior == ImageProcessing.BEHAVIOR_PRO) ? "White" : ImageProcessing.getDotPosition();
		}

		// BLACK
		if ((d.R+d.G+d.B) < 42) {
			return (behavior == ImageProcessing.BEHAVIOR_PRO) ? "Black" : ImageProcessing.getDotPosition();
		}

		// GRAY
		if ( (Math.abs(d.R-d.G) < 15) && (Math.abs(d.R-d.B) < 15) ) {
			return (behavior == ImageProcessing.BEHAVIOR_PRO) ? "Gray" : ImageProcessing.getDotPosition();
		}
		
		// PURPLE
		// Purple is g < r < b or g < b < r
		if ( ((d.G < d.R) && (d.R < d.B)) || ((d.G < d.B) && (d.B < d.R)) ) {
			return (behavior == ImageProcessing.BEHAVIOR_PRO) ? "Purple" : ImageProcessing.getDotPosition();
		}
		
		// YELLOW
		// r and g are close, blue is much lower. r must be high.
		if ( ((d.R-d.G) < 30) && ((d.G-d.B) > 80) && (d.R > 190) ) {
			return (behavior == ImageProcessing.BEHAVIOR_PRO) ? "Yellow" : ImageProcessing.getDotPosition();
		}
		
		// orange
		// stairstep r > g > b, diff r:g = diff g:b +- n
		// && r > b
		if ((((d.R-d.G) - (d.G-d.B)) < 20) && d.R > d.B) {
			return (behavior == ImageProcessing.BEHAVIOR_PRO) ? "Orange" : ImageProcessing.getDotPosition();			
		}

		// If didn't find anything better, we have r, g, or b.
		return baseC;
	}

	private static String getDotPosition() {
		int dots = 6;
		String dotstring = "";
		
		if (ImageProcessing.dotPosition <= 1) {
			ImageProcessing.dotPositionModifier = 1;
		} else if (ImageProcessing.dotPosition >= dots) {
			ImageProcessing.dotPositionModifier = -1;
		}
		
		ImageProcessing.dotPosition += ImageProcessing.dotPositionModifier;
	
		for (int i = 1; i <= dots; i++) {
			dotstring += (i == ImageProcessing.dotPosition) ? "." : " ";
		}
		
		return dotstring;
	}
}
