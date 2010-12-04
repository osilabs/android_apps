/**
 * 
 */
package com.osilabs.android.apps.seattletraffic;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

//public class Prefs extends PreferenceActivity implements OnSharedPreferenceChangeListener {
public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		
		//
		//
		// ALL THIS WAS TO JUST DISPLAY A MESSAGE SAYING "Preference Saved" upon change.
		//
		//
		
//		//Prefs customPref = (Prefs) findPreference("prefs");
//        this.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//             public boolean onPreferenceClick(Preference preference) {
//                     Toast.makeText(getBaseContext(),
//                                     "The custom preference has been clicked",
//                                     Toast.LENGTH_LONG).show();
//             }
//         });
         
//         this.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
//              public boolean onPreferenceChange(Preference preference) {
//                      Toast.makeText(getBaseContext(),
//                                      "The custom preference has been clicked",
//                                      Toast.LENGTH_LONG).show();
//              }
//          };
        
//        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
////            // Let's do something a preference value changes
////            if (key.equals(KEY_A_CHECKBOX_PREFERENCE)) {
////                mCheckBoxPreference.setSummary(sharedPreferences.getBoolean(key, false) ? "Disable this setting" : "Enable this setting");
////            }
////            else if (key.equals(KEY_AN_EDITTEXT_PREFERENCE)) {
////                mEditBoxPreference.setSummary("Current value is " + sharedPreferences.getString(key, "")); 
////            }
//        }
        
	}
//
//	@Override
//	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//	      Toast.makeText(getBaseContext(),
//	      "Preference saved",
//	      Toast.LENGTH_LONG).show();
//	}

}