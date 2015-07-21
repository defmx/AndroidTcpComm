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
	private String a1;
	private String a2;

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
		final EditTextPreference adv_1 = (EditTextPreference) findPreference("adv_1");
		final EditTextPreference adv_2 = (EditTextPreference) findPreference("adv_2");

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
				Toast.makeText(getActivity(), "Enviado", Toast.LENGTH_SHORT)
						.show();
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
			snew = "Piezas totales";
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
							snew = "Piezas totales";
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
		lenPref1.setSummary(s1 == null ? "Sin establecer" : s1 + " minutos");
		lenPref1.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len = Integer.parseInt(newValue.toString());
				if (len < 1 || len > 720) {
					Toast.makeText(
							getActivity(),
							"La duración del turno se puede establecer entre 1 y 720 minutos",
							Toast.LENGTH_SHORT).show();
					return false;
				} else {
					M.m().sendMessage(null, "H" + String.format("%03d", len));
					preference.setSummary(len + " minutos");
					return true;
				}
			}
		});
		lenPref2.setSummary(s2 == null ? "Sin establecer" : s2 + " minutos");
		lenPref2.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len = Integer.parseInt(newValue.toString());
				if (len < 1 || len > 720) {
					Toast.makeText(
							getActivity(),
							"La duración del turno se puede establecer entre 1 y 720 minutos",
							Toast.LENGTH_SHORT).show();
					return false;
				} else {
					M.m().sendMessage(null, "H" + String.format("%03d", len));
					preference.setSummary(len + " minutos");
					return true;
				}
			}
		});
		lenPref3.setSummary(s3 == null ? "Sin establecer" : s3 + " minutos");
		lenPref3.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len = Integer.parseInt(newValue.toString());
				if (len < 1 || len > 720) {
					Toast.makeText(
							getActivity(),
							"La duración del turno se puede establecer entre 1 y 720 minutos",
							Toast.LENGTH_SHORT).show();
					return false;
				} else {
					M.m().sendMessage(null, "H" + String.format("%03d", len));
					preference.setSummary(len + " minutos");
					return true;
				}
			}
		});
		
		adv_1.setSummary(a1 == null ? "Sin establecer" : a1 + " ms");
		adv_1.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len_a1 = 0;
				try{
				len_a1 = Integer.parseInt(newValue.toString());
				}
				catch(Exception e)
				{
					Toast.makeText(
							getActivity(),
							"Solo se aceptan numeros mayores que cero",	Toast.LENGTH_SHORT).show();
				}
				if (len_a1 < 100 || len_a1 > 20000) {
					Toast.makeText(
							getActivity(),
							"El valor puede se puede establecer entre 100 y 20000 ms",
							Toast.LENGTH_SHORT).show();
					return false;
				} else {
					M.m().sendMessage(null, "F" + String.format("%05d", len_a1));
					preference.setSummary(len_a1 + " ms");
					return true;
				}
			}
		});
		
		adv_2.setSummary(a2 == null ? "Sin establecer" : a2 + " ms");
		adv_2.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference,
					Object newValue) {
				int len = 0;
				try{
				len = Integer.parseInt(newValue.toString());
				}
				catch(Exception e)
				{
					Toast.makeText(
							getActivity(),
							"Solo se aceptan numeros mayores que cero",	Toast.LENGTH_SHORT).show();
				}
				if (len < 250 || len > 2000) {
					Toast.makeText(
							getActivity(),
							"El valor puede estar entre 250 y 2000",
							Toast.LENGTH_SHORT).show();
					return false;
				} else {
					M.m().sendMessage(null, "J" + String.format("%05d", len));
					preference.setSummary(len + " ms");
					return true;
				}
			}
		});

		return v;
	}
	
	
	
	

	private void updShifts() {
		settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
		s1 = settings.getString("pref_time1", null);
		s2 = settings.getString("pref_time2", null);
		s3 = settings.getString("pref_time3", null);
		a1 = settings.getString("adv_1", null);
		a2 = settings.getString("adv_2", null);
		infotoshow = settings.getString("pref_infoToShow", null);
	}
}
