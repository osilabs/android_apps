//package com.osilabs.android.apps.lifedropper;
//
//import com.osilabs.android.apps.lifedropper.R;
//import android.os.AsyncTask;
//import android.widget.TextView;
////import android.widget.TextView;
//
//class HandleFrameTask extends AsyncTask<byte[], Void, byte[]> {
//	//TextView tv = (TextView) findViewById(R.id.preview_text);
//	
//	
//	
//	// can use UI thread here
//	protected void onPreExecute() {
//		//this.dialog.setMessage("Selecting data...");
//		//this.dialog.show();
//	}
//	
//	@Override
//	protected byte[] doInBackground(byte[]... yuvs) {
//		// automatically done on worker thread (separate from UI thread).
//		// ** don't use UI thread here **
//		
//		// FIXME
//		int width = 176;
//		int height = 144;
//		
//		//for(int iByte = 0; iByte < data.length; iByte++)
//		//	(char)data[iByte];
//
//		//TextView tv = (TextView) findViewById(R.id.preview_text);
//		//tv.setText ((char) data[0]);
//		  //the end of the luminance data
//	    final int lumEnd = width * height;
//	    //points to the next luminance value pair
//	    int lumPtr = 0;
//	    //points to the next chromiance value pair
//	    int chrPtr = lumEnd;
//	    //points to the next byte output pair of RGB565 value
//	    int outPtr = 0;
//	    //the end of the current luminance scanline
//	    int lineEnd = width;
//	    
//	    byte[] rgbs = null;
//	    
//	    while (true) {
//
//	        //skip back to the start of the chromiance values when necessary
//	        if (lumPtr == lineEnd) {
//	            if (lumPtr == lumEnd) break; //we've reached the end
//	            //division here is a bit expensive, but's only done once per scanline
//	            chrPtr = lumEnd + ((lumPtr  >> 1) / width) * width;
//	            lineEnd += width;
//	        }
//
//	        //read the luminance and chromiance values
//	        final int Y1 = yuvs[0][lumPtr++] & 0xff; 
//	        final int Y2 = yuvs[0][lumPtr++] & 0xff; 
//	        final int Cr = (yuvs[0][chrPtr++] & 0xff) - 128; 
//	        final int Cb = (yuvs[0][chrPtr++] & 0xff) - 128;
//	        int R, G, B;
//
//	        //generate first RGB components
//	        B = Y1 + ((454 * Cb) >> 8);
//	        if(B < 0) B = 0; else if(B > 255) B = 255; 
//	        G = Y1 - ((88 * Cb + 183 * Cr) >> 8); 
//	        if(G < 0) G = 0; else if(G > 255) G = 255; 
//	        R = Y1 + ((359 * Cr) >> 8); 
//	        if(R < 0) R = 0; else if(R > 255) R = 255; 
//	        //NOTE: this assume little-endian encoding
//	        rgbs[outPtr++]  = (byte) (((G & 0x3c) << 3) | (B >> 3));
//	        rgbs[outPtr++]  = (byte) ((R & 0xf8) | (G >> 5));
//
//	        //generate second RGB components
//	        B = Y2 + ((454 * Cb) >> 8);
//	        if(B < 0) B = 0; else if(B > 255) B = 255; 
//	        G = Y2 - ((88 * Cb + 183 * Cr) >> 8); 
//	        if(G < 0) G = 0; else if(G > 255) G = 255; 
//	        R = Y2 + ((359 * Cr) >> 8); 
//	        if(R < 0) R = 0; else if(R > 255) R = 255; 
//	        //NOTE: this assume little-endian encoding
//	        rgbs[outPtr++]  = (byte) (((G & 0x3c) << 3) | (B >> 3));
//	        rgbs[outPtr++]  = (byte) ((R & 0xf8) | (G >> 5));
//	        
//	        // Not safe to access UI thread in here
//	    }
//		return rgbs;
//	}
//	
//	public static int unsignedByteToInt(byte b) {
//		return (int) b & 0xFF;
//	}
//	
//	public static String byteToHex(byte b){
//		int i = b & 0xFF;
//		return Integer.toHexString(i);
//	}
//	
//	// can use UI thread here
//	protected void onPostExecute(final byte[] rgb_result) {
//		
//		//int r = unsignedByteToInt(rgb_result[0]);
//		  //final TextView tv = (TextView) findViewById(R.id.preview_text);
//
//		TextView tv = (TextView) findViewById(R.id.preview_text);
//		tv.setBackgroundColor(rgb_result[0]);
//		
//		//TextView et = new TextView(activity);
//		//et.setText("350");
//		//et.setBackgroundColor(R.color.white);
//		
//		//if (this.dialog.isShowing()) {
//		//	this.dialog.dismiss();
//		//}
//		//Main.this.output.setText(result);
//	}
//
//	protected void onProgressUpdate(Integer... progress) {
//        //setProgressPercent(progress[0]);
//    }
//
//    protected void onPostExecute(Long result) {
//        //showDialog("Downloaded " + result + " bytes");
//    }
//}
