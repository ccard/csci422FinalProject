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
				help.update(id, help.getTitle(c), help.getAddress(c), help.getList(c), help.getNotes(c), help.getDate(c), 0, help.getPriority(c));
			}
			else
			{
				help.update(id, help.getTitle(c), help.getAddress(c), help.getList(c), help.getNotes(c), help.getDate(c), 100, help.getPriority(c));
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
