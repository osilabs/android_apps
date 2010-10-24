package com.osilabs.android.apps.lifedropper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
	private static final String TAG = "**** x14d **** ";
	Preview preview;
	Button buttonClick;

	// This is the array we pass back from each frame processed.
	private static final int RGB_ELEMENTS = 6;
	private static int[] aRGB = new int[RGB_ELEMENTS];

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
			Log.d(TAG, "onDraw'd");
			
			int center_x = 200;
			int center_y = 200;
			int line_len = 30;
			int corner_padding = 20;
			int w = 400;
			int h = 400;
			
			// Text
			//Paint paint = new Paint();
			//paint.setStyle(Paint.Style.FILL);
			//paint.setColor(Color.RED);
			//canvas.drawText("osilabs", 10, 10, paint);
			
			Paint paint = new Paint();
			paint.setColor(Color.YELLOW);
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
						Log.d(TAG, "onPreviewFrame called at: "
								+ System.currentTimeMillis());
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
			// this.dialog.setMessage("Selecting data...");
			// this.dialog.show();

			FRAMEBUFFER_IS = BUSY;
		}

		@Override
		// protected byte[] doInBackground(byte[]... yuvs) {
		protected int[] doInBackground(byte[]... yuvs) {
			// automatically done on worker thread (separate from UI thread).
			// ** don't use UI thread here **

			// byte[] rgbs = null;
			// if(1==1)return rgbs;

			// initialize RGB
			for (int i = 0; i < RGB_ELEMENTS; i++) {
				aRGB[i] = 0;
			}

			// aRGB = {0,0,0,0,0,0};

			// int logCtr = 1;
			// Log.d(TAG, "Processing the frame ***************************** "
			// + Integer.toString(logCtr++));

			// FIXME
			int width = 400;
			int height = 400;

			// for(int iByte = 0; iByte < data.length; iByte++)
			// (char)data[iByte];

			// TextView tv = (TextView) findViewById(R.id.preview_text);
			// tv.setText ((char) data[0]);
			// the end of the luminance data
			final int lumEnd = width * height;
			// points to the next luminance value pair
			int lumPtr = 0;
			// points to the next chromiance value pair
			int chrPtr = lumEnd;
			// points to the next byte output pair of RGB565 value
			int outPtr = 0;
			// the end of the current luminance scanline
			int lineEnd = width;

			// get just the center pixel
			lumPtr = width/2;
			
			while (true) {

				// skip back to the start of the chromiance values when
				// necessary
				if (lumPtr == lineEnd) {
					if (lumPtr == lumEnd)
						break; // we've reached the end
					// division here is a bit expensive, but's only done once
					// per scanline
					chrPtr = lumEnd + ((lumPtr >> 1) / width) * width;
					lineEnd += width;
				}

				// read the luminance and chromiance values
				final int Y1 = yuvs[0][lumPtr++] & 0xff;
				final int Y2 = yuvs[0][lumPtr++] & 0xff;
				final int Cr = (yuvs[0][chrPtr++] & 0xff) - 128;
				final int Cb = (yuvs[0][chrPtr++] & 0xff) - 128;
				int R, G, B;

				// generate first RGB components
				B = Y1 + ((454 * Cb) >> 8);
				// ///if(B < 0) B = 0; else if(B > 255) B = 255;
				if (B < 0)
					B += 255;
				else if (B > 255)
					B = 255;
				if (B < 0)
					B = 0;
				G = Y1 - ((88 * Cb + 183 * Cr) >> 8);
				// //if(G < 0) G = 0; else if(G > 255) G = 255;
				if (G < 0)
					G += 255;
				else if (G > 255)
					G = 255;
				if (G < 0)
					G = 0;
				R = Y1 + ((359 * Cr) >> 8);
				// //if(R < 0) R = 0; else if(R > 255) R = 255;
				if (R < 0)
					R += 255;
				else if (R > 255)
					R = 255;
				if (R < 0)
					R = 0;
				// NOTE: this assume little-endian encoding
				// rgbs[outPtr++] = (byte) (((G & 0x3c) << 3) | (B >> 3));
				// Log.d(TAG, "WHILE *********** " +
				// Integer.toString(logCtr++));
				// rgbs[outPtr++] = (byte) ((R & 0xf8) | (G >> 5));

				aRGB[0] = R;
				aRGB[1] = G;
				aRGB[2] = B;

				// generate second RGB components
				B = Y2 + ((454 * Cb) >> 8);
				// //if(B < 0) B = 0; else if(B > 255) B = 255;
				if (B < 0)
					B += 255;
				else if (B > 255)
					B = 255;
				if (B < 0)
					B = 0;
				G = Y2 - ((88 * Cb + 183 * Cr) >> 8);
				// //if(G < 0) G = 0; else if(G > 255) G = 255;
				if (G < 0)
					G += 255;
				else if (G > 255)
					G = 255;
				if (G < 0)
					G = 0;
				R = Y2 + ((359 * Cr) >> 8);
				// //if(R < 0) R = 0; else if(R > 255) R = 255;
				if (R < 0)
					R += 255;
				else if (R > 255)
					R = 255;
				if (R < 0)
					R = 0;
				// NOTE: this assume little-endian encoding
				// rgbs[outPtr++] = (byte) (((G & 0x3c) << 3) | (B >> 3));
				// rgbs[outPtr++] = (byte) ((R & 0xf8) | (G >> 5));
				// Log.d(TAG, "WHILE *********** " +
				// Integer.toString(logCtr++));

				aRGB[3] = R;
				aRGB[4] = G;
				aRGB[5] = B;

				// Not safe to access UI thread in here

				// Log.d(TAG, "*** G " + Integer.toString(G));
				break;
			}
			Log.d(TAG, "WHILE END *********** ");
			// return rgbs;
			return aRGB;
		}

		// can use UI thread here
		// protected void onPostExecute(final byte[] rgb_result) {
		protected void onPostExecute(final int[] rgb_result) {

			Log.d(TAG, "onPostExecute:");

			TextView tv = (TextView) findViewById(R.id.preview_text);
			tv.setText(" R:" + Integer.toString(rgb_result[0]) + ","
					+ Integer.toString(rgb_result[3]) + "  G:"
					+ Integer.toString(rgb_result[1]) + ","
					+ Integer.toString(rgb_result[4]) + "  B:"
					+ Integer.toString(rgb_result[2]) + ","
					+ Integer.toString(rgb_result[5]));
			tv.setBackgroundColor(Color.rgb(rgb_result[0], rgb_result[1],
					rgb_result[2]));

			FRAMEBUFFER_IS = AVAILABLE;

			// if (this.dialog.isShowing()) {
			// this.dialog.dismiss();
			// }
			// Main.this.output.setText(result);
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
