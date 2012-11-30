/*
 * Chris Card
 * Nathan Harvey
 * 11/19/2012
 * This class will set and reinitialize alarms from the todohelper database
 */

package csci422.CandN.to_dolist;

import java.util.Calendar;
import java.util.GregorianCalendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {

	public static final String NOTIFY_EXTRA = "csci422.CandN.to_dolist.notice";
	
	private static Calendar nowDate;
	
	@Override
	public void onReceive(Context ctxt, Intent intent) 
	{
		ToDoHelper helper = new ToDoHelper(ctxt);
		
		Cursor c = helper.getAll("date");
		
		while(c.moveToNext())
		{
			setAlarm(ctxt,helper,c);
		}
		c.close();
		helper.close();

	}
	
	/**
	 * This method sets the alarm to notify the user about a due task
	 * @note EXPECTED date format of the task 'MM/DD/YY HH:MM AM/PM'
	 * @param ctxt that the alarm will be set
	 * @param h containing the task info
	 * @param c cursor that represents the task
	 */
	public static void setAlarm(Context ctxt, ToDoHelper h, Cursor c)
	{
		nowDate = Calendar.getInstance();//gets the current date time of system
		
		Calendar cal = new GregorianCalendar();//to initialize to the due date of the task
		
		
		AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		
		if(!h.getDate(c).isEmpty())//if no date don't set alarm
		{
			String words[] = h.getDate(c).split("\\s+");//breaks the date string up based on expected format
			if(words.length == 3)
			{//if it matches the format
				//uses get methods to initialize the calendar obj
				cal.set(getYear(words[0]), getMonth(words[0]), getDay(words[0]), getHour(words[1]), getMin(words[1]));
				
				if("PM".equals(words[2]))
				{
					cal.set(Calendar.AM_PM, Calendar.PM);
				}
				else
				{
					cal.set(Calendar.AM_PM, Calendar.AM);
				}
				
				if(cal.getTimeInMillis() < System.currentTimeMillis())
				{//if the time of the due date id before the current time notify it and do nothing
					h.notified(c.getString(0), true);
				}
				else
				{
					h.notified(c.getString(0), false);//un notifies the task just incase
					
					Intent i = new Intent(ctxt,AlarmReceiver.class);
					
					i.putExtra(NOTIFY_EXTRA, c.getString(0));
					
					//this pending intent call getBriadcast will call a reciever class when intent is called
					PendingIntent pi = PendingIntent.getBroadcast(ctxt, c.getInt(0), i, 0);
					
					//sets the alarm for the time with the intent to start and have it do it
					//both when locked and not
					mgr.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
				}
			}
		}
		
	}
	
	/**
	 * This cancels the alarm of the task that was set
	 * @param ctxt context that the alarm was originally created if not nothing
	 * will happen
	 * @param h the ToDoHelper object that will contain the task info 
	 * @param c the cursor that represents the task
	 */
	public static void cancelAlarm(Context ctxt, ToDoHelper h, Cursor c)
	{
		//musht recreate the pending intent to be able to cancel it
		AlarmManager mgr = (AlarmManager)ctxt.getSystemService(Context.ALARM_SERVICE);
		
		Intent i = new Intent(ctxt,AlarmReceiver.class);
		
		i.putExtra(NOTIFY_EXTRA, c.getString(0));
		
		mgr.cancel(PendingIntent.getBroadcast(ctxt, c.getInt(0), i, 0));
	}
	
	//---------------------------------------------------------------
	//internal helper methods
	//---------------------------------------------------------------
	
	/**
	 * This returns the year contained in a string like 'MM/DD/YY'
	 * @param date string to pull year from
	 * @return the year in 2000
	 */
	private static int getYear(String date)
	{
		String words[] = date.split("/");
		
		if(words.length == 3)
		{
			//at list point will only work for 20** dates but not a problem
			String tempString = "20"+words[2];
			return (Integer.parseInt(tempString));
		}
		else
		{
			return nowDate.get(Calendar.YEAR);
		}
	}
	
	/**
	 * This gets month from string 'MM/DD/YY'
	 * @param date string to pull year from
	 * @return the month - 1 because calendar month is 0 indexed
	 */
	private static int getMonth(String date)
	{
		String words[] = date.split("/");
		
		if(words.length == 3)
		{
			return (Integer.parseInt(words[0])-1);
		}
		else
		{
			return nowDate.get(Calendar.MONTH);
		}
	}
	
	/**
	 * This gets the day from a string 'MM/DD/YY'
	 * @param date string to get day from
	 * @return the day of month as an int
	 */
	private static int getDay(String date)
	{
		String words[] = date.split("/");
		
		if(words.length == 3)
		{
			return Integer.parseInt(words[1]);
		}
		else
		{
			return nowDate.get(Calendar.DAY_OF_MONTH);
		}
	}
	
	/**
	 * This gets the hour from a string 'HH:MM'
	 * @param time string to get hour
	 * @return int that is hour of day
	 */
	private static int getHour(String time)
	{
		String words[] = time.split(":");
		
		if(words.length == 2)
		{
			return Integer.parseInt(words[0]);
		}
		else
		{
			return nowDate.get(Calendar.HOUR_OF_DAY);
		}
	}
	
	/**
	 * This returns the minute of a string 'HH:MM'
	 * @param time string to get minute
	 * @return int that is the minute of hour
	 */
	private static int getMin(String time)
	{
		String words[] = time.split(":");
		
		if(words.length == 2)
		{
			return Integer.parseInt(words[1]);
		}
		else
		{
			return nowDate.get(Calendar.MINUTE);
		}
	}

}
