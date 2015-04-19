package com.iek.wiflyremote.ui;

import android.os.Bundle;
import android.preference.Preference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iek.wiflyremote.R;

public class PreferenceFragment extends android.preference.PreferenceFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		Preference syncPref = findPreference("pref_syncclock");
		
		return v;
	}
}
