package com.iek.tcpcomm.ui;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

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
import com.iek.tcpcomm.task.NetworkHandler;
import com.iek.tcpcomm.ui.adapters.MultipleViewAdapter;

public class ConsoleFragment extends Fragment {
	private ListView mListView;
	private Thread listenerThread;
	private ServerSocket serverSocket;
	private String mHost = "192.168.0.9";
	private String mPort = "9999";
	private TextView mHostTextView;
	private NetworkHandler mNwHndlr;

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mHostTextView = new TextView(getActivity());
		TextView subtitle1 = new TextView(getActivity());
		View v = inflater.inflate(R.layout.fragment_main, null);
		final EditText cmdEdit = (EditText) v.findViewById(R.id.cmdEdit);
		MultipleViewAdapter adapter = new MultipleViewAdapter();
		cmdEdit.setImeActionLabel("Send", KeyEvent.KEYCODE_ENTER);
		cmdEdit.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == KeyEvent.KEYCODE_ENTER) {
					sendMessage(cmdEdit.getText());
					return true;
				}
				return false;
			}
		});
		mListView = (ListView) v.findViewById(R.id.listView);
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
										mHostTextView.setText("Host is: "
												+ mHost + ":" + mPort);
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
	}

	private void sendMessage(final CharSequence text) {
		TextView t = new TextView(getActivity());
		t.setLayoutParams(new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		t.setText("> " + text);
		((MultipleViewAdapter) mListView.getAdapter()).getViewList().add(t);
		((MultipleViewAdapter) mListView.getAdapter()).notifyDataSetChanged();
		new Thread(new Runnable() {
			Socket mSocket;

			@Override
			public void run() {
				try {
					if (mSocket == null) {
						mSocket = new Socket(mHost, Integer.parseInt(mPort));
					}
					InputStream in = mSocket.getInputStream();
					byte[] buff = new byte[1024];
					int bread = 0;
					final ByteArrayOutputStream ostream = new ByteArrayOutputStream(
							1024);
					PrintWriter pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(mSocket.getOutputStream())));
					pw.println(text);
					pw.flush();
					final long st = System.currentTimeMillis();
					while ((bread = in.read(buff)) != -1) {
						ostream.write(buff, 0, bread);
						ConsoleFragment.this.getActivity().runOnUiThread(
								new Runnable() {

									@Override
									public void run() {
										String s = "";
										long end = System.currentTimeMillis();
										try {
											s = ostream.toString("UTF-8");
											s = s.replace("\n", "");
											s += "\n" + (end - st) / 1000f
													+ " s";
										} catch (UnsupportedEncodingException e) {
											Log.e("SVRRESP", e.getMessage());
										}
										TextView t = new TextView(getActivity());
										t.setLayoutParams(new AbsListView.LayoutParams(
												ViewGroup.LayoutParams.MATCH_PARENT,
												ViewGroup.LayoutParams.WRAP_CONTENT));
										t.setText(s);
										MultipleViewAdapter adapter = ((MultipleViewAdapter) mListView
												.getAdapter());
										adapter.getViewList().add(t);
										adapter.notifyDataSetChanged();
										mListView
												.smoothScrollToPosition(adapter
														.getCount() - 1);
									}
								});
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
}
