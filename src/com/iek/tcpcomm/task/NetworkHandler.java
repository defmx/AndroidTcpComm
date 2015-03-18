package com.iek.tcpcomm.task;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observer;

import android.os.AsyncTask;

public class NetworkHandler extends AsyncTask<String, Integer, String> {
	private Socket mSocket;
	private String hostIp;
	private int hostPort;
	private Observer mObserver;

	public NetworkHandler(String ip, int port, Observer observer) {
		hostIp = ip;
		hostPort = port;
		mObserver = observer;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			if (mSocket == null) {
				mSocket = new Socket(hostIp, hostPort);
			}
			InputStream in = mSocket.getInputStream();
			byte[] buff = new byte[1024];
			int bread = 0;
			ByteArrayOutputStream ostream = new ByteArrayOutputStream(1024);
			PrintWriter pw = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(mSocket.getOutputStream())));
			pw.println(params[0]);
			pw.flush();
			while ((bread = in.read(buff)) != -1) {
				ostream.write(buff, 0, bread);
				if (mObserver != null) {
					mObserver.update(null, ostream.toString("UTF-8"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void closeConnection() {
		if (mSocket != null && !mSocket.isClosed()) {
			try {
				mSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public int getHostPort() {
		return hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}
}
