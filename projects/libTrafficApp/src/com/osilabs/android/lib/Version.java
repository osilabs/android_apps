package com.osilabs.android.lib;

import com.osilabs.android.apps.libtrafficapp.Config;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.webkit.WebView;

public class Version {
	private PackageInfo pInfo = null;
	private String appCode = "";
	
	public Version(Context app, String namespace, String ac) {
		// Code for webcontent to tell apps apart. i.e. CT, TCT, LD, ...
		appCode = ac;
		
		// Read in manifest
		try {
			pInfo = app.getPackageManager().getPackageInfo(Config.NAMESPACE, PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
//	public void checkWebserviceForVersion(WebView wv, String webcontentURL) {
//		if (pInfo != null) {
//			String URI = webcontentURL + "?ac=" + appCode + "&v=" + pInfo.versionCode;
//			Log.d("** osilabs *******************************", "Version::checkWebserviceForVersion: Calling with: " + URI);
//			wv.loadUrl(URI);
//		}
//	}
	
	public String getVersionCheckURLParams() {
		if (pInfo != null) {
			return "&checkfornewversion=1&ac=" + appCode + "&v=" + pInfo.versionCode;
		}
		
		return "";
	}
	
	public int versionCode() {
		if (pInfo != null) {
			return pInfo.versionCode;
		} else {
			return -1;
		}
	}
	public String versionName() {
		if (pInfo != null) {
			return pInfo.versionName;
		} else {
			return "";
		}
	}
}
