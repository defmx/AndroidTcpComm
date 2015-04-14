package com.iek.tcpcomm.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.stat.BoardResponse;
import com.iek.tcpcomm.stat.M;

public class ConsoleFragment extends Fragment {
	private TextView mactualSpeedText;
	private TextView mavgSpeedText;
	private TextView mdeadTimeText;
	private TextView mlinMtsText;
	private TextView mlastStopText;
	private Thread mThr;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, null);
		mactualSpeedText = (TextView) v.findViewById(R.id.actualSpeedText);
		mavgSpeedText = (TextView) v.findViewById(R.id.avgSpeedText);
		mdeadTimeText = (TextView) v.findViewById(R.id.deadTimeText);
		mlinMtsText = (TextView) v.findViewById(R.id.linMtsText);
		mlastStopText = (TextView) v.findViewById(R.id.lastStopText);

		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		mThr = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000);
						M.m().sendMessage(new Observer() {

							@Override
							public void update(Observable observable,
									final Object data) {
								if (getActivity() == null) {
									return;
								}
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if (data instanceof BoardResponse) {
											BoardResponse br = (BoardResponse) data;
											double v, vm, dt, d;
											String[] parts;
											try {
												parts = br.getMessage().split(
														",");
												v = Double
														.parseDouble(parts[0]);
												mactualSpeedText
														.setText("" + v);
												vm = Double
														.parseDouble(parts[1]);
												mavgSpeedText.setText("" + vm);
												dt = Double
														.parseDouble(parts[2]);
												mdeadTimeText.setText("" + dt);
												d = Double
														.parseDouble(parts[3]);
												mlinMtsText.setText("" + d);
											} catch (ArrayIndexOutOfBoundsException e) {
												Log.e("Console", e.getMessage());
											}
										} else if (data instanceof Integer) {
											Calendar c = Calendar.getInstance();
											SimpleDateFormat format1 = new SimpleDateFormat(
													"yyyy-MM-dd @ hh:mm:ss");
											mlastStopText.setText(format1
													.format(c.getTime()));
											try {
												mThr.join();
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									}
								});

							}
						}, "P#");
					} catch (InterruptedException e) {
						break;
					}

				}
			}
		});
		mThr.start();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mThr.interrupt();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		M.m().sendMessage(null, "close");
		mThr.interrupt();
	}

}
