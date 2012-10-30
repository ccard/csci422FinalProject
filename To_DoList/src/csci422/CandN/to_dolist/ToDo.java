/*
 * Chris Card
 * Nathan Harvey
 * 10/24/12
 */
package csci422.CandN.to_dolist;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ToDo extends ListActivity {

	public final static String ID_EXTRA = "csci422.CandN.to_dolist._ID";
	private Cursor model=null;

	private ToDoAdapter adapter=null;

	private ToDoHelper helper=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do);

		helper = new ToDoHelper(this);

		initList();
	}


	/**
	 * this initializes the list from the cursor
	 */
	private void initList()
	{
		if(model != null)
		{
			stopManagingCursor(model);
			model.close();
		}

		model = helper.getAll("Title");
		startManagingCursor(model);

		//sets adapter with this activity passed in a simple list item
		//and the list of restaurants
		adapter = new ToDoAdapter(model);

		setListAdapter(adapter);
	}

     @Override
     public void onDestroy()
     {
     	super.onDestroy();
     	helper.close();
     }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_to_do, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if(item.getItemId() == R.id.add)
		{
			startActivity(new Intent(ToDo.this, DetailForm.class));
			return true;
			/*ToDo here*/
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView list, View view, int position, long id) 
	{
		/*TODO here */
	}

	/**
	 * This class holds the RestaurantAdapter for populating the listview with the restaurants
	 * @author Chris
	 *
	 */
	class ToDoAdapter extends CursorAdapter
	{

		ToDoAdapter(Cursor c)
		{
			super(ToDo.this, c);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c)
		{
			RestaurantHolder holder = (RestaurantHolder)row.getTag();

			holder.populateForm(c, helper);
		}

		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent)
		{
			LayoutInflater inflater = getLayoutInflater();
			View row = inflater.inflate(R.layout.row, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);

			row.setTag(holder);

			return row;
		}
	}

	/**
	 * This static class is used to populate the ToDoAdapter rows
	 * @author Chris
	 *
	 */
	static class RestaurantHolder
	{

		private TextView title = null;
		private TextView date = null;
		private CheckBox check = null;

		RestaurantHolder(View row)
		{
			title = (TextView)row.findViewById(R.id.title);
			date = (TextView)row.findViewById(R.id.date);
			check = (CheckBox)row.findViewById(R.id.check);
		}

		void populateForm(Cursor c, ToDoHelper helper)
		{
			title.setText(helper.getTitle(c));
			date.setText(helper.getDate(c));

			if(helper.getState(c) == 0)
			{
				check.setChecked(false);
			}
			else
			{
				check.setChecked(true);
			}
		}
	}
}
