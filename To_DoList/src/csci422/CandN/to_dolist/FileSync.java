/*
 * Chris card
 * Nathan Harvey
 * 11/15/12
 * This class provides code to sync with google calandars or other files
 * and will be started by a intent service and will be using a singleton pattern
 */

package csci422.CandN.to_dolist;

import android.database.Cursor;

public class FileSync {

	//Singleton patter stores its own instance
	private static FileSync instance = null;

	//this flag will be set true if the user updated preferences to
	//sync with google cal
	private boolean toSyncCal;

	//to be used later but for sync the data base
	//with the todo.txt file format or just a file
	private boolean toSaveFile;

	//this tells other methods that sync is running so that they will not
	//with the state of this class
	private static boolean isRunning;
	
	/**
	 * Private constructor so that this class controls
	 * and ensures only one instance of the class
	 */
	private FileSync()
	{
		toSyncCal = false;
		toSaveFile = false;
		isRunning = false;
	}
	
	/**
	 * This class returns an instance of its self if instance exists
	 * or creates a new instance (enforces the singleton pattern)
	 * @return: new instance if one doesn't exist or a single instance
	 * 			of the class
	 */
	public static FileSync getInstance()
	{
		if(instance == null)
		{
			instance = new FileSync();
		}
		
		return instance;
	}

	public static boolean isItRunning()
	{
		return isRunning;
	}

	/**
	 * Toggles toSyncCal to the opposite value of what it is at the moment
	 */
	public void toggleCalSync()
	{
		//toggles toSync to the opposite of what it is 
		toSyncCal = (toSyncCal ? false : true);
	}

	/**
	 * This method returns if it is to sync with google calender
	 * @return: true if it is to sync with file
	 */
	public boolean isSyncCal()
	{
		return toSyncCal;
	}

	/**
	 * This method returns if it is to sync with google calender
	 * @return: true if it is to sync with file
	 */
	public boolean isSaveFile()
	{
		return toSaveFile;
	}

	/**
	 * Toggles toSaveFile to the opposite value of what it is at the moment
	 * This alows us to set the value from another file with out having to know its value
	 */
	public void toggleSaveFile()
	{
		toSaveFile = (toSaveFile ? false : true);
	}


	/**
	 * WARNING: ONLY CALL THIS FUNCTION FROM AN INTENTSERVICE OR RISK SLOWING DOWN
	 *			THE UI THREAD OR HAVE IT STOP BECAUSE USER CLOSES APP
	 *
	 * This function saves the entire data base stored in ToDoHelper to the
	 * CalenderContract api (aka google calendar)
	 * 
	 * @param: ToDoHelper that has the data base since this class can't get its
	 *			calling context.
	 */
	public void syncWithCal(ToDoHelper help)
	{
		if (toSyncCal) 
		{
			isRunning = true;//dont remove could cause problems
			Cursor c = help.getAll("title");
			c.moveToFirst();

			//TODO: Nathan put the calander contract code here
			

			c.close();
			isRunning = false;//don't remove could cause problems
		}//else do nothing
	}

	/**
	 * WARNING: ONLY CALL THIS FUNCTION FROM AN INTENTSERVICE OR RISK SLOWING DOWN
	 *			THE UI THREAD OR HAVE IT STOP BECAUSE USER CLOSES APP
	 *
	 * This function saves the entire data base stored in ToDoHelper to a
	 * file the might be able to be moved to another app (to be implemented latter mabe)
	 * 
	 * @param: ToDoHelper that has the data base since this class can't get its
	 *			calling context.
	 */
	public void saveToFile(ToDoHelper help)
	{
		if (toSaveFile) 
		{
			isRunning = true;//dont remove could cause problems
			Cursor c = help.getAll("title");
			c.moveToFirst();

			//TODO: put function to save to the file
			
			c.close();
			isRunning = false;//dont remove could cause problems
		}//else do nothing
	}


}
