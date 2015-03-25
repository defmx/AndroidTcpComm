package com.iek.tcpcomm.ui;

import java.util.Observable;
import java.util.Observer;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.stat.M;
import com.iek.tcpcomm.ui.adapters.MultipleViewAdapter;

public class ConsoleFragment extends Fragment {
	private String mHost = "192.168.0.9";
	private String mPort = "9999";
	private TextView mHostTextView;
	private Thread mThr;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mHostTextView = new TextView(getActivity());
		TextView subtitle1 = new TextView(getActivity());
		View v = inflater.inflate(R.layout.fragment_main, null);
		MultipleViewAdapter adapter = new MultipleViewAdapter();
		mHostTextView.setText("Host is: " + mHost + " : " + mPort);
		mHostTextView.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		subtitle1.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		adapter.getViewList().add(mHostTextView);
		adapter.getViewList().add(subtitle1);
		for (View vv : adapter.getViewList()) {
			vv.setPadding(0, 5, 0, 5);
		}
		
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
									Object data) {

							}
						}, "P#");
					} catch (InterruptedException e) {
						Log.e("THRRUN", e.getMessage());
					}

				}
			}
		});
		mThr.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mThr != null && mThr.isAlive()) {
		}
	}

}
