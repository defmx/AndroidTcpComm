package com.iek.wiflyremote.stat;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Receiver3 extends BroadcastReceiver {
	private String resp = "";

	@Override
	public void onReceive(final Context context, Intent intent) {
		Log.i("Receiver", "Alarm received");
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
						((Activity) context).runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(context,
										"El servidor dejó de responder",
										Toast.LENGTH_SHORT).show();
							}
						});

						if (context instanceof Activity) {
							((Activity) context).finish();
						}
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
