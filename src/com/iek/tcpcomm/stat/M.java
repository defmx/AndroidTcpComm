package com.iek.tcpcomm.stat;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Observer;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iek.tcpcomm.data.LocalDb;

public class M {
	private static M m = new M();
	private String host;
	private int port;
	private Board board;
	private LocalDb localdb;
	private SQLiteDatabase db;

	private M() {
		board = new Board();
	}

	public static M m() {
		return m;
	}

	public void sendMessage(final Observer observer, final CharSequence text) {
		new Thread(new Runnable() {
			Socket mSocket;

			@Override
			public void run() {
				try {
					if (mSocket == null) {
						mSocket = new Socket(getHost(), getPort());
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
						String s = "";
						long end = System.currentTimeMillis();
						try {
							s = ostream.toString("UTF-8");
							s = s.replace("\n", "");
							s += " (" + (end - st) / 1000f + " s)";
							Log.i("SVRRESP", s);
							if (observer != null) {
								observer.update(null, s);
							}
						} catch (UnsupportedEncodingException e) {
							Log.e("SVRRESP", e.getMessage());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public LocalDb getLocaldb() {
		return localdb;
	}

	public void setLocaldb(LocalDb localdb) {
		this.localdb = localdb;
		this.db = localdb.getWritableDatabase();
		localdb.onCreate(db);
	}
}
