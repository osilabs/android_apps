package com.osilabs.android.apps.lifedropper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

//import com.osilabs.android.apps.R;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LifeDropper extends Activity {
  private static final String TAG = "CameraDemo";
  Preview preview;
  Button buttonClick;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    preview = new Preview(this);
    ((FrameLayout) findViewById(R.id.preview)).addView(preview);

    buttonClick = (Button) findViewById(R.id.buttonClick);
    buttonClick.setOnClickListener(new OnClickListener() {
      public void onClick(View v) {
        preview.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
      }
    });

    Log.d(TAG, "onCreate'd");
  }

  // Called when shutter is opened
  ShutterCallback shutterCallback = new ShutterCallback() { 
    public void onShutter() {
      Log.d(TAG, "onShutter'd");
    }
  };

  // Handles data for raw picture
  PictureCallback rawCallback = new PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
      Log.d(TAG, "onPictureTaken - raw");
    }
  };

  // Handles data for jpeg picture
  PictureCallback jpegCallback = new PictureCallback() {
    public void onPictureTaken(byte[] data, Camera camera) {
      FileOutputStream outStream = null;
      try {
        // Write to SD Card
        outStream = new FileOutputStream(String.format("/sdcard/%d.jpg",
            System.currentTimeMillis()));
        outStream.write(data);
        outStream.close();
        Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
      }
      Log.d(TAG, "onPictureTaken - jpeg");
    }
  };
  
  private class Preview extends SurfaceView implements SurfaceHolder.Callback {
	  private static final String TAG = "Preview";

	  SurfaceHolder mHolder;
	  public Camera camera;

	  Preview(Context context) {
	    super(context);

	    // Install a SurfaceHolder.Callback so we get notified when the
	    // underlying surface is created and destroyed.
	    mHolder = getHolder();
	    mHolder.addCallback(this);
	    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	  }

	  // Called once the holder is ready
	  public void surfaceCreated(SurfaceHolder holder) {
	    // The Surface has been created, acquire the camera and tell it where
	    // to draw.
	    camera = Camera.open();
	    try {
	      camera.setPreviewDisplay(holder);

	      camera.setPreviewCallback(new PreviewCallback() {
	        // Called for each frame previewed
	        public void onPreviewFrame(byte[] data, Camera camera) {
	        	// processing the byte array:
	        	// http://code.google.com/p/android/issues/detail?id=823
	        	//

	        	new HandleFrameTask().execute(data);

	        	Log.d(TAG, "onPreviewFrame called at: " + System.currentTimeMillis());
	        	Preview.this.invalidate();
	        }
	      });
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	  // Called when the holder is destroyed
	  public void surfaceDestroyed(SurfaceHolder holder) {
	    camera.stopPreview();
	    camera = null;
	  }

	  // Called when holder has changed
	  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
	    camera.startPreview();
	  }

	}



	private class HandleFrameTask extends AsyncTask<byte[], Void, byte[]> {
		//TextView tv = (TextView) findViewById(R.id.preview_text);
		
		
		
		// can use UI thread here
		protected void onPreExecute() {
			//this.dialog.setMessage("Selecting data...");
			//this.dialog.show();
		}
		
		@Override
		protected byte[] doInBackground(byte[]... yuvs) {
			// automatically done on worker thread (separate from UI thread).
			// ** don't use UI thread here **
		      
			Log.d(TAG, "Processing the frame *****************************");
		      
			// FIXME
			int width = 176;
			int height = 144;
			
			//for(int iByte = 0; iByte < data.length; iByte++)
			//	(char)data[iByte];

			//TextView tv = (TextView) findViewById(R.id.preview_text);
			//tv.setText ((char) data[0]);
			  //the end of the luminance data
		    final int lumEnd = width * height;
		    //points to the next luminance value pair
		    int lumPtr = 0;
		    //points to the next chromiance value pair
		    int chrPtr = lumEnd;
		    //points to the next byte output pair of RGB565 value
		    int outPtr = 0;
		    //the end of the current luminance scanline
		    int lineEnd = width;
		    
		    byte[] rgbs = null;
		    
		    while (true) {

		        //skip back to the start of the chromiance values when necessary
		        if (lumPtr == lineEnd) {
		            if (lumPtr == lumEnd) break; //we've reached the end
		            //division here is a bit expensive, but's only done once per scanline
		            chrPtr = lumEnd + ((lumPtr  >> 1) / width) * width;
		            lineEnd += width;
		        }

		        //read the luminance and chromiance values
		        final int Y1 = yuvs[0][lumPtr++] & 0xff; 
		        final int Y2 = yuvs[0][lumPtr++] & 0xff; 
		        final int Cr = (yuvs[0][chrPtr++] & 0xff) - 128; 
		        final int Cb = (yuvs[0][chrPtr++] & 0xff) - 128;
		        int R, G, B;

		        //generate first RGB components
		        B = Y1 + ((454 * Cb) >> 8);
		        if(B < 0) B = 0; else if(B > 255) B = 255; 
		        G = Y1 - ((88 * Cb + 183 * Cr) >> 8); 
		        if(G < 0) G = 0; else if(G > 255) G = 255; 
		        R = Y1 + ((359 * Cr) >> 8); 
		        if(R < 0) R = 0; else if(R > 255) R = 255; 
		        //NOTE: this assume little-endian encoding
		        rgbs[outPtr++]  = (byte) (((G & 0x3c) << 3) | (B >> 3));
		        rgbs[outPtr++]  = (byte) ((R & 0xf8) | (G >> 5));

		        //generate second RGB components
		        B = Y2 + ((454 * Cb) >> 8);
		        if(B < 0) B = 0; else if(B > 255) B = 255; 
		        G = Y2 - ((88 * Cb + 183 * Cr) >> 8); 
		        if(G < 0) G = 0; else if(G > 255) G = 255; 
		        R = Y2 + ((359 * Cr) >> 8); 
		        if(R < 0) R = 0; else if(R > 255) R = 255; 
		        //NOTE: this assume little-endian encoding
		        rgbs[outPtr++]  = (byte) (((G & 0x3c) << 3) | (B >> 3));
		        rgbs[outPtr++]  = (byte) ((R & 0xf8) | (G >> 5));
		        
		        // Not safe to access UI thread in here
		    }
			return rgbs;
		}
		
		public int unsignedByteToInt(byte b) {
			return (int) b & 0xFF;
		}
		
		public String byteToHex(byte b){
			int i = b & 0xFF;
			return Integer.toHexString(i);
		}
		
		// can use UI thread here
		protected void onPostExecute(final byte[] rgb_result) {
			
			//int r = unsignedByteToInt(rgb_result[0]);
			  //final TextView tv = (TextView) findViewById(R.id.preview_text);

			TextView tv = (TextView) findViewById(R.id.preview_text);
			tv.setBackgroundColor(rgb_result[0]);
			
			//TextView et = new TextView(activity);
			//et.setText("350");
			//et.setBackgroundColor(R.color.white);
			
			//if (this.dialog.isShowing()) {
			//	this.dialog.dismiss();
			//}
			//Main.this.output.setText(result);
		}

		protected void onProgressUpdate(Integer... progress) {
	        //setProgressPercent(progress[0]);
	    }

	    protected void onPostExecute(Long result) {
	        //showDialog("Downloaded " + result + " bytes");
	    }
	}

}

//
//
// For handling stopping and starting the activity properly and orientation changes
// See http://www.screaming-penguin.com/node/7746
//
//
