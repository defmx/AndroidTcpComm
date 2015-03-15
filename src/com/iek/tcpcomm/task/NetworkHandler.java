package com.iek.tcpcomm.task;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class NetworkHandler extends AsyncTask<String, Integer, String> {

	@Override
	protected String doInBackground(String... params) {
		try {
			Socket s = new Socket(params[0], Integer.parseInt(params[1]));
			PrintWriter pw = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream())));
			pw.println(params[2]);
			pw.flush();
			s.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
