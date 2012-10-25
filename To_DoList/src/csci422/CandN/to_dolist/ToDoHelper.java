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
		db.execSQL("CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, address TEXT, notes TEXT, date TEXT, state REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	   /*ToDo on upgrade if schema changes*/
	}

	public Cursor getById(String id)
	{
		String[] args = {id};

		return getReadableDatabase().rawQuery("SELECT _id, title, address, notes, date, state FROM todos WHERE _ID=?", args);
	}

	public void update(String id, String title, String address, String notes, String date, int state)
	{
		ContentValues cv = new ContentValues();
		String[] args = {id};

		cv.put("title", title);
		cv.put("address", address);
		cv.put("notes", notes);
		cv.put("date", date);
		cv.put("state", state);

		getWritableDatabase().update("todos", cv, "_ID=?", args);
	}

	public void insert(String title, String address, String notes, String date, int state)
	{
		ContentValues cv = new ContentValues();

		cv.put("title", title);
		cv.put("address", address);
		cv.put("notes", notes);
		cv.put("date", date);
		cv.put("state", state);

		getWritableDatabase().insert("todos", "title", cv);
	}

	public Cursor getAll(String orderBy)
	{
		return getReadableDatabase().rawQuery("SELECT _id, title, address, notes, date, state FROM restaurants ORDER BY "+orderBy, null);
	}


	public String getTitle(Cursor c)
	{
		return c.getString(1);
	}

	public String getAddress(Cursor c)
	{
		return c.getString(2);
	}

	public String getNotes(Cursor c)
	{
		return c.getString(4);
	}

	public String getDate(Cursor c)
	{
		return c.getString(5);
	}

	public int getState(Cursor c)
	{
		return c.getInt(6);
	}
}
