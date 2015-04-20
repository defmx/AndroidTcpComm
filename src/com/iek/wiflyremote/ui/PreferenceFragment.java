package com.iek.wiflyremote.ui;

import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;
import com.iek.wiflyremote.stat.Receiver;
import com.iek.wiflyremote.stat.Receiver1;
import com.iek.wiflyremote.stat.Receiver2;

public class PreferenceFragment extends android.preference.PreferenceFragment {
	private String syncTime;
	private SharedPreferences settings;
	private String s1;
	private String s2;
	private String s3;
	private CharSequence infotoshow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		updShifts();
		final Preference syncPref = findPreference("pref_syncclock");
		final Preference resetPref = findPreference("pref_reset");
		final TimePreference timePref1 = (TimePreference) findPreference("pref_shift1");
		final TimePreference timePref2 = (TimePreference) findPreference("pref_shift2");
		final TimePreference timePref3 = (TimePreference) findPreference("pref_shift3");
		final EditTextPreference lenPref1 = (EditTextPreference) findPreference("pref_time1");
		final EditTextPreference lenPref2 = (EditTextPreference) findPreference("pref_time2");
		final EditTextPreference lenPref3 = (EditTextPreference) findPreference("pref_time3");
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

		resetPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				M.m().sendMessage(null, "R");
				Toast.makeText(getActivity(), "Ok", Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		timePref1
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String[] parts = ((String) newValue).split(":");
						int h = 0;
						int m = 0;
						if (parts.length == 2) {
							h = Integer.parseInt(parts[0]);
							m = Integer.parseInt(parts[1]);
						} else {
							return false;
						}
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(System.currentTimeMillis());
						cal.set(Calendar.HOUR_OF_DAY, h);
						cal.set(Calendar.MINUTE, m);
						Intent intent = new Intent(getActivity(),
								Receiver.class);
						intent.putExtra("id", 1);
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(getActivity(), 1, intent,
										Intent.FILL_IN_DATA);
						AlarmManager alarm = (AlarmManager) getActivity()
								.getSystemService(Context.ALARM_SERVICE);
						alarm.setRepeating(AlarmManager.RTC_WAKEUP,
								cal.getTimeInMillis(),
								AlarmManager.INTERVAL_DAY, pendingIntent);
						return true;
					}
				});
		timePref2
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String[] parts = ((String) newValue).split(":");
						int h = 0;
						int m = 0;
						if (parts.length == 2) {
							h = Integer.parseInt(parts[0]);
							m = Integer.parseInt(parts[1]);
						} else {
							return false;
						}
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(System.currentTimeMillis());
						cal.set(Calendar.HOUR_OF_DAY, h);
						cal.set(Calendar.MINUTE, m);
						Intent intent = new Intent(getActivity(),
								Receiver1.class);
						intent.putExtra("id", 2);
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(getActivity(), 2, intent,
										Intent.FILL_IN_DATA);
						AlarmManager alarm = (AlarmManager) getActivity()
								.getSystemService(Context.ALARM_SERVICE);
						alarm.setRepeating(AlarmManager.RTC_WAKEUP,
								cal.getTimeInMillis(),
								AlarmManager.INTERVAL_DAY, pendingIntent);
						return true;
					}
				});
		timePref3
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String[] parts = ((String) newValue).split(":");
						int h = 0;
						int m = 0;
						if (parts.length == 2) {
							h = Integer.parseInt(parts[0]);
							m = Integer.parseInt(parts[1]);
						} else {
							return false;
						}
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(System.currentTimeMillis());
						cal.set(Calendar.HOUR_OF_DAY, h);
						cal.set(Calendar.MINUTE, m);
						Intent intent = new Intent(getActivity(),
								Receiver2.class);
						intent.putExtra("id", 3);
						PendingIntent pendingIntent = PendingIntent
								.getBroadcast(getActivity(), 3, intent,
										Intent.FILL_IN_DATA);
						AlarmManager alarm = (AlarmManager) getActivity()
								.getSystemService(Context.ALARM_SERVICE);
						alarm.setRepeating(AlarmManager.RTC_WAKEUP,
								cal.getTimeInMillis(),
								AlarmManager.INTERVAL_DAY, pendingIntent);
						return true;
					}
				});
		String snew = "";
		if (infotoshow.equals("Mh")) {
			snew = "Hora";
		} else if (infotoshow.equals("Md")) {
			snew = "Metros lineales";
		} else if (infotoshow.equals("Mt")) {
			snew = "Tiempo muerto";
		} else if (infotoshow.equals("Mv")) {
			snew = "Velocidad instantánea";
		} else if (infotoshow.equals("Mu")) {
			snew = "Velocidad promedio";
		} else if (infotoshow.equals("Ma")) {
			snew = "Automático";
		}
		infoToShowPref.setSummary(snew);
		infoToShowPref
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						String snew = (String) newValue;
						M.m().sendMessage(null, snew);
						if (snew.equals("Mh")) {
							snew = "Hora";
						} else if (snew.equals("Md")) {
							snew = "Metros lineales";
						} else if (snew.equals("Mt")) {
							snew = "Tiempo muerto";
						} else if (snew.equals("Mv")) {
							snew = "Velocidad instantánea";
						} else if (snew.equals("Mu")) {
							snew = "Velocidad promedio";
						} else if (snew.equals("Ma")) {
							snew = "Automático";
						}
						infoToShowPref.setSummary(snew);
						return true;
					}
				});
		lenPref1.setSummary(s1 + " horas");
		lenPref1.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len = Integer.parseInt(newValue.toString());
				if (len < 5 || len > 12) {
					Toast.makeText(
							getActivity(),
							"La duración del turno debe estar entre 5 y 12 horas",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				preference.setSummary(len + " horas");
				return true;
			}
		});
		lenPref2.setSummary(s2 + " horas");
		lenPref2.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len = Integer.parseInt(newValue.toString());
				if (len < 5 || len > 12) {
					Toast.makeText(
							getActivity(),
							"La duración del turno debe estar entre 5 y 12 horas",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				preference.setSummary(len + " horas");
				return true;
			}
		});
		lenPref3.setSummary(s3 + " horas");
		lenPref3.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len = Integer.parseInt(newValue.toString());
				if (len < 5 || len > 12) {
					Toast.makeText(
							getActivity(),
							"La duración del turno debe estar entre 5 y 12 horas",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				preference.setSummary(len + " horas");
				return true;
			}
		});
		return v;
	}

	private void updShifts() {
		settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		s1 = settings.getString("pref_time1", null);
		s2 = settings.getString("pref_time2", null);
		s3 = settings.getString("pref_time3", null);
		infotoshow = settings.getString("pref_infoToShow", null);
	}
}
