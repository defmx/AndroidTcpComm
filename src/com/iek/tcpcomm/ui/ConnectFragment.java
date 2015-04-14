package com.iek.tcpcomm.ui;

import java.util.Observable;
import java.util.Observer;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Fragment;
import android.content.ContentValues;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.stat.BoardResponse;
import com.iek.tcpcomm.stat.M;

public class ConnectFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.connect_fragment, container, false);
		final EditText e = (EditText) v.findViewById(R.id.hostEdit);
		final TextView hostsV = (TextView) v.findViewById(R.id.hostsLink);
		Button b = (Button) v.findViewById(R.id.connectButton);

		hostsV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		b.setOnClickListener(new OnClickListener() {

			private String maddr;
			private String mport;

			@Override
			public void onClick(View arg0) {
				maddr = e.getText().toString();
				mport = "";
				try {
					mport = maddr.split(":")[1];
					maddr = maddr.split(":")[0];
				} catch (ArrayIndexOutOfBoundsException e) {
					Toast.makeText(getActivity(), R.string.addressisinvalid,
							Toast.LENGTH_SHORT).show();
				}
				M.m().setHost(maddr);
				M.m().setPort(Integer.parseInt(mport));
				M.m().sendMessage(new Observer() {

					@Override
					public void update(Observable arg0, final Object arg1) {
						if (arg1 instanceof BoardResponse) {
							BoardResponse r = (BoardResponse) arg1;
							if (r.getMessage() != null
									&& !r.getMessage().equals("")) {
								getActivity().runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if (InetAddressUtils
												.isIPv4Address(maddr)) {
											ContentValues cv = new ContentValues();
											cv.put("name", "hostip");
											cv.put("value", maddr);
											M.m()
													.getLocaldb()
													.insOrUpd("settings", cv,
															"w");
											cv.clear();
											cv.put("name", "hostport");
											cv.put("value", mport);
											M.m()
													.getLocaldb()
													.insOrUpd("settings", cv,
															"w");
											M.m()
													.getLocaldb()
													.insOrUpd("hosts", cv, null);
										}
										Toast.makeText(
												getActivity(),
												R.string.connectionwassuccessful,
												Toast.LENGTH_SHORT).show();
									}
								});

								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											Thread.sleep(5000);
										} catch (InterruptedException e) {
											Log.e("InnerThread", e.getMessage());
										}

									}
								}).start();
							}
						} else if (arg1 instanceof Integer) {
							if (getActivity() == null) {
								return;
							}
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(getActivity(),
											(Integer) arg1, Toast.LENGTH_SHORT)
											.show();
								}
							});
						}
					}
				}, "H");
			}
		});

		return v;
	}
}
