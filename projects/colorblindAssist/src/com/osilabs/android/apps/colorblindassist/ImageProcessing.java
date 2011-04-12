package com.osilabs.android.apps.colorblindassist;
import java.lang.Math;

public class ImageProcessing {
	// decode Y, U, and V values on the YUV 420 buffer described as YCbCr_422_SP
	// by Android David Manpearl 081201
	public static void decodeYUV(int[] out, byte[] fg, int width, int height)
			throws NullPointerException, IllegalArgumentException {

		int sz = width * height;

		if (out == null)
			throw new NullPointerException("buffer out is null");

		if (out.length < sz)
			throw new IllegalArgumentException("buf out size " + out.length
					+ " < minimum " + sz);

		if (fg == null)
			throw new NullPointerException("buffer 'fg' is null");

		if (fg.length < sz)
			throw new IllegalArgumentException("buffer fg size " + fg.length
					+ " < minimum " + sz * 3 / 2);
		int i, j;
		int Y, Cr = 0, Cb = 0;
		for (j = 0; j < height; j++) {
			int pixPtr = j * width;
			final int jDiv2 = j >> 1;
			for (i = 0; i < width; i++) {
				Y = fg[pixPtr];
				if (Y < 0)
					Y += 255;
				if ((i & 0x1) != 1) {
					final int cOff = sz + jDiv2 * width + (i >> 1) * 2;
					Cb = fg[cOff];
					if (Cb < 0)
						Cb += 127;
					else
						Cb -= 128;
					Cr = fg[cOff + 1];
					if (Cr < 0)
						Cr += 127;
					else
						Cr -= 128;
				}
				int R = Y + Cr + (Cr >> 2) + (Cr >> 3) + (Cr >> 5);
				if (R < 0)
					R = 0;
				else if (R > 255)
					R = 255;
				int G = Y - (Cb >> 2) + (Cb >> 4) + (Cb >> 5) - (Cr >> 1)
						+ (Cr >> 3) + (Cr >> 4) + (Cr >> 5);
				if (G < 0)
					G = 0;
				else if (G > 255)
					G = 255;
				int B = Y + Cb + (Cb >> 1) + (Cb >> 2) + (Cb >> 6);
				if (B < 0)
					B = 0;
				else if (B > 255)
					B = 255;
				out[pixPtr++] = 0xff000000 + (B << 16) + (G << 8) + R;
			}
		}
	}

	private int averageRGB(int[] outbuf, int[] region) {
		int xRED = 0;
		int xGRN = 0;
		int xBLU = 0;
		int items = 0;

		for (int i = 1; i <= region[0]; i++) {
			xRED += outbuf[region[i]] & 255;
			xGRN += (outbuf[region[i]] >> 8) & 255;
			xBLU += (outbuf[region[i]] >> 16) & 255;
			items++;
		}

		if (items > 0) {
			return 0xff000000 + (((int) xBLU / items) << 16)
					+ (((int) xGRN / items) << 8) + ((int) xRED / items);
		} else {
			return -16777216; // r=0, g=0, b=0
		}
	}

	public int calculateRGBCompliment(int r, int g, int b) {
		float var_r = (r) / 255;
		float var_g = (g) / 255;
		float var_b = (b) / 255;

		// Now plug these values into the rgb2hsl routine. Below is my PHP
		// version of EasyRGB.com's generic code for that conversion:

		// Input is var_r, var_g and var_b from above
		// Output is HSL equivalent as h, s and l — these are again expressed as
		// fractions of 1, like the input values

		float var_min = Math.min(var_r, Math.min(var_g, var_b));
		float var_max = Math.max(var_r, Math.max(var_g, var_b));
		float del_max = var_max - var_min;

		float l = (var_max + var_min) / 2;

		float h = 0;
		float s = 0;

		if (del_max == 0) {
			h = 0;
			s = 0;
		} else {
			if (l < 0.5) {
				s = del_max / (var_max + var_min);
			} else {
				s = del_max / (2 - var_max - var_min);
			}

			float del_r = (((var_max - var_r) / 6) + (del_max / 2)) / del_max;
			float del_g = (((var_max - var_g) / 6) + (del_max / 2)) / del_max;
			float del_b = (((var_max - var_b) / 6) + (del_max / 2)) / del_max;

			if (var_r == var_max) {
				h = del_b - del_g;
			} else if (var_g == var_max) {
				h = (1 / 3) + del_r - del_b;
			} else if (var_b == var_max) {
				h = (2 / 3) + del_g - del_r;
			}

			if (h < 0) {
				h += 1;
			}

			if (h > 1) {
				h -= 1;
			}

		}

		// So now we have the colour as an HSL value, in the variables h, s and
		// l. These three output variables are again held as fractions of 1 at
		// this stage, rather than as degrees and percentages. So e.g., cyan
		// (180° 100% 50%) would come out as h = 0.5, s = 1, and l = 0.5.
		// Next find the value of the opposite Hue, i.e., the one that's 180°,
		// or 0.5, away (I'm sure the mathematicians have a more elegant way of
		// doing this, but):
		// Calculate the opposite hue, h2

		float h2 = (float) (h + 0.5);

		if (h2 > 1) {
			h2 -= 1;
		}

		// The HSL value of the complementary colour is now in h2, s, l. So
		// we're ready to convert this back to RGB (again, my PHP version of the
		// EasyRGB.com formula). Note the input and output formats are different
		// this time, see my comments at the top of the code:
		// Input is HSL value of complementary colour, held in h2, s, l as
		// fractions of 1
		// Output is RGB in normal 255 255 255 format, held in r_opposite,
		// g_opposite, b_opposite
		// Hue is converted using function hue_2_rgb, shown at the end of this
		// code

		float r_opposite = 0;
		float g_opposite = 0;
		float b_opposite = 0;

		if (s == 0) {
			r_opposite = l * 255;
			g_opposite = l * 255;
			b_opposite = l * 255;

		} else {
			float var_2 = 0;
			if (l < 0.5) {
				var_2 = l * (1 + s);
			} else {
				var_2 = (l + s) - (s * l);
			}

			float var_1 = 2 * l - var_2;

			r_opposite = 255 * hue_2_rgb(var_1, var_2, h2 + (1 / 3));
			g_opposite = 255 * hue_2_rgb(var_1, var_2, h2);
			b_opposite = 255 * hue_2_rgb(var_1, var_2, h2 - (1 / 3));
		}

		// Function to convert hue to RGB, called from above
		return 0xff000000 + ((int) b_opposite << 16) + ((int) g_opposite << 8)
				+ (int) r_opposite;

	}
	
	public static String getColorNameFromRGB(ColorDrop d) {
		// Turn R, G, and B into a string indicating the color
		
		// 0=red, 1=green, 2=blue
		int c = 0;
		
		// -------------------------------------------------------------
		// Algorithm
		//
		// 1. Determine if red, green, or blue. If no other colors are
		//     set after this point, the color will end up falling
		//     back on one of these three colors.
		// 2. Figure out if black, gray, or white.
		// 3. Look for alternates: purple, yellow, pink, brown, orange.
		// -------------------------------------------------------------
		
		// Calculate Max and base color
		int max = d.R;
		String baseC = "red";
		if (d.G>d.R) {
			max = d.G;
			baseC = "green";
			c = 1;
		}
		if (d.B>max) {
			max = d.B;
			baseC = "blue";
			c = 2;
		}

		// WHITE
		if ((d.R+d.G+d.B) > 650) {
			//return "white";
			return "";
		}

		// BLACK
		if ((d.R+d.G+d.B) < 80) {
			return "";
			//return "black";
		}

		// GRAY
		if ( (Math.abs(d.R-d.G) < 15) && (Math.abs(d.R-d.B) < 15) ) {
			//return "gray";
			return "";
		}
		
		// PURPLE
		// Purple is g < r < b or g < b < r
		if ( ((d.G < d.R) && (d.R < d.B)) || ((d.G < d.B) && (d.B < d.R)) ) {
			//return "purple";
			return "";
		}
		
		// YELLOW
		// r and g are close, blue is much lower. r must be high.
		if ( ((d.R-d.G) < 30) && ((d.G-d.B) > 80) && (d.R > 190) ) {
			//return "yellow";
			return "";
		}
		
		// orange
		// stairstep r > g > b, diff r:g = diff g:b +- n
		// && r > b
		if ((((d.R-d.G) - (d.G-d.B)) < 20) && d.R > d.B) {
			//return "orange";
			return "";
		}
//		
////		// Look for purple - R and B are high, G is low
////		// R::B diff is low
////		if (Math.abs(d.R-d.B) <= 10) {
////			// R::G diff is high
////			if ((d.R-d.G) > 20) {
////				return "purple";
////			}
////		}

		// From base color, look for neighboring colors
		switch(c) {
		case 0:	
			// red
			
			break;
		case 1:	
			// green
			break;
		case 2:	
			// blue
			break;
		};
		return baseC;
	}

	
	
	public float hue_2_rgb(float v1, float v2, float vh) {
		if (vh < 0) {
			vh += 1;
		}

		if (vh > 1) {
			vh -= 1;
		}

		if ((6 * vh) < 1) {
			return (float) (v1 + (v2 - v1) * 6 * vh);
		}

		if ((2 * vh) < 1) {
			return (v2);
		}

		if ((3 * vh) < 2) {
			return (float) (v1 + (v2 - v1) * ((2 / 3 - vh) * 6));
		}

		return (v1);
	}

	public int unsignedByteToInt(byte b) {
		return (int) b & 0xFF;
	}

	public String byteToHex(byte b) {
		int i = b & 0xFF;
		return Integer.toHexString(i);
	}

}
