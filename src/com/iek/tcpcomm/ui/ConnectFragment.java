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
		Button b = (Button) v.findViewById(R.id.connectButton);

		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String addr = e.getText().toString();
				String port = "";
				try {
					port = addr.split(":")[1];
					addr = addr.split(":")[0];
				} catch (ArrayIndexOutOfBoundsException e) {
					Toast.makeText(getActivity(), R.string.addressisinvalid,
							Toast.LENGTH_SHORT).show();
				}
				if (InetAddressUtils.isIPv4Address(addr)) {
					M.m().setHost(addr);
					M.m().setPort(Integer.parseInt(port));
					Toast.makeText(getActivity(),
							"Host: " + addr + ", Puerto: " + port,
							Toast.LENGTH_SHORT).show();
					ContentValues cv = new ContentValues();
					cv.put("name", "hostip");
					cv.put("value", addr);
					M.m().getLocaldb().insOrUpd("settings", cv, null);
					cv.put("name", "hostport");
					cv.put("value", port);
					M.m().getLocaldb().insOrUpd("settings", cv, null);
				}
				M.m().sendMessage(new Observer() {

					@Override
					public void update(Observable arg0, Object arg1) {
						if (arg1 instanceof BoardResponse) {
							BoardResponse r = (BoardResponse) arg1;
							if (r.getMessage() != null
									&& r.getMessage().equals("")) {
								Toast.makeText(getActivity(),
										R.string.connectionwassuccessful,
										Toast.LENGTH_SHORT).show();
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
						}
					}
				}, "H");
			}
		});

		return v;
	}
}
