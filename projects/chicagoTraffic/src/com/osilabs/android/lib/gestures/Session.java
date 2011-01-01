package com.osilabs.android.lib.gestures;

import android.content.SharedPreferences;

public class Session {
	public static void saveString(SharedPreferences sp, String key, String value) {		
		SharedPreferences.Editor editor = sp.edit();
	    editor.putString(key, value);
	    editor.commit();
	}
	public static void saveInt(SharedPreferences sp, String key, int value) {		
		SharedPreferences.Editor editor = sp.edit();
	    editor.putInt(key, value);
	    editor.commit();
	}
}
