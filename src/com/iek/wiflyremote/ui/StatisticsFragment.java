package com.iek.wiflyremote.ui;

import java.util.Observable;
import java.util.Observer;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.iek.wiflyremote.R;
import com.iek.wiflyremote.stat.M;

public class StatisticsFragment extends Fragment {
	private TextView textLeft;
	private TextView textRight;
	private EditText etGInstVel;
	private EditText etGAvgVel;
	private EditText etGLinMet;
	private EditText etGDeadT;
	private String ip;
	private int port;
	private Runnable statsThr = new Runnable() {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					M.m().sendMessage(null, "P#");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.stats_fragment, container, false);
		etGInstVel = (EditText) v.findViewById(R.id.etGInstVel);
		etGAvgVel = (EditText) v.findViewById(R.id.etGAvgVel);
		etGLinMet = (EditText) v.findViewById(R.id.etGLinMet);
		etGDeadT = (EditText) v.findViewById(R.id.etGDeadT);
		Button btGInstVel = (Button) v.findViewById(R.id.btGInstVel);
		Button btGAvgVel = (Button) v.findViewById(R.id.btGAvgVel);
		Button btGLinMet = (Button) v.findViewById(R.id.btGLinMet);
		Button btGDeadT = (Button) v.findViewById(R.id.btGDeadT);

		ip = getActivity().getIntent().getExtras().getString("IP");
		port = Integer.parseInt(getActivity().getIntent().getExtras()
				.getString("PORT"));

		textLeft = (TextView) v.findViewById(R.id.textLeft);
		textRight = (TextView) v.findViewById(R.id.textRight);

		M.m().setBoardRespObserver(new Observer() {

			@Override
			public void update(Observable observable, Object data) {
				String s = (String) data;
				if (s.startsWith("H")) {
				} else if (s.startsWith("P#=")) {
					String str = s.replace("P#=", "");
					double v, vm, dt, d;
					String[] parts;
					try {
						parts = str.split(",");
						v = Double.parseDouble(parts[0]);
						vm = Double.parseDouble(parts[1]);
						dt = Double.parseDouble(parts[2]);
						d = Double.parseDouble(parts[3]);
						textRight.setText("Velocidad=" + v + "\n Vel. Media="
								+ vm + "\n Tiempo Muerto=" + dt
								+ "\n Distancia=" + d);
					} catch (ArrayIndexOutOfBoundsException e) {
						Log.e("Console", e.getMessage());
					}
				}
			}
		});

		btGInstVel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = 0;
				try {
					i = Integer.parseInt(etGInstVel.getText().toString());
				} catch (NumberFormatException e) {
					return;
				}
				if (i <= 999) {
					M.m().sendMessage(null,
							"V" + String.format("%03d", i) + "#");
				}
			}
		});
		btGAvgVel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = 0;
				try {
					i = Integer.parseInt(etGAvgVel.getText().toString());
				} catch (NumberFormatException e) {
					return;
				}
				if (i <= 999) {
					M.m().sendMessage(null,
							"A" + String.format("%03d", i) + "#");
				}
			}
		});
		btGLinMet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = 0;
				try {
					i = Integer.parseInt(etGLinMet.getText().toString());
				} catch (NumberFormatException e) {
					return;
				}
				if (i <= 99999) {
					M.m().sendMessage(null,
							"M" + String.format("%05d", i) + "#");
				}
			}
		});
		btGDeadT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int i;
				try {
					i = Integer.parseInt(etGDeadT.getText().toString());
				} catch (NumberFormatException e) {
					return;
				}
				if (i <= 999) {
					M.m().sendMessage(null,
							"T" + String.format("%03d", i) + "#");
				}
			}
		});

		return v;
	}
}
