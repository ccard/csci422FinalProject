/*
 *Chris Card
 *Nathan Harvey
 *10/27/12
 *This class contains the code for retrieving and saving users input to a new task or modification of an old task 
 */
package csci422.CandN.to_dolist;


import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
//import csci422.CandN.to_dolist.R;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class DetailForm extends Activity {
	public static final String tag = "todoDetail";

	private ImageButton[] priors = new ImageButton[4];
	private EditText datepick;
	private EditText address;
	private EditText street;
	private ToDoHelper helper;
	private Cursor cur = null;
	private Spinner pickList;
	private EditText taskName;
	private EditText notes;
	private String[] Listnames = {"Main","Homework","Shopping"};
	private SeekBar completion;
	/** -1 is ?, 0 is a dot, 1 is one !, 2 is two !!  */
	private int priority = 0;
	private Date dueDate;
	private DateFormat dateFormat;
	
	//this code is all for waiting to get the gps location
	private LocationManager locmgr = null;
	
	private ProgressDialog pd;//show spinning wheel while it is finding the location
	
	private AtomicBoolean cancelLocation = new AtomicBoolean(false);//thread safe boolean will be using with a listener for location
	
	private AtomicBoolean continueSearch = new AtomicBoolean(true);
	
	private AtomicBoolean hasDialogShown = new AtomicBoolean(false);
	
	private WaitForLocation gpsWait;
	
	//strings for telling user about latitude and longitude
	private static final String Lat = "LATITUDE:";
	private static final String Lon = "LONGITUDE:";
	
	private static final long gpsWaitDuration = 120000;//wait time for async task
	
	private Builder alertBuild;
	private AlertDialog promptContin;//builder for alert dialog
	
	//end code get gps location
	
	//delte code
	
	private boolean delete = false;
	
	//end delete code
	
	//private boolean hasSaved;
 
	private String id = "";//id needs to be accessible to whole class so it can be used with todo helper
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		completion = (SeekBar) findViewById(R.id.completion);
		
		//initializes priortity buttons
		priors[0] = (ImageButton) findViewById(R.id.Priorityq);
		priors[1] = (ImageButton) findViewById(R.id.Priority0);
		priors[2] = (ImageButton) findViewById(R.id.Priority1);
		priors[3] = (ImageButton) findViewById(R.id.Priority2);
		//date pickers
		datepick = (EditText)findViewById(R.id.dueDatePicker);
		dueDate = new Date(0);//current time
		dateFormat = new SimpleDateFormat();
		dateFormat.setLenient(true);
		pickList = ((Spinner) findViewById(R.id.pickList));
		taskName = ((EditText) findViewById(R.id.taskName));
		notes = ((EditText) findViewById(R.id.notes));
		address = (EditText)findViewById(R.id.address);
		street = (EditText)findViewById(R.id.street);
		loadCurrent();
		//pickList = ((ExpandableListView) findViewById(R.id.pickList));

		locmgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		gpsWait = new WaitForLocation();//creates new async task
		
		//inits the progress dialog with title message
		pd = new ProgressDialog(this);
		pd.setTitle("Finding location");
		pd.setMessage("This could take a few minutes depending on your location.");
		pd.setCancelable(true);//allows the dialog to be cancelable
		pd.setIndeterminate(true);//this sets the spinning animation instead of progress
		pd.setOnCancelListener(cancel);
		
		//inits alertdialog with appropriate listenere and message
		alertBuild = new AlertDialog.Builder(this);
		alertBuild.setPositiveButton("Yes", new OnClickListener(){

			public void onClick(DialogInterface arg0, int arg1) {
				//nothing needed to be done
				arg0.dismiss();
				continueSearch.set(true);
			}
			
		});
		
		alertBuild.setNegativeButton("No", new OnClickListener(){

			public void onClick(DialogInterface arg0, int arg1) {
				continueSearch.set(false);
				gpsWait.cancel(true);
				arg0.dismiss();
			}
			
		});
		
		alertBuild.setMessage("Do you wish to continue to find your location?");
		
		promptContin = alertBuild.create();
		
		ArrayAdapter<CharSequence> adpt = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, Listnames);
		pickList.setAdapter(adpt);
	}

	private void loadCurrent() {
		helper=new ToDoHelper(this);
		id = getIntent().getStringExtra("csci422.CandN.to_dolist.curItem");
		if(id.length()==0)return;
		cur = helper.getById(id);
		cur.moveToFirst();//need to set cursor to the beginning 
		taskName.setText(helper.getTitle(cur));
		loadAddressFields(helper.getAddress(cur));
		notes.setText(helper.getNotes(cur));
		try {
			dueDate = dateFormat.parse(helper.getDate(cur));
			datepick.setText(helper.getDate(cur));
		} catch (ParseException e) {
			Log.e(tag, "Can't parse the date.");
		}
		completion.setProgress(helper.getState(cur));
		priority = helper.getPriority(cur);
		priors[priority+1].setBackgroundResource(R.drawable.highlight);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(gpsWait.getStatus() == AsyncTask.Status.RUNNING)
		{
			gpsWait.cancel(true);
		}
		
		if(!delete)
		{
			saveStuff();
		}
	}
	public void onDone(View v){
		finish();
	}
	public void saveStuff(){
		Log.v(tag, "Progress: "+completion.getProgress());
		Log.v(tag, completion.getKeyProgressIncrement()+" was done with keys");
		Log.v(tag, "Secondary progress: "+completion.getSecondaryProgress());
		Log.v(tag, "Thumb offset: "+completion.getThumbOffset());
		Log.v(tag, "Max is: "+completion.getMax());
		int state = completion.getProgress();
		//float percent= completion.getProgress()/((float)completion.getMax());
		try {
			dueDate = dateFormat.parse(datepick.getText().toString());
		} catch (ParseException e) {
			Log.e(tag, "Can't parse the date.");
			Log.e(tag, e.getMessage());
			dueDate = new Date();
		}
		if(cur==null){//make a new one
			helper.insert(taskName.getText().toString(), parseAddressSave(), notes.getText().toString(), dateFormat.format(dueDate), state, priority);
		}else {//edit current
			helper.update(id, taskName.getText().toString(), parseAddressSave(), notes.getText().toString(), dateFormat.format(dueDate), state, priority);
		}
	}
	
	/**
	 * This method takes the text from street and address text fields and parses it for
	 * the data base
	 * @return the string with the two fields seperated by a + or empty string
	 */
	private String parseAddressSave()
	{
		String ret;
		if(!address.getText().toString().isEmpty() || !street.getText().toString().isEmpty())
		{
			ret = street.getText().toString()+"+"+address.getText().toString();
			return ret;
		}
		else
		{
			ret = "";
			return ret;
		}
	}
	
	/**
	 * This method sets the two address fields if to the passed in string
	 * or nothing if empty passed in string 
	 * @param addre the street and address fields seperated by + or empty
	 */
	private void loadAddressFields(String addre)
	{
		if(!addre.isEmpty())
		{
			String words[] = addre.split("\\+");
			
			street.setText(words[0]);
			address.setText(words[1]);
		}
	}
	
	public void priq(View v){priority=-1;clr(v);}
	public void prin(View v){priority=0;clr(v);}
	public void prio(View v){priority=1;clr(v);}
	public void prit(View v){priority=2;clr(v);}

	/**
	 * Clears the backgrounds for all priority buttons except one.
	 * @param v the view to give an active background.
	 */
	public void clr(View v){
		Log.d(tag, "Priority: "+priority);
		for(ImageButton b : priors){
			b.setBackgroundResource(R.drawable.priorityblank);
		}
		v.setBackgroundResource(R.drawable.highlight);
	}

	public void deleteTask(View v)
	{
		if(!id.isEmpty())
		{
			helper.delete(id);
			delete = true;
			finish();
		}
		else
		{
			delete = true;
			finish();
		}

	}
	
	public void openCal(View v){
		Builder calDialog = new Builder(this);
		LayoutInflater inf = getLayoutInflater();
		calDialog.setView(inf.inflate(R.layout.date_dialog, null));
		calDialog.setPositiveButton("Done", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//TODO save the cal stuff.
				dialog.dismiss();
			}
		});
		Calendar cal = Calendar.getInstance();              
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", cal.getTimeInMillis());//TODO pass in string instead
		intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
		intent.putExtra("title", "A Test Event from android app");
		startActivityForResult(intent,1);
		//TODO implement
		//pass the string 
	}
	
	public void openMaps(View v){
		if(!address.getText().toString().isEmpty() && !street.getText().toString().isEmpty())
		{//add code here for lat and lon if applicable
			String uri = "geo:0,0?q="+street.getText().toString()+"+"+address.getText().toString();
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
		}
		else
		{
			Toast.makeText(this, "Please fill out the two address fields or click the here button", Toast.LENGTH_LONG).show();
		}
	}
	
	//gets location from gps
	private LocationListener onLocChange = new LocationListener(){

		public void onLocationChanged(Location location) 
		{	
			if(null != location)
			{
				//brings the latitude and longitude into something google maps can intemperate
				//correctly
				street.setText(Lat+(int)(location.getLatitude()*1E6));
				address.setText(Lon+(int)(location.getLongitude()*1E6));
				
				cancelLocation.set(true);
				
				gpsWait.cancel(true);//cancels the async task if the location is gotten
			}	
		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	//this is a listener for the spinner dialog so when the back button is pressed it performes that tasks bellow
	private OnCancelListener cancel = new OnCancelListener(){

		public void onCancel(DialogInterface arg0) {
			cancelLocation.set(true);
			gpsWait.cancel(true);
		}
	};
	
	/**
	 * The method is called by the here button add a locationlistenere to start gps to look for the users location
	 * shows spinner dialog and starts the async task to wait for the location change
	 * @param v
	 */
	public void getLoc(View v)
	{
		if(gpsWait.getStatus() == AsyncTask.Status.RUNNING)
		{
			gpsWait.cancel(true);
		}
		
		gpsWait = new WaitForLocation();
		
		
		if(gpsWait.getStatus() == AsyncTask.Status.PENDING)
		{
			locmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocChange);//starts gps and listening for location change
			pd.show();
			gpsWait.execute("");
		}
		
		
	}
	
	/**
	 * This async task will do waits until the user cancels or the location is found
	 * @author Chris Card
	 *
	 */
	private class WaitForLocation extends AsyncTask<String, String, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			//while the atomic booleans both are false
			while(!cancelLocation.get() && continueSearch.get())
			{
				
				try{
					Thread.sleep(gpsWaitDuration);//sleeps the async task thread
					
					if(!cancelLocation.get() && continueSearch.get())//checks both attomic booleans
					{//not sure if this part works haven't got here yer
						pd.dismiss();
						publishProgress(params);
						
						Thread.sleep(4000);
						publishProgress(params);
					}
				}catch (InterruptedException e)
				{
					Log.e("DetailForm", e.getMessage());
				}
			}
			return "Finished";
		}
		
		@Override
		protected void onProgressUpdate(String... prog)
		{
			if(continueSearch.get() && hasDialogShown.get())
			{
				pd.show();
				hasDialogShown.set(false);
			}
			else if(continueSearch.get())
			{
				promptContin.show();
				hasDialogShown.set(true);
			}
			
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			pd.dismiss();//dismiss the progress dialog
			if(cancelLocation.get() || !continueSearch.get())
			{
				//remove location listener
				locmgr.removeUpdates(onLocChange);
			}
			//reset atomic booleans
			cancelLocation.set(false);
			continueSearch.set(true);
			hasDialogShown.set(false);
		}
		
		@Override
		protected void onCancelled()
		{
			//removes listener
			locmgr.removeUpdates(onLocChange);
			pd.dismiss();
			cancelLocation.set(false);
			continueSearch.set(true);
			hasDialogShown.set(false);
		}
		
	}


}
