/*
 * Chris Card
 * 9/14/12
 * This is a data base adapter to query and set sql data tables for restaurnts to persit states of
 * restaurants
 */
package csci422.CandN.to_dolist;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
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
		db.execSQL("CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, address TEXT, list TEXT, notes TEXT, date TEXT, state REAL, priority REAL, notified REAL, notifyID REAL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	   /*ToDo on upgrade if schema changes*/
	}

	public Cursor getById(String id)
	{
		String[] args = {id};

		return getReadableDatabase().rawQuery("SELECT _id, title, address, list, notes, date, state, priority, notified, notifyID FROM todos WHERE _ID=?", args);
	}
	
	public void delete(String id)
	{
		String ID[] ={id};
		
		getWritableDatabase().delete("todos", "_ID=?", ID);
	}
	
	public String getId(Cursor c){
		return c.getString(0);//TODO is this the right number?
	}

	public void update(String id, String title, String address, String list, String notes, String date, int state, int priority)
	{
		ContentValues cv = new ContentValues();
		String[] args = {id};

		cv.put("title", title);
		cv.put("address", address);
		cv.put("list", list);
		cv.put("notes", notes);
		cv.put("date", date);
		cv.put("state", state);
		cv.put("priority", priority);

		getWritableDatabase().update("todos", cv, "_ID=?", args);
	}

	public void insert(String title, String address, String list, String notes, String date, int state, int priority)
	{
		ContentValues cv = new ContentValues();
		cv.put("title", title);
		cv.put("address", address);
		cv.put("list", list);
		cv.put("notes", notes);
		cv.put("date", date);
		cv.put("state", state);
		cv.put("priority", priority);
		cv.put("notified", 0);
		cv.put("notifyID", System.currentTimeMillis());//this field should only be set here and not modified any where else!!!!

		getWritableDatabase().insert("todos", "title", cv);
	}

	public Cursor getAll(String orderBy)
	{
		return getReadableDatabase().rawQuery("SELECT _id, title, address, list, notes, date, state, priority, notified, notifyID FROM todos ORDER BY "+orderBy, null);
	}
	
	public void updateState(String id, int state)
	{
		String args[] = {id};
		ContentValues cv = new ContentValues();
		
		cv.put("state", state);
		
		getWritableDatabase().update("todos", cv, "_ID=?", args);
	}


	public void notified(String id, boolean val)
	{
		String args[] = {id};
		ContentValues cv = new ContentValues();
		
		cv.put("notified", val?1:0);
		
		getWritableDatabase().update("todos", cv, "_ID=?", args);
	}
	
	public String getTitle(Cursor c)
	{
		return c.getString(1);
	}

	public String getAddress(Cursor c)
	{
		return c.getString(2);
	}

	public String getList(Cursor c)
	{
		return c.getString(3);
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
	public int getPriority(Cursor c)
	{
		return c.getInt(7);
	}
	public boolean getNotified(Cursor c)
	{
		return c.getInt(8)>0;
	}
	
	public int getNotifyID(Cursor c)
	{
		return c.getInt(9);
	}
}
