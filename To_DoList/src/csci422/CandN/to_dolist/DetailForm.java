/*
 *Chris Card
 *Nathan Harvey
 *10/27/12
 *This class contains the code for retrieving and saving users input to a new task or modification of an old task 
 */
package csci422.CandN.to_dolist;

import java.sql.Date;
import java.text.AttributedString;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
public class DetailForm extends Activity {
	public static final String tag = "todoDetail";
	private ImageButton[] priors = new ImageButton[4];
	private DatePicker datepick;
	private ExpandableListView pickList;
	private String[] Listnames = {"Main","Homework","Shopping"};
	private SeekBar completion;
	/** -1 is ?, 0 is a dot, 1 is one !, 2 is two !!  */
	private int priority;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		completion = (SeekBar) findViewById(R.id.completion);
		priors[0] = (ImageButton) findViewById(R.id.Priorityq);
		priors[1] = (ImageButton) findViewById(R.id.Priority0);
		priors[2] = (ImageButton) findViewById(R.id.Priority1);
		priors[3] = (ImageButton) findViewById(R.id.Priority2);
		datepick = ((DatePicker) findViewById(R.id.dueDatePicker));
		pickList = ((ExpandableListView) findViewById(R.id.pickList));
		for(String listname:Listnames){
			TextView t = new TextView(this);
			t.setText(listname);
			pickList.addView(t, 50, 20);
		}
	}
	public void saveStuff(View v){
		Log.v(tag, "Progress: "+completion.getProgress());
		Log.v(tag, completion.getKeyProgressIncrement()+" was done with keys");
		Log.v(tag, "Secondary progress: "+completion.getSecondaryProgress());
		Log.v(tag, "Thumb offset: "+completion.getThumbOffset());
		Log.v(tag, "Max is: "+completion.getMax());
		float pro = completion.getProgress()/((float)completion.getMax());
		//have to cast to avoid integer division.
		Toast.makeText(this, "I don't know how to save stuff!"+pro, Toast.LENGTH_LONG).show();
	}
	public void priq(View v){priority=-1;clr(v);}
	public void prin(View v){priority=0;clr(v);}
	public void prio(View v){priority=1;clr(v);}
	public void prit(View v){priority=2;clr(v);}

	/**
	 * Clears the backgrounds for all priority buttons except one.
	 * @param v the view to give an active background
	 */
	public void clr(View v){
		Log.d(tag, "Priority: "+priority);
		for(ImageButton b : priors){
			b.setBackgroundResource(R.drawable.priorityblank);
		}
		v.setBackgroundResource(R.drawable.widget_frame);
	}
}
