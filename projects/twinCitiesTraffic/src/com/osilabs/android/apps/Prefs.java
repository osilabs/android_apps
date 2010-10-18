/**
 * 
 */
package com.osilabs.android.apps;

//import com.osilabs.android.apps.R;	    menu.add(0, MENU_MNDOT_MOBILE_FREEWAYS, 0, "Cameras");

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author dezurik
 *
 */
public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
	}
}
