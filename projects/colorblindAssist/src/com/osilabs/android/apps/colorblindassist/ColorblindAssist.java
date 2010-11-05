package com.osilabs.android.apps.colorblindassist;

import java.io.IOException;
import com.osilabs.android.apps.colorblindassist.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ColorblindAssist extends Activity {
	//
	// CONSTS
	//
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
	private static final String TAG = "<<< ** osilabs.com ** >>> ";
	
	private static int FRAMEBUFFER_IS = AVAILABLE;
	private static int IS_PAUSING = NO;

	// RGB values are set as they become available.
	private static final int RGB_ELEMENTS = 4;
	private static int[] RGBs = new int[RGB_ELEMENTS];
	private static int RGBint = 0;
	private static String HEXVAL = "000000";
	private static String RGBVAL = "0,0,0";
	
	// Will be allocated based on byte[] size, will grow
	//  if needed.
	private static int[] decodeBuf;
	private static int BUFALLOCSIZE = 0;
	
	// View properties
	private int view_w = 0;
	private int view_h = 0;
	
	// In my testing, a good length/width for a byte[]
	//  size of 497664 is 576x864
	private int yuv_w = 0; 
	private int yuv_h = 0;
	
    String version = "0.1";

	private Preview preview;
	private Button captureButton;
	private DrawOnTop mDraw;
    private PowerManager.WakeLock wl;  

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Log.d(TAG, "onCreate'd");

		// Prevent screen dimming
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");  
	  
		// Default to black
		RGBs[_RED] = 0;
		RGBs[_GRN] = 0;
		RGBs[_BLU] = 0;
		RGBs[_ALP] = 0;

		mDraw = new DrawOnTop(this);

		setContentView(R.layout.main);

		addContentView(mDraw, new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		preview = new Preview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		
		captureButton = (Button) findViewById(R.id.capture_button);
		captureButton.setOnClickListener(new OnClickListener() {
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
                
//                //set up text
//                TextView text = (TextView) dialog.findViewById(R.id.TextView01);
//                text.setText(R.string.drop_color_chosen_message);
 
                //set up image view
//                ImageView img = (ImageView) dialog.findViewById(R.id.ImageView01);
//                img.setImageResource(R.drawable.icon);
 
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
		
		preview.onCreate();
	}

	@Override
	public void onPause() {
		//Log.d(TAG, "onPause'd activity");
		super.onPause();
        wl.release();  
		IS_PAUSING = YES;
		preview.onPause();
	}

	@Override
	public void onResume() {
		//Log.d(TAG, "onResumed'd");
		IS_PAUSING = NO;
		super.onResume();
		wl.acquire();
	}

	public void onRestoreInstanceState() {
		//Log.d(TAG, "OnRestoreInstanceState'd");
	}
	
	//
	// Camera shutter
	//
	
	// Called when shutter is opened
	ShutterCallback shutterCallback = new ShutterCallback() { 
		public void onShutter() {
			//Log.d(TAG, "onShutter'd");
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
			//Log.w(TAG, "OnDraw'd");
			//
			// FIXME - this is redrawing the decorations with every frame,
			//  should only redraw after a framebuffer has been processed.
			// Er .. May need to recalculate w each frame in case of
			//  resizing or orientation changes.
			//
			
			int w = preview.getWidth();
			int h = preview.getHeight();
			
			int line_len = (w/20); //30;
			int corner_padding = 20;
			int cirele_radius = 4;

			int center_x = (int) w / 2;
			int center_y = (int) h / 2;

			try{
					// Text
					Paint paint = new Paint();
					paint.setStyle(Paint.Style.FILL);
					paint.setColor(Color.RED);
					canvas.drawText("osilabs.com", 15, h - 8, paint);

					// FIXME - make a function to convert all these rgbs to and fro
					paint.setColor(Color.rgb(
									255 - RGBs[_RED], 
									255 - RGBs[_GRN],
									255 - RGBs[_BLU]));
					canvas.drawCircle(center_x, center_y, cirele_radius, paint);
		
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
				//Log.d(TAG, "YuvImage error:" + e.getMessage());
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
				//Log.d(TAG, "YuvImage error:" + e.getMessage());
				e.printStackTrace();
			}
		}

	    //@Override
		public void onCreate() {
	    	//Log.d(TAG, "Preview onCreated'd");
	    	
	    	// Coming out of pause
			IS_PAUSING = NO;
	    }

		// Called once the holder is ready
		public void surfaceCreated(SurfaceHolder holder) {
			//Log.d(TAG, "surfaceCreated'd");

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
						//Log.w(TAG, "onPreviewFrame'd");

						// Allocate space for processing buffer. Allow it
						//  to grow if necessary. No max cap.
						int size = data.length;
						if (data.length > BUFALLOCSIZE) {
							// Buffer is not big enough for data
							// allocate more spacs.
							//Log.v(TAG, "Allocating bufer for size: " + Integer.toString(size));
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
							//Log.d(TAG, "Framebuffer available for reuse");
							new HandleFrameTask().execute(data);
						}

						Preview.this.invalidate();
					}
				});
			} catch (IOException e) {
	            camera.release();
	            camera = null;
				e.printStackTrace();
			}
		}

		// Called when holder has changed
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			//Log.d(TAG, "surfaceChange'd");
			try{
				Camera.Parameters p = camera.getParameters();
				Camera.Size s = p.getPreviewSize();
				//p.setPictureFormat(ImageFormat.RGB_565);
				//camera.setParameters(p);
				view_w = s.width;
				view_h = s.height;
				
//				// getsupportedpreviewsizes needs v5
//				if (Build.VERSION.SDK_INT >= 5) {
//		
//					List<Camera.Size> ls = p.getSupportedPreviewSizes();
//					for (Iterator it = ls.iterator(); it.hasNext();) {
//						Camera.Size sz = (Camera.Size) it.next();
//						//Log.d(TAG, "prv sz:" + Integer.toString(sz.width) + ","
//								+ Integer.toString(sz.height));
//					}
//		
//					ls = p.getSupportedPictureSizes();
//					for (Iterator it = ls.iterator(); it.hasNext();) {
//						Camera.Size sz = (Camera.Size) it.next();
//						//Log.d(TAG, "pic sz:" + Integer.toString(sz.width) + ","
//								+ Integer.toString(sz.height));
//					}
//				}
//				
				camera.startPreview();
			} catch (Exception e) {
				// FIXME - put toast errors if this happens
				//Log.d(TAG, "YuvImage error:" + e.getMessage());
				e.printStackTrace();
			}			
		}
		
		// Called when the holder is destroyed
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			//Log.d(TAG, "surfaceDestroyed'd start");

			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's
			// very important to release it when the activity is paused.
			if (camera != null) {
				camera.stopPreview();
			}

			//Log.d(TAG, "surfaceDestroyed'd finish");
		}

		public void onPause() {
			//Log.d(TAG, "onPause'd - preview class");
			
			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's
			// very important to release it when the activity is paused.
			
			if (camera != null) {
				camera.setPreviewCallback(null);
				camera.stopPreview();
				camera.release();
				camera = null;
			}
			
			//Log.d(TAG, "CAMERA RELEASED HERE");
		}
	}

	// private class HandleFrameTask extends AsyncTask<byte[], Void, byte[]> {
	private class HandleFrameTask extends AsyncTask<byte[], Void, int[]> {
		// can use UI thread here
		protected void onPreExecute() {
			//Log..d(TAG, "OnPreExecute'd");
			if (IS_PAUSING == YES) {
				finish();
			}
			FRAMEBUFFER_IS = BUSY;
		}

		@Override
		protected int[] doInBackground(byte[]... yuvs) {
			//Log.d(TAG, "doInBackground time=" + System.currentTimeMillis());

			int[] iii = { 0 };

			int center = (int) ((view_w * view_h) / 2) + (view_w / 2);
			try {
				ImageProcessing.decodeYUV(decodeBuf, yuvs[0], view_w, view_h);
			} catch (Exception e) {
				//Log.e(TAG, "decodeYUV error: " + e.getMessage());
				e.printStackTrace();
			}
			iii[0] = decodeBuf[center];

			return iii;
		}

		// can use UI thread here
		// protected void onPostExecute(final byte[] rgb_result) {
		protected void onPostExecute(int[] iRGB) {
			//Log.d(TAG, "doinbackground onPostExecute'd" + System.currentTimeMillis());

			// Allow the next frame to be processed
			FRAMEBUFFER_IS = AVAILABLE;
			
			// Don't process the result of the async task if pausing
			if (IS_PAUSING == NO) {
				// Set global int
				RGBint = iRGB[0];

				// Set global reg green and blue
				RGBs[_ALP] = (iRGB[0] >> 24) & 255;
				RGBs[_RED] = (iRGB[0] >> 16) & 255;
				RGBs[_GRN] = (iRGB[0] >> 8) & 255;
				RGBs[_BLU] = iRGB[0] & 255;
	
				// Set global rgb value for display
				RGBVAL = RGBs[_RED] + "," + RGBs[_GRN] + "," + RGBs[_BLU];

				// Set global hexval
				HEXVAL = Integer.toHexString(iRGB[0]).substring(2).toUpperCase();

				// Make display string for previewer
				String rgbDisplay= "rgb(" + RGBVAL + ")";
	
				// Set drop color
				TextView tv = (TextView) findViewById(R.id.preview_text);
				tv.setText(rgbDisplay);
				tv.setBackgroundColor(iRGB[0]);

				// Set capture button color
				captureButton = (Button) findViewById(R.id.capture_button);
				//captureButton.setBackgroundColor(Color.rgb(200,200,230));
				//captureButton.setError("error x14d");
				captureButton.setHapticFeedbackEnabled(true);
				TextView bl_tv = (TextView) findViewById(R.id.color_value_display);
				bl_tv.setText("#" + HEXVAL);
			}
		}

//		protected void onProgressUpdate(Integer... progress) {
//			// setProgressPercent(progress[0]);
//		}
//
//		protected void onPostExecute(Long result) {
//			// showDialog("Downloaded " + result + " bytes");
//		}
	}
	
	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
	    //menu.add(0, MENU_PREFS, 0, "Preferences");
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
		        //
		        // Set locals with manifest variables
		        //
		        PackageInfo pInfo = null;
				try {
					pInfo = getPackageManager().getPackageInfo("com.osilabs.android.apps.colorblindassist",
							PackageManager.GET_META_DATA);
					version = pInfo.versionName;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
		        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		        alertDialog.setTitle(R.string.app_name);
		        alertDialog.setMessage("You are running version " + version);
		        alertDialog.setButton(this.getString(R.string.sample_button),
		        		new DialogInterface.OnClickListener() {
		        	public void onClick(DialogInterface dialog, int which) {
		        		Intent mIntent = new Intent(Intent.ACTION_VIEW, 
		        				Uri.parse("http://osilabs.com/m/mobilecontent/colorblindassist/about.php#" + version)); 
        				startActivity(mIntent); 
		            } 
		        });
		        alertDialog.setIcon(R.drawable.icon);
		        alertDialog.show();
		    	return true;

		    case MENU_QUIT:
		        finish();
		        return true;
	    }
	    return false;
	}

}