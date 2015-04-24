package com.iek.wiflyremote.stat;

import java.util.Observable;
import java.util.Observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver3 extends BroadcastReceiver {
	private String resp = "";
	private final int MAXNBROFRETRIES = 3;
	private int NBROFRETRIES = 0;

	private Observer obs = new Observer() {

		@Override
		public void update(Observable observable, Object data) {
			String s = (String) data;
			if (s.contains("C")) {
				resp = "C";
			}
		}
	};

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.i("Receiver3", "Alarm received");
		NBROFRETRIES = 0;
		if (!M.m().appIsActive()) {
			M.m().connect(null);
		}
		try {
			Thread.sleep(150);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		M.m().addBoardRespObserver(obs);
		checkConnection();
	}

	private void checkConnection() {
		try {
			Thread.sleep(150);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		M.m().sendMessage(null, "C");
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					if (!resp.equals("C")) {
						if (NBROFRETRIES < MAXNBROFRETRIES) {
							Log.i("Receiver3",
									"El servidor dejó de responder, reintentando ("
											+ NBROFRETRIES + ") ...");
							checkConnection();
							NBROFRETRIES++;
						} else {
							Log.i("Receiver3", "El servidor dejó de responder");
							NBROFRETRIES = 0;
							M.m().delBoardRespObserver(obs);
							if (!M.m().appIsActive()) {
								M.m().disconnect();
							}
						}
					} else {
						NBROFRETRIES = 0;
						Log.i("Receiver3", "Serividor OK");
						resp = "";
						if (!M.m().appIsActive()) {
							M.m().disconnect();
						}
					}
				} catch (InterruptedException e) {

				} catch (Throwable e) {
					e.printStackTrace();
				} finally {
				}
			}
		}).start();
	}

}
