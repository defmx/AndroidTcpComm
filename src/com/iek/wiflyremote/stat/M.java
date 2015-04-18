package com.iek.wiflyremote.stat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Observer;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class M {
	private static M m = new M();
	private String host;
	private int port;
	// private Board board;
	// private LocalDb localdb;
	private SQLiteDatabase db;
	private Observer boardRespObserver;
	private Socket globalSocket;

	public Socket getGlobalSocket() {
		return globalSocket;
	}

	public void setGlobalSocket(Socket globalSocket) {
		this.globalSocket = globalSocket;
	}

	private M() {
		// board = new Board();
	}

	public static M m() {
		return m;
	}

	public static class catalog {
		public static Map<String, String> settings;
	}

	public void sendMessage(final Observer observer, final String msg) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				PrintWriter pw;
				try {
					pw = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(
									globalSocket.getOutputStream())));

					pw.println(msg);
					pw.flush();
					if (observer != null) {
						observer.update(null, msg);
					}
					Log.i("SEND", msg);
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

	public Observer getBoardRespObserver() {
		return boardRespObserver;
	}

	public void setBoardRespObserver(Observer boardRespObserver) {
		this.boardRespObserver = boardRespObserver;
	}

	// public Board getBoard() {
	// return board;
	// }
	//
	// public void setBoard(Board board) {
	// this.board = board;
	// }
	//
	// public LocalDb getLocaldb() {
	// return localdb;
	// }
	//
	// public void setLocaldb(LocalDb localdb) {
	// this.localdb = localdb;
	// this.db = localdb.getWritableDatabase();
	// localdb.onCreate(db);
	// }
	//
	// public void loadCatalogs() {
	// catalog.settings = new HashMap<String, String>();
	// List<CatRow> l = getLocaldb().selectCat("settings");
	// for (CatRow c : l) {
	// catalog.settings.put(c.getName(), c.getValue());
	// }
	//
	// }
}
