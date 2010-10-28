package com.osilabs.android.apps.lifedropper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class LifeDropper extends Activity {
	private static final int BUSY = 0;
	private static final int AVAILABLE = 1;
	private static int FRAMEBUFFER_IS = AVAILABLE;
	private static final String TAG = "**** x14d **** >>>>>>>> ";
	Preview preview;
	Button buttonClick;

	// This is the array we pass back from each frame processed.
	private static final int RGB_ELEMENTS = 6;
	private static int[] RGBs = new int[RGB_ELEMENTS];

	// View properties
	protected int view_w = 0;
	protected int view_h = 0;
	protected int yuv_w = 864;
	protected int yuv_h = 576;
	
	protected static int[] decodeBuf;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DrawOnTop mDraw = new DrawOnTop(this);

		setContentView(R.layout.main);

		addContentView(mDraw, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		preview = new Preview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);

		buttonClick = (Button) findViewById(R.id.buttonClick);
		buttonClick.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				preview.camera.takePicture(shutterCallback, rawCallback,
						jpegCallback);
			}
		});

		Log.d(TAG, "onCreate'd");
	}

	@Override
	public void onPause() {
		Log.d(TAG, "onPause'd");

	}

	//
	// Draw on viewer
	//
	private class DrawOnTop extends View {

		public DrawOnTop(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onDraw(Canvas canvas) {
			//
			// FIXME - this is redrawing the decorations with every frame,
			//  should only redraw after a framebuffer has been processed.
			//
			////////Log.d(TAG, "onDraw'd");
			
			int line_len = 30;
			int corner_padding = 20;

			int w = 400;
			int h = 400;
			int center_x = (int) w / 2;
			int center_y = (int) h / 2;
			
			// Text
			//Paint paint = new Paint();
			//paint.setStyle(Paint.Style.FILL);
			//paint.setColor(Color.RED);
			//canvas.drawText("osilabs", 10, 10, paint);
			
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			canvas.drawCircle(center_x, center_y, 5, paint);

			// crosshairs
			canvas.drawLine(center_x, center_y, center_x-line_len, center_y, paint); // left
			canvas.drawLine(center_x, center_y, center_x, center_y+line_len, paint); // top
			canvas.drawLine(center_x, center_y, center_x+line_len, center_y, paint); // right
			canvas.drawLine(center_x, center_y, center_x, center_y-line_len, paint); // bottom

			// corners
			canvas.drawLine(0+corner_padding, 0+corner_padding, 0+corner_padding+line_len, 0+corner_padding, paint); // top-left
			canvas.drawLine(0+corner_padding, 0+corner_padding, 0+corner_padding, 0+corner_padding+line_len, paint); // top-left
			canvas.drawLine(w-corner_padding, 0+corner_padding, w-corner_padding-line_len, 0+corner_padding, paint); // top-right
			canvas.drawLine(w-corner_padding, 0+corner_padding, w-corner_padding, 0+corner_padding+line_len, paint); // top-right
			canvas.drawLine(w-corner_padding, h-corner_padding, w-corner_padding, h-corner_padding-line_len, paint); // bottom-left
			canvas.drawLine(w-corner_padding, h-corner_padding, w-corner_padding-line_len, h-corner_padding, paint); // bottom-left
			canvas.drawLine(0+corner_padding, h-corner_padding, 0+corner_padding+line_len, h-corner_padding, paint); // bottom-right
			canvas.drawLine(0+corner_padding, h-corner_padding, 0+corner_padding, h-corner_padding-line_len, paint); // bottom-right
			
			super.onDraw(canvas);
		}
	}
  
	//
	// Camer shutter
 	//
	
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
				outStream = new FileOutputStream(String.format(
						"/sdcard/%d.jpg", System.currentTimeMillis()));
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

	//
	// Frame previewer
	//
	private class Preview extends SurfaceView implements SurfaceHolder.Callback {

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
			
			// The Surface has been created, acquire the camera and tell it
			// where
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
						
						//Log.d(TAG, "onPreviewFrame called at: "
						//		+ System.currentTimeMillis());
						
						// Discard frames until processing completes from the
						// last
						// processed frame
						if (FRAMEBUFFER_IS == AVAILABLE) {
							new HandleFrameTask().execute(data);
						}

						// Log.d(TAG, "onPreviewFrame called at: " +
						// System.currentTimeMillis());
						Preview.this.invalidate();
					}
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// Called when the holder is destroyed
		public void surfaceDestroyed(SurfaceHolder holder) {
			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's
			// very
			// important to release it when the activity is paused.
			camera.stopPreview();
			camera.release();
			camera = null;
		}

		// Called when holder has changed
		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			Camera.Parameters p = camera.getParameters();
			Camera.Size s = p.getPreviewSize();
			Log.d(TAG, "fmt:" + Integer.toString(p.getPreviewFormat()));
			p.setPictureFormat(ImageFormat.RGB_565);
			//p.setPictureSize(200,200);
			//p.setPreviewSize(200, 200);
			camera.setParameters(p);
			//view_w = this.getWidth();
			//view_h = this.getHeight();
			view_w = s.width;
			view_h = s.height;
			decodeBuf = new int[(yuv_w * yuv_h)];			
			
			List<Camera.Size> ls = p.getSupportedPreviewSizes();
			for (Iterator it = ls.iterator(); it.hasNext(); ) {
				   Camera.Size sz = (Camera.Size)it.next();
				   Log.d(TAG, "prv sz:" + Integer.toString(sz.width) + "," + Integer.toString(sz.height));
			}
			
			ls = p.getSupportedPictureSizes();
			for (Iterator it = ls.iterator(); it.hasNext(); ) {
				   Camera.Size sz = (Camera.Size)it.next();
				   Log.d(TAG, "pic sz:" + Integer.toString(sz.width) + "," + Integer.toString(sz.height));
			}

			camera.startPreview();
		}
		
		public void onPause() {
			camera.release();
			
		}
		
		
// THIS MAY WORK		
//		@Override
//		public void draw(Canvas canvas) {
//			super.draw(canvas);
//			Paint p = new Paint(Color.RED);
//			Log.d(TAG, "draw");
//			canvas.drawText("PREVIEW", canvas.getWidth() / 2,
//					canvas.getHeight() / 2, p);
//		}

	}

	// private class HandleFrameTask extends AsyncTask<byte[], Void, byte[]> {
	private class HandleFrameTask extends AsyncTask<byte[], Void, int[]> {
		// TextView tv = (TextView) findViewById(R.id.preview_text);

		// can use UI thread here
		protected void onPreExecute() {
			FRAMEBUFFER_IS = BUSY;
		}

		@Override
		// protected byte[] doInBackground(byte[]... yuvs) {
		protected int[] doInBackground(byte[]... yuvs) {
//			// automatically done on worker thread (separate from UI thread).
//			// ** don't use UI thread here **
//			//initRGBArray();
//			
//			try {
//				decodeYUV(decodeBuf, yuvs[0], view_w, view_h);
//			}
//			catch(Exception e) {
//				Log.d("EEEEEEEEEEEEEEE ", "Error with decodeYUV");
//			}
//			
			
			Log.d(TAG, "**************** len " + Integer.toString(yuvs[0].length));
			Log.d(TAG, "**************** w " + Integer.toString(view_w));
			Log.d(TAG, "**************** h " + Integer.toString(view_h));
//			int[] iii = {0};
//			iii[0] = averageRGB(getSampleRegion());
//			return iii;
			
			int center = (int)(view_w*view_h)/2;
			int ctr=0;
			int[] iii = {0};
			//Log.d(TAG, "1");
			try {
				//Log.d(TAG, "2");
				YuvImage yi = new YuvImage(yuvs[0], ImageFormat.NV21, view_w, view_h, null);
	
				///Rect r = new Rect(center-2,center+2, center+2,center-2);
				Rect r = new Rect(0,0,view_w-1,view_h-1);
				//Log.d(TAG, "4");
	
				//ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				//Log.d(TAG, "5");
	
				yi.compressToJpeg(r, 80, outStream);
				//Log.d(TAG, "6");
				
				ByteArrayInputStream is = new ByteArrayInputStream(outStream.toByteArray());
				//Log.d(TAG, "7");
			
				Bitmap bit = BitmapFactory.decodeStream(is);
				//Log.d(TAG, "8");
				// This is probably a better way. Figure out how to determine stride.
				//bit.getPixels(decodeBuf, 0, stride, x, y, width, height)
				
				iii[0] = bit.getPixel(view_w/2,view_h/2);
				
				//for(int j=0; j<view_w; j++) {
				//	Log.d(TAG, Integer.toString(j) + "=" + Integer.toString(bit.getPixel(j,view_h/2)));
				//}
				
				//Log.d(TAG, "9");
				//Log.d(TAG, Integer.toHexString(bit.getPixel(center,center)));
				//Log.d(TAG, Integer.toHexString(bit.getPixel(10,10)));
				
				///////Bitmap facePic = Bitmap.createBitmap(bit, 0, 0, 50, 50);
				
			}
			catch(Exception e) {
				Log.d(TAG, "YUV err:" + e.getMessage());
				e.printStackTrace();
			}

			return iii;
			//return decodeBuf;
		}

		private int[] getSampleRegion() {
			// Use a 9 pixel square in the middle of the screen for now
			int center = (int)(view_w*view_h)/2;
			
			int[] region = {
					9, // number elements
					center-view_w-1, center-view_w, center-view_w+1,
					center-1,        center,        center+1,
					center+view_w-1, center+view_w, center+view_w+1
			};
			
			return region;
		}
		
		private int averageRGB(int[] region) {
			int RED = 0; int GRN = 0; int BLU = 0;
			int items = 0;

			for(int i=1; i<=region[0]; i++) {
				RED += decodeBuf[ region[i] ] & 255;
				GRN += (decodeBuf[ region[i] ] >> 8) & 255;
				BLU += (decodeBuf[ region[i] ] >> 16) & 255;
				items++;
			}

			if (items > 0) {
				return 0xff000000	+ (((int)BLU/items) << 16) 
									+ (((int)GRN/items) << 8) 
									+ ((int)RED/items);
			} else {
				return -16777216; // r=0, g=0, b=0
			}
		}

		private void initRGBArray() {
			//Log.d(TAG, "InitRGBArray");
			// initialize RGB
			for (int i = 0; i < RGB_ELEMENTS; i++) {
				RGBs[i] = 0;
			}
		}

		// decode Y, U, and V values on the YUV 420 buffer described as YCbCr_422_SP 
		// by Android David Manpearl 081201 
		protected void decodeYUV(int[] out, byte[] fg, int width, int height)
		        throws NullPointerException, IllegalArgumentException {

			int sz = width * height;
		    
		    if (out == null)
		        throw new NullPointerException("buffer out is null");
		    
		    if (out.length < sz)
		        throw new IllegalArgumentException("buf out size "+out.length+" < minimum "+ sz);

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

		// can use UI thread here
		// protected void onPostExecute(final byte[] rgb_result) {
		protected void onPostExecute(int[] rgb_result) {

			//int pos = (int)(view_w * view_h) / 2;

			//int iRGB = rgb_result[pos+1];
			int iRGB = rgb_result[0];
			//int RED = iRGB & 255;
			//int GRN = (iRGB >> 8) & 255;
			//int BLU = (iRGB >> 16) & 255;
			//int ALP = (iRGB >> 24) & 255;
			int BLU = iRGB & 255;
			int GRN = (iRGB >> 8) & 255;
			int RED = (iRGB >> 16) & 255;
			int ALP = (iRGB >> 24) & 255;
			
			
			//GRN -= 128;
			//if(GRN<0) GRN=0;
			
			String msg = "rgb(" + RED + "," + GRN + "," + BLU + ")";

			TextView tv = (TextView) findViewById(R.id.preview_text);
			tv.setText(msg);
			tv.setBackgroundColor(Color.rgb(RED,GRN,BLU));

			////////////TextView bl_tv = (TextView) findViewById(R.id.bl_display);
			////////////bl_tv.setText("#" + Integer.toHexString(rgb_result[0]).substring(2).toUpperCase());

			Log.d(TAG, "onPostExecute: " + msg);

			FRAMEBUFFER_IS = AVAILABLE;
		}

		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		protected void onPostExecute(Long result) {
			// showDialog("Downloaded " + result + " bytes");
		}


		public int unsignedByteToInt(byte b) {
			return (int) b & 0xFF;
		}

		public String byteToHex(byte b) {
			int i = b & 0xFF;
			return Integer.toHexString(i);
		}
	}
}

//
//
// For handling stopping and starting the activity properly and orientation
// changes
// See http://www.screaming-penguin.com/node/7746
//
//
