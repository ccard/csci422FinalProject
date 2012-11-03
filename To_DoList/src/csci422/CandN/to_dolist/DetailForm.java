/*
 *Chris Card
 *Nathan Harvey
 *10/27/12
 *This class contains the code for retrieving and saving users input to a new task or modification of an old task 
 */
package csci422.CandN.to_dolist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;
//Comment out stuff here and in XML file
//And see what is causing the bug.
public class DetailForm extends Activity {
	private SeekBar completion;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		completion = (SeekBar) findViewById(R.id.completion);
		float per = completion.getProgress()/completion.getMax();
		Toast.makeText(this, ""+per, Toast.LENGTH_LONG).show();
	}

}
