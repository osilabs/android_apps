package com.osilabs.android.apps.lifedropper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class LifeDropper extends Activity {

	private static final int BUSY = 0;
	private static final int AVAILABLE = 1;
	private static final int YES = 0;
	private static final int NO = 1;
	private static final int _RED = 0;
	private static final int _GRN = 1;
	private static final int _BLU = 2;
	private static final int _ALP = 3; // alpha
	private static final int MENU_PREFS = 0;
	private static final int MENU_ABOUT = 1;
	private static final int MENU_QUIT = 2;
	
	private static int FRAMEBUFFER_IS = AVAILABLE;
	private static int IS_PAUSING = NO;
	private static final String TAG = "<<< ** osilabs.com ** >>> ";

	// This is the array we pass back from each frame processed.
	private static final int RGB_ELEMENTS = 4;
	private static int[] RGBs = new int[RGB_ELEMENTS];
	private static int RGBint = 0;
	private static int BUFALLOCSIZE = 0;
	private static String HEXVAL = "000000";
	private static String RGBVAL = "0,0,0";
	
	protected static int[] decodeBuf;

	// View properties
	protected int view_w = 0;
	protected int view_h = 0;
	protected int yuv_w = 0; //864;
	protected int yuv_h = 0; //576;	

	private Preview preview;
	//public Camera camera;
	Button buttonClick;
	DrawOnTop mDraw;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Default to black
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
                //set up dialog
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.drop);
                dialog.setTitle("Your drop is #" + HEXVAL);
                dialog.setCancelable(true);

                //there are a lot of settings, for dialog, check them all out!
                //set up text
                TextView drop = (TextView) dialog.findViewById(R.id.drop_textview);
                drop.setBackgroundColor(Color.rgb(RGBs[_RED], RGBs[_GRN], RGBs[_BLU]));
                
                //set up text
                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
                text.setText(R.string.drop_color_chosen_message);
 
                //set up image view
                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
                img.setImageResource(R.drawable.icon);
 
                //set up button
                Button button = (Button) dialog.findViewById(R.id.Button01);
                button.setOnClickListener(new OnClickListener() {
                @Override
                    public void onClick(View vc) {
                		dialog.cancel();
                    }
                });
                //now that the dialog is set up, it's time to show it    
                dialog.show();
			}
		});
	}

	//@Override
	public void onPause() {
		Log.d(TAG, "onPause'd activity");
		super.onPause();
		preview.onPause();
		//camera.stopPreview();
		//camera.release();
		IS_PAUSING = YES;
	}

	@Override
	public void onResume() {
		//Log.d(TAG, "onResumed'd");
		super.onResume();
		//preview.onResume();
		//camera.open();
	}

	public void onRestoreInstanceState() {
		Log.d(TAG, "OnRestoreInstanceState'd");
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

			try{
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
			} catch (Exception e) {
				// FIXME - put toast errors if this happens
				Log.d(TAG, "YuvImage error:" + e.getMessage());
				e.printStackTrace();
			}

			super.onDraw(canvas);
		}
	}

	//
	// Frame previewer
	//
	private class Preview extends SurfaceView implements SurfaceHolder.Callback {

		SurfaceHolder mHolder;
		public Camera camera;

		Preview(Context context) {
			super(context);
			
			try {
				// Install a SurfaceHolder.Callback so we get notified when the
				// underlying surface is created and destroyed.
				mHolder = getHolder();
				mHolder.addCallback(this);
				mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			} catch (Exception e) {
				// FIXME - put toast errors if this happens
				Log.d(TAG, "YuvImage error:" + e.getMessage());
				e.printStackTrace();
			}
		}

		// Called once the holder is ready
		public void surfaceCreated(SurfaceHolder holder) {

			// The Surface has been created, acquire the camera and tell it
			// where to draw.
			camera = Camera.open();
			try {
				camera.setPreviewDisplay(holder);
				camera.setPreviewCallback(new PreviewCallback() {
					//
					// FIXME - Take these callbacks out of inline. Read
					//          somewhere this was an efficiency problem
					//
					// Called for each frame previewed
					public void onPreviewFrame(byte[] data, Camera camera) {

						// Allocate space for processing buffer. Allow it
						//  to grow if necessary. No max cap.
						int size = data.length;
						if (data.length > BUFALLOCSIZE) {
							// Buffer is not big enough for data
							// allocate more spacs.
							Log.v(TAG, "Allocating bufer for size: " + Integer.toString(size));
							decodeBuf = new int[size];
							BUFALLOCSIZE = size;
							
							// Set the w and h for the yuv image processing. 
							// Don't need an actual picture dimension because
							//  I am not doing anything with the converted
							//  rgb image except grabbing a pixel. Set dims
							//  to just a single row.
							yuv_w = size;
							yuv_h = 1;
						}
						
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
			//camera = null;
			camera.release();
		}

		// Called when holder has changed
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			try{
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
				//decodeBuf = new int[(yuv_w * yuv_h)];
				
				// getsupportedpreviewsizes needs v5
				if (Build.VERSION.SDK_INT >= 5) {
		
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
				}
				
				camera.startPreview();
			} catch (Exception e) {
				// FIXME - put toast errors if this happens
				Log.d(TAG, "YuvImage error:" + e.getMessage());
				e.printStackTrace();
			}
		}

		
		//@Override
		public void onPause() {
			Log.d(TAG, "onPause'd - preview class");
			//super.onPause();
			//preview.pause();
			camera.stopPreview();
			camera.release();
		}
		
	}

	// private class HandleFrameTask extends AsyncTask<byte[], Void, byte[]> {
	private class HandleFrameTask extends AsyncTask<byte[], Void, int[]> {
		// can use UI thread here
		protected void onPreExecute() {
			FRAMEBUFFER_IS = BUSY;
		}

		@Override
		protected int[] doInBackground(byte[]... yuvs) {
//			Log.d(TAG, "**************** len "
//					+ Integer.toString(yuvs[0].length));
//			Log.d(TAG, "**************** w " + Integer.toString(view_w));
//			Log.d(TAG, "**************** h " + Integer.toString(view_h));

			int[] iii = { 0 };

			//Log.d(TAG, "onPreviewFrame called at: "
			//		+ System.currentTimeMillis());

			// YuvImage() needs min sdk version 8
			if (Build.VERSION.SDK_INT >= 8) {
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
			// Allow the next frame to be processed
			FRAMEBUFFER_IS = AVAILABLE;
			
			// Don't process the result of the async task if pausing
			if (IS_PAUSING == NO) {
				// Set global int
				RGBint = iRGB[0];
				
				// Set global hexval
				HEXVAL = Integer.toHexString(iRGB[0]).substring(2).toUpperCase();

				// Set global reg green and blue
				RGBs[_ALP] = (iRGB[0] >> 24) & 255;
				RGBs[_RED] = (iRGB[0] >> 16) & 255;
				RGBs[_GRN] = (iRGB[0] >> 8) & 255;
				RGBs[_BLU] = iRGB[0] & 255;
	
				// Set global rgb value for display
				RGBVAL = RGBs[_RED] + "," + RGBs[_GRN] + "," + RGBs[_BLU];

				// Make display string for previewer
				String msg = "rgb(" + RGBVAL + ")";
	
				TextView tv = (TextView) findViewById(R.id.preview_text);
				tv.setText(msg);
				tv.setBackgroundColor(iRGB[0]);
	
				TextView bl_tv = (TextView) findViewById(R.id.bl_display);
				bl_tv.setText("#" + HEXVAL);
			}
		}

		protected void onProgressUpdate(Integer... progress) {
			// setProgressPercent(progress[0]);
		}

		protected void onPostExecute(Long result) {
			// showDialog("Downloaded " + result + " bytes");
		}
	}
	
	
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    menu.add(0, MENU_PREFS, 0, "Preferences");
	    menu.add(0, MENU_ABOUT, 0, "About");
	    menu.add(0, MENU_QUIT, 0, "Quit");
	    
	    return true;
	}

	
	
	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		        
		    case MENU_PREFS:
		    	Toast.makeText(getApplicationContext(), "Prefs", Toast.LENGTH_SHORT).show();
		    	//Intent intent = new Intent()
		    	//	.setClass(this, com.osilabs.android.apps.Prefs.class);
		    	//this.startActivityForResult(intent, 0);
		    	return true;

			case MENU_ABOUT:
		        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		        alertDialog.setTitle(R.string.sample_title);
		        alertDialog.setMessage("You are running version " + Build.VERSION.RELEASE);
		        alertDialog.setButton(this.getString(R.string.sample_button),
		        		new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		Intent mIntent = new Intent(Intent.ACTION_VIEW, 
		        				Uri.parse("http://osilabs.com/m/mobilecontent/livedropper/about.php#" + Build.VERSION.RELEASE)); 
        				startActivity(mIntent); 
		            } 
		        });
		        //alertDialog.setIcon(R.drawable.ic_launcher_main);
		        alertDialog.show();
		    	return true;

		    case MENU_QUIT:
		        finish();
		        return true;
	    }
	    return false;
	}

}

//
//
// For handling stopping and starting the activity properly and orientation
// changes
// See http://www.screaming-penguin.com/node/7746
//
//
