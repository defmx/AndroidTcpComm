package com.iek.wiflyremote.ui;

import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;

public class StatisticsFragment extends Fragment {
	private TextView textRight;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.stats_fragment, container, false);

		textRight = (TextView) v.findViewById(R.id.textRight);

		M.m().addBoardRespObserver(new Observer() {

			double v, vm, dt, d;

			@Override
			public void update(Observable observable, Object data) {
				String s = (String) data;
				if (s.startsWith("P#=")) {
					String str = s.replace("P#=", "");
					String[] parts;
					try {
						parts = str.split(",");
						v = Double.parseDouble(parts[0]);
						vm = Double.parseDouble(parts[1]);
						dt = Double.parseDouble(parts[2]);
						d = Double.parseDouble(parts[3]);
						for (int i = 0; i < parts.length; i++) {
							ContentValues cv = new ContentValues();
							cv.put("type", i);
							cv.put("value", parts[i]);
							M.m().getLocaldb().insOrUpd("statistics", cv, null);
						}
						if (getActivity() != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									textRight.setText("Velocidad=" + v
											+ "\n Vel. Media=" + vm
											+ "\n Tiempo Muerto=" + dt
											+ "\n Distancia=" + d);
								}
							});
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						Log.e("Console", e.getMessage());
					} catch (NumberFormatException e1) {
						Log.e("Console", e1.getMessage());
					}
				}
			}
		});

		return v;
	}
}
