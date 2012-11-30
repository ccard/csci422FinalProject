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
import android.widget.RemoteViews;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) 
	{
		String id = intent.getExtras().getString(OnBootReceiver.NOTIFY_EXTRA);
		
		ToDoHelper help = new ToDoHelper(context);
		
		Cursor c = help.getById(id);
		
		c.moveToFirst();
		
		help.notified(id, true);
		
		NotificationManager mgr = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		
		//initializes the notification with all necessary fields
		Notification note = new Notification();
		
		note.icon = R.drawable.ic_launcher;//app icon
		
		note.tickerText = "Task due!";//text that appears across top of screen in notification bar
		
		note.when = System.currentTimeMillis();//when it was posted
		
		note.flags |= Notification.FLAG_AUTO_CANCEL;//clears notification when user clicks on it
		
		//sets the layout of notification to our custom layout
		RemoteViews content = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
		
		//sets the text filed in custom layout
		content.setTextViewText(R.id.notifyText, "Task Due: "+help.getTitle(c));
		
		note.contentView = content;
		
		Intent i = new Intent(context, DetailForm.class);
		
		i.putExtra(DetailForm.DETAIL_EXTRA, id);
		
		//sets the notifications action to start the pending intent
		note.contentIntent = PendingIntent.getActivity(context, Integer.parseInt(id), i, 0);
		
		//gets the notification id from from the data base
		//then closes the data base
		int notId = help.getNotifyID(c);
		c.close();
		help.close();
		
		mgr.notify(notId,note);//this sends the notification to the os
	}

}
