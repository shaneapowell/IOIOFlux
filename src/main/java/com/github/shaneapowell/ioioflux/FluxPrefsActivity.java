package com.github.shaneapowell.ioioflux;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**********************************************************
 *
 ***********************************************************/
public class FluxPrefsActivity extends PreferenceActivity
{


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.ioioflux_preferences);
	}
}
