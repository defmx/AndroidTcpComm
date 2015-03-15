package com.iek.tcpcomm.ui;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.iek.tcpcomm.R;
import com.iek.tcpcomm.task.CommunicationThread;
import com.iek.tcpcomm.task.NetworkHandler;
import com.iek.tcpcomm.ui.adapters.MultipleViewAdapter;

public class ConsoleFragment extends Fragment {
	private ListView mListView;
	private Thread listenerThread;
	private ServerSocket serverSocket;
	private String mHost = "192.168.0.9";
	private String mPort = "9999";
	private TextView mHostTextView;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mHostTextView = new TextView(getActivity());
		EditText messageEditText = new EditText(getActivity());
		View v = inflater.inflate(R.layout.fragment_main, null);
		MultipleViewAdapter adapter = new MultipleViewAdapter();
		mListView = (ListView) v.findViewById(R.id.listView);
		mHostTextView.setText("Host is: " + mHost + " : " + mPort);
		mHostTextView.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		messageEditText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
					sendMessage(v.getText());
				}
				return false;
			}
		});
		adapter.setViewList(new ArrayList<View>());
		adapter.getViewList().add(mHostTextView);
		adapter.getViewList().add(messageEditText);
		for (View vv : adapter.getViewList()) {
			vv.setPadding(0, 5, 0, 5);
		}
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							getActivity());
					final EditText input = new EditText(getActivity());
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.MATCH_PARENT);
					input.setLayoutParams(lp);
					input.setHint(R.string.exipaddr);
					dialog.setTitle(R.string.sethost);
					dialog.setView(input);
					dialog.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									String addr = input.getText().toString();
									String port;
									try {
										port = addr.split(":")[1];
										addr = addr.split(":")[0];
									} catch (ArrayIndexOutOfBoundsException e) {
										return;
									}
									if (InetAddressUtils.isIPv4Address(addr)) {
										mHost = addr;
										mPort = port;
										mHostTextView.setText("Host is: " + mHost + ":"
												+ mPort);
									}
								}
							});

					dialog.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.cancel();
								}
							});
					dialog.show();
				}
			}
		});
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		sendMessage("hola");
		listenerThread = new Thread(new Runnable() {

			@Override
			public void run() {
				Socket s = null;
				try {
					serverSocket = new ServerSocket(6000);
				} catch (IOException e) {
					Log.e("ConsoleFragment", e.getMessage());
				}
				while (!Thread.currentThread().isInterrupted()) {
					try {
						s = serverSocket.accept();
						Thread t2 = new Thread(new CommunicationThread(s,
								new Observer() {

									@Override
									public void update(Observable observable,
											Object data) {
									}
								}));
						t2.start();
					} catch (IOException e) {
						Log.e("ConsoleFragment", e.getMessage());
					}
				}
			}
		});
		// listenerThread.start();
	}

	private void sendMessage(CharSequence text) {
		NetworkHandler nh = new NetworkHandler();
		nh.execute(mHost, mPort, text + "");
	}

}
