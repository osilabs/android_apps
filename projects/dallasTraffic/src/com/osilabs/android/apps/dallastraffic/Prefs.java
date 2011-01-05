/**
 * 
 */
package com.osilabs.android.apps.dallastraffic;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Prefs extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		
		// TODO - inflate options popups and add icons.
	}
}