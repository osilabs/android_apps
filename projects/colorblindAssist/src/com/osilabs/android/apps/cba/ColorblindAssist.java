/**
 * ColorBlind Assist
 * 
 * Terminology:
 * METER - Each box or r g and b the move in real time to display the level of the color
 * HUD   - Heads up Display box with meters, color output, etc...
 * ----------------------------------------------------------------------------
 * // Next Version
 * - About screen
 * - LD icon
 * - Work on accuracy of colors
 * - Shift meters to center
 * FIXME - Detect cameras and don't allow to run if don't have cameras.
 * ----------------------------------------------------------------------------
 * TODO - Rename to Super ColorVision, or ColorVisionAssist
 * TODO - Move color bars to bottom of screen
 * TODO - Tweak the color so they are more right-ish.
 * TODO - Unit tests for colors
 * TODO - Choose different threashold modes. course = only r g and b diffs, fine uses more colors.
 * TODO - Choose different colorblindness orientations. Red/green, pink/blue.
 * TODO - Vertical orientation option
 * TODO - left and right handedness options.
 * TODO - Camera shutter button pauses meters
 * TODO - Bat saver mode stops processing frames if it doesn't detect movement for 30 seconds.
 * ----------------------------------------------------------------------------
 * OPTIMIZE - Create all the paint in onCreate. Do any static calculations there too.
 * OPTIMIZE - color_value_display is finding and setting values with each frame.
 * ----------------------------------------------------------------------------
 */


package com.osilabs.android.apps.cba;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.osilabs.android.lib.osicolor.ColorDrop;
import com.osilabs.android.lib.osicolor.OsiColor;

public class ColorblindAssist extends Activity {
	//
	// CONSTS
	//
	protected static final int     PRO_BEHAVIOR            = 0; // 1=pro, 0=normal
	protected static final String  MOBILECONTENT_URL_HELP  = "http://osilabs.com/m/apps/help/cba.php";
	protected static final String  NAMESPACE               = "com.osilabs.android.apps.cba";
	protected static final String  MARKET_URL              = "market://search?q=pname:com.osilabs.android.apps.cba";
	protected static final String  MARKET_URL_HTTP         = "https://market.android.com/details?id=com.osilabs.android.apps.cba";

	private static final int BUSY = 0;
	private static final int AVAILABLE = 1;
	private static final int YES = 0;
	private static final int NO = 1;
	private static final String TAG = "<<< ** osilabs.com ** >>> ";
	
	private static int FRAMEBUFFER_IS = AVAILABLE;
	private static int IS_PAUSING = NO;
    private static boolean SHOWING_PREVIEW = false; 

	// View properties
	private int view_w = 0;
	private int view_h = 0;
	
	// In my testing, a good length/width for a byte[]
	//  size of 497664 is 576x864
	private int yuv_w = 0;
	private int yuv_h = 0;
	
    String version = "0.1";

	private Preview preview;
	private DrawOnTop mDraw;
    private PowerManager.WakeLock wl;
	private ColorDrop d;
	
	// Sub title, need height for calc HUD size
	private TextView vSubTitle;
	public int subTitleHeight;
	
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Log.d(TAG, "onCreate'd");

		mPreview = null;
        mHolder = null;
        mCamera = null;

		d = new ColorDrop();
		
		// Prevent screen dimming
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);  
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");  

		mDraw = new DrawOnTop(this);

		setContentView(R.layout.main);

		addContentView(mDraw, new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		preview = new Preview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(preview);

		vSubTitle = (TextView) findViewById(R.id.bl_display);
		subTitleHeight = vSubTitle.getHeight();
		
		
		preview.onCreate();
	}

	@Override
	public void onStart() {
		super.onPause();
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
		super.onResume();
		wl.acquire();
		preview.onResume();
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
			
			// Droid: w=427, h=404
			int preview_w = preview.getWidth();
			int preview_h = preview.getHeight();
	    	//Toast.makeText(getApplicationContext(), "Width:"+Integer.toString(preview_w)+",Height:"+Integer.toString(preview_h), Toast.LENGTH_LONG).show();

			int line_len = (preview_w/20); //30;
			int corner_padding = 20;
			int circle_radius = 10;

			int center_x = (int) preview_w / 2;
			int center_y = (int) preview_h / 2;

			try{
				int inverseColor = Color.rgb(255 - d.R, 255 - d.G, 255 - d.B);
				
				// Inverse color paint
				Paint pInverseColor = new Paint();
				pInverseColor.setColor(inverseColor);
				
				// For the RGB Meter texts
				Paint meterLabelPaint = new Paint();
				meterLabelPaint.setTextSize(40);
				meterLabelPaint.setColor(Color.GRAY);
				
				// Reusable paint
				Paint paint = new Paint();
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(inverseColor);
				canvas.drawText("osilabs.com", 15, preview_h - 8, paint);
				
				canvas.drawCircle(center_x, center_y, circle_radius, paint);
				
				// Set paddings
				int meter_edge_gap = 10;
				int meter_gap = 8;
				int meters_off_bottom_gap = 4;
				
				// Calculate meter widths
				int meter_width = (preview_w - (meter_edge_gap*2) - (meter_gap*2)) / 3;
				
				// The HUD is the heads up display and has the meters in it.
				int hud_width  = preview_w;
				int hud_height = (preview_h/2);
				int hud_bl_x   = preview_w + 0; // bottom left x
				int hud_bl_y   = preview_h - meters_off_bottom_gap; // bottom left y
				
				// Red Bar
				int r_height = ((((d.R*100)/255)*hud_height)/100);
				paint.setStyle(Paint.Style.STROKE);
				paint.setColor(Color.RED);
				canvas.drawRect(hud_bl_x,               0 + hud_bl_y - r_height, 
								hud_bl_x + meter_width, 0 + hud_bl_y, 
								paint);
				canvas.drawText("R", hud_bl_x+5, hud_bl_y - (d.R/2), meterLabelPaint);

				// Green Bar
				hud_bl_x += meter_width+meter_gap; // Shift right for the next bar
				int g_height = ((((d.G*100)/255)*hud_height)/100);
				paint.setColor(Color.GREEN);
				canvas.drawRect(hud_bl_x,               0 + hud_bl_y - g_height,
								hud_bl_x + meter_width, 0 + hud_bl_y, 
								paint);
				canvas.drawText("G", hud_bl_x+5, hud_bl_y - (d.G/2), meterLabelPaint);
				
				// Blue Bar
				hud_bl_x += meter_width+meter_gap; // Shift right for the next bar
				int b_height = ((((d.B*100)/255)*hud_height)/100);
				paint.setColor(Color.BLUE);
				canvas.drawRect(hud_bl_x,               0 + hud_bl_y - b_height, 
								hud_bl_x + meter_width, 0 + hud_bl_y, 
								paint);
				canvas.drawText("B", hud_bl_x+5, hud_bl_y - (d.B/2), meterLabelPaint);


				// crosshairs
				paint.setColor(inverseColor);
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
				canvas.drawLine(preview_w - corner_padding, 0 + corner_padding, preview_w
						- corner_padding - line_len, 0 + corner_padding, paint); // top-right
				canvas.drawLine(preview_w - corner_padding, 0 + corner_padding, preview_w
						- corner_padding, 0 + corner_padding + line_len, paint); // top-right
				canvas.drawLine(preview_w - corner_padding, preview_h - corner_padding, preview_w
						- corner_padding, preview_h - corner_padding - line_len, paint); // bottom-left
				canvas.drawLine(preview_w - corner_padding, preview_h - corner_padding, preview_w
						- corner_padding - line_len, preview_h - corner_padding, paint); // bottom-left
				canvas.drawLine(0 + corner_padding, preview_h - corner_padding, 0
						+ corner_padding + line_len, preview_h - corner_padding, paint); // bottom-right
				canvas.drawLine(0 + corner_padding, preview_h - corner_padding,
						0 + corner_padding, preview_h - corner_padding - line_len, paint); // bottom-right
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
		Size mPreviewSize;

		Preview(Context context) {
			super(context);
			
			try {
				// Install a SurfaceHolder.Callback so we get notified when the
				// underlying surface is created and destroyed.
				mHolder = getHolder();
				mHolder.addCallback(this);
				mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	    //@Override
		public void onCreate() {
	    	//Log.d(TAG, "Preview onCreated'd");
	    }

		public void onPause() {
			//Log.d(TAG, "onPause'd - preview class");
			
			IS_PAUSING = YES;
			
			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's
			// very important to release it when the activity is paused.
			if (mCamera != null) {
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				SHOWING_PREVIEW = false;
				mCamera.release();
				mCamera = null;
			}
			
			//Log.d(TAG, "CAMERA RELEASED HERE");
			
			// Fixed bug with powering off then coming back to a blank screen by
			//  calling surfaceDestroyed/surfaceCreated from onPause/onResume.
			this.surfaceDestroyed(mHolder);
		}
		
		public void onResume() {
			IS_PAUSING = NO;

			// Open the default i.e. the first rear facing camera.
			mCamera = Camera.open();
			if (mCamera != null) {
			    requestLayout();
			    mCamera.startPreview();
			} 
			
			// Need this for killing power then coming back.
			this.surfaceCreated(mHolder);
		}

		// Called once the holder is ready
		public void surfaceCreated(SurfaceHolder holder) {
			//Log.d(TAG, "surfaceCreated'd");

			// The Surface has been created, tell the camera where to draw.
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.setPreviewCallback(new PreviewCallback() {
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
						if (data.length > d.bufallocsize) {
							// Buffer is not big enough for data allocate more spacs.
							//Log.v(TAG, "Allocating bufer for size: " + Integer.toString(size));
							d.decodeBuf = new int[size];
							d.bufallocsize = size;
							
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
				mCamera.release();
				mCamera = null;
				e.printStackTrace();
			}
		}

		// Called when holder has changed
		public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
			//Log.d(TAG, "surfaceChange'd");
			try{
				
                // I found this on http://pastebin.com/06FunF5k and thought it may
                //  be a benefit. 
                if (SHOWING_PREVIEW) {
                        SHOWING_PREVIEW = false;
                        mCamera.stopPreview();
                }

                
                if (IS_PAUSING == NO) {
					Camera.Parameters p = mCamera.getParameters();
					Camera.Size s = p.getPreviewSize();
					//p.setPictureFormat(ImageFormat.RGB_565);
					//camera.setParameters(p);
					view_w = s.width;
					view_h = s.height;
				
					SHOWING_PREVIEW = true;
					mCamera.startPreview();
                }
			} catch (Exception e) {
				Log.e(TAG, "YuvImage error:" + e.getMessage());
				e.printStackTrace();
			}			
		}
		
		// Called when the holder is destroyed
		//@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			//Log.d(TAG, "surfaceDestroyed'd start");

			// Surface will be destroyed when we return, so stop the preview.
			// Because the CameraDevice object is not a shared resource, it's
			// very important to release it when the activity is paused.
			if (mCamera != null) {
				SHOWING_PREVIEW = false;
				mCamera.stopPreview();
			}

			//Log.d(TAG, "surfaceDestroyed'd finish");
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
				OsiColor.decodeYUV(d.decodeBuf, yuvs[0], view_w, view_h);
			} catch (Exception e) {
				//Log.e(TAG, "decodeYUV error: " + e.getMessage());
				e.printStackTrace();
			}
			iii[0] = d.decodeBuf[center];

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
				d.RGBint = iRGB[0];

				// Set global reg green and blue
				d.A = (iRGB[0] >> 24) & 255;
				d.R = (iRGB[0] >> 16) & 255;
				d.G = (iRGB[0] >> 8) & 255;
				d.B = iRGB[0] & 255;
	
				// Set global rgb value for display
				d.RGBstr = d.R+","+d.G+","+d.B;

				// Set global hexval
				//d.hexval = Integer.toHexString(iRGB[0]).substring(2).toUpperCase();
				
				// Set color
				d.colorname = ImageProcessing.getColorNameFromRGB(d, PRO_BEHAVIOR);
				d.hexval = "";

				// Make display string for previewer
				//d.RGBdisplay= "rgb("+d.RGBstr+")";
				d.RGBdisplay = "";
	
				// Set drop color
				TextView tv = (TextView) findViewById(R.id.preview_text);
				tv.setBackgroundColor(Color.BLACK);
				
				TextView bl_tv = (TextView) findViewById(R.id.color_value_display);
				bl_tv.setTextSize(70);
				bl_tv.setTextColor(Color.WHITE);
				bl_tv.setText(d.colorname );
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
//	    //menu.add(0, MENU_PREFS, 0, "Preferences");
//	    menu.add(0, MENU_ABOUT, 0, "About");
//	    menu.add(0, MENU_QUIT, 0, "Quit");
//	    
//	    return true;
//	    
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.options, menu);
	    
	    return true;
	    
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		        
		    case R.id.menu_help:
        		Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(MOBILECONTENT_URL_HELP));
				startActivity(mIntent); 
		    	return true;

			case R.id.menu_about:
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
		        alertDialog.setIcon(R.drawable.ic_launcher);
		        alertDialog.setMessage("You are running version " + version + "\n\nosilabs.com LLC");
//		        alertDialog.setButton(this.getString(R.string.sample_button),
//		        		new DialogInterface.OnClickListener() {
//		        	public void onClick(DialogInterface dialog, int which) {
//		        		Intent mIntent = new Intent(Intent.ACTION_VIEW, 
//		        				Uri.parse("http://osilabs.com/m/mobilecontent/colorblindassist/about.php#" + version)); 
//        				startActivity(mIntent); 
//		            } 
//		        });
		        alertDialog.show();
		    	return true;
		    	
		    case R.id.menu_share:
		    	String appname = this.getResources().getString(R.string.app_name);
		    	
				final Intent shareintent = new Intent(Intent.ACTION_SEND);
				shareintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				shareintent.setType("text/plain");
				shareintent.putExtra(Intent.EXTRA_SUBJECT, "I like  " + appname + " for Android!");
				shareintent.putExtra(Intent.EXTRA_TEXT,
						"I am sharing " + appname + " for Android with you. A free app!\n\n"
						
						+ "From your mobile device or computer:\n"
						+ " " + MARKET_URL_HTTP + "\n\n"
						
						+ "If the link does not work for you or you do not use Android Market, please search your market for:\n"
						+ " " + appname + "\n\n"
						
						+ "Thanks!"
				);
				startActivity(Intent.createChooser(shareintent, "Share"));
	    }
	    return false;
	}
}