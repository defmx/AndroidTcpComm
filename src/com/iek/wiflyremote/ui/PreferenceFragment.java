package com.iek.wiflyremote.ui;

import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;

public class PreferenceFragment extends android.preference.PreferenceFragment {
	private String syncTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		final Preference syncPref = findPreference("pref_syncclock");
		final ListPreference infoToShowPref = (ListPreference) findPreference("pref_infoToShow");

		syncPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						syncPref.setEnabled(false);
					}
				});
				Toast.makeText(
						getActivity(),
						"Sincronizando reloj en "
								+ ((int) (60 - Calendar.getInstance().get(
										Calendar.SECOND)) + " s"),
						Toast.LENGTH_LONG).show();
				new Thread(new Runnable() {

					@Override
					public void run() {
						int s = Calendar.getInstance().get(Calendar.SECOND);
						do {
							s = Calendar.getInstance().get(Calendar.SECOND);
						} while (s != 0);
						final Calendar c = Calendar.getInstance();
						int h = c.get(Calendar.HOUR);
						int m = c.get(Calendar.MINUTE);
						int ampm = c.get(Calendar.AM_PM);
						h = ampm == 0 ? h : h + 12;
						syncTime = String.format(Locale.US, "%02d", h);
						syncTime += String.format(Locale.US, "%02d", m);
						M.m().sendMessage(new Observer() {

							@Override
							public void update(Observable observable,
									Object data) {
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
									}
								});
							}
						}, "E" + syncTime);
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								syncPref.setEnabled(true);
								syncPref.setSummary("Última sincronización: "
										+ syncTime);
							}
						});
					}
				}).start();
				return true;
			}
		});

		infoToShowPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String snew = (String) newValue;
						infoToShowPref.setSummary(((ListPreference) preference)
								.getValue());
						M.m().sendMessage(null, snew);
						return false;
					}
				});

		return v;
	}
}
