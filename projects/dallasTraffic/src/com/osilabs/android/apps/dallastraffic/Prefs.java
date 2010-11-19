/**
 * 
 */
package com.osilabs.android.apps.dallastraffic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.Toast;

public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		
//        // Get the custom preference
//        Preference pref = (Preference) findPreference("pref_selected");
//        
//        pref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//            public boolean onPreferenceClick(Preference preference) {
//        		
//        		
//                Toast.makeText(getApplicationContext(), "pref: " + preference., Toast.LENGTH_SHORT).show();
//
//            	
//            	SharedPreferences prefs 
//            		= getSharedPreferences("com.osilabs.android.apps.dallastraffic", 0);
//                SharedPreferences.Editor editor = prefs.edit();
//                editor.putInt("pref_default", 2); //Integer.parseInt(preference.toString()));
//                editor.commit();
//                return true;
//            }
//        });
	}
}