/*
 * Chris Card
 * 9/14/12
 * This is a data base adapter to query and set sql data tables for restaurnts to persit states of
 * restaurants
 */
package csci422.CandN.to_dolist;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.Cursor;
import android.content.ContentValues;

public class ToDoHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "ToDo.db";
	private static final int SCHEMA_VERSION = 1;

	public ToDoHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//modify table elements for the todolist rather than restaurants
		db.execSQL("CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, address TEXT, type TEXT, notes TEXT, feed TEXT, lat REAL, lon REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	   /*ToDo on upgrade if schema changes*/
	}

	public Cursor getById(String id)
	{
		String[] args = {id};

		return getReadableDatabase().rawQuery("SELECT _id, name, address, type, notes, feed, lat, lon FROM restaurants WHERE _ID=?", args);
	}

	public void update(String id, String name, String address, String type, String notes, String feed)
	{
		ContentValues cv = new ContentValues();
		String[] args = {id};

		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);

		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}

	public void insert(String name, String address, String type, String notes, String feed)
	{
		ContentValues cv = new ContentValues();

		cv.put("name", name);
		cv.put("address", address);
		cv.put("type", type);
		cv.put("notes", notes);
		cv.put("feed", feed);

		getWritableDatabase().insert("restaurants", "name", cv);
	}

	public Cursor getAll(String orderBy)
	{
		return getReadableDatabase().rawQuery("SELECT _id, name, address, type, notes, feed, lat, lon FROM restaurants ORDER BY "+orderBy, null);
	}

	public void updateLocation(String id, double lat, double lon)
	{
		ContentValues cv = new ContentValues();
		String args[] = {id};

		cv.put("lat", lat);
		cv.put("lon", lon);

		getWritableDatabase().update("restaurants", cv, "_ID=?", args);
	}

	public String getName(Cursor c)
	{
		return c.getString(1);
	}

	public String getAddress(Cursor c)
	{
		return c.getString(2);
	}

	public String getType(Cursor c)
	{
		return c.getString(3);
	}

	public String getNotes(Cursor c)
	{
		return c.getString(4);
	}

	public String getFeed(Cursor c)
	{
		return c.getString(5);
	}

	public double getLatitude(Cursor c)
	{
		return c.getDouble(6);
	}

	public double getLongitude(Cursor c)
	{
		return c.getDouble(7);
	}
}
