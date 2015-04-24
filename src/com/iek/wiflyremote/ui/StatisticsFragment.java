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
	private Observer observer=new Observer() {

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
					}
					if (getActivity() != null) {
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								textRight.setText("\t Velocidad = " + v + " m/min\n\n"
										+ "\t Vel. Media = " + vm + " m/min\n\n"
										+ "\t Tiempo Muerto = " + dt + " min\n\n"
										+ "\t Distancia = " + d + " m");
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
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.stats_fragment, container, false);

		textRight = (TextView) v.findViewById(R.id.textRight);

		M.m().addBoardRespObserver(observer);

		return v;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		M.m().delBoardRespObserver(observer);
	}
}
