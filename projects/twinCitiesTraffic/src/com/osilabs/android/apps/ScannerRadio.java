package com.osilabs.android.apps;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

public class ScannerRadio {
	// -----------------------------------------------
	// Scanner Radio
	// 
		
	// I tried moving it to it's own class but need to watch for having pass
	//  context and creating memory leaks. see"
	// http://developer.android.com/resources/articles/avoiding-memory-leaks.html
	
	public static final String SCANNER_RADIO_NAMESPACE = "net.gordonedwards.scannerradio";
	public static final String SCANNER_RADIO_ACTION = "ACTION_PLAY_SCANNER";
	
	public static boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =
	        packageManager.queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	public static void launchScanner(int which_scanner) {
		if(Config.DEBUG>0)Log.d("** osilabs.com **", "Scanner node: " + Integer.toString(which_scanner));
		
		boolean scannerAvailable = isIntentAvailable(App.me,
				SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);

		if (scannerAvailable) {
			Intent intent = new Intent(SCANNER_RADIO_NAMESPACE + ".intent.action." + SCANNER_RADIO_ACTION);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("node", which_scanner);
			App.me.startActivity(intent);
		} else {
		    AlertDialog scannerAlert = new AlertDialog.Builder(App.me).create();
	        scannerAlert.setTitle(R.string.app_name);
	        scannerAlert.setMessage("To use this feature, install the \"Scanner Radio\" app from the market");
	        scannerAlert.setIcon(R.drawable.ic_launcher); 
	        scannerAlert.setButton("Get the plugin", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int which) {
	    			Intent intent = new Intent(
	    					Intent.ACTION_VIEW,
	    					Uri.parse("market://details?id=com.scannerradio"));
	    			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    			App.me.startActivity(intent);
	            } 
	        });
	        scannerAlert.show();
		}
	}

	// 
	// Scanner Radio
    // -----------------------------------------------

}
