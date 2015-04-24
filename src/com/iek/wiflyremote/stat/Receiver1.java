package com.iek.wiflyremote.stat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class Receiver1 extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("Turno2", "Alarm received");
		SharedPreferences shift = PreferenceManager
				.getDefaultSharedPreferences(context);
		int h2 = 0;
		String defaultValue = "00:00";
		h2 = Integer.parseInt(shift.getString("pref_time2", defaultValue));
		if (!M.m().appIsActive()) {
			M.m().connect(null);
		}
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		M.m().sendMessage(null, "R");
		M.m().sendMessage(null, "H" + String.format("%02d", h2));
		if (!M.m().appIsActive()) {
			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			M.m().disconnect();
		}
	}
}
