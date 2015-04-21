package com.iek.wiflyremote.stat;

import java.util.Observable;
import java.util.Observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Receiver3 extends BroadcastReceiver {
	private String resp = "";

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.i("Receiver3", "Alarm received");
		final Observer obs = new Observer() {

			@Override
			public void update(Observable observable, Object data) {
				String s = (String) data;
				if (s.equals("C")) {
					resp = s;
				}
			}
		};
		M.m().addBoardRespObserver(obs);
		M.m().sendMessage(null, "C");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					if (!resp.equals("C")) {
						Log.i("Receiver3", "El servidor dejó de responder");
					} else {
						resp = "";
					}
				} catch (InterruptedException e) {

				} finally {
					M.m().delBoardRespObserver(obs);
				}
			}
		}).start();
	}
}
