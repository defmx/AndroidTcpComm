package com.iek.wiflyremote;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Control extends Activity {
	public static final int MESSAGE_DATA_RECEIVE = 0;

	private TextView textLeft;
	private TextView textRight;
	private EditText etGInstVel;
	private EditText etGAvgVel;
	private EditText etGLinMet;
	private EditText etGDeadT;

	Socket s;
	String ip;
	int port;
	private Socket mSocket;
	private Runnable statsThr = new Runnable() {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000);
					sendMessage("P#", false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	private Runnable listenThr = new Runnable() {

		@Override
		public void run() {
			InputStream in;
			try {
				in = mSocket.getInputStream();
				byte[] buff = new byte[1024];
				int bread = 0;

				while ((bread = in.read(buff)) != -1) {
					final ByteArrayOutputStream ostream = new ByteArrayOutputStream(
							1024);
					ostream.write(buff, 0, bread);
					try {
						final String s = ostream.toString("UTF-8").replace(
								"\n", "");
						Log.i("SVRRESP", s);
						runOnUiThread(new Runnable() {
							public void run() {
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
										textRight.setText("Velocidad=" + v
												+ "\n Vel. Media=" + vm
												+ "\n Tiempo Muerto=" + dt
												+ "\n Distancia=" + d);
									} catch (ArrayIndexOutOfBoundsException e) {
										Log.e("Console", e.getMessage());
									}
								}
							}
						});
					} catch (UnsupportedEncodingException e) {
						Log.e("SVRRESP", e.getMessage());
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.control);
		etGInstVel = (EditText) findViewById(R.id.etGInstVel);
		etGAvgVel = (EditText) findViewById(R.id.etGAvgVel);
		etGLinMet = (EditText) findViewById(R.id.etGLinMet);
		etGDeadT = (EditText) findViewById(R.id.etGDeadT);
		Button btGInstVel = (Button) findViewById(R.id.btGInstVel);
		Button btGAvgVel = (Button) findViewById(R.id.btGAvgVel);
		Button btGLinMet = (Button) findViewById(R.id.btGLinMet);
		Button btGDeadT = (Button) findViewById(R.id.btGDeadT);

		ip = getIntent().getExtras().getString("IP");
		port = Integer.parseInt(getIntent().getExtras().getString("PORT"));

		textLeft = (TextView) findViewById(R.id.textLeft);
		textRight = (TextView) findViewById(R.id.textRight);

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
					sendMessage("V" + String.format("%03d", i) + "#", true);
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
					sendMessage("A" + String.format("%03d", i) + "#", true);
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
					sendMessage("M" + String.format("%05d", i) + "#", true);
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
					sendMessage("T" + String.format("%03d", i) + "#", true);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		connect();
	}

	public void onPause() {
		super.onPause();
		try {
			if (s != null) {
				s.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		finish();
	}

	public void sendMessage(final String msg, final boolean toast) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				PrintWriter pw;
				try {
					pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(mSocket.getOutputStream())));

					pw.println(msg);
					pw.flush();
					if (toast) {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"Send OK", Toast.LENGTH_SHORT).show();
							}
						});
					}
					Log.i("SEND", msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (mSocket == null) {
						mSocket = new Socket();
						mSocket.connect(new InetSocketAddress(ip, port), 3000);
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(getApplicationContext(),
										"Connect OK", Toast.LENGTH_SHORT)
										.show();
							}
						});
						new Thread(listenThr).start();
					}
				} catch (Exception e) {
					Log.i("CONNECT",
							e.getMessage() == null ? ":(" : e.getMessage());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							Toast.makeText(getApplicationContext(),
									"Connect Failed", Toast.LENGTH_SHORT)
									.show();
						}

					});
					finish();
				}
			}
		}).start();
	}

}
