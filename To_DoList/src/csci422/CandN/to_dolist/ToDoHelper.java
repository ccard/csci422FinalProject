/*
 * Chris Card
 * 9/14/12
 * This is a data base adapter to query and set tasks and their elements
 */
package csci422.CandN.to_dolist;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.util.Log;
import android.content.ContentValues;

public class ToDoHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "ToDo.db";
	private static final int SCHEMA_VERSION = 1;
	public static final String tag = "ToDoHelper";
	
	//quieres to data base to initialize or get elements in the table
	private static final String DATABASE_INIT = "CREATE TABLE todos (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, address TEXT, list TEXT, notes TEXT, date TEXT, state REAL, priority REAL, notified REAL, notifyID REAL);";
	private static final String DATABASE_BYID_Q = "SELECT _id, title, address, list, notes, date, state, priority, notified, notifyID FROM todos WHERE _ID=?";
	private static final String DATABASE_GETALL_Q = "SELECT _id, title, address, list, notes, date, state, priority, notified, notifyID FROM todos ORDER BY ";
	
	public ToDoHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//modify table elements for the todolist rather than restaurants
		db.execSQL(DATABASE_INIT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	   /*ToDo on upgrade if schema changes*/
	}

	/**
	 * This returns a cursor that contains the element in data base with that id
	 * @param id of the element in the data base
	 * @return cursor containing the requested element
	 */
	public Cursor getById(String id)
	{
		String[] args = {id};

		return getReadableDatabase().rawQuery(DATABASE_BYID_Q, args);
	}
	
	/**
	 * This gets all entries in data base and returns them in the order based on the passed in
	 * request
	 * @param orderBy  the sql syntax order query to pass to the database
	 * @return a cursor containing all rows in the table
	 */
	public Cursor getAll(String orderBy)
	{
		return getReadableDatabase().rawQuery(DATABASE_GETALL_Q+orderBy, null);
	}
	
	/**
	 * Removes the row in the table specified by the id
	 * @param id the id of row to remove
	 */
	public void delete(String id)
	{
		String ID[] ={id};
		
		getWritableDatabase().delete("todos", "_ID=?", ID);
	}

	/**
	 * This updates the fields of the id passed
	 * @param id of element to update
	 * @param title of task
	 * @param address of task
	 * @param list the list he task is contained in
	 * @param notes
	 * @param date task is due
	 * @param state the completion status of the task
	 * @param priority of task
	 */
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

	/**
	 * This inserts a new task
	 * @param title of task
	 * @param address of task
	 * @param list task is in
	 * @param notes
	 * @param date task is due
	 * @param state completion
	 * @param priority of task
	 */
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
	
	/**
	 * This updates the stat of a task in a much more expedient fashon than calling update
	 * @param id of task
	 * @param state new state of task (0-100)
	 */
	public void updateState(String id, int state)
	{
		String args[] = {id};
		ContentValues cv = new ContentValues();
		
		cv.put("state", state);
		
		getWritableDatabase().update("todos", cv, "_ID=?", args);
	}

	/**
	 * This updates the notified field when the alarm has sent the notification
	 * for the task with this id
	 * @param id of task to update
	 * @param val true if has been notified false for not notified
	 */
	public void notified(String id, boolean val)
	{
		String args[] = {id};
		ContentValues cv = new ContentValues();
		
		cv.put("notified", val?1:0);
		
		getWritableDatabase().update("todos", cv, "_ID=?", args);
	}
	
	public String getId(Cursor c){
		String datum;
		try {
			datum =  c.getString(0);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = "Error: Can't read database";
		}
		return datum;
	}
	
	/**
	 * This returns title of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return string containing the title
	 */
	public String getTitle(Cursor c)
	{
		String datum;
		try {
			datum =  c.getString(1);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = "Error: Can't read database";
		}
		return datum;
	}

	/**
	 * This returns address of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return string containing the address
	 */
	public String getAddress(Cursor c)
	{
		String datum;
		try {
			datum =  c.getString(2);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = "Error: Can't read database";
		}
		return datum;
	}

	/**
	 * This returns list the task is in of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return string containing the list the task is associated with
	 */
	public String getList(Cursor c)
	{
		String datum;
		try {
			datum =  c.getString(3);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = "Error: Can't read database";
		}
		return datum;
	}

	/**
	 * This returns notes of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return string containing the notes of the task
	 */
	public String getNotes(Cursor c)
	{
		String datum;
		try {
			datum =  c.getString(4);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = "Error: Can't read database";
		}
		return datum;
	}

	/**
	 * This returns due date of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return string containing the due date
	 */
	public String getDate(Cursor c)
	{
		String datum;
		try {
			datum =  c.getString(5);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = "Error: Can't read database";
		}
		return datum;	
	}

	/**
	 * This returns state of completion of task in cursor, from 0 to 100.
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return int that is the state of completion
	 */
	public int getState(Cursor c)
	{
		int datum;
		try {
			datum =  c.getInt(6);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = 0;
		}
		return datum;	
	}

	/**
	 * This returns priority of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return int representing priority
	 */
	public int getPriority(Cursor c)
	{
		int datum;
		try {
			datum =  c.getInt(7);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = 0;
		}
		return datum;
	}
	
	/**
	 * This returns if the task has notified the user of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return true if it has been notified false other wise
	 */
	public boolean getNotified(Cursor c)
	{
		return c.getInt(8)>0;
	}
	
	/**
	 * This returns the unique notifyID used for posting notifications of task in cursor
	 * @note the cursor that is passed into this must be generated from either getAll or getById
	 * @param c the cursor that contains (or marks) the place of the task
	 * @return int that is the notifyID
	 */
	public int getNotifyID(Cursor c)
	{
		int datum;
		try {
			datum =  c.getInt(9);
		} catch (CursorIndexOutOfBoundsException e) {
			Log.e(tag, "Cursor: " + c.toString() + " Out of bounds");
			datum = -1;
		}
		return datum;
	}
}
