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
import android.os.Build;
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
	private static final int _RED = 0;
	private static final int _GRN = 1;
	private static final int _BLU = 2;
	private static final int _ALP = 3; // alpha

	private static int FRAMEBUFFER_IS = AVAILABLE;
	private static final String TAG = "**** x14d **** >>>>>>>> ";

	// This is the array we pass back from each frame processed.
	private static final int RGB_ELEMENTS = 4;
	private static int[] RGBs = new int[RGB_ELEMENTS];

	protected static int[] decodeBuf;

	// View properties
	protected int view_w = 0;
	protected int view_h = 0;
	protected int yuv_w = 864;
	protected int yuv_h = 576;

	Preview preview;
	Button buttonClick;
	DrawOnTop mDraw;

	public int toggle = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Default to black ... sounds like a metallica song
		RGBs[_RED] = 0;
		RGBs[_GRN] = 0;
		RGBs[_BLU] = 0;
		RGBs[_ALP] = 0;

		mDraw = new DrawOnTop(this);

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
		}

		@Override
		protected void onDraw(Canvas canvas) {
			//
			// FIXME - this is redrawing the decorations with every frame,
			// should only redraw after a framebuffer has been processed.
			//

			int line_len = 30;
			int corner_padding = 20;

			// FIXME !!! The view dimensions must be dynamically determined
			int w = 400;
			int h = 400;
			int center_x = (int) w / 2;
			int center_y = (int) h / 2;

			// Text
			Paint paint = new Paint();
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.RED);
			canvas.drawText("osilabs", 10, h - 8, paint);

			// Paint paint = new Paint();

			// FIXME - make a function to convert all these rgbs to and fro
			paint.setColor(Color.rgb(255 - RGBs[_RED], 255 - RGBs[_GRN],
					255 - RGBs[_BLU]));
			canvas.drawCircle(center_x, center_y, 5, paint);

			// crosshairs
			canvas.drawLine(center_x, center_y, center_x - line_len, center_y,
					paint); // left
			canvas.drawLine(center_x, center_y, center_x, center_y + line_len,
					paint); // top
			canvas.drawLine(center_x, center_y, center_x + line_len, center_y,
					paint); // right
			canvas.drawLine(center_x, center_y, center_x, center_y - line_len,
					paint); // bottom

			// corners
			canvas.drawLine(0 + corner_padding, 0 + corner_padding, 0
					+ corner_padding + line_len, 0 + corner_padding, paint); // top-left
			canvas.drawLine(0 + corner_padding, 0 + corner_padding,
					0 + corner_padding, 0 + corner_padding + line_len, paint); // top-left
			canvas.drawLine(w - corner_padding, 0 + corner_padding, w
					- corner_padding - line_len, 0 + corner_padding, paint); // top-right
			canvas.drawLine(w - corner_padding, 0 + corner_padding, w
					- corner_padding, 0 + corner_padding + line_len, paint); // top-right
			canvas.drawLine(w - corner_padding, h - corner_padding, w
					- corner_padding, h - corner_padding - line_len, paint); // bottom-left
			canvas.drawLine(w - corner_padding, h - corner_padding, w
					- corner_padding - line_len, h - corner_padding, paint); // bottom-left
			canvas.drawLine(0 + corner_padding, h - corner_padding, 0
					+ corner_padding + line_len, h - corner_padding, paint); // bottom-right
			canvas.drawLine(0 + corner_padding, h - corner_padding,
					0 + corner_padding, h - corner_padding - line_len, paint); // bottom-right

			super.onDraw(canvas);
		}
	}

	//
	// Camera shutter
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
			// where to draw.
			camera = Camera.open();
			try {
				camera.setPreviewDisplay(holder);
				camera.setPreviewCallback(new PreviewCallback() {
					// Called for each frame previewed
					public void onPreviewFrame(byte[] data, Camera camera) {

						// Discard frames until processing completes
						if (FRAMEBUFFER_IS == AVAILABLE) {
							new HandleFrameTask().execute(data);
						}

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
			// very important to release it when the activity is paused.
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
			// p.setPictureSize(200,200);
			// p.setPreviewSize(200, 200);
			camera.setParameters(p);
			// view_w = this.getWidth();
			// view_h = this.getHeight();
			view_w = s.width;
			view_h = s.height;
			decodeBuf = new int[(yuv_w * yuv_h)];

			List<Camera.Size> ls = p.getSupportedPreviewSizes();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				Camera.Size sz = (Camera.Size) it.next();
				Log.d(TAG, "prv sz:" + Integer.toString(sz.width) + ","
						+ Integer.toString(sz.height));
			}

			ls = p.getSupportedPictureSizes();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				Camera.Size sz = (Camera.Size) it.next();
				Log.d(TAG, "pic sz:" + Integer.toString(sz.width) + ","
						+ Integer.toString(sz.height));
			}

			camera.startPreview();
		}

		public void onPause() {
			camera.release();
			Log.d(TAG, "Preview onPause'd:");
		}
	}

	// private class HandleFrameTask extends AsyncTask<byte[], Void, byte[]> {
	private class HandleFrameTask extends AsyncTask<byte[], Void, int[]> {
		// TextView tv = (TextView) findViewById(R.id.preview_text);

		// can use UI thread here
		protected void onPreExecute() {
			FRAMEBUFFER_IS = BUSY;
		}

		@Override
		protected int[] doInBackground(byte[]... yuvs) {
			Log.d(TAG, "**************** len "
					+ Integer.toString(yuvs[0].length));
			Log.d(TAG, "**************** w " + Integer.toString(view_w));
			Log.d(TAG, "**************** h " + Integer.toString(view_h));

			int[] iii = { 0 };

			Log.d(TAG, "onPreviewFrame called at: "
					+ System.currentTimeMillis());

			// YuvImage() needs min sdk version 8
			if (Build.VERSION.SDK_INT >= 8) {
				Log.d(TAG, "New algorithm");
				try {
					YuvImage yi = new YuvImage(yuvs[0], ImageFormat.NV21,
							view_w, view_h, null);
					Rect r = new Rect(0, 0, view_w - 1, view_h - 1);
					ByteArrayOutputStream outStream = new ByteArrayOutputStream();
					yi.compressToJpeg(r, 80, outStream);
					ByteArrayInputStream is = new ByteArrayInputStream(
							outStream.toByteArray());
					Bitmap bit = BitmapFactory.decodeStream(is);
					iii[0] = bit.getPixel(view_w / 2, view_h / 2);
				} catch (Exception e) {
					// FIXME - put toast errors if this happens
					Log.d(TAG, "YuvImage error:" + e.getMessage());
					e.printStackTrace();
				}

			} else {
				Log.d(TAG, "Pre v 8 algorithm");
				int center = (int) ((view_w * view_h) / 2) + (view_w / 2);
				try {
					ImageProcessing.decodeYUV(decodeBuf, yuvs[0], view_w,
							view_h);
				} catch (Exception e) {
					Log.d("EEEEEEEEEEEEEEE ", "Error with decodeYUV");
				}
				iii[0] = decodeBuf[center];
			}

			return iii;
		}

		// can use UI thread here
		// protected void onPostExecute(final byte[] rgb_result) {
		protected void onPostExecute(int[] iRGB) {

			RGBs[_ALP] = (iRGB[0] >> 24) & 255;
			RGBs[_RED] = (iRGB[0] >> 16) & 255;
			RGBs[_GRN] = (iRGB[0] >> 8) & 255;
			RGBs[_BLU] = iRGB[0] & 255;

			String msg = "rgb(" + RGBs[_RED] + "," + RGBs[_GRN] + ","
					+ RGBs[_BLU] + ")";

			TextView tv = (TextView) findViewById(R.id.preview_text);
			tv.setText(msg);
			tv
					.setBackgroundColor(Color.rgb(RGBs[_RED], RGBs[_GRN],
							RGBs[_BLU]));

			TextView bl_tv = (TextView) findViewById(R.id.bl_display);
			bl_tv.setText("#"
					+ Integer.toHexString(iRGB[0]).substring(2).toUpperCase());

			// Allow the next frame to be processed
			FRAMEBUFFER_IS = AVAILABLE;
		}

		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		protected void onPostExecute(Long result) {
			// showDialog("Downloaded " + result + " bytes");
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
