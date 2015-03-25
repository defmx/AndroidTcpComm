package com.iek.tcpcomm.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

public class LocalDb extends SQLiteOpenHelper {

	private SQLiteDatabase db;

	public LocalDb(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		String q1 = "CREATE TABLE IF NOT EXISTS params(_id integer primary key autoincrement,name text unique,value text)";
		String q2 = "CREATE TABLE IF NOT EXISTS stopreasons(_id integer primary key autoincrement,name text unique)";
		try {
			if (db.isOpen()) {
				db.execSQL(q1);
				db.execSQL(q2);
			}
		} catch (SQLiteException e) {
			Log.e("LocalDb", e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public int insOrUpd(String table, ContentValues cv, String where) {
		int count = 0;
		if (cv.containsKey("_id")) {
			cv.remove("_id");
		}
		try {
			db.beginTransaction();
			if (!cv.containsKey("_id")) {
				db.insert(table, null, cv);
				count = 1;
			} else {
				count = db.update(table, cv, where, null);
			}
		} catch (SQLiteException e) {
			Log.e("LocalDb", e.getMessage());
		} finally {
		}
		return count;
	}

	public SparseArray<String> selectCat(String table, String where) {
		SparseArray<String> s = new SparseArray<String>();
		String q = "SELECT _id,name FROM " + table;
		if (where != null && !where.equals("")) {
			q += " WHERE " + where;
		}
		try {
			Cursor c = db.rawQuery(q, null);
			if (c.moveToFirst()) {
				do {
					s.append(c.getInt(0), c.getString(1));
				} while (c.moveToNext());
			}
		} catch (SQLiteException e) {
			Log.e("LocalDb", e.getMessage());
		}

		return s;
	}
}
