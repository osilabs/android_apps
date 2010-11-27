/**
 * 
 */
package com.osilabs.android.apps.dallastraffic;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		
//		Preference p_prefs = (Preference) findPreference("pref_police_radios");
//		Preference w_prefs = (Preference) findPreference("pref_weather_radios");
//		
//        p_prefs.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//        	public boolean onPreferenceChange(Preference preference, Object newval) {
//                finish();
//        		return true;
//        	}
//        });
//        p_prefs.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//            	SharedPreferences sprefs = getSharedPreferences("com.osilabs.android.apps.dallastraffic", 0);
//                SharedPreferences.Editor editor = sprefs.edit();
//                editor.putInt("session_camera_1", Integer.parseInt(preference.toString()));
//                editor.commit();
////	                Prefs.finish();
//                return true;
//            }
//        });
		
//        //
//        // Catch the onCameraSelect event to finish() so control is sent back to main activity.
//        //
//        Preference campref_US75 = (Preference) findPreference("session_selected_US75");
//        campref_US75.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//        	public boolean onPreferenceChange(Preference preference, Object newval) {
//                Toast.makeText(getApplicationContext(), "Camera Selected", Toast.LENGTH_SHORT).show();
//                finish();
//        		return true;
//        	}
//        });
//        campref_US75.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//                Toast.makeText(getApplicationContext(), "pref: " + preference, Toast.LENGTH_SHORT).show();
////            	SharedPreferences prefs 
////            		= getSharedPreferences("com.osilabs.android.apps.dallastraffic", 0);
////                SharedPreferences.Editor editor = prefs.edit();
////                editor.putInt("pref_default", 2); //Integer.parseInt(preference.toString()));
////                editor.commit();
////                Prefs.finish();
//                return true;
//            }
//        });
//	
//    
//	    //
//	    // Catch the onCameraSelect event to finish() so control is sent back to main activity.
//	    //
//	    Preference campref_SH183 = (Preference) findPreference("session_selected_SH183");
//	    campref_SH183.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//	    	public boolean onPreferenceChange(Preference preference, Object newval) {
//	            Toast.makeText(getApplicationContext(), "Camera Selected", Toast.LENGTH_SHORT).show();
//	            finish();
//	    		return true;
//	    	}
//	    });
//	    campref_SH183.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//	        public boolean onPreferenceClick(Preference preference) {
//	            Toast.makeText(getApplicationContext(), "pref: " + preference, Toast.LENGTH_SHORT).show();
//	            return true;
//	        }
//	    });
	}
}