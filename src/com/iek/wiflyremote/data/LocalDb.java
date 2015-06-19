package com.iek.wiflyremote.data;

import java.util.ArrayList;
import java.util.List;

import com.iek.wiflyremote.stat.CatRow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDb extends SQLiteOpenHelper {

	private SQLiteDatabase db;

	public LocalDb(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		this.db = db;
		String q1 = "CREATE TABLE IF NOT EXISTS settings(_id integer primary key autoincrement,name text unique,value text)";
		String q3 = "CREATE TABLE IF NOT EXISTS hosts(_id integer primary key autoincrement,name text unique,value text)";
		String q2 = "CREATE TABLE IF NOT EXISTS cstopreasons(_id integer primary key autoincrement,name text unique,value text)";
		String q4 = "CREATE TABLE IF NOT EXISTS statistics(_id integer primary key autoincrement,utime integer,v real,vm real,dt real,d real)";
		try {
			if (db.isOpen()) {
				db.execSQL(q1);
				db.execSQL(q2);
				db.execSQL(q3);
				db.execSQL(q4);
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
				if (cv.containsKey("name")) {
					String n = cv.getAsString("name");
					count = db.update(table, cv, "name=?", new String[] { n });
					if (count == 0) {
						db.insert(table, null, cv);
						count = 1;
					}
				} else {
					db.insert(table, null, cv);
					count = 1;
				}
			} else {
				count = db.update(table, cv, where, null);
			}
			db.setTransactionSuccessful();
		} catch (SQLiteException e) {
			Log.e("LocalDb", e.getMessage());
		} finally {
			db.endTransaction();
		}
		Log.i("LocalDb", count + " rows affected");
		return count;
	}

	public List<Object[]> select(String table, String where) {
		List<Object[]> r = new ArrayList<Object[]>();

		String q = "SELECT * FROM " + table;
		if (where != null && where != "") {
			q += " WHERE" + where;
		}
		try {
			Cursor c = db.rawQuery(q, null);
			if (c.moveToFirst()) {
				do {
					Object[] o = new Object[c.getColumnCount()];
					for (int i = 0; i < c.getColumnCount(); i++) {
						switch (c.getType(i)) {
						case Cursor.FIELD_TYPE_INTEGER:
							o[i] = c.getInt(i);
							break;
						case Cursor.FIELD_TYPE_STRING:
							o[i] = c.getString(i);
							break;
						case Cursor.FIELD_TYPE_FLOAT:
							o[i] = c.getFloat(i);
							break;
						default:
							break;
						}
					}
					r.add(o);
				} while (c.moveToNext());
			}
		} catch (SQLiteException e) {
			Log.e("LocalDb", e.getMessage());
		}

		return r;
	}

	public List<CatRow> selectCat(String table) {
		List<CatRow> s = new ArrayList<CatRow>();
		String q = "SELECT _id,name,value FROM " + table;
		try {
			Cursor c = db.rawQuery(q, null);
			if (c.moveToFirst()) {
				do {
					CatRow cr = new CatRow(c.getInt(0), c.getString(1),
							c.getString(0));
					s.add(cr);
				} while (c.moveToNext());
			}
		} catch (SQLiteException e) {
			Log.e("LocalDb", e.getMessage());
		}

		return s;
	}
}
