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

public class SyncHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "Sync.db";
	private static final int SCHEMA_VERSION = 1;

	public SyncHelper(Context context)
	{
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//modify table elements for the todolist rather than restaurants
		db.execSQL("CREATE TABLE sync (_id INTEGER PRIMARY KEY AUTOINCREMENT, issync TEXT, issave TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	   /*ToDo on upgrade if schema changes*/
	}

	public Cursor getById(String id)
	{
		String[] args = {id};

		return getReadableDatabase().rawQuery("SELECT _id, issync, issave FROM sync WHERE _ID=?", args);
	}
	
	public void delete(String id)
	{
		String ID[] ={id};
		
		getWritableDatabase().delete("sync", "_ID=?", ID);
	}
	
//	public String getId(Cursor c){
//		return c.getString(0);//TODO is this the right number?
//	}

	public void update(String id, boolean issync, boolean issave)
	{
		ContentValues cv = new ContentValues();
		String[] args = {id};

		cv.put("issync", (issync ? "true":"false"));
		cv.put("issave", (issave ? "true":"false"));

		getWritableDatabase().update("sync", cv, "_ID=?", args);
	}

	public void insert(boolean issync, boolean issave)
	{
		ContentValues cv = new ContentValues();

		cv.put("issync", (issync ? "true":"false"));
		cv.put("issave", (issave ? "true":"false"));

		getWritableDatabase().insert("sync", "issync", cv);
	}

	public Cursor getAll()
	{
		return getReadableDatabase().rawQuery("SELECT _id, issync, issave FROM sync", null);
	}


	public String getSync(Cursor c)
	{
		return c.getString(1);
	}

	public String getSave(Cursor c)
	{
		return c.getString(2);
	}
}
