package com.iek.wiflyremote.stat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver3 extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("Receiver", "Alarm received");
		M.m().sendMessage(null, "C");
	}
}
