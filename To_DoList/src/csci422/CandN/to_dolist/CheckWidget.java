/*
 * Chris Card
 * Nathan Harvey
 * 11/28/12
 * This class is spawned when the user clicks the check button in the widget
 */
package csci422.CandN.to_dolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CheckWidget extends Activity{

	private String id;
	private Cursor c;
	ToDoHelper help;
	
	@Override
	public void onCreate(Bundle instance)
	{
		super.onCreate(instance);
		setContentView(R.layout.widget_dialog);
		
		help = new ToDoHelper(this);
		
		id = getIntent().getStringExtra(DetailForm.DETAIL_EXTRA);
		
		c = help.getById(id);
		c.moveToFirst();
		
		TextView message = (TextView)findViewById(R.id.dialogMessage);
		
		String messageText = "Task: "+help.getTitle(c);
		
		message.setText(messageText);
		
		Button complete = (Button)findViewById(R.id.completeDia);
		Button canceled = (Button)findViewById(R.id.cancel);
		Button openD = (Button)findViewById(R.id.open);
		
		if(help.getState(c) >= ToDo.DONE)
		{
			complete.setText("Task not complete");
		}
		else
		{
			complete.setText("Task complete");
		}
		
		complete.setOnClickListener(completeion);
		
		canceled.setOnClickListener(cancel);
		
		openD.setOnClickListener(open);
		
	}
	
	
	private OnClickListener cancel = new OnClickListener()
	{
		public void onClick(View arg0) 
		{
			c.close();
			finish();	
		}
	};
	
	private OnClickListener completeion = new OnClickListener()
	{
		public void onClick(View arg0) 
		{
			if(help.getState(c) >= ToDo.DONE)
			{
				help.updateState(id, 0);
			}
			else
			{
				help.updateState(id, 100);
			}
			
			if(null != AppWidget.widg) AppWidget.widg.onUpdate(null, null, null);
			c.close();
			finish();	
		}
	};
	
	private OnClickListener open = new OnClickListener()
	{
		public void onClick(View arg0) 
		{
			c.close();
			
			Intent i = new Intent(CheckWidget.this,DetailForm.class);
			
			i.putExtra(DetailForm.DETAIL_EXTRA, id);
			startActivity(i);
			finish();	
		}
	};
	

}
