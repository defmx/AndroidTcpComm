package com.iek.tcpcomm.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observer;

import android.util.Log;

public class CommunicationThread implements Runnable {
	private BufferedReader input;
	private Observer observer;

	public CommunicationThread(Socket s, Observer o) {
		this.observer = o;
		try {
			input = new BufferedReader(
					new InputStreamReader(s.getInputStream()));
		} catch (IOException e) {
			Log.e("CommunicationThread", e.getMessage());
		}
	}

	@Override
	public void run() {
		while (!Thread.currentThread().isInterrupted()) {
			try {
				String msg = input.readLine();
				if (observer != null) {
					observer.update(null, msg);
				}
			} catch (IOException e) {
				Log.e("ConsoleFragment", e.getMessage());
			}
		}
	}
}
