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
import android.widget.Toast;

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
		
		note.when = System.currentTimeMillis();
		
		note.flags |= Notification.FLAG_AUTO_CANCEL;
		
		RemoteViews content = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
		
		content.setTextViewText(R.id.notifyText, "Task Due: "+help.getTitle(c));
		
		note.contentView = content;
		
		Intent i = new Intent(context, DetailForm.class);
		
		i.putExtra(DetailForm.DETAIL_EXTRA, id);
		
		note.contentIntent = PendingIntent.getActivity(context, Integer.parseInt(id), i, 0);
		
		mgr.notify(NOTIFY_ME_ID,note);
		Log.v("AlarmReceiver","got here");
	}

}
