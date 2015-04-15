package com.iek.wiflyremote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Control extends Activity {
	public static final int MESSAGE_DATA_RECEIVE = 0;

	TextView textLeft, textRight;

	Boolean task_state = true;
	Vibrator vibrator;

	Socket s;
	String ip;
	int port;
	private boolean keep = true;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.control);
		ip = getIntent().getExtras().getString("IP");
		port = Integer.parseInt(getIntent().getExtras().getString("PORT"));

		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

		textLeft = (TextView) findViewById(R.id.textLeft);
		textRight = (TextView) findViewById(R.id.textRight);
		Runnable readThread = new Runnable() {

			public void run() {

			}
		};
		new Thread(readThread).start();
		Log.i("Check IP", ip + ":" + port);
	}

	@Override
	protected void onStart() {
		super.onStart();

		Runnable statsThr = new Runnable() {

			@Override
			public void run() {
				while (keep) {
					try {
						Thread.sleep(1000);
						sendMessage("P#");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		sendMessage("H");
		new Thread(statsThr).start();
	}

	public void onPause() {
		super.onPause();
		task_state = false;

		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		finish();
	}

	public void sendMessage(final String msg) {
		new Thread(new Runnable() {
			Socket mSocket;

			@Override
			public void run() {
				try {
					if (mSocket == null) {
						mSocket = new Socket();
						mSocket.connect(new InetSocketAddress(ip, port), 3000);
						keep = true;
					} else {
						keep = true;
					}
					InputStream in = mSocket.getInputStream();
					byte[] buff = new byte[1024];
					int bread = 0;
					final ByteArrayOutputStream ostream = new ByteArrayOutputStream(
							1024);
					PrintWriter pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(mSocket.getOutputStream())));
					pw.println(msg);
					pw.flush();
					final long st = System.currentTimeMillis();
					while ((bread = in.read(buff)) != -1) {
						ostream.write(buff, 0, bread);
						long end = System.currentTimeMillis();
						try {
							final String s = ostream.toString("UTF-8").replace(
									"\n", "");
							Log.i("SVRRESP", s);
							runOnUiThread(new Runnable() {
								public void run() {
									if (s.startsWith("H")) {
										Toast.makeText(getApplicationContext(),
												"Connect OK",
												Toast.LENGTH_SHORT).show();
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
											textRight.setText("V=" + v
													+ ", Vm=" + vm + ", Tm="
													+ dt + ", D=" + d);
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
				} catch (Exception e) {
					keep = false;
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

	public void sendData(String str) {
		try {
			s = new Socket();
			s.connect((new InetSocketAddress(InetAddress.getByName(ip), port)),
					2000);
			OutputStream out = s.getOutputStream();
			out.write(str.getBytes());
			out.flush();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					s.getInputStream()));
			final String strRead = in.readLine();
			runOnUiThread(new Runnable() {
				public void run() {
					if (strRead.startsWith("P#=")) {
						String str = strRead.replace("P#=", "");
						double v, vm, dt, d;
						String[] parts;
						try {
							parts = str.split(",");
							v = Double.parseDouble(parts[0]);
							vm = Double.parseDouble(parts[1]);
							dt = Double.parseDouble(parts[2]);
							d = Double.parseDouble(parts[3]);
							textRight.setText("V=" + v + ", Vm=" + vm + "Tm="
									+ dt + "D=" + d);
						} catch (ArrayIndexOutOfBoundsException e) {
							Log.e("Console", e.getMessage());
						}
					}
				}
			});

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Connection Failed", Toast.LENGTH_SHORT).show();
				}
			});
			finish();
		} catch (NullPointerException e) {
			Toast.makeText(getApplicationContext(),
					"Please check your WiFi Connection", Toast.LENGTH_SHORT)
					.show();
		}
	}

}
