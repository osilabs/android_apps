/**
 * 
 */
package com.osilabs.android.apps.cba;

public class ColorDrop {
	public int R = 0;
	public int G = 0;
	public int B = 0;
	public int A = 0; // alpha
	public int RGBint = 0;
	public int bufallocsize = 0;
	public int[] decodeBuf = {};
	public String RGBstr = "0,0,0";
	public String RGBdisplay = "rgb("+this.RGBstr+")";
	public String hexval = "000000";
	public String colorname = "";
	
	/**
	 * Sets members based on rgb string
	 * @param rgb i.e. "60,255,63"
	 */
	public void setRGB(String rgb) {
		String[] toks = rgb.split(",");
		this.R = Integer.parseInt(toks[0]);
		this.G = Integer.parseInt(toks[1]);
		this.B = Integer.parseInt(toks[2]);
	}
}
