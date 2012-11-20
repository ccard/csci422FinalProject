/*
 * Chris Card
 * Nathan Harvey
 * 11/19/12
 * This class will receives a broadcast from the alarm manager to do a task
 */

package csci422.CandN.to_dolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	private static final int NOTIFY_ME_ID = 1337;
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		Log.d("AlaramReceiver","got here");
		String id = intent.getExtras().getString(OnBootReceiver.NOTIFY_EXTRA);
		
		ToDoHelper help = new ToDoHelper(context);
		
		Cursor c = help.getById(id);
		
		c.moveToFirst();
		
		NotificationManager mgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		
		Notification note = new Notification();
		
		note.icon = R.drawable.ic_launcher;
		
		note.tickerText = "Task Due: "+help.getTitle(c);
		
		note.when = System.currentTimeMillis();
		
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		
		Intent i = new Intent(context, DetailForm.class);
		
		i.putExtra(ToDo.ID_EXTRA, id);
		
		note.contentIntent = PendingIntent.getActivity(context, 0, i, 0);
		
		mgr.notify(NOTIFY_ME_ID,note);

	}

}
