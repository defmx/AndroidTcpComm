package com.iek.wiflyremote.stat;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Random;
import java.util.UUID;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.iek.wiflyremote.data.LocalDb;

public class M {
	private static M m = new M();
	private String host;
	private int port;
	// private Board board;
	// private LocalDb localdb;
	private SQLiteDatabase db;
	private List<Observer> boardRespObservers = new ArrayList<Observer>();
	private Socket globalSocket;
	private LocalDb localdb;
	private Thread listenThr;
	private long mStopId;
	private boolean appIsActive;
	private long sttime = 0;

	public Socket getGlobalSocket() {
		return globalSocket;
	}

	public void setGlobalSocket(Socket globalSocket) {
		this.globalSocket = globalSocket;
	}

	private M() {
	}

	public static M m() {
		return m;
	}

	public static class catalog {
		public static Map<String, String> settings;
	}

	public void connect(final Observer observer) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					M.m().setGlobalSocket(new Socket());
					getGlobalSocket().connect(
							new InetSocketAddress(M.m().getHost(), M.m()
									.getPort()), 3000);
					if (observer != null) {
						observer.update(null, "ok");
					}
					listenThr = new Thread(new Runnable() {

						@Override
						public void run() {
							InputStream in;
							try {
								in = getGlobalSocket().getInputStream();
								byte[] buff = new byte[1024];
								int bread = 0;

								while ((bread = in.read(buff)) != -1) {
									final ByteArrayOutputStream ostream = new ByteArrayOutputStream(
											1024);
									ostream.write(buff, 0, bread);
									try {
										final String s = ostream.toString(
												"UTF-8").replace("\n", "");
										///
										if (s.startsWith("P#=") && !s.contains("C")) {
											if (sttime == 0) {
												sttime = System.currentTimeMillis();
											}
											String str = s.replace("P#=", "");
											String[] parts;
										parts = str.split(",");
										if(parts.length==4){
											//double v = Double.parseDouble(parts[0]);
											//double vm = Double.parseDouble(parts[1]);
											//double dt = Double.parseDouble(parts[2]);
											//double d = Double.parseDouble(parts[3]);
											long t = System.currentTimeMillis();
											if (t - sttime >= 60000) {
												sttime = 0;
												ContentValues cv = new ContentValues();
												cv.put("utime", t/1000);
												cv.put("v", parts[0]);
												cv.put("vm", parts[1]);
												cv.put("dt", parts[2]);
												cv.put("d", parts[3]);
												M.m().getLocaldb().insOrUpd("statistics", cv, "");
											}
										}
										}
										
										///
										if (s.contains("?")) {
											ContentValues cv = new ContentValues();
											cv.put("start_time",
													System.currentTimeMillis()/1000);
											mStopId = getLocaldb().insOrUpd(
													"stops", cv, null);
										}
										if (s.contains("!") && mStopId != 0) {
											ContentValues cv = new ContentValues();
											cv.put("_id", mStopId);
											cv.put("end_time",
													System.currentTimeMillis()/1000);
											getLocaldb().insOrUpd("stops", cv,
													null);
											mStopId = 0;
										}
										Log.i("SVRRESP", s);
										for (Observer obs : boardRespObservers) {
											obs.update(null, s);
										}
									} catch (UnsupportedEncodingException e) {
										Log.e("SVRRESP", e.getMessage());
									}
								}
							} catch (IOException e1) {
								e1.printStackTrace();
							}
						}
					});
					listenThr.start();
				} catch (Exception e) {
					Log.i("CONNECT",
							e.getMessage() == null ? ":(" : e.getMessage());
					if (observer != null) {
						observer.update(null, "no");
					}
				}
			}
		}).start();
	}

	public void disconnect() {
		if (listenThr != null && listenThr.isAlive()) {
			try {
				globalSocket.shutdownInput();
				globalSocket.shutdownOutput();
				listenThr.join();
				globalSocket.close();
			} catch (Exception e) {
				Log.e("M DISCONNECT", e.getMessage());
			}
		}
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
				} catch (Exception e) {
					Log.e("SEND",
							e.getMessage() == null ? ":(" : e.getMessage());
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

	public void addBoardRespObserver(Observer observer) {
		if (observer != null) {
			boardRespObservers.add(observer);
		}
	}

	public void delBoardRespObserver(Observer observer) {
		if (observer != null) {
			boardRespObservers.remove(observer);
		}
	}

	public void clearBoardRespObserver() {
		this.boardRespObservers.clear();
	}

	// public Board getBoard() {
	// return board;
	// }
	//
	// public void setBoard(Board board) {
	// this.board = board;
	// }
	//
	public LocalDb getLocaldb() {
		return localdb;
	}

	public void setLocaldb(LocalDb localdb) {
		this.localdb = localdb;
		this.db = localdb.getWritableDatabase();
		localdb.onCreate(db);
	}

	public boolean appIsActive() {
		Log.i("Receiver3", "App is " + (appIsActive ? "" : "not ") + "active");
		return appIsActive;
	}

	public void setAppIsActive(boolean appIsActive) {
		this.appIsActive = appIsActive;
	}

}
